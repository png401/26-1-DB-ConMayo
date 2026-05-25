package daoImpl;

import dao.CancellationDAO;
import dto.CancellationDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CancellationDAOImpl implements CancellationDAO {
    private final Connection conn;

    public CancellationDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(CancellationDTO cancellation) {
        // INSERT INTO cancellation (booking_id, refund_amount, cancellation_fee, cancel_status)
        // → canceled_at은 DEFAULT CURRENT_TIMESTAMP
        // → BookingService.cancel()에서 booking UPDATE와 트랜잭션으로 묶어서 호출
    } // TODO

    @Override
    public CancellationDTO findByBookingId(int bookingId) {
        // SELECT * FROM cancellation WHERE booking_id = ?
        // → 마이페이지에서 취소 이력 조회
        return null; // TODO
    }

    @Override
    public void updateStatus(int bookingId, String status) {
        // UPDATE cancellation SET cancel_status = ? WHERE booking_id = ?
        // → REQUESTED → PENDING_REFUND → REFUNDED 순서로 상태 변경
    } // TODO
}
