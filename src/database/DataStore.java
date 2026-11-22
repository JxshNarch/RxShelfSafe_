package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.HashMap;

import java.sql.Connection;      
import java.sql.SQLException;     



import model.Account;
import model.Batch;
import model.BatchList;

public class DataStore {


       
    public java.time.LocalDateTime getLastUpdated() {      
        File db = new File(DBConnection.findDatabase());    
        return java.time.LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(db.lastModified()),     
                java.time.ZoneId.systemDefault());      
    }     

    private BatchList batches = new BatchList();
    private ArrayList<Account> accounts = new ArrayList<Account>();
    private ArrayList<String> categories = new ArrayList<String>();
    private ArrayList<String> requests = new ArrayList<String>();   // Batch IDs waiting for the admin (first in, first out)   //fixed (comment was missing)


    private HashMap<String, String> requesters = new HashMap<String, String>();   // Batch ID -> username of the staff who requested it     

        private String fileName =("rxshelfsafe_data.txt");      

    private Connection connection;                                  
    private AccountDAO accountDAO = new AccountDAO();              
    private CategoryDAO categoryDAO = new CategoryDAO();          
    private BatchDAO batchDAO = new BatchDAO();                    
    private RequestDAO requestDAO = new RequestDAO();    
               
 public DataStore() {
        load();
    }

    public BatchList getBatches() {
        return batches;
    }

    public HashMap<String, String> getRequesters() {
        return requesters;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public ArrayList<String> getRequests() {
        return requests;
    }


    // how many accounts have this role 
    public int countRole(String role) {
        int count = 0;
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getRole().equals(role)) {
                count++;
            }
        }
        return count;
    }

    //delete a batch from the list and from the removal requests
    public void deleteBatch(String batchId) {
        batches.remove(batchId);
        requests.remove(batchId);
        requesters.remove(batchId);        
    }

    // 
    // LOAD    read the text file and fill the list, accounts, etc.
    //
    // 
        public void load() {
        try {
            connection = DBConnection.open(DBConnection.findDatabase());      //new

            if (accountDAO.count(connection) == 0) {      //new (a brand new database)
                if (new File(fileName).exists()) {      //new
                    importTextFile();                   // copy the old text file into the database      //new
                } else {      //new
                    addSampleData();      //new
                }      //new
                makeSureDefaults();      //new
                save();      //new
                return;      //new
            }      //new

            accounts.addAll(accountDAO.getAll(connection));      //new
            categories.addAll(categoryDAO.getAll(connection));      //new
            batchDAO.loadInto(connection, batches);      //new
            requestDAO.loadInto(connection, requests, requesters);      //new
        } catch (SQLException e) {      //new
            System.out.println("Could not read the database: " + e.getMessage());      //new
        }      //new

        makeSureDefaults();      //fixed (moved into its own method)
    }

    // make sure there is always at least one admin and one staff account
    private void makeSureDefaults() {      //new
        if (accounts.size() == 0) {
            addDefaultAccounts();
        }
        if (categories.size() == 0) {
            addDefaultCategories();
        }
    }

    private void importTextFile() {      //fixed (this is your old load(), renamed)
        File file = new File(fileName);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                String[] p = line.split("\t", -1);    // the parts are separated by a tab
                if (p[0].equals("USER")) {
                    accounts.add(new Account(p[1], p[2], p[3]));
                } else if (p[0].equals("CAT")) {
                    categories.add(p[1]);
                } else if (p[0].equals("BATCH")) {
                    Batch b = new Batch(p[1], p[2], Integer.parseInt(p[3]),
                            LocalDate.parse(p[4]), p[5], p[6]);
                    b.setChecked(p[7].equals("1"));
                    batches.addLast(b);
                } else if (p[0].equals("REQ")) {
                    requests.add(p[1]);
                    if (p.length > 2) {
                        requesters.put(p[1], p[2]);
                    }
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Could not read the old text file: " + e.getMessage());
        }
    }
    //
    // SAVE: write everything to the text file
    //
    // 
        public void save() {
        if (connection == null) {      //new
            System.out.println("No database connection, nothing was saved.");      //new
            return;      //new
        }      //new
        try {
            connection.setAutoCommit(false);        // all or nothing: if one part fails, nothing is changed      //new
            accountDAO.replaceAll(connection, accounts);      //new
            categoryDAO.replaceAll(connection, categories);      //new
            batchDAO.replaceAll(connection, batches);      //new
            requestDAO.replaceAll(connection, requests, requesters);      //new
            connection.commit();      //new
            connection.setAutoCommit(true);      //new
        } catch (SQLException e) {      //fixed
            System.out.println("Could not save to the database: " + e.getMessage());
            try {      //new
                connection.rollback();      //new
                connection.setAutoCommit(true);      //new
            } catch (SQLException e2) {      //new
                System.out.println("Rollback failed: " + e2.getMessage());      //new
            }      //new
        }
    }
    //
    

    //
    // sample data for the first time the program runs
    // 
    private void addDefaultAccounts() {
        accounts.add(new Account("admin", "admin123", "ADMIN"));
        accounts.add(new Account("staff", "staff123", "STAFF"));
    }

    private void addDefaultCategories() {
        categories.add("Pain Relievers");
        categories.add("Antibiotics");
        categories.add("Antihistamines");
        categories.add("Vitamins");
        categories.add("Antacids");
    }

    private void addSampleData() {
        addDefaultAccounts();
        addDefaultCategories();
        LocalDate today = LocalDate.now();     // the dates are based on today so all 3 colors show
        batches.addLast(new Batch("B10001", "Paracetamol", 120, today.minusDays(19), "Pain Relievers", "500mg"));
        batches.addLast(new Batch("B10002", "Amoxicillin", 60, today.plusDays(12), "Antibiotics", "250mg"));
        batches.addLast(new Batch("B10003", "Cetirizine", 200, today.plusDays(76), "Antihistamines", "10mg"));
        batches.addLast(new Batch("B10004", "Vitamin C", 150, today.plusDays(21), "Vitamins", "500mg"));
        batches.addLast(new Batch("B10005", "Omeprazole", 80, today.plusDays(240), "Antacids", "20mg"));
    }
}