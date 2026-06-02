package dao;
import dto.CancellationDTO;
import java.sql.Connection;

public interface CancellationDAO {
    void insert(Connection conn, CancellationDTO cancellation);  // 취소 이력 INSERT (booking UPDATE와 트랜잭션으로 묶임) — 트랜잭션용
    CancellationDTO findByBookingId(int bookingId);              // 예매 ID로 취소 이력 조회
    void updateStatus(int bookingId, String status);
}             // 취소 상태 변경 (REQUESTED→PENDING_REFUND→REFUNDED)
