package controller;
import service.PerformanceSeatService;
import view.AdminView;
public class PerformanceSeatController {
    private final PerformanceSeatService performanceSeatService;
    private final AdminView adminView;
    public PerformanceSeatController(PerformanceSeatService performanceSeatService, AdminView adminView) {
        this.performanceSeatService = performanceSeatService;
        this.adminView = adminView;
    }

    public void showSeats(int performanceId) {}  // 공연별 좌석-가격 목록 출력
    public void add() {}                         // 공연좌석 등록
    public void modifyPrice() {}                 // 좌석 가격 수정
}
