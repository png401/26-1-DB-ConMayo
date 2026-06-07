package daoImpl;

import dao.VenueDAO;

import db.DatabaseConnector;
import dto.VenueDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class VenueDAOImpl implements VenueDAO {

    // conn 필드 제거 — 매번 getConnection() 호출

    @Override
    public List<VenueDTO> findAll() {
        // SELECT * FROM venue
        // → 관리자 공연장 목록 조회
        List<VenueDTO> list = new ArrayList<>();
        String sql = "SELECT venue_id, venue_name, address FROM venue ORDER BY venue_id ASC";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
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
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public VenueDTO findById(int venueId) {
        // SELECT * FROM venue WHERE venue_id = ?
        // → 특정 공연장 조회 (수정/삭제 전 존재 확인)
        String sql = "SELECT venue_id, venue_name, address FROM venue WHERE venue_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
            e.printStackTrace();
        }
        return null;
    }
    
    //수정 - 공연장 삽입 이후 공연장 id를 반환하기
    @Override
    public int insert(VenueDTO venue) {
        // INSERT INTO venue (venue_name, address) VALUES (?, ?)
        // → venue_id는 AUTO_INCREMENT라 생략
        String sql = "INSERT INTO venue (venue_name, address) VALUES (?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement pstmt =
                        conn.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )) {

               pstmt.setString(1, venue.getVenueName());
               pstmt.setString(2, venue.getAddress());

               pstmt.executeUpdate();

               try (ResultSet rs = pstmt.getGeneratedKeys()) {
                   if (rs.next()) {
                       int venueId = rs.getInt(1);

                       System.out.println("공연장 등록 성공: " + venue.getVenueName());
                       System.out.println("생성된 공연장 ID = " + venueId);

                       return venueId;
                   }
               }

               throw new RuntimeException("생성된 공연장 ID를 가져오지 못했습니다.");

           } catch (SQLException e) {
               throw new RuntimeException(
                       "공연장 등록 실패: " + e.getMessage(),
                       e
               );
           }
    }

    @Override
    public void update(VenueDTO venue) {
        // UPDATE venue SET venue_name = ?, address = ? WHERE venue_id = ?
        String sql = "UPDATE venue SET venue_name = ?, address = ? WHERE venue_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, venue.getVenueName());
            pstmt.setString(2, venue.getAddress());
            pstmt.setInt(3, venue.getVenueId());
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("공연장 정보 수정 성공! (ID: " + venue.getVenueId() + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int venueId) {
        // DELETE FROM venue WHERE venue_id = ?
        // → 공연이 연결된 venue는 FK로 막힘 (애플리케이션에서 먼저 체크)
        String sql = "DELETE FROM venue WHERE venue_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, venueId);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("공연장 삭제 성공! (ID: " + venueId + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}