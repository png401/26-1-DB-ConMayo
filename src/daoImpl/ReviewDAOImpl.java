package daoImpl;

import dao.ReviewDAO;
import dto.ReviewDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAOImpl implements ReviewDAO {
    private final Connection conn;

    public ReviewDAOImpl(Connection conn) {
        this.conn = conn;
    }

    // [조회] 특정 좌석의 리뷰 목록 반환 (최신순)
    @Override
    public List<ReviewDTO> findBySeatId(int seatId) {
        List<ReviewDTO> list = new ArrayList<>(); // 메서드 안 맨 위에 선언

        // review에 seat_id가 없으므로 booking -> performance_seat 경유해서 조회
        // review → booking -> performance_seat -> seat_id 순으로 JOIN
        String sql = """
                SELECT r.review_id,
                       r.booking_id,
                       r.seat_rating,
                       r.written_at,
                       r.content
                FROM review r
                JOIN booking b
                    ON r.booking_id = b.booking_id
                JOIN performance_seat ps
                    ON b.performance_seat_id = ps.performance_seat_id
                WHERE ps.seat_id = ?
                ORDER BY r.written_at DESC
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, seatId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new ReviewDTO(
                            rs.getInt("review_id"),
                            rs.getInt("booking_id"),
                            rs.getInt("seat_rating"),
                            rs.getTimestamp("written_at").toLocalDateTime(),
                            rs.getString("content")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("리뷰 목록 조회 실패: " + e.getMessage());
        }
        return list;
    }

    // [등록] 리뷰 INSERT -> 성공 시 true, 실패 시 false
    // Service에서 BOOKED 상태 확인 + 중복 체크 후 호출
    @Override
    public boolean insert(ReviewDTO review) {
        // written_at은 DB DEFAULT CURRENT_TIMESTAMP → INSERT에서 생략
        String sql = """
                INSERT INTO review (booking_id, seat_rating, content)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, review.getBookingId());
            pstmt.setInt(2, review.getSeatRating());
            pstmt.setString(3, review.getContent());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("리뷰 등록 실패: " + e.getMessage());
            return false;
        }
    }

    // [중복 확인] 해당 예매에 리뷰가 이미 있으면 true
    // DB UNIQUE 제약 있지만 Service에서 미리 체크해 에러 없이 막음
    @Override
    public boolean existsByBookingId(int bookingId) {
        String sql = """
                SELECT COUNT(*) 
                FROM review 
                WHERE booking_id = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookingId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("리뷰 중복 확인 실패: " + e.getMessage());
        }
        return false;
    }
}