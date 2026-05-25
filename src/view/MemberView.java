package view;
import dto.MemberDTO;
import java.util.Scanner;
public class MemberView {
    private final Scanner sc = new Scanner(System.in);

    public int showMenu() { return 0; }                        // 메인 메뉴 출력 후 선택값 반환
    public String inputId() { return null; }                   // 아이디 입력받기
    public String inputPassword() { return null; }             // 비밀번호 입력받기
    public MemberDTO inputMemberInfo() { return null; }        // 회원가입 정보 입력받기
    public void printLoginSuccess(MemberDTO member) {}         // 로그인 성공 메시지 출력
    public void printBlacklistWarning(String until) {}         // 블랙리스트 경고 출력
    public void printError(String msg) {}                      // 에러 메시지 출력
    public void printSuccess(String msg) {}                    // 성공 메시지 출력
}
