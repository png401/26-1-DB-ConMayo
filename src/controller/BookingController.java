package controller;

import dto.BookingDTO;
import dto.BookingStatus;
import dto.CancellationDTO;
import service.BookingService;
import service.CancellationService;
import view.BookingView;
import view.CancellationView;

import java.util.List;

public class BookingController {
    private final BookingService bookingService;
    private final BookingView bookingView;

    public BookingController(BookingService bookingService, BookingView bookingView) {
        this.bookingService = bookingService;
        this.bookingView = bookingView;
    }

    public void book(int performanceSeatId) {
        // SeatPanel(Swing)에서 memberId와 payment를 함께 넘겨줄 때 사용
        // 실제 호출은 아래 오버로드 메서드로
    }

    public void book(String memberId, int performanceSeatId, int payment) {
        BookingDTO booking = new BookingDTO();
        booking.setMemberId(memberId);
        booking.setPerformanceSeatId(performanceSeatId);
        booking.setBookingStatus(BookingStatus.HOLD);
        booking.setPayment(payment);
        try {
            bookingService.book(booking);
            bookingView.printSuccess("예매가 완료되었습니다! 예매ID: " + booking.getBookingId());
        } catch (Exception e) {
            bookingView.printError("예매 실패: " + e.getMessage());
        }
    }

    public void showMyBookings(String memberId) {
        List<BookingDTO> list = bookingService.getMyBookings(memberId);
        bookingView.printMyBookings(list);

        if (list.isEmpty()) return;

        int index = bookingView.inputBookingIndex(list.size());
        if (index == 0) return;

        BookingDTO selected = list.get(index - 1);
        int action = bookingView.inputAction();

        switch (action) {
            case 1 -> handleCancel(selected);
            case 2 -> System.out.println("[리뷰 작성] ReviewController로 위임");
            case 0 -> { }
            default -> bookingView.printError("올바른 번호를 입력해주세요.");
        }
    }

    private void handleCancel(BookingDTO booking) {
        CancellationView cancelView = new CancellationView();
        if (!cancelView.confirmCancel(booking)) {
            System.out.println("취소를 중단했습니다.");
            return;
        }
        int fee = (int)(booking.getPayment() * 0.1);
        try {
            bookingService.cancel(booking.getBookingId(), fee);
            bookingView.printSuccess("예매가 취소되었습니다.");
        } catch (Exception e) {
            bookingView.printError("취소 실패: " + e.getMessage());
        }
    }
}