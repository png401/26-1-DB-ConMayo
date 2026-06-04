package controller;

import service.ReviewService;
import view.ReviewView;
import dto.ReviewDTO;
import java.util.List;

public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewView reviewView;

    public ReviewController(ReviewService reviewService, ReviewView reviewView) {
        this.reviewService = reviewService;
        this.reviewView = reviewView;
    }

    // [조회] 좌석 클릭 시 View에서 호출 -> 리뷰 목록 조회 후 ReviewView에 전달
    // 흐름: 좌석 클릭 -> ReviewController.showReviews() -> ReviewService -> ReviewDAO -> DB
    public void showReviews(int seatId) {
        List<ReviewDTO> reviews = reviewService.getReviewsBySeat(seatId);
        reviewView.displayReviews(reviews);
    }

    // [등록] 리뷰 작성 버튼 클릭 시 View에서 호출
    // View에서 평점/내용 입력받고 -> ReviewDTO 구성 -> Service에 전달
    // 흐름: 리뷰 작성 버튼 -> ReviewController.writeReview() -> ReviewService -> ReviewDAO -> DB
    public void writeReview(int bookingId) {
        // View에서 평점 입력받기 (-1이면 취소)
        int rating = reviewView.inputRating();
        if (rating == -1) return;

        // View에서 내용 입력받기
        String content = reviewView.inputContent();
        if (content == null || content.isBlank()) {
            reviewView.showMessage("리뷰 내용을 입력해주세요.");
            return;
        }

        // DTO 구성 후 Service에 전달
        ReviewDTO review = new ReviewDTO();
        review.setBookingId(bookingId);
        review.setSeatRating(rating);
        review.setContent(content);

        boolean success = reviewService.writeReview(review);
        reviewView.showMessage(success ? "리뷰가 등록되었습니다." : "리뷰 등록에 실패했습니다.");
    }
    
    // 좌석 ID 기준 리뷰 목록 출력 (JOptionPane으로 표시)
    public void showReviewsBySeat(int seatId) {
        List<ReviewDTO> reviews = reviewService.getReviewsBySeat(seatId);
        reviewView.printReviewsBySeatDialog(reviews); // JOptionPane 팝업으로
    }
    
    // [조회] 내가 쓴 리뷰 목록 출력 + 수정 진입
    public void showMyReviews(String memberId) {
        List<ReviewDTO> reviews = reviewService.getMyReviews(memberId);
        if (reviews.isEmpty()) {
            reviewView.showMessage("작성한 리뷰가 없습니다.");
            return;
        }
        // View에서 목록 출력 후 수정할 리뷰 번호 선택 (-1이면 그냥 나가기)
        int reviewId = reviewView.printMyReviewsAndSelect(reviews);
        if (reviewId == -1) return;

        // 선택한 리뷰 찾기
        ReviewDTO target = reviews.stream()
                .filter(r -> r.getReviewId() == reviewId)
                .findFirst().orElse(null);
        if (target == null) {
            reviewView.showMessage("존재하지 않는 리뷰입니다.");
            return;
        }

        // 수정 입력
        int rating = reviewView.inputRating();
        if (rating == -1) return;
        String content = reviewView.inputContent();
        if (content == null || content.isBlank()) {
            reviewView.showMessage("리뷰 내용을 입력해주세요.");
            return;
        }

        target.setSeatRating(rating);
        target.setContent(content);

        boolean success = reviewService.updateReview(target);
        reviewView.showMessage(success ? "리뷰가 수정되었습니다." : "리뷰 수정에 실패했습니다.");
    }
}