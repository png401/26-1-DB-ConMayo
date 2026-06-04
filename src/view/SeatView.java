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

    public void showSeatPanel(List<SeatDTO> seats, int availableCount, String memberId) {
    	SeatPanel seatPanel = new SeatPanel(
                seats, availableCount,
                seatController, bookingController,
                reviewController, // 추가
                memberId);
        seatPanel.setVisible(true);
    }
}