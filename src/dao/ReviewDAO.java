// ReviewDAO.java
package dao;
import dto.ReviewDTO;
import java.util.List;
public interface ReviewDAO {
    List<ReviewDTO> findBySeatId(int seatId);        // 좌석 ID로 리뷰 목록 조회
    void insert(ReviewDTO review);                   // 리뷰 등록
    boolean existsByBookingId(int bookingId);         // 해당 예매에 리뷰가 이미 있는지 확인 (중복 방지)
}