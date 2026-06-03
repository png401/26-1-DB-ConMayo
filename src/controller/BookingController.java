package controller;

import dto.BookingDTO;
import dto.BookingStatus;
import dto.CancellationDTO;
import service.BookingService;
import service.CancellationService;
import view.BookingView;
import view.CancellationView;
import controller.ReviewController;
import db.DatabaseConnector;

import java.util.List;

public class BookingController {
    private final BookingService bookingService;
    private final BookingView bookingView;

    public BookingController(BookingService bookingService, BookingView bookingView) {
        this.bookingService = bookingService;
        this.bookingView = bookingView;
    }
    
    /*
    public void book(int performanceSeatId) {
        // SeatPanel(Swing)에서 memberId와 payment를 함께 넘겨줄 때 사용
        // 실제 호출은 아래 오버로드 메서드로
    }*/

    public void book(String memberId, int performanceSeatId, int payment) {
        BookingDTO booking = new BookingDTO();
        booking.setMemberId(memberId);
        booking.setPerformanceSeatId(performanceSeatId);
        booking.setBookingStatus(BookingStatus.HOLD);
        booking.setPayment(payment);
        // 예외를 위로 던져서 SeatPanel에서 처리
        bookingService.book(booking);
    }

    public boolean showMyBookings(String memberId, ReviewController reviewController) { 
        List<BookingDTO> list = bookingService.getMyBookings(memberId);
        bookingView.printMyBookings(list);

        if (list.isEmpty()) return false;

        int index = -1;
        while(true) {
            index = bookingView.inputBookingIndex();
            if (index == 0) return false;
            if (index >= 1 && index <= list.size()) break;
            
            bookingView.printError("존재하지 않는 예매 선택 번호입니다. 다시 확인해 주세요.");
            System.out.println();
        }
        
        BookingDTO selected = list.get(index - 1);
        int action = bookingView.inputAction();
            
        switch (action) {
            case 1 -> {
                // handleCancel의 결과를 상위 MemberController로 토스하기 위해 return 문 배치
                return handleCancel(selected);
            }
            case 2 -> {
                if(selected.getBookingStatus() != BookingStatus.BOOKED){
                    bookingView.printError("예매 완료 상태에서만 리뷰를 작성할 수 있습니다");
                } else {
                    reviewController.writeReview(selected.getBookingId());
                }
            }
            case 0 -> { }
            default -> bookingView.printError("올바른 번호를 입력해주세요.");
        }       
        return false;
    }

    private boolean handleCancel(BookingDTO booking) {
        CancellationView cancelView = new CancellationView();
        if (!cancelView.confirmCancel(booking)) {
            System.out.println("취소를 중단했습니다.");
            return false;
        }
        int fee = (int)(booking.getPayment() * 0.1);
        try {
            boolean blacklisted = bookingService.cancel(booking.getBookingId(), fee);
        
            bookingView.printSuccess("예매가 취소되었습니다.");
            
            if (blacklisted) {
                bookingView.printError("최근 7일간 취소 횟수가 3회 이상이므로 블랙리스트로 등록되었습니다.");
                DatabaseConnector.reset();
                return true; // 블랙리스트 감지 신호 발송
            }
            
            return false;
            
        } catch (Exception e) {
            bookingView.printError("취소 실패: " + e.getMessage());
            return false;
        }
    }
}