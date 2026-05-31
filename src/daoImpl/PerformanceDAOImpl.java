package daoImpl;

import dao.PerformanceDAO;
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
    private final Connection conn;

    public PerformanceDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<PerformanceDTO> findAll() {
        // SELECT * FROM performance ORDER BY start_time
        // → 공연 목록 화면에 전체 출력
    	List<PerformanceDTO> list = new ArrayList<>();
        String sql = "SELECT performance_id, title, category, start_time, running_time, sales_status, booking_open, venue_id " +
                     "FROM performance ORDER BY start_time";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
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
                list.add(performance);
            }
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return list; // TODO
    }

    @Override
    public List<PerformanceDTO> findByCategory(String category) {
        // SELECT * FROM performance WHERE category = ?
        // → 카테고리 필터 조회 (콘서트/뮤지컬/스포츠)
    	List<PerformanceDTO> list = new ArrayList<>();
        String sql = "SELECT performance_id, title, category, start_time, running_time, sales_status, booking_open, venue_id " +
                     "FROM performance WHERE category = ? ORDER BY start_time";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
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
                    list.add(performance);
                }
            }
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return list; // TODO
    }

    @Override
    public PerformanceDTO findById(int performanceId) {
        // SELECT * FROM performance WHERE performance_id = ?
        // → 공연 상세 조회
    	String sql = "SELECT performance_id, title, category, start_time, running_time, sales_status, booking_open, venue_id " +
                "FROM performance WHERE performance_id = ?";

	   try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	       pstmt.setInt(1, performanceId);
	
	       try (ResultSet rs = pstmt.executeQuery()) {
	           if (rs.next()) {
	               return new PerformanceDTO(
	                   rs.getInt("performance_id"),
	                   rs.getString("title"),
	                   rs.getString("category"),
	                   rs.getTimestamp("start_time").toLocalDateTime(),
	                   rs.getInt("running_time"),
	                   SalesStatus.valueOf(rs.getString("sales_status")),
	                   rs.getTimestamp("booking_open").toLocalDateTime(),
	                   rs.getInt("venue_id")
	               );
	           }
	       }
	   } catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   }
        return null; // TODO
    }

    @Override
    public void insert(PerformanceDTO performance) {
        // INSERT INTO performance (title, category, start_time, running_time,
        //   sales_status, booking_open, venue_id) VALUES (?,?,?,?,?,?,?)
    	String sql = "INSERT INTO performance (title, category, start_time, running_time, sales_status, booking_open, venue_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

	   try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
		// TODO Auto-generated catch block
		e.printStackTrace();
	   }
    } // TODO

    @Override
    public void update(PerformanceDTO performance) {
        // UPDATE performance SET title=?, category=?, ... WHERE performance_id=?
    	String sql = "UPDATE performance SET title = ?, category = ?, start_time = ?, running_time = ?, " +
                "sales_status = ?, booking_open = ?, venue_id = ? WHERE performance_id = ?";

	   try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
	       }
	   } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
    } // TODO

    @Override
    public void delete(int performanceId) {
        // DELETE FROM performance WHERE performance_id = ?
        // → 연결된 performance_seat, booking이 있으면 FK로 막힘
    	String sql = "DELETE FROM performance WHERE performance_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, performanceId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("공연 삭제 성공 (ID: " + performanceId + ")");
            }
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    } // TODO
}