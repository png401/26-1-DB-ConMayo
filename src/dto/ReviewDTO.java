package dto;

import java.time.LocalDateTime;

public class ReviewDTO {
    private int reviewId;    // 리뷰 ID (PK, AUTO_INCREMENT)
    private int bookingId;   // 예매 ID (FK -> booking, UNIQUE — 1예매 1리뷰)
    private int seatRating;  // 좌석 평점 (1~5, CHECK 제약)
    private LocalDateTime writtenAt; // 작성 시각 (DEFAULT CURRENT_TIMESTAMP)
    private String content;  // 리뷰 내용
    
    private String performanceTitle; // 공연명
    private String section;          // 구역
    private int rowNum;              // 행
    private int colNum;              // 열
    private String venueName;

    public ReviewDTO() {}

	public ReviewDTO(int reviewId, int bookingId, int seatRating, 
			LocalDateTime writtenAt, String content) {
		this.reviewId = reviewId;
		this.bookingId = bookingId;
		this.seatRating = seatRating;
		this.writtenAt = writtenAt;
		this.content = content;
	}

	public int getReviewId() {
		return reviewId;
	}

	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public int getSeatRating() {
		return seatRating;
	}

	public void setSeatRating(int seatRating) {
		this.seatRating = seatRating;
	}

	public LocalDateTime getWrittenAt() {
		return writtenAt;
	}

	public void setWrittenAt(LocalDateTime writtenAt) {
		this.writtenAt = writtenAt;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getPerformanceTitle() {
		return performanceTitle; 
	}
	
	public void setPerformanceTitle(String performanceTitle) {
		this.performanceTitle = performanceTitle; 
	}

	public String getSection() { 
		return section; 
	}
	
	public void setSection(String section) {
		this.section = section; 
	}

	public int getRowNum() { 
		return rowNum;
	}
	
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum; 
	}

	public int getColNum() {
		return colNum; 
	}
	
	public void setColNum(int colNum) {
		this.colNum = colNum; 
	}
	
	public String getVenueName() {
		return venueName;
	}
	
	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	@Override
	public String toString() {
		return String.format("[리뷰%d] 예매:%d ★%d점 %s\n내용: %s",
				reviewId, bookingId, seatRating, writtenAt, content);
	}
    
}