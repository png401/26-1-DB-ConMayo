package daoImpl;

import dao.PerformanceDAO;
import dto.PerformanceDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        return new ArrayList<>(); // TODO
    }

    @Override
    public List<PerformanceDTO> findByCategory(String category) {
        // SELECT * FROM performance WHERE category = ?
        // → 카테고리 필터 조회 (콘서트/뮤지컬/스포츠)
        return new ArrayList<>(); // TODO
    }

    @Override
    public PerformanceDTO findById(int performanceId) {
        // SELECT * FROM performance WHERE performance_id = ?
        // → 공연 상세 조회
        return null; // TODO
    }

    @Override
    public void insert(PerformanceDTO performance) {
        // INSERT INTO performance (title, category, start_time, running_time,
        //   sales_status, booking_open, venue_id) VALUES (?,?,?,?,?,?,?)
    } // TODO

    @Override
    public void update(PerformanceDTO performance) {
        // UPDATE performance SET title=?, category=?, ... WHERE performance_id=?
    } // TODO

    @Override
    public void delete(int performanceId) {
        // DELETE FROM performance WHERE performance_id = ?
        // → 연결된 performance_seat, booking이 있으면 FK로 막힘
    } // TODO
}