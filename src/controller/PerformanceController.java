package controller;

import java.util.List;

import dto.PerformanceDTO;
import service.PerformanceService;
import view.AdminView;
import view.PerformanceView;
import view.SeatView;

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

    // 1. 공연 목록 출력 (유저 화면 혹은 관리자 화면에 리스트 전달)
    public int showList() {
    	List<PerformanceDTO> list = performanceService.getAllPerformances();
        performanceView.printList(list);

        int performanceId = performanceView.inputPerformanceId();
        if (performanceId == 0) return 0;

        PerformanceDTO performance = performanceService.getPerformance(performanceId);
        performanceView.printDetail(performance, null);

        int action = performanceView.inputPerformanceId(); // 1. 예매하기 0. 뒤로
        if (action == 1) return performanceId;

        return 0;
    }

    // 2. 공연 상세 출력
    public void showDetail(int performanceId) {
    	PerformanceDTO performance = performanceService.getPerformance(performanceId);
        if (performance == null) {
            if (adminView != null) adminView.printError("해당 공연을 찾을 수 없습니다.");
            return;
        }
        System.out.println("공연명: " + performance.getTitle());
    }
    
    // 3. 공연 등록 (관리자 화면에서 입력값 받아오기)
    public void add() {
    	PerformanceDTO performance = performanceView.inputPerformanceInfo(); // 입력 받기
        performanceService.addPerformance(performance);                      // DB 저장
        adminView.printSuccess("공연 등록 성공");
        showList();
    }
    
    // 4. 공연 수정 (관리자)
    public void modify() {
    	if (adminView == null) return;
        int performanceId = adminView.inputPerformanceId();
        PerformanceDTO existing = performanceService.getPerformance(performanceId);
        if (existing == null) {
            adminView.printError("해당 공연을 찾을 수 없습니다.");
            return;
        }
        PerformanceDTO updated = performanceView.inputPerformanceInfo(); // 새 정보 입력
        updated.setPerformanceId(performanceId);
        performanceService.modifyPerformance(updated);
        adminView.printSuccess("공연 수정 성공");
        showList();
    }
    
    // 5. 공연 삭제 (관리자)
    public void remove() {
    	if (adminView == null) return;
        int performanceId = adminView.inputPerformanceId();

        performanceService.removePerformance(performanceId);
        adminView.printSuccess("공연이 성공적으로 삭제되었습니다.");
        showList();
    }
}
