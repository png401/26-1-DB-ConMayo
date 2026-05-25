package controller;
import service.BookingService;
import view.BookingView;
public class BookingController {
    private final BookingService bookingService;
    private final BookingView bookingView;
    public BookingController(BookingService bookingService, BookingView bookingView) {
        this.bookingService = bookingService;
        this.bookingView = bookingView;
    }

    public void book(int performanceSeatId) {}       // 예매 처리
    public void cancel(int bookingId) {}             // 예매 취소 처리 (수수료 계산 포함)
    public void showMyBookings(String memberId) {}   // 내 예매 내역 출력
}