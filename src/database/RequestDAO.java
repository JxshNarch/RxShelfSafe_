package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

// DAO = the only class that talks to the "removal_requests" table
public class RequestDAO {

    // fills the queue (first in, first out) and the "who requested it" map
    public void loadInto(Connection connection, ArrayList<String> requests, HashMap<String, String> requesters)
            throws SQLException {
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("SELECT batch_id, requested_by FROM removal_requests ORDER BY position");
        while (rs.next()) {
            requests.add(rs.getString("batch_id"));
            if (rs.getString("requested_by") != null) {
                requesters.put(rs.getString("batch_id"), rs.getString("requested_by"));
            }
        }
        rs.close();
        st.close();
    }

    public void replaceAll(Connection connection, ArrayList<String> requests, HashMap<String, String> requesters)
            throws SQLException {
        Statement st = connection.createStatement();
        st.executeUpdate("DELETE FROM removal_requests");
        st.close();

        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO removal_requests (batch_id, requested_by, position) VALUES (?, ?, ?)");
        for (int i = 0; i < requests.size(); i++) {
            String who = requesters.get(requests.get(i));
            if (who == null) {
                who = "Staff";
            }
            ps.setString(1, requests.get(i));
            ps.setString(2, who);
            ps.setInt(3, i);
            ps.executeUpdate();
        }
        ps.close();
    }
}