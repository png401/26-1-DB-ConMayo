package controller;

import db.DatabaseConnector;
import service.MemberService;
import view.AdminView;
import view.MemberView;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import dto.BookingDTO;
import dto.MemberDTO;
import dto.MemberRole;

import view.SeatView;
import dto.SeatDTO;

import dao.WaitingQueueDAO;
import daoImpl.WaitingQueueDAOImpl;
import dto.WaitingQueueDTO;

public class MemberController {
    private final MemberService memberService;
    private final MemberView memberView;
    private final AdminView adminView;
    private final PerformanceController performanceController;
    private final BookingController bookingController;
    private final PerformanceSeatController performanceSeatController;
    private final VenueController venueController;
    private final SeatController seatController;
    private final SeatView seatView;

    private ReviewController reviewController; // 추가

    public MemberController(
            MemberService memberService,
            MemberView memberView,
            AdminView adminView,
            PerformanceController performanceController,
            BookingController bookingController,
            PerformanceSeatController performanceSeatController,
            VenueController venueController,
            SeatController seatController,
            SeatView seatView) {

        this.memberService = memberService;
        this.memberView = memberView;
        this.adminView = adminView;
        this.performanceController = performanceController;
        this.bookingController = bookingController;
        this.performanceSeatController = performanceSeatController;
        this.venueController = venueController;
        this.seatController = seatController;
        this.seatView = seatView;
    }

    public void setReviewController(ReviewController reviewController) {
        this.reviewController = reviewController;
    }

    private void runUserMenu(MemberDTO loginMember) {
        seatController.setMemberId(loginMember.getMemberId());

        while (true) {

            int menu = memberView.showUserMenu();

            switch (menu) {

            case 1:
                int performanceId = performanceController.showList();
                if (performanceId != 0) {
                    // 대기열 입장
                    WaitingQueueDAO waitingDAO = new WaitingQueueDAOImpl();
                    WaitingQueueDTO queueDTO = new WaitingQueueDTO(
                        loginMember.getMemberId(), performanceId);
                    int myQueueId = waitingDAO.enter(queueDTO);

                    // 폴링 시작
                    view.BookingView bv = new view.BookingView();
                    java.util.Timer timer = new java.util.Timer();
                    boolean[] canEnter = {false}; // 입장 가능 여부 플래그

                    timer.scheduleAtFixedRate(new java.util.TimerTask() {
                        public void run() {
                            int remaining = seatController.getAvailableCount(performanceId);
                            int myRank = waitingDAO.getMyRank(myQueueId, performanceId);
                            bv.printWaiting(myRank, remaining);
                            if (myRank == 1) {
                                canEnter[0] = true;
                                System.out.println("당신의 순번입니다! Enter를 눌러 입장하세요.");
                            }
                        }
                    }, 0, 1000);

                    // myRank == 1 될 때까지 대기
                    while (!canEnter[0]) {
                        try { Thread.sleep(500); } catch (InterruptedException e) {}
                    }
                    
                    // 입장 가능해지면 Enter 입력 받음
                    new java.util.Scanner(System.in).nextLine();
                    timer.cancel();

                    // 대기열 퇴장
                    waitingDAO.leave(myQueueId);

                    // 좌석 선택
                    List<SeatDTO> seats = seatController.openSeatPanel(performanceId);
                    int availableCount = seatController.getAvailableCount(performanceId);
                    seatView.showSeatPanel(seats, availableCount, loginMember.getMemberId());
                }
                break;

                case 2:
                	boolean isBlacklisted = (boolean) bookingController.showMyBookings(
                            loginMember.getMemberId());
                	
                	if (isBlacklisted) {
                		return;
                	}
                    break;
                
                case 3:
                	List<BookingDTO> myBookings = bookingController.getMyBookings(loginMember.getMemberId());
                    reviewController.showMyReviews(loginMember.getMemberId(), myBookings);
                    break;

                case 4:
                    int venueId = venueController.showListForUser();
                    if (venueId != 0) {
                        List<SeatDTO> seats = seatController.openReviewPanel(venueId);
                        seatView.showReviewOnlyPanel(seats, 0, loginMember.getMemberId());
                    }
                    break;
                    	
                case 0:
                    DatabaseConnector.reset(); // 로그아웃 시 기본 계정으로 복귀
                    return;

                default:
                    memberView.printError("잘못된 메뉴입니다.");
            }
        }
    }

    private void runAdminMenu() {

        while (true) {

            int menu = adminView.showAdminMenu();

            switch (menu) {

                case 1:
                    // 공연 추가
                    performanceController.add();
                    break;

                case 2:
                    // 공연 수정
                    performanceController.modify();
                    break;

                case 3:
                    // 공연 삭제
                    performanceController.remove();
                    break;

                case 4:
                    // 공연장 관리
                    runVenueManageMenu();
                    break;

                case 5:
                    // 좌석 가격 설정
                    performanceSeatController.modifyPrice();
                    break;

                case 6:
                    runMemberManageMenu();
                    break;

                case 0:
                    DatabaseConnector.reset(); // 로그아웃 시 기본 계정으로 복귀
                    return;

                default:
                    adminView.printError("잘못된 메뉴입니다.");
            }
        }
    }

    private void runVenueManageMenu() {

        while (true) {

            int menu = adminView.showVenueManageMenu();

            switch (menu) {

                case 1:
                    venueController.add();
                    break;

                case 2:
                    venueController.modify();
                    break;

                case 3:
                    venueController.remove();
                    break;

                case 0:
                    return;

                default:
                    adminView.printError("잘못된 메뉴입니다.");
            }
        }
    }

    private void runMemberManageMenu() {

        while (true) {

            int menu = adminView.showMemberManageMenu();

            switch (menu) {

                case 1:
                    showBlacklist();
                    break;

                case 2:
                    addBlacklist();
                    break;

                case 3:
                    releaseBlacklist();
                    break;

                case 0:
                    return;

                default:
                    adminView.printError("잘못된 메뉴입니다.");
            }
        }
    }

    // 로그인/회원가입 메뉴 진입점
    public void start() {

        while (true) {

            int menu = memberView.showMenu();

            switch (menu) {

                case 1:

                    MemberDTO loginMember = login();

                    if (loginMember == null) {
                        break;
                    }

                    if (loginMember.getMemberRole() == MemberRole.ADMIN) {
                        DatabaseConnector.init(MemberRole.ADMIN); // 관리자 계정으로 전환
                        runAdminMenu();
                    } else { // 로그인 성공
                        DatabaseConnector.init(MemberRole.USER);  // 일반 회원 계정으로 전환
                        runUserMenu(loginMember);
                    }

                    break;

                case 2:
                    register();
                    break;

                case 0:
                    return;

                default:
                    memberView.printError("잘못된 메뉴입니다.");
            }
        }
    }

    // 로그인 처리 (블랙리스트 체크 포함)
    public MemberDTO login() {

        String memberId = memberView.inputId();
        String passwd = memberView.inputPassword();

        MemberDTO member = memberService.login(memberId, passwd);

        if (member == null) {
            memberView.printError("아이디 또는 비밀번호가 올바르지 않습니다.");
            return null;
        }

        if (memberService.isBlacklisted(member)) {

            String until = member.getBlacklistUntil().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            memberView.printBlacklistWarning(until);
            return null;
        }

        memberView.printLoginSuccess(member);

        return member;
    }

    // 회원가입 처리
    public void register() {

        String memberId = memberView.inputId();

        try {

            if (memberService.isDuplicatedId(memberId)) {
                memberView.printError("중복된 아이디입니다.");
                return;
            }

            String passwd = memberView.inputPassword();

            MemberDTO member = memberView.inputMemberInfo();

            memberService.register(memberId, passwd, member);

            memberView.printSuccess("회원가입이 완료되었습니다.");

        } catch (SQLIntegrityConstraintViolationException e) {

            memberView.printError("이미 사용 중인 전화번호입니다.");

        } catch (SQLException e) {

            memberView.printError("회원가입 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    // 현재 블랙리스트 출력
    public void showBlacklist() {

        List<MemberDTO> blacklist = memberService.getCurrentBlacklist();

        if (blacklist.isEmpty()) {
            adminView.printError("현재 블랙리스트 회원이 없습니다.");
            return;
        }

        adminView.printBlacklist(blacklist);
    }

    // 수동 블랙리스트 등록
    public void addBlacklist() {
    	// 추가
    	// 수동 블리 등록 전 각 회원별 최근 일주일동안 취소 횟수 출력
    	List<Object[]> list =
    	        memberService.getRecentCancellationCounts();

    	System.out.println(
    	    "\n====== 최근 7일 취소 횟수 ======"
    	);

    	System.out.printf(
    	    "%-15s %-10s%n",
    	    "회원ID",
    	    "취소횟수"
    	);

    	for (Object[] row : list) {

    	    System.out.printf(
    	        "%-15s %-10d%n",
    	        row[0],
    	        row[1]
    	    );
    	}
    	
        String memberId = adminView.inputMemberIdToBlacklist();
        	
        // 추가 (0: 되돌아가기)
        if ("0".equals(memberId)) {
            return;
        }
       
        boolean success = memberService.addToBlacklist(memberId);
        
        if (success) {
            adminView.printSuccess("블랙리스트 등록이 완료되었습니다.");
        } else {
            adminView.printError("존재하지 않는 회원 ID입니다.");
        }
    }

    // 블랙리스트 해제
    public void releaseBlacklist() {
    	
    	// 추가: 블리 해제 할 회원 id 입력 전에 현재 블리에 있는 회원 정보를 먼저 출력한다.
    	showBlacklist();
    	
        String memberId = adminView.inputMemberIdToBlacklist();
        
        // 추가 (0: 되돌아가기)
        if ("0".equals(memberId)) {
            return;
        }
        
        int result = memberService.releaseBlacklist(memberId);

        if (result == 0) {
            adminView.printSuccess("블랙리스트가 해제되었습니다.");
        } else if (result == 1) {
            adminView.printError("존재하지 않는 회원 ID입니다.");
        } else if (result == 2) {
            adminView.printError("블랙리스트 회원이 아닙니다.");
        }
    }
}