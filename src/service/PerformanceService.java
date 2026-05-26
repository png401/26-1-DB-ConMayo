// PerformanceService.java
package service;
import dao.PerformanceDAO;
import dto.PerformanceDTO;
import java.util.List;
public class PerformanceService {
    private final PerformanceDAO performanceDAO;
    public PerformanceService(PerformanceDAO performanceDAO) { this.performanceDAO = performanceDAO; }

    public List<PerformanceDTO> getAllPerformances() { return null; }               // 공연 전체 목록
    public List<PerformanceDTO> getByCategory(String category) { return null; }    // 카테고리 필터 조회
    public PerformanceDTO getPerformance(int performanceId) { return null; }        // 공연 상세 조회
    public void addPerformance(PerformanceDTO performance) {}                       // 공연 등록 (관리자)
    public void modifyPerformance(PerformanceDTO performance) {}                    // 공연 수정 (관리자)
    public void removePerformance(int performanceId) {}                             // 공연 삭제 (관리자)
}
