package controller;

import service.SeatService;
import dto.SeatDTO;
import java.util.List;

public class SeatController {
    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }
    /*
    [오픈] 대기 순번 0이 되면 view에서 호출함
    seatservice -> seatDAO -> DB에서 공연별 좌석 목록 가져옴
    각 좌석에 avgRating, color 세팅된 상태로 반환
    view(seatPanel)은 이 리스트 받아서 좌석 버튼 색상대로 그리기만 하면 됨
    흐름 : view -> seatcontroller.openSeatPanel() -> SeatService -> SeatDAO -> DB
     */
    public List<SeatDTO> openSeatPanel(int performanceId) {
        return seatService.getSeatsByPerformance(performanceId);
    }

    /*
    [선택] 사용자가 좌석 버튼 클릭하면 view에서 호출
    일단은 클릭한 좌석 그대로 반환
    TODO : BookingController.startBooking(seat)으로 넘겨서 예매 진행하기
    흐름 : 좌석 클릭 -> view -> seatcontroller.onSeatSelected() -> BookingController
     */
    public SeatDTO onSeatSelected(SeatDTO seat) {
        return seat; // 나중에 바꿔야 함
    }

    /*
    [조회] 대기화면에서 잔여석 수 표시할 때 호출
    대기 중에도 실시간으로 잔여석 수 바뀌니까 주기적으로 이 메소드 호출해서 화면 갱신해야함
    흐름 : 대기화면 -> seatcontroller.getAvailableCount() -> SeatService -> SeatDAO -> DB
     */
    public int getAvailableCount(int performanceId) {
        return seatService.getAvailableCount(performanceId);
    }
}