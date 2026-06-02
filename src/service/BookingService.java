package service;

import dao.BookingDAO;
import dao.CancellationDAO;
import db.TransactionManager;
import dto.BookingDTO;
import dto.BookingStatus;
import dto.CancellationDTO;
import dto.CancelStatus;

import java.sql.Connection;
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
            Connection conn = tm.begin();
            bookingDAO.lockSeat(conn, booking.getPerformanceSeatId());
            if (bookingDAO.isAlreadyBooked(conn, booking.getPerformanceSeatId())) {
                throw new RuntimeException("이미 예매된 좌석입니다.");
            }
            bookingDAO.insert(conn, booking);
            bookingDAO.updateStatus(conn, booking.getBookingId(), BookingStatus.BOOKED.name());
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
        BookingDTO booking = bookingDAO.findById(bookingId); // 트랜잭션 밖 — 그냥 getConnection()
        if (booking == null)
            throw new RuntimeException("존재하지 않는 예매입니다.");
        if (booking.getBookingStatus() != BookingStatus.BOOKED)
            throw new RuntimeException("취소 가능한 예매가 아닙니다.");

        try {
            Connection conn = tm.begin(); // conn 받아서
            bookingDAO.updateStatus(conn, bookingId, BookingStatus.CANCELED.name()); // conn 넘김
            CancellationDTO cancellation = new CancellationDTO();
            cancellation.setBookingId(bookingId);
            cancellation.setRefundAmount(booking.getPayment() - cancelFee);
            cancellation.setCancellationFee(cancelFee);
            cancellation.setCancelStatus(CancelStatus.REQUESTED);
            cancellationDAO.insert(conn, cancellation); // conn 넘김
            tm.commit();
        } catch (Exception e) {
            tm.rollback();
            throw new RuntimeException("취소 처리 중 오류 발생: " + e.getMessage(), e);
        } finally {
            tm.end();
        }
    }
}