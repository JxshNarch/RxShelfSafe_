package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;

/**
 * Batch class - represents one batch of medicine in the pharmacy.
 * 
 * This is the NODE class for our Linked List (BatchList).
 * 
 * In Data Structures class, a node usually has:
 *   1. The data itself (here: batchId, name, quantity, etc.)
 *   2. A pointer/reference to the NEXT node (the "next" field)
 * 
 * This allows us to connect many Batch objects together like a chain:
 *   Batch1 -> Batch2 -> Batch3 -> null
 * 
 * Why linked list instead of ArrayList?
 * - We practiced implementing insert, delete, and search ourselves.
 * - Good practice for understanding how data structures work under the hood.
 */
public class Batch {

    // How many days before expiration is considered "Near expiry"
    public static final int NEAR_DAYS = 30;

    // Date format used everywhere in the program: MM/dd/yyyy
    // STRICT means it will reject invalid dates like 02/30/2025
    public static final DateTimeFormatter DATE_FORMAT
            = DateTimeFormatter.ofPattern("MM/dd/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    // ===== DATA FIELDS =====
    private String batchId;      // unique ID, example: "B10001"
    private String name;         // medicine name, example: "Paracetamol"
    private int quantity;        // how many units left
    private LocalDate expiration;// when it expires
    private String category;     // example: "Pain Relievers"
    private String dosage;       // example: "500mg"
    private boolean checked;     // true if staff/admin already inspected this batch

    // ===== LINKED LIST POINTER =====
    // This is the most important field for the data structure!
    // It points to the next Batch in the list.
    // If this is the last batch, next will be null.
    private Batch next;

    /**
     * Constructor - creates a new Batch (a new node)
     */
    public Batch(String batchId, String name, int quantity,
                 LocalDate expiration, String category, String dosage) {
        this.batchId = batchId;
        this.name = name;
        this.quantity = quantity;
        this.expiration = expiration;
        this.category = category;
        this.dosage = dosage;
        this.checked = false;   // new batches start as not checked
        this.next = null;       // not connected to anything yet
    }

    // ===== GETTERS =====

    public String getBatchId() {
        return batchId;
    }
    public String getName() {
        return name;
    }
    public int getQuantity() {
        return quantity;
    }
    public LocalDate getExpiration() {
        return expiration;
    }
    public String getCategory() {
        return category;
    }
    public String getDosage() {
        return dosage;
    }
    public boolean isChecked() {
        return checked;
    }

    /**
     * Returns the next node in the linked list.
     * This is how we walk through the list:
     *   current = current.getNext();
     */
    public Batch getNext() {
        return next;
    }

    // ===== SETTERS =====

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setExpiration(LocalDate expiration) {
        this.expiration = expiration;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    /**
     * Connects this node to another node.
     * Used by BatchList when we add or remove nodes.
     */
    public void setNext(Batch next) {
        this.next = next;
    }

    // ===== HELPER METHODS =====

    /**
     * Calculates how many days are left until expiration.
     * Negative number means it is already expired.
     * 
     * Example: if today is Nov 22 and expiration is Nov 25 -> returns 3
     */
    public long getDaysLeft() {
        return ChronoUnit.DAYS.between(LocalDate.now(), expiration);
    }

    /**
     * Returns a simple status string based on days left:
     * - "Expired"      -> days < 0
     * - "Near expiry"  -> 0 to 30 days
     * - "Good"         -> more than 30 days
     * 
     * This status is used to color the rows in the table (red / orange / green).
     */
    public String getStatus() {
        long days = getDaysLeft();
        if (days < 0) {
            return "Expired";
        } else if (days <= NEAR_DAYS) {
            return "Near expiry";
        } else {
            return "Good";
        }
    }
}
