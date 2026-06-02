package daoImpl;

import dao.PerformanceDAO;
import db.DatabaseConnector;
import dto.PerformanceDTO;
import dto.SalesStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PerformanceDAOImpl implements PerformanceDAO {

    // conn 필드 제거 — 매번 getConnection() 호출

    // 공통 SQL — venue JOIN으로 venue_name, remaining_seats 함께 조회
    private static final String BASE_SQL = """
            SELECT p.performance_id, p.title, p.category, p.start_time, p.running_time,
                   p.sales_status, p.booking_open, p.venue_id,
                   v.venue_name,
                   COUNT(ps.performance_seat_id) - COUNT(b.booking_id) AS remaining_seats
            FROM performance p
            JOIN venue v ON p.venue_id = v.venue_id
            LEFT JOIN performance_seat ps ON ps.performance_id = p.performance_id
            LEFT JOIN booking b ON b.performance_seat_id = ps.performance_seat_id
                               AND b.booking_status IN ('BOOKED', 'HOLD')
            """;

    // ResultSet → PerformanceDTO 변환 (공통 매핑 메서드)
    private PerformanceDTO mapRow(ResultSet rs) throws SQLException {
        PerformanceDTO performance = new PerformanceDTO(
                rs.getInt("performance_id"),
                rs.getString("title"),
                rs.getString("category"),
                rs.getTimestamp("start_time").toLocalDateTime(),
                rs.getInt("running_time"),
                SalesStatus.valueOf(rs.getString("sales_status")),
                rs.getTimestamp("booking_open").toLocalDateTime(),
                rs.getInt("venue_id")
        );
        performance.setVenueName(rs.getString("venue_name"));        // 공연장 이름 세팅
        performance.setRemainingSeats(rs.getInt("remaining_seats")); // 잔여석 세팅
        return performance;
    }

    @Override
    public List<PerformanceDTO> findAll() {
        // SELECT * FROM performance ORDER BY start_time
        // → 공연 목록 화면에 전체 출력
        List<PerformanceDTO> list = new ArrayList<>();
        String sql = BASE_SQL + """
                GROUP BY p.performance_id, p.title, p.category, p.start_time, p.running_time,
                         p.sales_status, p.booking_open, p.venue_id, v.venue_name
                ORDER BY p.performance_id
                """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
        	throw new RuntimeException("공연 목록 조회 실패: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<PerformanceDTO> findByCategory(String category) {
        // SELECT * FROM performance WHERE category = ?
        // → 카테고리 필터 조회 (콘서트/뮤지컬/스포츠)
        List<PerformanceDTO> list = new ArrayList<>();
        String sql = BASE_SQL + """
                WHERE p.category = ?
                GROUP BY p.performance_id, p.title, p.category, p.start_time, p.running_time,
                         p.sales_status, p.booking_open, p.venue_id, v.venue_name
                ORDER BY p.performance_id
                """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
        	throw new RuntimeException("카테고리별 공연 조회 실패: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public PerformanceDTO findById(int performanceId) {
        // SELECT * FROM performance WHERE performance_id = ?
        // → 공연 상세 조회
        String sql = BASE_SQL + """
                WHERE p.performance_id = ?
                GROUP BY p.performance_id, p.title, p.category, p.start_time, p.running_time,
                         p.sales_status, p.booking_open, p.venue_id, v.venue_name
                """;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, performanceId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
        	throw new RuntimeException("공연 상세 조회 실패: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void insert(PerformanceDTO performance) {
        // INSERT INTO performance (title, category, start_time, running_time,
        //   sales_status, booking_open, venue_id) VALUES (?,?,?,?,?,?,?)
        String sql = "INSERT INTO performance (title, category, start_time, running_time, sales_status, booking_open, venue_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, performance.getTitle());
            pstmt.setString(2, performance.getCategory());
            pstmt.setTimestamp(3, Timestamp.valueOf(performance.getStartTime()));
            pstmt.setInt(4, performance.getRunningTime());
            pstmt.setString(5, performance.getSalesStatus().name()); // Enum을 String으로 변환하여 저장
            pstmt.setTimestamp(6, Timestamp.valueOf(performance.getBookingOpen()));
            pstmt.setInt(7, performance.getVenueId());
            pstmt.executeUpdate();
            System.out.println("공연 등록 성공: " + performance.getTitle());
        } catch (SQLException e) {
        	throw new RuntimeException("DB 공연 등록 실패: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(PerformanceDTO performance) {
        // UPDATE performance SET title=?, category=?, ... WHERE performance_id=?
        String sql = "UPDATE performance SET title = ?, category = ?, start_time = ?, running_time = ?, " +
                "sales_status = ?, booking_open = ?, venue_id = ? WHERE performance_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, performance.getTitle());
            pstmt.setString(2, performance.getCategory());
            pstmt.setTimestamp(3, Timestamp.valueOf(performance.getStartTime()));
            pstmt.setInt(4, performance.getRunningTime());
            pstmt.setString(5, performance.getSalesStatus().name());
            pstmt.setTimestamp(6, Timestamp.valueOf(performance.getBookingOpen()));
            pstmt.setInt(7, performance.getVenueId());
            pstmt.setInt(8, performance.getPerformanceId());
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("공연 정보 수정 성공 (ID: " + performance.getPerformanceId() + ")");
            } else {
                throw new RuntimeException("수정할 공연 정보가 존재하지 않습니다. (ID: " + performance.getPerformanceId() + ")");
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB 공연 수정 실패: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int performanceId) {
        // DELETE FROM performance WHERE performance_id = ?
        // → 연결된 performance_seat, booking이 있으면 FK로 막힘
        String sql = "DELETE FROM performance WHERE performance_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, performanceId);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("공연 삭제 성공 (ID: " + performanceId + ")");
            } else {
                throw new RuntimeException("삭제할 공연 정보가 존재하지 않습니다. (ID: " + performanceId + ")");
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB 공연 삭제 실패 (하위 예매 내역이나 좌석이 있는지 확인하세요): " + e.getMessage(), e);
        }
    }
}