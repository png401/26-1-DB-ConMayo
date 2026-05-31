package daoImpl;

import dao.VenueDAO;
import dto.VenueDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VenueDAOImpl implements VenueDAO {
    private final Connection conn;

    public VenueDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<VenueDTO> findAll() {
        // SELECT * FROM venue
        // → 관리자 공연장 목록 조회
    	List<VenueDTO> list = new ArrayList<>();
    	String sql = "SELECT venue_id, venue_name, address FROM venue";
    	
    	try (PreparedStatement pstmt = conn.prepareStatement(sql);
    			ResultSet rs = pstmt.executeQuery()) {
    		
    		while (rs.next()) {
    			VenueDTO venue = new VenueDTO(
                        rs.getInt("venue_id"),
                        rs.getString("venue_name"),
                        rs.getString("address")
                    );
                    list.add(venue);
    		}
    		
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return list; // TODO
    }

    @Override
    public VenueDTO findById(int venueId) {
        // SELECT * FROM venue WHERE venue_id = ?
        // → 특정 공연장 조회 (수정/삭제 전 존재 확인)
    	String sql = "SELECT venue_id, venue_name, address FROM venue WHERE venue_id = ?";
    	
    	try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, venueId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new VenueDTO(
                        rs.getInt("venue_id"),
                        rs.getString("venue_name"),
                        rs.getString("address")
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
    public void insert(VenueDTO venue) {
        // INSERT INTO venue (venue_name, address) VALUES (?, ?)
        // → venue_id는 AUTO_INCREMENT라 생략
    	String sql = "INSERT INTO venue (venue_name, address) VALUES (?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, venue.getVenueName());
            pstmt.setString(2, venue.getAddress());
            
            pstmt.executeUpdate();
            System.out.println("공연장 등록 성공: " + venue.getVenueName());
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    } // TODO

    @Override
    public void update(VenueDTO venue) {
        // UPDATE venue SET venue_name = ?, address = ? WHERE venue_id = ?
    	String sql = "UPDATE venue SET venue_name = ?, address = ? WHERE venue_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, venue.getVenueName());
            pstmt.setString(2, venue.getAddress());
            pstmt.setInt(3, venue.getVenueId());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("공연장 정보 수정 성공! (ID: " + venue.getVenueId() + ")");
            }
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    } // TODO

    @Override
    public void delete(int venueId) {
        // DELETE FROM venue WHERE venue_id = ?
        // → 공연이 연결된 venue는 FK로 막힘 (애플리케이션에서 먼저 체크)
    	String sql = "DELETE FROM venue WHERE venue_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, venueId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("공연장 삭제 성공! (ID: " + venueId + ")");
            }
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    } // TODO
}