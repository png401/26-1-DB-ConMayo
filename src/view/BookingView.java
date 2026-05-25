package view;
import dto.BookingDTO;
import java.util.List;
import java.util.Scanner;
public class BookingView {
    private final Scanner sc = new Scanner(System.in);

    public void printWaiting(int queueNum, int availableCount) {}  // 대기 순번 + 잔여석 출력
    public void printMyBookings(List<BookingDTO> list) {}          // 예매 내역 목록 출력
    public int inputBookingId() { return 0; }                      // 예매 ID 입력받기
    public boolean confirmCancel(BookingDTO booking) { return false; } // 취소 확인 (수수료 안내 후 y/n)
    public void printSuccess(String msg) {}                        // 성공 메시지 출력
    public void printError(String msg) {}                          // 에러 메시지 출력
}
