package view;

import controller.BookingController;
import controller.SeatController;
import dto.SeatDTO;
import java.util.List;

public class SeatView {
    private final SeatController seatController;
    private final BookingController bookingController;

    public SeatView(SeatController seatController, BookingController bookingController) {
        this.seatController = seatController;
        this.bookingController = bookingController;
    }

    public void showSeatPanel(List<SeatDTO> seats, int availableCount, String memberId) {
        SeatPanel seatPanel = new SeatPanel(seats, availableCount, seatController, bookingController, memberId);
        seatPanel.setVisible(true);
    }
}