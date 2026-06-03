package controller;
import java.util.List;

import dto.PerformanceDTO;
import dto.PerformanceSeatDTO;
import service.PerformanceService;
import service.PerformanceSeatService;
import view.AdminView;
public class PerformanceSeatController {
    private final PerformanceSeatService performanceSeatService;
    private final PerformanceService performanceService;
    private final AdminView adminView;
    public PerformanceSeatController(PerformanceSeatService performanceSeatService, PerformanceService performanceService, AdminView adminView) {
        this.performanceSeatService = performanceSeatService;
        this.performanceService = performanceService;
        this.adminView = adminView;
    }

    // 1. 공연별 좌석-가격 목록 출력
    public void showSeats(int performanceId) {
    	List<PerformanceSeatDTO> seatList = performanceSeatService.getSeatsByPerformance(performanceId);
    	PerformanceDTO perf = performanceService.getPerformance(performanceId);
    	String title = (perf != null) ? perf.getTitle() : "알 수 없는 공연";
        if (adminView != null) {
            adminView.printPerfSeatList(performanceId, title, seatList);
        }
    }
    
    // 2. 공연좌석 등록 (관리자가 특정 공연에 특정 좌석과 가격을 연결해 배치할 때)
    public void add() {
    	if (adminView == null) return;
        // DTO 조립 예시 (팀원 연동에 맞춤)
        PerformanceSeatDTO perfSeat = new PerformanceSeatDTO(0, adminView.inputPerformanceId(), 1, adminView.inputPrice());

        performanceSeatService.addPerfSeat(perfSeat);
        adminView.printSuccess("공연 좌석 가격이 등록되었습니다.");
        showSeats(perfSeat.getPerformanceId());
    }

    // 3. 좌석 가격 수정
    public void modifyPrice() {
    	if (adminView == null) return;
    	
    	int subMenu = adminView.showSeatPriceMenu();
        int targetPerformanceId = 0;
        
        if (subMenu == 1) { // 공연ID 입력
            targetPerformanceId = adminView.inputPerformanceId();
        } else if (subMenu == 2) { 
            // 공연장 이름 검색어 입력받기 및 결과 출력
            String keyword = adminView.inputPerformanceTitleKeyword();
            List<PerformanceDTO> searchResult = performanceService.getPerformancesByTitleKeyword(keyword);

            if (searchResult.isEmpty()) {
                adminView.printError("검색 결과가 존재하지 않습니다.");
                return;
            }
            
            for (PerformanceDTO perf : searchResult) {
                System.out.printf("공연 ID: %d | 제목: %s\n", perf.getPerformanceId(), perf.getTitle());
            }

            // 출력된 결과를 보고 사용자가 공연 ID 선택
            targetPerformanceId = adminView.inputPerformanceId();
        } else {
            adminView.printError("잘못된 선택입니다.");
            return;
        }
        
        // 선택한 공연의 존재 여부
        PerformanceDTO targetPerf = performanceService.getPerformance(targetPerformanceId);
        if (targetPerf == null) {
            adminView.printError("존재하지 않는 공연 ID입니다.");
            return;
        }
        showSeats(targetPerformanceId);

    	int perfSeatId = adminView.inputPerformanceSeatId(); 
        if (perfSeatId <= 0) {
            adminView.printError("잘못된 공연좌석 번호입니다.");
            return;
        }
        
        int newPrice = adminView.inputPrice();
        if (newPrice < 0) {
            adminView.printError("수정할 가격은 0원 이상이어야 합니다.");
            return;
        }

        try {
            performanceSeatService.modifyPrice(perfSeatId, newPrice);
            adminView.printSuccess("좌석 가격이 정상적으로 수정되었습니다.");
        } catch (Exception e) {
            adminView.printError("가격 수정 중 오류 발생: " + e.getMessage());
        }
    }
}
