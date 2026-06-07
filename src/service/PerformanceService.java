// PerformanceService.java
package service;
import dao.PerformanceDAO;
import dao.PerformanceSeatDAO;
import dto.PerformanceDTO;
import dto.SalesStatus;

import java.time.LocalDateTime;
import java.util.List;
public class PerformanceService {
    private final PerformanceDAO performanceDAO;
    private final PerformanceSeatDAO perfSeatDAO;
    public PerformanceService(PerformanceDAO performanceDAO, PerformanceSeatDAO perfSeatDAO) {
    	this.performanceDAO = performanceDAO;
    	this.perfSeatDAO = perfSeatDAO;
    }

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
        //performanceDAO.insert(performance);
        // 수정 -> PerformanceDAO의 insert()메소드의 반환형을 int로 바꿈에 따라 insert()하고 공연 아이디를 받아야 한다.
        int performanceId = performanceDAO.insert(performance); 
        // 추가 -> PerformanceSeatDAO의 createSeatsForPerformance() 메소드 호출
        // 받아온 공연아이디와, performance.getVenueId()로 공연장 아이디를 받아서 매개변수로 넘긴다.
        perfSeatDAO.createSeatsForPerformance(performanceId, performance.getVenueId());
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
        	throw new IllegalArgumentException("삭제하려는 공연이 존재하지 않습니다.");
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
        // 3. 그 외의 경우 (현재 예매 기간일 때)
        else {
        	if (perf.getRemainingSeats() <=0 ) {
        		perf.setSalesStatus(SalesStatus.SOLD_OUT);
        	}
        	// 매진(SOLD_OUT)이 아니라면 -> 예매중(OPEN) 상태 유지
        	else if (perf.getSalesStatus() != SalesStatus.SOLD_OUT) {
            perf.setSalesStatus(SalesStatus.OPEN);
        	}
        }
        
    }
    
    // 공연 이름 검색어 찾기
    public List<PerformanceDTO> getPerformancesByTitleKeyword(String keyword) {
        List<PerformanceDTO> allPerformances = performanceDAO.findAll();
        if (allPerformances == null) {
            return java.util.Collections.emptyList();
        }
        
        // 키워드가 포함된 공연만 필터링하여 반환 
        return allPerformances.stream()
                .filter(perf -> perf.getTitle() != null && perf.getTitle().contains(keyword))
                .peek(this::adjustSalesStatus) // 상태 보정 적용
                .toList();
    }
}
