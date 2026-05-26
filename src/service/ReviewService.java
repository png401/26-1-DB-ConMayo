package service;
import dao.BookingDAO;
import dao.ReviewDAO;
import dto.ReviewDTO;
import java.util.List;
public class ReviewService {
    private final ReviewDAO reviewDAO;
    private final BookingDAO bookingDAO;
    public ReviewService(ReviewDAO reviewDAO, BookingDAO bookingDAO) {
        this.reviewDAO = reviewDAO;
        this.bookingDAO = bookingDAO;
    }

    public List<ReviewDTO> getReviewsBySeat(int seatId) { return null; }  // 좌석 리뷰 목록 조회
    public void writeReview(ReviewDTO review) {}                           // 리뷰 등록 (BOOKED 상태 + 중복 여부 체크 후 INSERT)
}
