package model;

public class Account {
    // FIELDS data object stores
    private String username;
    private String password; 
    private String role;       
    /**
     * Constructor creates a new Account object 
     * @param username the login name
     * @param password the password
     * @param role     ADMIN or STAFF
     */
    public Account(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    //GETTERS ways to read the data
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getRole() {
        return role;
    }
    //SETTERS ways to edit the data
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
