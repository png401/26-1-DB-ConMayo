package daoImpl;

import dao.SeatDAO;
import dto.SeatDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDAOImpl implements SeatDAO {
    private final Connection conn;

    public SeatDAOImpl(Connection conn) {
        this.conn = conn;
    }

    // [조회] 공연별 전체 좌석 목록 반환 (평점, 색상 포함)
    @Override
    public List<SeatDTO> findByPerformance(int performanceId) {
        List<SeatDTO> list = new ArrayList<>();

        // LEFT JOIN 이유: 예약/리뷰 없는 좌석도 결과에 포함해야 함
        // GROUP BY: 한 좌석에 리뷰 여러 개일 수 있으므로 AVG 집계 위해 묶음
        String sql = """
                SELECT s.seat_id,
                       s.venue_id,
                       ps.performance_seat_id,
                       s.section,
                       s.row_num,
                       s.col_num,
                       ps.price,
                       AVG(r.seat_rating) AS avg_rating
                FROM performance_seat ps
                JOIN seat s
                    ON ps.seat_id = s.seat_id
                LEFT JOIN booking b
                    ON b.performance_seat_id = ps.performance_seat_id
                    AND b.booking_status IN ('BOOKED', 'HOLD')
                LEFT JOIN review r
                    ON r.booking_id = b.booking_id
                WHERE ps.performance_id = ?
                GROUP BY s.seat_id, s.venue_id, ps.performance_seat_id, s.section, s.row_num, s.col_num, ps.price
                ORDER BY s.section, s.row_num, s.col_num
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, performanceId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SeatDTO seat = new SeatDTO(
                            rs.getInt("seat_id"),
                            rs.getInt("venue_id"),
                            rs.getInt("performance_seat_id"),
                            rs.getString("section"),
                            rs.getInt("row_num"),
                            rs.getInt("col_num")
                    );

                    double avg = rs.getDouble("avg_rating");
                    // AVG 결과가 NULL(리뷰 없는 좌석)이면 wasNull()이 true -> 0.0 처리
                    seat.setAvgRating(rs.wasNull() ? 0.0 : avg);

                    list.add(seat);
                }
            }
        } catch (SQLException e) {
            System.out.println("공연별 좌석 조회 실패: " + e.getMessage());
        }
        return list;
    }

    // [조회] 특정 좌석의 평균 평점 반환 (리뷰 없으면 0.0)
    @Override
    public double getAvgRating(int seatId) {
        // review에 seat_id 없으므로 booking -> performance_seat 경유
        String sql = """
                SELECT AVG(r.seat_rating) AS avg_rating
                FROM review r
                JOIN booking b
                    ON b.booking_id = r.booking_id
                JOIN performance_seat ps
                    ON ps.performance_seat_id = b.performance_seat_id
                WHERE ps.seat_id = ?
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, seatId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    double avg = rs.getDouble("avg_rating");
                    return rs.wasNull() ? 0.0 : avg;
                }
            }
        } catch (SQLException e) {
            System.out.println("평균 평점 조회 실패: " + e.getMessage());
        }
        return 0.0;
    }

    // [조회] 공연의 잔여 좌석 수 반환
    @Override
    public int getAvailableCount(int performanceId) {
        // BOOKED/HOLD 상태 booking이 없는 performance_seat 수 = 잔여 좌석
        String sql = """
                SELECT COUNT(*) AS available_count
                FROM performance_seat
                WHERE performance_id = ?
                  AND performance_seat_id NOT IN (
                      SELECT performance_seat_id
                      FROM booking
                      WHERE booking_status IN ('BOOKED', 'HOLD')
                  )
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, performanceId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("available_count");
                }
            }
        } catch (SQLException e) {
            System.out.println("잔여 좌석 수 조회 실패: " + e.getMessage());
        }
        return 0;
    }
}