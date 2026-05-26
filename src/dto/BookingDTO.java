package dto;

import java.time.LocalDateTime;

public class BookingDTO {
	
    private int bookingId;          // 예매 ID (PK, AUTO_INCREMENT)
    private String memberId;        // 회원 ID (FK → member)
    private int performanceSeatId;  // 공연좌석 ID (FK → performance_seat)
    private BookingStatus bookingStatus;   // 예매 상태 (HOLD / BOOKED / CANCELED)
    private LocalDateTime bookedAt;	// 예매 시각 (DEFAULT CURRENT_TIMESTAMP)
    private int payment;            // 실결제 금액 (HOLD면 0)
    
    public BookingDTO() {}

	public BookingDTO(int bookingId, String memberId, 
			int performanceSeatId, BookingStatus bookingStatus,
			LocalDateTime bookedAt, int payment) {
		super();
		this.bookingId = bookingId;
		this.memberId = memberId;
		this.performanceSeatId = performanceSeatId;
		this.bookingStatus = bookingStatus;
		this.bookedAt = bookedAt;
		this.payment = payment;
	}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public int getPerformanceSeatId() {
		return performanceSeatId;
	}

	public void setPerformanceSeatId(int performanceSeatId) {
		this.performanceSeatId = performanceSeatId;
	}

	public BookingStatus getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(BookingStatus bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public LocalDateTime getBookedAt() {
		return bookedAt;
	}

	public void setBookedAt(LocalDateTime bookedAt) {
		this.bookedAt = bookedAt;
	}

	public int getPayment() {
		return payment;
	}

	public void setPayment(int payment) {
		this.payment = payment;
	}
	
}