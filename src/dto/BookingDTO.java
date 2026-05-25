package dto;

public class BookingDTO {
    private int bookingId;          // 예매 ID (PK, AUTO_INCREMENT)
    private String memberId;        // 회원 ID (FK → member)
    private int performanceSeatId;  // 공연좌석 ID (FK → performance_seat)
    private String bookingStatus;   // 예매 상태 (HOLD / BOOKED / CANCELED)
    private String bookedAt;        // 예매 시각 (DEFAULT CURRENT_TIMESTAMP)
    private int payment;            // 실결제 금액 (HOLD면 0)

    public BookingDTO() {}
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
    public int getPerformanceSeatId() { return performanceSeatId; }
    public void setPerformanceSeatId(int performanceSeatId) { this.performanceSeatId = performanceSeatId; }
    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
    public String getBookedAt() { return bookedAt; }
    public void setBookedAt(String bookedAt) { this.bookedAt = bookedAt; }
    public int getPayment() { return payment; }
    public void setPayment(int payment) { this.payment = payment; }
}