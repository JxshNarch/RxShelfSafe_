package database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// Opens the SQLite database (one file: rxshelfsafe.db) and makes the tables if they are not there yet.
// SQLite needs no server, the whole database is just that one file.
public class DBConnection {

    // find the database file; if it does not exist yet, it will be made next to the old text file
    public static String findDatabase() {
        String found = FileFinder.find("rxshelfsafe.db");
        if (new File(found).exists()) {
            return found;
        }
        String textFile = FileFinder.find("rxshelfsafe_data.txt");
        File folder = new File(textFile).getAbsoluteFile().getParentFile();
        return new File(folder, "rxshelfsafe.db").getAbsolutePath();
    }

    public static Connection open(String path) throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");          // load the driver (the sqlite-jdbc .jar)
        } catch (ClassNotFoundException e) {
            throw new SQLException("sqlite-jdbc.jar is not added to the project libraries.");
        }
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        createTables(connection);
        return connection;
    }

    // IF NOT EXISTS = do nothing when the table is already there
    private static void createTables(Connection connection) throws SQLException {
        Statement st = connection.createStatement();
        st.execute("CREATE TABLE IF NOT EXISTS accounts ("
                + "username TEXT PRIMARY KEY, password TEXT NOT NULL, role TEXT NOT NULL, position INTEGER)");
        st.execute("CREATE TABLE IF NOT EXISTS categories ("
                + "name TEXT PRIMARY KEY, position INTEGER)");
        st.execute("CREATE TABLE IF NOT EXISTS batches ("
                + "batch_id TEXT PRIMARY KEY, name TEXT NOT NULL, quantity INTEGER NOT NULL, "
                + "expiration TEXT NOT NULL, category TEXT, dosage TEXT, checked INTEGER DEFAULT 0, "
                + "position INTEGER)");      // position = the order of the nodes in the linked list
        st.execute("CREATE TABLE IF NOT EXISTS removal_requests ("
                + "batch_id TEXT PRIMARY KEY, requested_by TEXT, position INTEGER)");      // position = first in, first out
        st.close();
    }
}