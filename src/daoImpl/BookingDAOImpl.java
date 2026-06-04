package daoImpl;

import dao.BookingDAO;
import db.DatabaseConnector;
import dto.BookingDTO;
import dto.BookingStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {

    // conn 필드 제거 — 트랜잭션용은 conn 직접 받고, 나머지는 매번 getConnection() 호출

    // ===== 트랜잭션용 (conn 직접 받음) =====

    @Override
    public void insert(Connection conn, BookingDTO booking) {
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
    public void updateStatus(Connection conn, int bookingId, String status) {
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
    public void lockSeat(Connection conn, int performanceSeatId) {
        String sql = "SELECT * FROM performance_seat WHERE performance_seat_id = ? FOR UPDATE";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, performanceSeatId);
            pstmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("좌석 락 실패: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isAlreadyBooked(Connection conn, int performanceSeatId) {
        String sql = "SELECT COUNT(*) FROM booking WHERE performance_seat_id = ? AND booking_status IN ('BOOKED', 'HOLD')";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, performanceSeatId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("예매 중복 확인 실패: " + e.getMessage(), e);
        }
        return false;
    }

    // ===== 트랜잭션 밖 (매번 getConnection()) =====

    @Override
    public BookingDTO findById(int bookingId) {
        String sql = "SELECT * FROM booking WHERE booking_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
        String sql = "SELECT b.*, p.title, "
                + "CASE WHEN r.review_id IS NOT NULL THEN 1 ELSE 0 END AS has_review "
                + "FROM booking b "
                + "JOIN performance_seat ps ON b.performance_seat_id = ps.performance_seat_id "
                + "JOIN performance p ON p.performance_id = ps.performance_id "
                + "LEFT JOIN review r ON r.booking_id = b.booking_id "
                + "WHERE b.member_id = ? ORDER BY b.booked_at DESC";
        List<BookingDTO> list = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BookingDTO dto = mapRow(rs);
                    dto.setHasReview(rs.getInt("has_review") == 1);
                    list.add(dto);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("예매 내역 조회 실패: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public int getAvailableCount(int performanceId) {
        String sql = "SELECT " +
                     "  (SELECT COUNT(*) FROM performance_seat WHERE performance_id = ?) " +
                     "- (SELECT COUNT(*) FROM booking b " +
                     "   JOIN performance_seat ps ON b.performance_seat_id = ps.performance_seat_id " +
                     "   WHERE ps.performance_id = ? AND b.booking_status IN ('HOLD', 'BOOKED')) " +
                     "AS available_count";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
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

    @Override
    public LocalDateTime getPerformanceStartTime(int bookingId) {
        String sql = "SELECT p.start_time " +
                    "FROM booking b " +
                    "JOIN performance_seat ps ON b.performance_seat_id = ps.performance_seat_id " +
                    "JOIN performance p ON ps.performance_id = p.performance_id " +
                    "WHERE b.booking_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
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

    private BookingDTO mapRow(ResultSet rs) throws SQLException {
        BookingDTO dto = new BookingDTO();
        dto.setBookingId(rs.getInt("booking_id"));
        dto.setMemberId(rs.getString("member_id"));
        dto.setPerformanceSeatId(rs.getInt("performance_seat_id"));
        dto.setBookingStatus(BookingStatus.valueOf(rs.getString("booking_status")));
        Timestamp ts = rs.getTimestamp("booked_at");
        if (ts != null) dto.setBookedAt(ts.toLocalDateTime());
        dto.setPayment(rs.getInt("payment"));
        try { dto.setPerformanceTitle(rs.getString("title")); } catch (SQLException ignored) {}
        return dto;
    }

	@Override
	public boolean isMemberBlacklisted(int bookingId) {
		String sql = "SELECT COUNT(*) FROM member m " +
                "JOIN booking b ON m.member_id = b.member_id " +
                "WHERE b.booking_id = ? AND m.blacklist_until > NOW()";
                
   try (Connection conn = DatabaseConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
       
       stmt.setInt(1, bookingId);
       try (ResultSet rs = stmt.executeQuery()) {
           if (rs.next()) {
               return rs.getInt(1) > 0; // 블랙리스트 조건에 맞으면 true
           }
       }
   } catch (SQLException e) {
       e.printStackTrace();
   }
		return false;
	}
}