// SeatService.java
package service;
import dao.SeatDAO;
import dto.SeatDTO;
import java.util.List;
public class SeatService {
    private final SeatDAO seatDAO;
    public SeatService(SeatDAO seatDAO) { this.seatDAO = seatDAO; }

    public List<SeatDTO> getSeatsByPerformance(int performanceId) { return null; }  // 공연별 좌석 목록 (SeatPanel에 전달)
    public int getAvailableCount(int performanceId) { return 0; }                   // 잔여석 수 (대기화면에 표시)
    public String getRatingColor(double avgRating) { return null; }                 // 평점 → 색상 변환 (4↑초록, 3노랑, 미만빨강)
}