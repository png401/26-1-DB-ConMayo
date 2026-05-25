package daoImpl;

import dao.ReviewDAO;
import dto.ReviewDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAOImpl implements ReviewDAO {
    private final Connection conn;

    public ReviewDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<ReviewDTO> findBySeatId(int seatId) {
        // 좌석의 리뷰 목록 조회
        // JOIN review + booking + performance_seat
        // WHERE ps.seat_id = ?
        // ORDER BY r.written_at DESC
        return new ArrayList<>(); // TODO
    }

    @Override
    public void insert(ReviewDTO review) {
        // INSERT INTO review (booking_id, seat_rating, content)
        // → written_at은 DEFAULT CURRENT_TIMESTAMP
        // → Service에서 BOOKED 상태 + 중복 여부 체크 후 호출
    } // TODO

    @Override
    public boolean existsByBookingId(int bookingId) {
        // SELECT COUNT(*) FROM review WHERE booking_id = ?
        // → 중복 리뷰 방지 (리뷰 작성 전 체크)
        // → DB에 UNIQUE 제약 있지만 Service에서 미리 체크해서 에러 방지
        return false; // TODO
    }
}
