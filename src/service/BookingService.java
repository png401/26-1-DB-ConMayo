package service;

import dao.BookingDAO;
import dao.CancellationDAO;
import dto.BookingDTO;
import dto.BookingStatus;
import dto.CancellationDTO;
import dto.CancelStatus;

import java.util.List;

public class BookingService {
    private final BookingDAO bookingDAO;
    private final CancellationDAO cancellationDAO;

    public BookingService(BookingDAO bookingDAO, CancellationDAO cancellationDAO) {
        this.bookingDAO = bookingDAO;
        this.cancellationDAO = cancellationDAO;
    }

    public void book(BookingDTO booking) {
        bookingDAO.insert(booking);
        bookingDAO.updateStatus(booking.getBookingId(), BookingStatus.BOOKED.name());
        booking.setBookingStatus(BookingStatus.BOOKED);
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

        bookingDAO.updateStatus(bookingId, BookingStatus.CANCELED.name());

        CancellationDTO cancellation = new CancellationDTO();
        cancellation.setBookingId(bookingId);
        cancellation.setRefundAmount(booking.getPayment() - cancelFee);
        cancellation.setCancellationFee(cancelFee);
        cancellation.setCancelStatus(CancelStatus.REQUESTED);
        cancellationDAO.insert(cancellation);
    }
}