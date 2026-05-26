// PerformanceSeatService.java
package service;
import dao.PerformanceSeatDAO;
import dto.PerformanceSeatDTO;
import java.util.List;
public class PerformanceSeatService {
    private final PerformanceSeatDAO perfSeatDAO;
    public PerformanceSeatService(PerformanceSeatDAO perfSeatDAO) { this.perfSeatDAO = perfSeatDAO; }

    public List<PerformanceSeatDTO> getSeatsByPerformance(int performanceId) { return null; }  // 공연별 좌석-가격 목록
    public void addPerfSeat(PerformanceSeatDTO perfSeat) {}                                    // 공연좌석 등록 (관리자)
    public void modifyPrice(int performanceSeatId, int price) {}                               // 가격 수정 (관리자)
}
