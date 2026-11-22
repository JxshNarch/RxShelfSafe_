package model;

/**
 * Account class
 * 
 * This is a simple class that holds information about a user who can log in
 * to the RxShelfSafe system.
 * 
 * Each account has:
 * - username (unique name they type to log in)
 * - password (secret code)
 * - role     (either "ADMIN" or "STAFF")
 * 
 * ADMIN can do everything (manage accounts, approve removals, etc.)
 * STAFF can manage batches and request removals, but cannot manage accounts.
 * 
 * This is a basic data class (also called a model or POJO).
 * Students learning data structures usually create classes like this first
 * before putting them into lists or linked lists.
 */
public class Account {

    // FIELDS (the data this object stores)
    private String username;   // login name
    private String password;   // login password
    private String role;       // "ADMIN" or "STAFF"

    /**
     * Constructor - creates a new Account object
     * 
     * @param username the login name
     * @param password the password
     * @param role     "ADMIN" or "STAFF"
     */
    public Account(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // ===== GETTERS (ways to read the data) =====

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    // ===== SETTERS (ways to change the data) =====

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
