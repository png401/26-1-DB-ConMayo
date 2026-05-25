package dto;

public class ReviewDTO {
    private int reviewId;    // 리뷰 ID (PK, AUTO_INCREMENT)
    private int bookingId;   // 예매 ID (FK → booking, UNIQUE — 1예매 1리뷰)
    private int seatRating;  // 좌석 평점 (1~5, CHECK 제약)
    private String writtenAt; // 작성 시각 (DEFAULT CURRENT_TIMESTAMP)
    private String content;  // 리뷰 내용

    public ReviewDTO() {}
    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public int getSeatRating() { return seatRating; }
    public void setSeatRating(int seatRating) { this.seatRating = seatRating; }
    public String getWrittenAt() { return writtenAt; }
    public void setWrittenAt(String writtenAt) { this.writtenAt = writtenAt; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}