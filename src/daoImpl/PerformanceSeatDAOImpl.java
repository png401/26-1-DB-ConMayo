package daoImpl;

import dao.PerformanceSeatDAO;
import dto.PerformanceSeatDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PerformanceSeatDAOImpl implements PerformanceSeatDAO {
    private final Connection conn;

    public PerformanceSeatDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<PerformanceSeatDTO> findByPerformance(int performanceId) {
        // SELECT * FROM performance_seat WHERE performance_id = ?
        // → 관리자 가격 설정 화면에서 사용
    	List<PerformanceSeatDTO> list = new ArrayList<>();
        String sql = "SELECT ps.performance_seat_id, ps.performance_id, ps.seat_id, ps.price, b.booking_status " +
                     "FROM performance_seat ps " +
                     "LEFT JOIN booking b ON ps.performance_seat_id = b.performance_seat_id AND b.booking_status IN ('HOLD', 'BOOKED') " +
                     "WHERE ps.performance_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, performanceId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PerformanceSeatDTO perfSeat = new PerformanceSeatDTO(
                        rs.getInt("performance_seat_id"),
                        rs.getInt("performance_id"),
                        rs.getInt("seat_id"),
                        rs.getInt("price")
                    );

                    // booking_status가 존재한다면 이미 예약(선점)된 좌석이므로 true로 세팅
                    String status = rs.getString("booking_status");
                    if (status != null) {
                        perfSeat.setBooked(true);
                    } else {
                        perfSeat.setBooked(false);
                    }

                    list.add(perfSeat);
                }
            }
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return list; // TODO
    }

    @Override
    public void insert(PerformanceSeatDTO perfSeat) {
        // INSERT INTO performance_seat (performance_id, seat_id, price)
        // → venue 교차 검증은 Service에서 처리 후 호출
    	String sql = "INSERT INTO performance_seat (performance_id, seat_id, price) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, perfSeat.getPerformanceId());
            pstmt.setInt(2, perfSeat.getSeatId());
            pstmt.setInt(3, perfSeat.getPrice());

            pstmt.executeUpdate();
            System.out.println("공연 좌석 등록 성공 (공연 ID: " + perfSeat.getPerformanceId() + ", 좌석 ID: " + perfSeat.getSeatId() + ")");
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    } // TODO

    @Override
    public void updatePrice(int performanceSeatId, int price) {
        // UPDATE performance_seat SET price = ? WHERE performance_seat_id = ?
        // → 관리자 가격 수정
    	String sql = "UPDATE performance_seat SET price = ? WHERE performance_seat_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, price);
            pstmt.setInt(2, performanceSeatId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("좌석 가격 수정 성공! (공연좌석 ID: " + performanceSeatId + " -> " + price + "원)");
            }
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    } // TODO
}