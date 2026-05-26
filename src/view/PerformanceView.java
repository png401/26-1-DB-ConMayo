package view;
import dto.PerformanceDTO;
import java.util.List;
import java.util.Scanner;
public class PerformanceView {
    private final Scanner sc = new Scanner(System.in);

    public void printList(List<PerformanceDTO> list) {}          // 공연 목록 출력
    public void printDetail(PerformanceDTO performance) {}       // 공연 상세 출력
    public String inputCategory() { return null; }               // 카테고리 입력받기
    public int inputPerformanceId() { return 0; }                // 공연 ID 입력받기
    public PerformanceDTO inputPerformanceInfo() { return null; } // 공연 등록/수정 정보 입력받기
}
