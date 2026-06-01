package db;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {
    private final Connection conn;

    public TransactionManager(Connection conn) {
        this.conn = conn;
    }

    public void begin() throws SQLException {
        conn.setAutoCommit(false);
    }

    public void commit() throws SQLException {
        conn.commit();
    }

    public void rollback() {
        try { conn.rollback(); } catch (SQLException e) { e.printStackTrace(); }
    }

    public void end() {
        try { conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
    }
}
