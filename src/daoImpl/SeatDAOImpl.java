// SeatDAOImpl.java
package daoImpl;

import dao.SeatDAO;
import dto.SeatDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SeatDAOImpl implements SeatDAO {
    private final Connection conn;

    public SeatDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<SeatDTO> findByPerformance(int performanceId) {
        // 공연별 전체 좌석 조회 (평점, 가격, 예약여부 포함)
        // JOIN seat + performance_seat + booking + review
        // AVG(r.seat_rating) AS avg_rating
        // booking_status IN ('BOOKED','HOLD') → isBooked = true
        return new ArrayList<>(); // TODO
    }

    @Override
    public double getAvgRating(int seatId) {
        // SELECT AVG(r.seat_rating) FROM review r
        // JOIN booking b ON b.booking_id = r.booking_id
        // JOIN performance_seat ps ON ps.performance_seat_id = b.performance_seat_id
        // WHERE ps.seat_id = ?
        return 0.0; // TODO
    }

    @Override
    public int getAvailableCount(int performanceId) {
        // 예약 안 된 performance_seat 수 계산
        // performance_seat WHERE performance_id = ?
        // AND performance_seat_id NOT IN (
        //   SELECT performance_seat_id FROM booking
        //   WHERE booking_status IN ('BOOKED','HOLD'))
        return 0; // TODO
    }
}
