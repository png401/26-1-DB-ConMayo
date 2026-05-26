package service;
import dao.BookingDAO;
import dao.CancellationDAO;
import dto.BookingDTO;
import java.util.List;
public class BookingService {
    private final BookingDAO bookingDAO;
    public BookingService(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

    public void book(BookingDTO booking) {}                              // 예매 처리 (HOLD → BOOKED 트랜잭션)
    public List<BookingDTO> getMyBookings(String memberId) { return null; } // 내 예매 내역 조회
    public int getAvailableCount(int performanceId) { return 0; }      // 잔여석 수 조회
}
