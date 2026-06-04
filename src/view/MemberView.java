package view;
import dto.MemberDTO;
import java.util.Scanner;
public class MemberView {
    private final Scanner sc = new Scanner(System.in);

 // 메인 메뉴 출력 후 선택값 반환
    public int showMenu() { 
    	
    	System.out.println("====== 콘마요 예매 시스템 ======");
    	System.out.println("1. 로그인");
    	System.out.println("2. 회원가입");
    	System.out.println("0. 종료");
    	System.out.println();
    	System.out.print("선택 > ");

    	int menu = sc.nextInt();
    	sc.nextLine();
    	
    	return menu;
    	
    }
    
    public int showUserMenu() {
    	System.out.println("\n====== 회원 메뉴 ======");
    	System.out.println("1. 공연 조회");
    	System.out.println("2. 내 예매 조회");
    	System.out.println("3. 내 리뷰 조회/수정");   // 추가
        System.out.println("4. 공연장 리뷰 조회");     // 추가
    	System.out.println("0. 로그아웃");
    	System.out.println();
    	System.out.print("선택 > ");

        int menu = sc.nextInt();
        sc.nextLine();

        return menu;
    }
    
 // 아이디 입력받기
    public String inputId() {
    	System.out.print("아이디 > ");
    	return sc.nextLine();
    }
    
 // 비밀번호 입력받기
    public String inputPassword() {
    	System.out.print("비밀번호 > ");
    	return sc.nextLine();
    } 
    
 // 회원가입 정보 입력받기 
    public MemberDTO inputMemberInfo() {
    	
    	MemberDTO member = new MemberDTO();

        System.out.print("이름 > ");
        member.setMemberName(sc.nextLine());

        System.out.print("전화번호 (010-0000-0000) > ");
        member.setPhone(sc.nextLine());

        return member;
        
    }
    
 // 로그인 성공 메시지 출력
    public void printLoginSuccess(MemberDTO member) {
    	System.out.println("로그인 성공!\n환영합니다, " 
    			+ member.getMemberName() + "님");
    }
    
 // 블랙리스트 경고 출력
    public void printBlacklistWarning(String until) {
    	System.out.print("====== ⚠ 이용 제한 안내 ======\n\n"
    			+ "해당 계정은 이용이 제한되었습니다.\n"
    			+ "제한 해제일: " + until + "\n\n");
    }
    
 // 에러 메시지 출력
    public void printError(String msg) {
    	System.out.println("\n⚠ " + msg + '\n');
    }
    
 // 성공 메시지 출력
    public void printSuccess(String msg) {
    	System.out.println("\n✓ " + msg + '\n');
    }
}
