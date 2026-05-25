package dto;

public class CancellationDTO {
    private int bookingId;        // 예매 ID (PK, FK → booking — 1:1)
    private int refundAmount;     // 환불 금액
    private String canceledAt;   // 취소 시각 (DEFAULT CURRENT_TIMESTAMP)
    private int cancellationFee; // 취소 수수료 (DEFAULT 0)
    private String cancelStatus; // 취소 상태 (REQUESTED / PENDING_REFUND / REFUNDED)

    public CancellationDTO() {}
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public int getRefundAmount() { return refundAmount; }
    public void setRefundAmount(int refundAmount) { this.refundAmount = refundAmount; }
    public String getCanceledAt() { return canceledAt; }
    public void setCanceledAt(String canceledAt) { this.canceledAt = canceledAt; }
    public int getCancellationFee() { return cancellationFee; }
    public void setCancellationFee(int cancellationFee) { this.cancellationFee = cancellationFee; }
    public String getCancelStatus() { return cancelStatus; }
    public void setCancelStatus(String cancelStatus) { this.cancelStatus = cancelStatus; }
}