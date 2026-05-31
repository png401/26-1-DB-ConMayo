package controller;

import java.util.List;

import dto.PerformanceDTO;
import service.PerformanceService;
import dto.PerformanceSeatDTO;
import view.AdminView;
import view.PerformanceView;
import view.SeatView;

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
    	System.out.println("\n=== 공연 정보 수정 ===");
    	int performanceId = adminView.inputPerformanceId();

    	// 기존 공연 정보 먼저 불러옴
    	PerformanceDTO existingPerf = performanceService.getPerformance(performanceId);
        if (existingPerf == null) {
            adminView.printError("수정하려는 공연이 존재하지 않습니다.");
            return;
        }

        System.out.println("\n[현재 등록된 공연 정보]");
        System.out.println("- 제목 : " + existingPerf.getTitle());
        System.out.println("- 카테고리: " + existingPerf.getCategory());
        if (existingPerf.getStartTime() != null) {
            System.out.println("- 일시 : " + existingPerf.getStartTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
        System.out.println("- 장소 : " + (existingPerf.getVenueName() != null ? existingPerf.getVenueName() : "장소ID " + existingPerf.getVenueId()));
        System.out.println("- 판매상태: " + existingPerf.getSalesStatus());
        System.out.println("=================================");

    	System.out.println("\n--- 새로운 공연 정보를 입력하세요 ---");
    	PerformanceDTO updatedPerformance = performanceView.inputPerformanceInfo();        updatedPerformance.setPerformanceId(performanceId);
    	updatedPerformance.setPerformanceId(performanceId);
    	performanceService.modifyPerformance(updatedPerformance);

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
