package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import model.Batch;
import model.BatchList;

//the only class that talks to the batches table
public class BatchDAO {

    //read the rows in order and add them one by one at the end of the linked list
    public void loadInto(Connection connection, BatchList list) throws SQLException {
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("SELECT batch_id, name, quantity, expiration, category, dosage, checked "
                + "FROM batches ORDER BY position");
        while (rs.next()) {
            Batch b = new Batch(rs.getString("batch_id"), rs.getString("name"), rs.getInt("quantity"),
                    LocalDate.parse(rs.getString("expiration")), rs.getString("category"), rs.getString("dosage"));
            b.setChecked(rs.getInt("checked") == 1);
            list.addLast(b);
        }
        rs.close();
        st.close();
    }

    //walk the linked list from the head and save every node (position = its place in the list)
    public void replaceAll(Connection connection, BatchList list) throws SQLException {
        Statement st = connection.createStatement();
        st.executeUpdate("DELETE FROM batches");
        st.close();

        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO batches (batch_id, name, quantity, expiration, category, dosage, checked, position) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        Batch current = list.getHead();
        int position = 0;
        while (current != null) {
            ps.setString(1, current.getBatchId());
            ps.setString(2, current.getName());
            ps.setInt(3, current.getQuantity());
            ps.setString(4, current.getExpiration().toString()); 
            ps.setString(5, current.getCategory());
            ps.setString(6, current.getDosage());
            ps.setInt(7, current.isChecked() ? 1 : 0);
            ps.setInt(8, position);
            ps.executeUpdate();
            position++;
            current = current.getNext();
        }
        ps.close();
    }
}