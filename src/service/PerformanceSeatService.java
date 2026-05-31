// PerformanceSeatService.java
package service;
import dao.PerformanceSeatDAO;
import dto.PerformanceSeatDTO;
import java.util.List;
public class PerformanceSeatService {
    private final PerformanceSeatDAO perfSeatDAO;
    public PerformanceSeatService(PerformanceSeatDAO perfSeatDAO) { this.perfSeatDAO = perfSeatDAO; }

    // 1. 공연별 좌석-가격 목록 조회 (관리자 화면 등에서 순수 가격 매핑용 데이터 조회)
    public List<PerformanceSeatDTO> getSeatsByPerformance(int performanceId) { 
        return perfSeatDAO.findByPerformance(performanceId); 
    }
    
    // 2. 공연좌석 등록 (관리자)
    public void addPerfSeat(PerformanceSeatDTO perfSeat) {
        if (perfSeat == null || perfSeat.getPerformanceId() <= 0 || perfSeat.getSeatId() <= 0) {
            System.out.println("오류: 올바르지 않은 공연 좌석 정보입니다.");
            return;
        }
        
        if (perfSeat.getPrice() < 0) {
            System.out.println("오류: 좌석 가격은 0원 이상이어야 합니다.");
            return;
        }

        perfSeatDAO.insert(perfSeat);
    }
    
    // 3. 가격 수정 (관리자)
    public void modifyPrice(int performanceSeatId, int price) {
        if (performanceSeatId <= 0) {
            System.out.println("오류: 올바르지 않은 공연좌석 ID입니다.");
            return;
        }

        if (price < 0) {
            System.out.println("오류: 수정할 가격은 0원 이상이어야 합니다.");
            return;
        }

        perfSeatDAO.updatePrice(performanceSeatId, price);
    }
}
