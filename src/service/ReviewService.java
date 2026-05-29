package service;

import dao.BookingDAO;
import dao.ReviewDAO;
import dto.ReviewDTO;
import dto.BookingDTO;
import dto.BookingStatus;
import java.util.List;
import java.time.LocalDateTime; //bookingdao 에 getPerformanceStartTime() 추가해야함

public class ReviewService {
    private final ReviewDAO reviewDAO;
    private final BookingDAO bookingDAO;

    public ReviewService(ReviewDAO reviewDAO, BookingDAO bookingDAO) {
        this.reviewDAO = reviewDAO;
        this.bookingDAO = bookingDAO;
    }

    // [조회] 좌석 리뷰 목록 반환
    public List<ReviewDTO> getReviewsBySeat(int seatId) {
        return reviewDAO.findBySeatId(seatId);
    }

    // 고민 : 리뷰 수정하시겠습니까? 이런 거 해야할지 뷰에서...
    // [등록] 리뷰 작성
    // 체크 순서: booked 상태인지 -> 공연 시작 후인지 -> 중복 리뷰 없는지 -> 평점 범위 확인 -> insert
    public boolean writeReview(ReviewDTO review) {

        // 1. BOOKED 상태인지 확인 (CANCELED/HOLD 동시 차단)
        BookingDTO booking = bookingDAO.findById(review.getBookingId());
        if (booking == null || booking.getBookingStatus() != BookingStatus.BOOKED) {
            System.out.println("예매 완료 상태에서만 리뷰를 작성할 수 있습니다.");
            return false;
        }

        // 2. 공연 시작 후인지 확인 (코드로 막기로 했던 것 반영)
        // TODO: BookingDAO에 getPerformanceStartTime() 추가 후 주석 해제
        // LocalDateTime startTime = bookingDAO.getPerformanceStartTime(review.getBookingId());
        // if (startTime == null || LocalDateTime.now().isBefore(startTime)) {
        //     System.out.println("공연 시작 후에만 리뷰를 작성할 수 있습니다.");
        //     return false;
        // }

        // 3. 중복 리뷰 확인 (1예매 1리뷰)
        if (reviewDAO.existsByBookingId(review.getBookingId())) {
            System.out.println("이미 작성된 리뷰가 있습니다.");
            return false;
        }

        // 4. 평점 범위 확인 (1~5)
        if (review.getSeatRating() < 1 || review.getSeatRating() > 5) {
            System.out.println("평점은 1~5 사이여야 합니다.");
            return false;
        }

        // 모든 체크 통과 -> INSERT
        return reviewDAO.insert(review);
    }
}