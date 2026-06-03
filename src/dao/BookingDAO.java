package dao;
import dto.BookingDTO;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingDAO {
    void insert(Connection conn, BookingDTO booking);                        // 예매 INSERT (HOLD 상태로 시작) — 트랜잭션용
    BookingDTO findById(int bookingId);                                      // 예매 ID로 1건 조회
    List<BookingDTO> findByMemberId(String memberId);                        // 회원의 예매 내역 전체 조회
    void updateStatus(Connection conn, int bookingId, String status);        // 예매 상태 변경 (HOLD→BOOKED, BOOKED→CANCELED) — 트랜잭션용
    int getAvailableCount(int performanceId);                                // 잔여석 수 조회
    LocalDateTime getPerformanceStartTime(int bookingId);                    // 추가 - 예매 ID로 공연 시작 시간 조회
    void lockSeat(Connection conn, int performanceSeatId);                   // FOR UPDATE 락 — 트랜잭션용
    boolean isAlreadyBooked(Connection conn, int performanceSeatId);         // 트랜잭션용
	boolean isMemberBlacklisted(int bookingId);
}
