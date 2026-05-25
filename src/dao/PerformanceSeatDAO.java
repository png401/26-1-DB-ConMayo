package dao;
import dto.PerformanceSeatDTO;
import java.util.List;
public interface PerformanceSeatDAO {
    List<PerformanceSeatDTO> findByPerformance(int performanceId);   // 공연별 좌석-가격 목록 조회
    void insert(PerformanceSeatDTO performanceSeat);                        // 공연좌석 등록 (관리자)
    void updatePrice(int performanceSeatId, int price);              // 좌석 가격 수정 (관리자)
}