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
        return new ArrayList<>(); // TODO
    }

    @Override
    public void insert(PerformanceSeatDTO perfSeat) {
        // INSERT INTO performance_seat (performance_id, seat_id, price)
        // → venue 교차 검증은 Service에서 처리 후 호출
    } // TODO

    @Override
    public void updatePrice(int performanceSeatId, int price) {
        // UPDATE performance_seat SET price = ? WHERE performance_seat_id = ?
        // → 관리자 가격 수정
    } // TODO
}