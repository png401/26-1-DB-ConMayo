package controller;
import java.util.List;

import dto.PerformanceSeatDTO;
import service.PerformanceSeatService;
import view.AdminView;
public class PerformanceSeatController {
    private final PerformanceSeatService performanceSeatService;
    private final AdminView adminView;
    public PerformanceSeatController(PerformanceSeatService performanceSeatService, AdminView adminView) {
        this.performanceSeatService = performanceSeatService;
        this.adminView = adminView;
    }

    // 1. 공연별 좌석-가격 목록 출력
    public void showSeats(int performanceId) {
    	List<PerformanceSeatDTO> seatList = performanceSeatService.getSeatsByPerformance(performanceId);
        if (adminView != null) {
            adminView.printPerfSeatList(seatList);
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
        int perfSeatId = 1; 
        int newPrice = adminView.inputPrice();

        performanceSeatService.modifyPrice(perfSeatId, newPrice);
        adminView.printSuccess("좌석 가격이 정상적으로 수정되었습니다.");
    }
}
