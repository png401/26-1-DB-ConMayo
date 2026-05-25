package daoImpl;

import dao.BookingDAO;
import dto.BookingDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {
    private final Connection conn;

    public BookingDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(BookingDTO booking) {
        // INSERT INTO booking (member_id, performance_seat_id, booking_status, payment)
        // → 예매 시작 시 HOLD 상태로 INSERT
        // → 결제 완료 후 updateStatus()로 BOOKED로 변경
    } // TODO

    @Override
    public BookingDTO findById(int bookingId) {
        // SELECT * FROM booking WHERE booking_id = ?
        // → 취소 처리 전 예매 정보 확인, 리뷰 작성 시 상태 체크
        return null; // TODO
    }

    @Override
    public List<BookingDTO> findByMemberId(String memberId) {
        // SELECT * FROM booking WHERE member_id = ? ORDER BY booked_at DESC
        // → 마이페이지 예매 내역 조회
        return new ArrayList<>(); // TODO
    }

    @Override
    public void updateStatus(int bookingId, String status) {
        // UPDATE booking SET booking_status = ? WHERE booking_id = ?
        // → HOLD → BOOKED (결제 완료)
        // → BOOKED → CANCELED (취소, cancellation INSERT와 트랜잭션으로 묶임)
    } // TODO

    @Override
    public int getAvailableCount(int performanceId) {
        // 잔여석 수 조회 (대기 화면에 실시간 표시)
        // SeatDAOImpl.getAvailableCount()와 동일한 쿼리
        return 0; // TODO
    }
}