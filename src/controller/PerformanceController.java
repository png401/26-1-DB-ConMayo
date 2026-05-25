package controller;

import service.PerformanceService;
import view.AdminView;
import view.PerformanceView;

public class PerformanceController {
    private final PerformanceService performanceService;  // 클래스명은 그대로
    private final PerformanceView performanceView;        // 클래스명은 그대로
    private final AdminView adminView;

    public PerformanceController(PerformanceService performanceService,
                                 PerformanceView performanceView,
                                 AdminView adminView) {
        this.performanceService = performanceService;
        this.performanceView = performanceView;
        this.adminView = adminView;
    }

    public void showList() {}                    // 공연 목록 출력
    public void showDetail(int performanceId) {} // 공연 상세 출력
    public void add() {}                         // 공연 등록 (관리자)
    public void modify() {}                      // 공연 수정 (관리자)
    public void remove() {}                      // 공연 삭제 (관리자)
}
