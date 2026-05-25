// SeatDAO.java
package dao;
import dto.SeatDTO;
import java.util.List;
public interface SeatDAO {
    List<SeatDTO> findByPerformance(int performanceId);   // 공연별 좌석 전체 조회 (평점/예약여부 포함)
    double getAvgRating(int seatId);                      // 특정 좌석의 평균 평점 조회
    int getAvailableCount(int performanceId);              // 공연의 잔여석 수 조회
}