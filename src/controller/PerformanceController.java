package controller;
import java.util.List;
import dto.PerformanceDTO;
import dto.PerformanceSeatDTO; //추가
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
        //추가
        List<PerformanceSeatDTO> seatList =
        	    perfSeatService.getSeatsByPerformance(performanceId);


        performanceView.printDetail(performance, seatList); //수정 null -> seatList

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
        //추가
        List<PerformanceSeatDTO> seatList =
        	    perfSeatService.getSeatsByPerformance(performanceId);

        if (performance == null) {
            if (adminView != null) adminView.printError("해당 공연을 찾을 수 없습니다.");
            return;
        }
        performanceView.printDetail(performance, seatList); //수정 null -> seatList
    }

    // 4. 공연 등록 (관리자)
    public void add() {
    	if (adminView == null) return;
    	printListForAdmin();

        try {
            PerformanceDTO newPerformance = performanceView.inputPerformanceInfo();
            
            // 1. 기본적인 입력값 검증 (Validation)
            if (newPerformance.getTitle() == null || newPerformance.getTitle().isBlank()) {
                adminView.printError("공연 제목은 필수 입력 항목입니다.");
                return;
            }
            if (newPerformance.getVenueId() <= 0) {
                adminView.printError("올바른 공연장 번호(Venue ID)를 입력해주세요.");
                return;
            }
            
            // 2. 서비스 호출 (이 과정에서 DB 외래키 에러 발생 가능)
            performanceService.addPerformance(newPerformance);
            
            // 3. 성공 시에만 출력 (예외가 던져지면 이 아래 코드는 실행되지 않음)
            adminView.printSuccess("공연 등록 성공");
            printListForAdmin();

        } catch (java.time.format.DateTimeParseException e) {
            adminView.printError("날짜/시간 형식이 올바르지 않습니다. (올바른 형식: yyyy-MM-dd HH:mm)");
            // return; // 메서드 끝이므로 생략 가능하지만 명시적으로 흐름 끊기
        } catch (RuntimeException e) {
            // DAOImpl에서 던진 예외 중 외래키나 제약조건 위반 검사
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.contains("foreign key constraint fails")) {
                adminView.printError("공연 등록 실패: 존재하지 않는 공연장 번호(Venue ID)입니다. 등록된 공연장 번호를 확인해주세요.");
            } else {
                adminView.printError("공연 등록 중 오류가 발생했습니다: " + errorMsg);
            }
        } catch (Exception e) {
            adminView.printError("알 수 없는 오류가 발생했습니다.");
        }
    }

    // 5. 공연 수정 (관리자)
    public void modify() {
    	if (adminView == null) return;
    	printListForAdmin();

        int performanceId = adminView.inputPerformanceId();

        PerformanceDTO existingPerf = performanceService.getPerformance(performanceId);
        if (existingPerf == null) {
            adminView.printError("수정하려는 공연이 존재하지 않습니다.");
            return;
        }

        System.out.println("\n[현재 등록된 공연 정보]");
        System.out.println("- 제목 : " + existingPerf.getTitle());
        System.out.println("- 카테고리(뮤지컬/스포츠/콘서트): " + existingPerf.getCategory());
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
        
        try {
            performanceService.modifyPerformance(updatedPerformance);
            adminView.printSuccess("공연 수정 성공");
            printListForAdmin();
        } catch (Exception e) {
            System.out.println("\n[오류] 공연 수정에 실패했습니다.");
            if (e.getMessage() != null && e.getMessage().contains("foreign key constraint fails")) {
                System.out.println("⚠ 원인: 입력하신 '공연장 ID'가 잘못되었습니다. 존재하는 ID인지 확인하세요.");
            } else {
                System.out.println("⚠ 에러 원인: " + e.getMessage());
            }
        }
    }

    // 6. 공연 삭제 (관리자)
    public void remove() {
    	if (adminView == null) return;
    	printListForAdmin();
    	
        int performanceId = adminView.inputPerformanceId();
        
        // 공연 존재 여부 체크
        PerformanceDTO targetPerf = performanceService.getPerformance(performanceId);
        if (targetPerf == null) {
            adminView.printError("존재하지 않는 공연 ID입니다.");
            return;
        }
        
        var seatList = perfSeatService.getSeatsByPerformance(performanceId);
        if (seatList != null && !seatList.isEmpty()) {
            System.out.println("\n❌ [삭제 불가] 관객의 예매 정보나 좌석 배치가 남아있는 공연은 삭제할 수 없습니다.");
            return; // 쿼리를 날리지 않고 여기서 바로 함수 종료
        }
        
        if (!performanceView.confirmDelete(targetPerf.getTitle())) {
            System.out.println("❌ 공연 삭제가 취소되었습니다.");
            return;
        }
        
        try {
            performanceService.removePerformance(performanceId);
            adminView.printSuccess("공연이 성공적으로 삭제되었습니다.");
            printListForAdmin();
        } catch (Exception e) {
            System.out.println("\n 공연 삭제에 실패했습니다.");
            System.out.println("⚠ 원인: " + e.getMessage());
            }
        }
    
}