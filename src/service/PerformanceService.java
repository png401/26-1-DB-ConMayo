// PerformanceService.java
package service;
import dao.PerformanceDAO;
import dto.PerformanceDTO;
import dto.SalesStatus;

import java.time.LocalDateTime;
import java.util.List;
public class PerformanceService {
    private final PerformanceDAO performanceDAO;
    public PerformanceService(PerformanceDAO performanceDAO) { this.performanceDAO = performanceDAO; }

    // 1. 공연 전체 목록 조회
    public List<PerformanceDTO> getAllPerformances() { 
        List<PerformanceDTO> list = performanceDAO.findAll();
        for (PerformanceDTO perf : list) {
            adjustSalesStatus(perf);
        }
        return list; 
    }

	// 2. 카테고리 필터 조회
    public List<PerformanceDTO> getByCategory(String category) { 
        List<PerformanceDTO> list = performanceDAO.findByCategory(category);
        for (PerformanceDTO perf : list) {
            adjustSalesStatus(perf);
        }
        return list; 
    }
    
    // 3. 공연 상세 조회
    public PerformanceDTO getPerformance(int performanceId) { 
        PerformanceDTO perf = performanceDAO.findById(performanceId);
        if (perf != null) {
            adjustSalesStatus(perf);
        }
        return perf; 
    }
    
    // 4. 공연 등록 (관리자)
    public void addPerformance(PerformanceDTO performance) {
        if (performance == null || performance.getTitle() == null || performance.getTitle().trim().isEmpty()) {
            System.out.println("오류: 올바른 공연 정보를 입력하세요.");
            return;
        }
        performanceDAO.insert(performance);
    }
    
    // 5. 공연 수정 (관리자)
    public void modifyPerformance(PerformanceDTO performance) {
        PerformanceDTO exist = performanceDAO.findById(performance.getPerformanceId());
        if (exist == null) {
            System.out.println("오류: 수정하려는 공연이 존재하지 않습니다.");
            return;
        }
        performanceDAO.update(performance);
    }
    
    // 6. 공연 삭제 (관리자)
    public void removePerformance(int performanceId) { 
        PerformanceDTO exist = performanceDAO.findById(performanceId);
        if (exist == null) {
            System.out.println("오류: 삭제하려는 공연이 존재하지 않습니다.");
            return;
        }
        performanceDAO.delete(performanceId);
    }
    
    private void adjustSalesStatus(PerformanceDTO perf) {
        LocalDateTime now = LocalDateTime.now();

        // 1. 공연 시작 시간이 현재 시간보다 이전이면 -> 마감(CLOSED)
        if (perf.getStartTime() != null && perf.getStartTime().isBefore(now)) {
            perf.setSalesStatus(SalesStatus.CLOSED);
        } 
        // 2. 예매 오픈 시간이 현재 시간보다 미래이면 -> 오픈 예정(COMING_SOON)
        else if (perf.getBookingOpen() != null && perf.getBookingOpen().isAfter(now)) {
            perf.setSalesStatus(SalesStatus.COMING_SOON);
        }
        // 3. 그 외의 경우 중 매진(SOLD_OUT)이 아니라면 -> 예매중(OPEN) 상태 유지
        else if (perf.getSalesStatus() != SalesStatus.SOLD_OUT) {
            perf.setSalesStatus(SalesStatus.OPEN);
        }
    }
}
