package daoImpl;

import dao.CancellationDAO;
import dto.CancellationDTO;
import dto.CancelStatus;

import java.sql.*;

public class CancellationDAOImpl implements CancellationDAO {
    private final Connection conn;

    public CancellationDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(CancellationDTO cancellation) {
        String sql = "INSERT INTO cancellation (booking_id, refund_amount, cancellation_fee, cancel_status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cancellation.getBookingId());
            pstmt.setInt(2, cancellation.getRefundAmount());
            pstmt.setInt(3, cancellation.getCancellationFee());
            pstmt.setString(4, cancellation.getCancelStatus().name());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("취소 INSERT 실패: " + e.getMessage(), e);
        }
    }

    @Override
    public CancellationDTO findByBookingId(int bookingId) {
        String sql = "SELECT * FROM cancellation WHERE booking_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("취소 이력 조회 실패: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void updateStatus(int bookingId, String status) {
        String sql = "UPDATE cancellation SET cancel_status = ? WHERE booking_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, bookingId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("취소 상태 변경 실패: " + e.getMessage(), e);
        }
    }

    private CancellationDTO mapRow(ResultSet rs) throws SQLException {
        CancellationDTO dto = new CancellationDTO();
        dto.setBookingId(rs.getInt("booking_id"));
        dto.setRefundAmount(rs.getInt("refund_amount"));
        dto.setCancellationFee(rs.getInt("cancellation_fee"));
        dto.setCancelStatus(CancelStatus.valueOf(rs.getString("cancel_status")));
        Timestamp ts = rs.getTimestamp("canceled_at");
        if (ts != null) dto.setCanceledAt(ts.toLocalDateTime());
        return dto;
    }
}