package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

// DAO = the only class that talks to the "categories" table
public class CategoryDAO {

    public ArrayList<String> getAll(Connection connection) throws SQLException {
        ArrayList<String> list = new ArrayList<String>();
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("SELECT name FROM categories ORDER BY position");
        while (rs.next()) {
            list.add(rs.getString("name"));
        }
        rs.close();
        st.close();
        return list;
    }

    public void replaceAll(Connection connection, ArrayList<String> list) throws SQLException {
        Statement st = connection.createStatement();
        st.executeUpdate("DELETE FROM categories");
        st.close();

        PreparedStatement ps = connection.prepareStatement("INSERT INTO categories (name, position) VALUES (?, ?)");
        for (int i = 0; i < list.size(); i++) {
            ps.setString(1, list.get(i));
            ps.setInt(2, i);
            ps.executeUpdate();
        }
        ps.close();
    }
}