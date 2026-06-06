package controller;

import service.ReviewService;
import view.ReviewView;
import dto.BookingDTO;
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
    public void showMyReviews(String memberId, List<BookingDTO> bookings) {
        // BOOKED 상태이고 리뷰 없는 예매만 작성 가능
        // 리뷰 있는 예매는 조회/수정 가능
        
        // 전체 내 리뷰 먼저 보여주기
        List<ReviewDTO> reviews = reviewService.getMyReviews(memberId);
        
        // 예매 목록 보여주고 선택
        int bookingId = reviewView.printBookingsAndSelect(bookings);
        if (bookingId == -1) return;
        
        // 선택한 예매에 리뷰 있는지 확인
        BookingDTO selected = bookings.stream()
                .filter(b -> b.getBookingId() == bookingId)
                .findFirst().orElse(null);
        if (selected == null) return;
        
        if (selected.isHasReview()) {
            // 리뷰 있으면 조회 + 수정
            ReviewDTO target = reviews.stream()
                    .filter(r -> r.getBookingId() == bookingId)
                    .findFirst().orElse(null);
            if (target == null) return;
            reviewView.printReviewDetail(target);
            if (!reviewView.confirmUpdate()) return;
            // 수정 여부 확인
            //int reviewId = target.getReviewId();
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
        } else {
        	// 리뷰 없으면 작성 — 팝업 뜨기 전에 먼저 검사
            if (!reviewService.canWriteReview(bookingId)) {
                System.out.println("공연 시작 후에만 리뷰를 작성할 수 있습니다.");
                return;
            }
            writeReview(bookingId);
        }
    }
    
    
}