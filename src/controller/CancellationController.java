package controller;

import service.CancellationService;
import view.CancellationView;

public class CancellationController {
    private final CancellationService cancellationService;
    private final CancellationView cancellationView;

    public CancellationController(CancellationService cancellationService,
                                  CancellationView cancellationView) {
        this.cancellationService = cancellationService;
        this.cancellationView = cancellationView;
    }

    public void cancel() {
        // 관리자가 환불 상태를 단계적으로 진행시킬 때 사용
        // bookingId는 AdminView 등에서 입력받아 cancel(bookingId)로 위임
    }
}