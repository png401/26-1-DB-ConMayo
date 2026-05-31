package controller;

import java.util.List;

import dto.PerformanceDTO;
import service.PerformanceService;
import view.AdminView;
import view.PerformanceView;

public class PerformanceController {
    private final PerformanceService performanceService;  
    private final PerformanceView performanceView;        
    private final AdminView adminView;

    public PerformanceController(PerformanceService performanceService,
                                 PerformanceView performanceView,
                                 AdminView adminView) {
        this.performanceService = performanceService;
        this.performanceView = performanceView;
        this.adminView = adminView;
    }

    // 1. 공연 목록 출력 
    public void showList() {
    	List<PerformanceDTO> list = performanceService.getAllPerformances();
    	
    	if (performanceView != null) {
            performanceView.printList(list);
        } else if (adminView != null) {
            System.out.println("--- 공연 목록 리스트 출력 ---");
            for (PerformanceDTO p : list) {
                System.out.println("[" + p.getPerformanceId() + "] " + p.getTitle() + " (" + p.getSalesStatus() + ")");
            }
        }
    }

    // 2. 공연 상세 출력
    public void showDetail(int performanceId) {
    	PerformanceDTO performance = performanceService.getPerformance(performanceId);
        if (performance == null) {
            if (adminView != null) adminView.printError("해당 공연을 찾을 수 없습니다.");
            return;
        }
        
    }
    // 3. 공연 등록 (관리자 화면에서 입력값 받아오기)
    public void add() {
    	if (adminView == null) return;
    	PerformanceDTO newPerformance = performanceView.inputPerformanceInfo();
    	performanceService.addPerformance(newPerformance);
    	if (adminView != null) {
            adminView.printSuccess("공연 등록 성공");
        }
    	
        showList();
    }
    
    // 4. 공연 수정 (관리자)
    public void modify() {
    	if (adminView == null) return;
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
