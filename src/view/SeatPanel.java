package view;
import controller.SeatController;
import dto.SeatDTO;
import javax.swing.JDialog;
import java.util.List;
public class SeatPanel extends JDialog {
    private final SeatController seatController;
    private final List<SeatDTO> seats;
    private final int availableCount;

    // seats: 좌석 목록 (평점/색상 포함), availableCount: 잔여석 수, controller: 좌석 선택 이벤트 처리
    public SeatPanel(List<SeatDTO> seats, int availableCount, SeatController seatController) {
        this.seats = seats;
        this.availableCount = availableCount;
        this.seatController = seatController;
        // TODO: Swing UI 초기화 (좌석 버튼 그리드, 색상 표시)
    }
}
