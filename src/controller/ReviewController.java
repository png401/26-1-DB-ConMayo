package controller;
import service.ReviewService;
import view.ReviewView;
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewView reviewView;
    public ReviewController(ReviewService reviewService, ReviewView reviewView) {
        this.reviewService = reviewService;
        this.reviewView = reviewView;
    }

    public void showReviews(int seatId) {}   // 좌석 리뷰 목록 출력
    public void writeReview(int bookingId) {} // 리뷰 작성 (BOOKED 상태 체크 후 등록)
}
