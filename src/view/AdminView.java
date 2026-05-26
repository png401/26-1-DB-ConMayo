package view;
import dto.MemberDTO;
import dto.PerformanceSeatDTO;
import dto.VenueDTO;
import java.util.List;
import java.util.Scanner;
public class AdminView {
    private final Scanner sc = new Scanner(System.in);

    public int showAdminMenu() { return 0; }                              // 관리자 메뉴 출력 후 선택값 반환
    public void printVenueList(List<VenueDTO> list) {}                    // 공연장 목록 출력
    public String inputVenueName() { return null; }                       // 공연장 이름 입력받기
    public String inputVenueAddress() { return null; }                    // 공연장 주소 입력받기
    public int inputVenueId() { return 0; }                               // 공연장 ID 입력받기
    public void printBlacklist(List<MemberDTO> list) {}                   // 블랙리스트 목록 출력
    public void printSuspects(List<MemberDTO> list, String title) {}      // 의심 회원 목록 출력
    public String inputMemberIdToBlacklist() { return null; }             // 블랙리스트 등록/해제할 회원 ID 입력받기
    public int inputPerformanceId() { return 0; }                         // 공연 ID 입력받기
    public void printPerfSeatList(List<PerformanceSeatDTO> list) {}       // 공연좌석-가격 목록 출력
    public int inputPrice() { return 0; }                                 // 가격 입력받기
    public void printSuccess(String msg) {}                               // 성공 메시지 출력
    public void printError(String msg) {}                                 // 에러 메시지 출력
}
