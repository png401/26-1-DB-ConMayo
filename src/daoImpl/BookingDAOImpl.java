package daoImpl;

import dao.BookingDAO;
import dto.BookingDTO;
import dto.BookingStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {
    private final Connection conn;

    public BookingDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(BookingDTO booking) {
        String sql = "INSERT INTO booking (member_id, performance_seat_id, booking_status, payment) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, booking.getMemberId());
            pstmt.setInt(2, booking.getPerformanceSeatId());
            pstmt.setString(3, booking.getBookingStatus().name());
            pstmt.setInt(4, booking.getPayment());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) booking.setBookingId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("예매 INSERT 실패: " + e.getMessage(), e);
        }
    }

    @Override
    public BookingDTO findById(int bookingId) {
        String sql = "SELECT * FROM booking WHERE booking_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("예매 조회 실패: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<BookingDTO> findByMemberId(String memberId) {
        String sql = "SELECT * FROM booking WHERE member_id = ? ORDER BY booked_at DESC";
        List<BookingDTO> list = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("예매 내역 조회 실패: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public void updateStatus(int bookingId, String status) {
        String sql = "UPDATE booking SET booking_status = ? WHERE booking_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, bookingId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("예매 상태 변경 실패: " + e.getMessage(), e);
        }
    }

    @Override
    public int getAvailableCount(int performanceId) {
        String sql = "SELECT " +
                     "  (SELECT COUNT(*) FROM performance_seat WHERE performance_id = ?) " +
                     "- (SELECT COUNT(*) FROM booking b " +
                     "   JOIN performance_seat ps ON b.performance_seat_id = ps.performance_seat_id " +
                     "   WHERE ps.performance_id = ? AND b.booking_status IN ('HOLD', 'BOOKED')) " +
                     "AS available_count";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, performanceId);
            pstmt.setInt(2, performanceId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt("available_count");
            }
        } catch (SQLException e) {
            throw new RuntimeException("잔여석 조회 실패: " + e.getMessage(), e);
        }
        return 0;
    }

    private BookingDTO mapRow(ResultSet rs) throws SQLException {
        BookingDTO dto = new BookingDTO();
        dto.setBookingId(rs.getInt("booking_id"));
        dto.setMemberId(rs.getString("member_id"));
        dto.setPerformanceSeatId(rs.getInt("performance_seat_id"));
        dto.setBookingStatus(BookingStatus.valueOf(rs.getString("booking_status")));
        Timestamp ts = rs.getTimestamp("booked_at");
        if (ts != null) dto.setBookedAt(ts.toLocalDateTime());
        dto.setPayment(rs.getInt("payment"));
        return dto;
    }

    @Override
    public LocalDateTime getPerformanceStartTime(int bookingId) {
        String sql = "SELECT p.start_time " +
                    "FROM booking b " +
                    "JOIN performance_seat ps ON b.performance_seat_id = ps.performance_seat_id " +
                    "JOIN performance p ON ps.performance_id = p.performance_id " +
                    "WHERE b.booking_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp ts = rs.getTimestamp("start_time");
                    return ts != null ? ts.toLocalDateTime() : null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("공연 시작 시간 조회 실패: " + e.getMessage(), e);
        }
        return null;
    }    
}