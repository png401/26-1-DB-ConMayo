package dto;

import java.time.LocalDateTime;

public class CancellationDTO {
    private int bookingId;		// 예매 ID (PK, FK → booking — 1:1)
    private int refundAmount;	// 환불 금액
    private LocalDateTime canceledAt;	// 취소 시각 (DEFAULT CURRENT_TIMESTAMP)
    private int cancellationFee;		// 취소 수수료 (DEFAULT 0)
    private CancelStatus cancelStatus;	// 취소 상태 (REQUESTED / PENDING_REFUND / REFUNDED)

    public CancellationDTO() {}

	public CancellationDTO(int bookingId, int refundAmount, 
			LocalDateTime canceledAt, int cancellationFee,
			CancelStatus cancelStatus) {
		super();
		this.bookingId = bookingId;
		this.refundAmount = refundAmount;
		this.canceledAt = canceledAt;
		this.cancellationFee = cancellationFee;
		this.cancelStatus = cancelStatus;
	}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public int getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(int refundAmount) {
		this.refundAmount = refundAmount;
	}

	public LocalDateTime getCanceledAt() {
		return canceledAt;
	}

	public void setCanceledAt(LocalDateTime canceledAt) {
		this.canceledAt = canceledAt;
	}

	public int getCancellationFee() {
		return cancellationFee;
	}

	public void setCancellationFee(int cancellationFee) {
		this.cancellationFee = cancellationFee;
	}

	public CancelStatus getCancelStatus() {
		return cancelStatus;
	}

	public void setCancelStatus(CancelStatus cancelStatus) {
		this.cancelStatus = cancelStatus;
	}
    
}