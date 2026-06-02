package db;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {
    private Connection conn;

    public Connection begin() throws SQLException {
        conn = DatabaseConnector.getConnection();
        conn.setAutoCommit(false);
        return conn; // BookingService한테 conn 넘겨줌
    }

    public void commit() throws SQLException {
        conn.commit();
    }

    public void rollback() {
        try { conn.rollback(); } catch (SQLException e) { e.printStackTrace(); }
    }

    public void end() {
        try {
            conn.setAutoCommit(true);
            conn.close();
            conn = null;
        } catch (SQLException e) { e.printStackTrace(); }
    }
}