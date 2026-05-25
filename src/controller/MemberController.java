package controller;
import service.MemberService;
import view.AdminView;
import view.MemberView;
import dto.MemberDTO;
public class MemberController {
    private final MemberService memberService;
    private final MemberView memberView;
    private final AdminView adminView;
    public MemberController(MemberService memberService, MemberView memberView, AdminView adminView) {
        this.memberService = memberService;
        this.memberView = memberView;
        this.adminView = adminView;
    }

    public void start() {}            // 로그인/회원가입 메뉴 진입점
    public MemberDTO login() { return null; }  // 로그인 처리 (블랙리스트 체크 포함)
    public void register() {}         // 회원가입 처리
    public void showBlacklist() {}    // 현재 블랙리스트 출력
    public void detectFastBookers() {}      // 비정상 예매속도 탐지 후 블랙리스트 등록 여부 선택
    public void detectHighCancelRate() {}   // 상습 취소자 탐지 후 블랙리스트 등록 여부 선택
    public void addBlacklist() {}     // 수동 블랙리스트 등록
    public void releaseBlacklist() {} // 블랙리스트 해제
}
