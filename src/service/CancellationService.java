package service;

import dao.CancellationDAO;
import dto.CancellationDTO;

public class CancellationService {
    private final CancellationDAO cancellationDAO;

    public CancellationService(CancellationDAO cancellationDAO) {
        this.cancellationDAO = cancellationDAO;
    }

    public void cancel(int bookingId) {
        CancellationDTO cancellation = cancellationDAO.findByBookingId(bookingId);
        if (cancellation == null)
            throw new RuntimeException("취소 이력이 없습니다.");
        cancellationDAO.updateStatus(bookingId, "PENDING_REFUND");
    }

    public CancellationDTO getCancellation(int bookingId) {
        return cancellationDAO.findByBookingId(bookingId);
    }
}