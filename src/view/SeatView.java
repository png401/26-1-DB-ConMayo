package view;

import controller.BookingController;
import controller.ReviewController;
import controller.SeatController;
import dto.SeatDTO;
import java.util.List;

public class SeatView {
    private final SeatController seatController;
    private final BookingController bookingController;
    private final ReviewController reviewController;

    public SeatView(SeatController seatController, BookingController bookingController, ReviewController reviewController) {
        this.seatController = seatController;
        this.bookingController = bookingController;
        this.reviewController = reviewController;
    }
    
    //[예매용]
    public void showSeatPanel(List<SeatDTO> seats, int availableCount, String memberId) {
    	SeatPanel seatPanel = new SeatPanel(
                seats, availableCount,
                seatController, bookingController,
                reviewController, // 추가
                memberId,
               false);//reviewOnly 아님 
        seatPanel.setVisible(true);
    }
    
    //[리뷰 조회용]
    public void showReviewOnlyPanel(List<SeatDTO> seats, int availableCount, String memberId) {
        SeatPanel seatPanel = new SeatPanel(
                seats, availableCount,
                seatController, bookingController,
                reviewController,
                memberId,
                true); // reviewOnly = true
        seatPanel.setVisible(true);
    }
    
}