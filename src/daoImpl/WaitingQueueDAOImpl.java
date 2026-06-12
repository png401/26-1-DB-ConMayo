package daoImpl;

import dao.WaitingQueueDAO;
import db.DatabaseConnector;
import dto.WaitingQueueDTO;

import java.sql.*;

public class WaitingQueueDAOImpl implements WaitingQueueDAO {

    @Override
    public int enter(WaitingQueueDTO dto) {
        String sql = "INSERT INTO waiting_queue (member_id, performance_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, dto.getMemberId());
            pstmt.setInt(2, dto.getPerformanceId());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1); // queue_id 반환
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int getMyRank(int queueId, int performanceId) {
        // 나보다 queue_id가 작은 사람 수 = 내 앞에 있는 사람 수
        String sql = "SELECT COUNT(*) FROM waiting_queue " +
                     "WHERE performance_id = ? AND queue_id < ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, performanceId);
            pstmt.setInt(2, queueId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1) + 1; // 내 순번 = 앞 사람 수 + 1
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    public void leave(int queueId) {
        String sql = "DELETE FROM waiting_queue WHERE queue_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, queueId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}