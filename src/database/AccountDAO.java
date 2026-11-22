package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Account;

//the only class that talks to theaccounts table
public class AccountDAO {

    public ArrayList<Account> getAll(Connection connection) throws SQLException {
        ArrayList<Account> list = new ArrayList<Account>();
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("SELECT username, password, role FROM accounts ORDER BY position");
        while (rs.next()) {
            list.add(new Account(rs.getString("username"), rs.getString("password"), rs.getString("role")));
        }
        rs.close();
        st.close();
        return list;
    }
    public int count(Connection connection) throws SQLException {
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM accounts");
        int total = rs.getInt(1);
        rs.close();
        st.close();
        return total;
    }
    //delete everything then insert the list again (the ? are filled by the values, so no SQL injection)
    public void replaceAll(Connection connection, ArrayList<Account> list) throws SQLException {
        Statement st = connection.createStatement();
        st.executeUpdate("DELETE FROM accounts");
        st.close();

        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO accounts (username, password, role, position) VALUES (?, ?, ?, ?)");
        for (int i = 0; i < list.size(); i++) {
            Account a = list.get(i);
            ps.setString(1, a.getUsername());
            ps.setString(2, a.getPassword());
            ps.setString(3, a.getRole());
            ps.setInt(4, i);
            ps.executeUpdate();
        }
        ps.close();
    }
}