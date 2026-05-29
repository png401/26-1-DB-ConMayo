package service;

import dao.SeatDAO;
import dto.SeatDTO;
import java.util.List;

public class SeatService {
    private final SeatDAO seatDAO;

    public SeatService(SeatDAO seatDAO) {
        this.seatDAO = seatDAO;
    }

    // [조회] 공연별 좌석 목록 반환 (avgRating, color 포함) -> SeatPanel에 전달
    public List<SeatDTO> getSeatsByPerformance(int performanceId) {
        List<SeatDTO> seats = seatDAO.findByPerformance(performanceId);
        for (SeatDTO seat : seats) {
            seat.setColor(getRatingColor(seat.getAvgRating())); // DAOImpl 아닌 Service에서 색상 세팅
        }
        return seats;
    }

    // [조회] 공연의 잔여석 수 반환 -> 대기화면에 표시
    public int getAvailableCount(int performanceId) {
        return seatDAO.getAvailableCount(performanceId);
    }

    // [변환] 평균 평점 ->     색상 문자열 반환
    public String getRatingColor(double avgRating) {
        if (avgRating == 0.0) return "GRAY";
        if (avgRating < 2.0)  return "RED";
        if (avgRating < 3.0)  return "ORANGE";
        if (avgRating < 4.0)  return "YELLOW";
        if (avgRating < 5.0)  return "LIME";
        return "GREEN";
    }
}