package service;

import dao.CancellationDAO;

public class CancellationService {
    private final CancellationDAO cancellationDAO;

    public CancellationService(CancellationDAO cancellationDAO) {
        this.cancellationDAO = cancellationDAO;
    }
    
    public void cancel(int bookingId) {}	// 취소 처리 (BOOKED → CANCELED + cancellation INSERT 트랜잭션)
}
