package controller;
import service.SeatService;
import dto.SeatDTO;
public class SeatController {
    private final SeatService seatService;
    public SeatController(SeatService seatService) { this.seatService = seatService; }

    public void openSeatPanel(int performanceId) {}  // 대기 순번 0 되면 Swing SeatPanel 팝업 오픈
    public void onSeatSelected(SeatDTO seat) {}      // 좌석 클릭 시 선택 처리
}