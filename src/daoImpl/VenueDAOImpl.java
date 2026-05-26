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
        return new ArrayList<>(); // TODO
    }

    @Override
    public VenueDTO findById(int venueId) {
        // SELECT * FROM venue WHERE venue_id = ?
        // → 특정 공연장 조회 (수정/삭제 전 존재 확인)
        return null; // TODO
    }

    @Override
    public void insert(VenueDTO venue) {
        // INSERT INTO venue (venue_name, address) VALUES (?, ?)
        // → venue_id는 AUTO_INCREMENT라 생략
    } // TODO

    @Override
    public void update(VenueDTO venue) {
        // UPDATE venue SET venue_name = ?, address = ? WHERE venue_id = ?
    } // TODO

    @Override
    public void delete(int venueId) {
        // DELETE FROM venue WHERE venue_id = ?
        // → 공연이 연결된 venue는 FK로 막힘 (애플리케이션에서 먼저 체크)
    } // TODO
}