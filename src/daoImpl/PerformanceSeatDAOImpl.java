package daoImpl;

import dao.PerformanceSeatDAO;
import db.DatabaseConnector;
import dto.PerformanceSeatDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PerformanceSeatDAOImpl implements PerformanceSeatDAO {

    // conn 필드 제거 — 매번 getConnection() 호출

    @Override
    public List<PerformanceSeatDTO> findByPerformance(int performanceId) {
        // SELECT * FROM performance_seat WHERE performance_id = ?
        // → 관리자 가격 설정 화면에서 사용
        List<PerformanceSeatDTO> list = new ArrayList<>();
        String sql =
                "SELECT ps.performance_seat_id, " +
                "       ps.performance_id, " +
                "       ps.seat_id, " +
                "       ps.price, " +
                "       s.section, " +
                "       b.booking_status " +
                "FROM performance_seat ps " +
                "JOIN seat s ON ps.seat_id = s.seat_id " +
                "LEFT JOIN booking b ON ps.performance_seat_id = b.performance_seat_id " +
                "                  AND b.booking_status IN ('HOLD', 'BOOKED') " +
                "WHERE ps.performance_id = ?"; // 쿼리 수정 - seat 조인 추가

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, performanceId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PerformanceSeatDTO perfSeat = new PerformanceSeatDTO(
                            rs.getInt("performance_seat_id"),
                            rs.getInt("performance_id"),
                            rs.getInt("seat_id"),
                            rs.getInt("price")
                    );
                    // 추가
                    perfSeat.setSection(rs.getString("section"));
                    // booking_status가 존재한다면 이미 예약(선점)된 좌석이므로 true로 세팅
                    String status = rs.getString("booking_status");
                    perfSeat.setBooked(status != null);
                    list.add(perfSeat);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void insert(PerformanceSeatDTO perfSeat) {
        // INSERT INTO performance_seat (performance_id, seat_id, price)
        // → venue 교차 검증은 Service에서 처리 후 호출
        String sql = "INSERT INTO performance_seat (performance_id, seat_id, price) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, perfSeat.getPerformanceId());
            pstmt.setInt(2, perfSeat.getSeatId());
            pstmt.setInt(3, perfSeat.getPrice());
            pstmt.executeUpdate();
            System.out.println("공연 좌석 등록 성공 (공연 ID: " + perfSeat.getPerformanceId() + ", 좌석 ID: " + perfSeat.getSeatId() + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //추가
    //새로운 공연 추가 시 받아온 performanceId 가지고 공연좌석들 여러개를 생성
    @Override
    public void createSeatsForPerformance(
            int performanceId,
            int venueId
    ) {
        String sql =
                """
                INSERT INTO performance_seat
                (performance_id, seat_id, price)
                SELECT
                    ?,
                    seat_id,
                    CASE
                        WHEN section IN ('VIP','STANDING','GROUND')
                            THEN 180000
                        WHEN section IN ('R','1층')
                            THEN 150000
                        WHEN section IN ('S','2층')
                            THEN 120000
                        ELSE 99000
                    END
                FROM seat
                WHERE venue_id = ?
                """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, performanceId);
            pstmt.setInt(2, venueId);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePrice(int performanceSeatId, int price) {
        // UPDATE performance_seat SET price = ? WHERE performance_seat_id = ?
        // → 관리자 가격 수정
        String sql = "UPDATE performance_seat SET price = ? WHERE performance_seat_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, price);
            pstmt.setInt(2, performanceSeatId);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("좌석 가격 수정 성공! (공연좌석 ID: " + performanceSeatId + " -> " + price + "원)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //추가
    //공연장 변경 시 공연좌석 정보 한번에 지우는 메소드 구현
    @Override
    public void deleteByPerformanceId(
            int performanceId
    ) {
        String sql =
                """
                DELETE FROM performance_seat
                WHERE performance_id = ?
                """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt =
                     conn.prepareStatement(sql)) {

            pstmt.setInt(1, performanceId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
}