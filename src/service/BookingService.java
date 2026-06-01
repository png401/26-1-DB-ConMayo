package service;

import dao.BookingDAO;
import dao.CancellationDAO;
import db.TransactionManager;
import dto.BookingDTO;
import dto.BookingStatus;
import dto.CancellationDTO;
import dto.CancelStatus;

import java.util.List;

public class BookingService {
    private final BookingDAO bookingDAO;
    private final CancellationDAO cancellationDAO;
    private final TransactionManager tm;

    public BookingService(BookingDAO bookingDAO, CancellationDAO cancellationDAO, TransactionManager tm) {
        this.bookingDAO = bookingDAO;
        this.cancellationDAO = cancellationDAO;
        this.tm = tm;
    }

    public void book(BookingDTO booking) {
    	try {
            tm.begin();
            bookingDAO.lockSeat(booking.getPerformanceSeatId()); // 락 획득
         // 이미 BOOKED/HOLD인지 확인
            if (bookingDAO.isAlreadyBooked(booking.getPerformanceSeatId())) {
                throw new RuntimeException("이미 예매된 좌석입니다.");
            }

            bookingDAO.insert(booking);
            bookingDAO.updateStatus(booking.getBookingId(), BookingStatus.BOOKED.name());
            booking.setBookingStatus(BookingStatus.BOOKED);
            tm.commit();
        } catch (Exception e) {
            tm.rollback();
            throw new RuntimeException("예매 실패: " + e.getMessage(), e);
        } finally {
            tm.end();
        }
    }

    public List<BookingDTO> getMyBookings(String memberId) {
        return bookingDAO.findByMemberId(memberId);
    }

    public int getAvailableCount(int performanceId) {
        return bookingDAO.getAvailableCount(performanceId);
    }

    public void cancel(int bookingId, int cancelFee) {
    	BookingDTO booking = bookingDAO.findById(bookingId);
        if (booking == null)
            throw new RuntimeException("존재하지 않는 예매입니다.");
        if (booking.getBookingStatus() != BookingStatus.BOOKED)
            throw new RuntimeException("취소 가능한 예매가 아닙니다.");

        try {
            tm.begin();
            bookingDAO.updateStatus(bookingId, BookingStatus.CANCELED.name());

            CancellationDTO cancellation = new CancellationDTO();
            cancellation.setBookingId(bookingId);
            cancellation.setRefundAmount(booking.getPayment() - cancelFee);
            cancellation.setCancellationFee(cancelFee);
            cancellation.setCancelStatus(CancelStatus.REQUESTED);
            cancellationDAO.insert(cancellation);

            tm.commit();
        } catch (Exception e) {
            tm.rollback();
            throw new RuntimeException("취소 처리 중 오류 발생: " + e.getMessage(), e);
        } finally {
            tm.end();
        }
    }
}