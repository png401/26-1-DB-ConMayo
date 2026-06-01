package controller;
import java.util.List;
import dto.PerformanceDTO;
import service.PerformanceSeatService;
import service.PerformanceService;
import view.AdminView;
import view.MemberView;
import view.PerformanceView;

public class PerformanceController {
    private final PerformanceService performanceService;
    private final PerformanceView performanceView;
    private final AdminView adminView;
    private final PerformanceSeatService perfSeatService;

    public PerformanceController(PerformanceService performanceService,
                                 PerformanceView performanceView,
                                 AdminView adminView,
                                 PerformanceSeatService perfSeatService) {
        this.performanceService = performanceService;
        this.performanceView = performanceView;
        this.adminView = adminView;
        this.perfSeatService = perfSeatService;
    }

    // 1. 공연 목록 출력 (유저용 — 공연 선택 + 예매 흐름 포함, performanceId 반환)
    public int showList() {
    	int choice = performanceView.inputCategory();

        List<PerformanceDTO> list;

        switch (choice) {
            case 1:
                list = performanceService.getAllPerformances();
                break;

            case 2:
                list = performanceService.getByCategory("콘서트");
                break;

            case 3:
                list = performanceService.getByCategory("뮤지컬");
                break;

            case 4:
                list = performanceService.getByCategory("스포츠");
                break;

            case 0:
                return 0;

            default:
                adminView.printError("잘못된 입력입니다.");
                return 0;
        }

        performanceView.printList(list);

        int performanceId = performanceView.inputPerformanceId();

        if (performanceId == 0) {
            return 0;
        }

        PerformanceDTO performance =
                performanceService.getPerformance(performanceId);

        performanceView.printDetail(performance, null);

        int action = performanceView.inputPerformanceId();

        if (action == 1) {
        	if (performance.getSalesStatus() != dto.SalesStatus.OPEN) {
                System.out.println("\n❌ 예매가 불가능한 공연입니다. (" + performance.getSalesStatus() + ")");
                System.out.println("판매 상태가 'OPEN'인 공연만 예매할 수 있습니다.");
                return 0; // 0을 리턴하여 SeatPanel로 가지 않고 이전 회원 메뉴로 돌아가게 만듦
            }
        	
            return performanceId;
        }

        return 0;
    }

    // 2. 공연 목록 출력 (관리자용 — 목록 출력만)
    private void printListForAdmin() {
        List<PerformanceDTO> list = performanceService.getAllPerformances();
        performanceView.printList(list);
    }

    // 3. 공연 상세 출력
    public void showDetail(int performanceId) {
        PerformanceDTO performance = performanceService.getPerformance(performanceId);
        if (performance == null) {
            if (adminView != null) adminView.printError("해당 공연을 찾을 수 없습니다.");
            return;
        }
        performanceView.printDetail(performance, null);
    }

    // 4. 공연 등록 (관리자)
    public void add() {
        if (adminView == null) return;
        PerformanceDTO newPerformance = performanceView.inputPerformanceInfo();
        performanceService.addPerformance(newPerformance);
        adminView.printSuccess("공연 등록 성공");
        printListForAdmin();
    }

    // 5. 공연 수정 (관리자)
    public void modify() {
        if (adminView == null) return;
        int performanceId = adminView.inputPerformanceId();

        PerformanceDTO existingPerf = performanceService.getPerformance(performanceId);
        if (existingPerf == null) {
            adminView.printError("수정하려는 공연이 존재하지 않습니다.");
            return;
        }

        System.out.println("\n[현재 등록된 공연 정보]");
        System.out.println("- 제목 : " + existingPerf.getTitle());
        System.out.println("- 카테고리: " + existingPerf.getCategory());
        if (existingPerf.getStartTime() != null) {
            System.out.println("- 일시 : " + existingPerf.getStartTime()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
        System.out.println("- 장소 : " + (existingPerf.getVenueName() != null
                ? existingPerf.getVenueName()
                : "장소ID " + existingPerf.getVenueId()));
        System.out.println("- 판매상태: " + existingPerf.getSalesStatus());
        System.out.println("\n--- 새로운 공연 정보를 입력하세요 ---");

        PerformanceDTO updatedPerformance = performanceView.inputPerformanceInfo();
        updatedPerformance.setPerformanceId(performanceId);
        performanceService.modifyPerformance(updatedPerformance);
        adminView.printSuccess("공연 수정 성공");
        printListForAdmin();
    }

    // 6. 공연 삭제 (관리자)
    public void remove() {
        if (adminView == null) return;
        int performanceId = adminView.inputPerformanceId();
        performanceService.removePerformance(performanceId);
        adminView.printSuccess("공연이 성공적으로 삭제되었습니다.");
        printListForAdmin();
    }
}