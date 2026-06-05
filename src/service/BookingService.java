package service;

import dao.BookingDAO; //이미 존재
import dao.CancellationDAO;
import db.TransactionManager;
import dto.BookingDTO;
import dto.BookingStatus;
import dto.CancellationDTO;
import dto.CancelStatus;

import java.sql.Connection;
import java.util.List;
import java.time.LocalDate; //추가
import java.time.LocalDateTime; //추가
import java.time.temporal.ChronoUnit; //추가

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

    public boolean cancel(int bookingId, int cancelFee) {
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
            cancellation.setCancelStatus(CancelStatus.REFUNDED); //수정
            cancellationDAO.insert(conn, cancellation); // conn 넘김
            
            tm.commit();
            
            return bookingDAO.isMemberBlacklisted(bookingId);
        } catch (Exception e) {
            tm.rollback();
            throw new RuntimeException("취소 처리 중 오류 발생: " + e.getMessage(), e);
        } finally {
            tm.end();
        }
    }
    
    //수수료 계산 메서드 추가
    public int calculateCancellationFee(BookingDTO booking) {

        LocalDateTime startTime =
                bookingDAO.getPerformanceStartTime(
                        booking.getBookingId()
                );

        long daysUntilPerformance =
                ChronoUnit.DAYS.between(
                        LocalDate.now(),
                        startTime.toLocalDate()
                );
        
        double rate;

        if (daysUntilPerformance < 0) {
            throw new RuntimeException("공연 종료 후에는 취소할 수 없습니다.");
        }
        else if (daysUntilPerformance == 0) {
            rate = 0.70;
        }
        else if (daysUntilPerformance <= 2) {
            rate = 0.30;
        }
        else if (daysUntilPerformance <= 6) {
            rate = 0.20;
        }
        else if (daysUntilPerformance <= 9) {
            rate = 0.10;
        }
        else {
            rate = 0.07;
        }
        return (int)(booking.getPayment() * rate);
    }
}