package controller;
import service.MemberService;
import view.AdminView;
import view.MemberView;

import java.time.format.DateTimeFormatter;
import java.util.List;

import dto.MemberDTO;
import dto.MemberRole;
public class MemberController {
    private final MemberService memberService;
    private final MemberView memberView;
    private final AdminView adminView;
	private final PerformanceController performanceController;
	private final BookingController bookingController;
	
    
    public MemberController(
            MemberService memberService,
            MemberView memberView,
            AdminView adminView,
            PerformanceController performanceController,
            BookingController bookingController) {

        this.memberService = memberService;
        this.memberView = memberView;
        this.adminView = adminView;
        this.performanceController = performanceController;
        this.bookingController = bookingController;
    }
    
    private void runUserMenu(MemberDTO loginMember) {

        while (true) {

            int menu = memberView.showUserMenu();

            switch (menu) {

                case 1:
                    performanceController.showList();
                    break;

                case 2:
                    bookingController.showMyBookings(
                            loginMember.getMemberId());
                    break;
                    

                case 0:
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
                	
                    break;

                case 5:
                    // 좌석 가격 설정
                    break;

                case 6:
                    runMemberManageMenu();
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
                        runAdminMenu();
                    } else {
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
    	
    	MemberDTO member = memberView.inputMemberInfo();
    	memberService.register(member);
    	memberView.printSuccess("회원가입이 완료되었습니다.");
    	
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
    	
    	String memberId = adminView.inputMemberIdToBlacklist();
    	memberService.addToBlacklist(memberId);
    	adminView.printSuccess("블랙리스트 등록이 완료되었습니다.");
    	
    }
    
    
 // 블랙리스트 해제
    public void releaseBlacklist() {
    	
    	String memberId = adminView.inputMemberIdToBlacklist();
    	memberService.releaseBlacklist(memberId);
    	adminView.printSuccess("블랙리스트가 해제되었습니다.");
    }
}
