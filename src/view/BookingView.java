package view;

import dto.BookingDTO;
import dto.BookingStatus;

import java.util.List;
import java.util.Scanner;

public class BookingView {
    private final Scanner sc = new Scanner(System.in);

    public void printWaiting(int queueNum, int availableCount) {
        System.out.println("=== 예매 대기 ==============");
        System.out.println("내 대기 순번: " + queueNum);
        System.out.println("현재 잔여석 : " + availableCount);
        System.out.println("[갱신 중... 3초마다 자동 갱신]");
        System.out.println("============================");
    }

    public void printMyBookings(List<BookingDTO> list) {
        System.out.println("=== 예매 내역 ==============");
        if (list.isEmpty()) {
            System.out.println("예매 내역이 없습니다.");
            System.out.println("============================");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            BookingDTO b = list.get(i);
            System.out.printf("[%d] 예매ID: %d | %,d원 | 상태: %s%n",
                    i + 1, b.getBookingId(), b.getPayment(), statusLabel(b.getBookingStatus()));
        }
        System.out.println("============================");
    }

    public int inputBookingId() {
        System.out.print("예매 ID 입력 > ");
        try { return Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    public int inputBookingIndex(int max) {
        System.out.print("공연 번호 선택 또는 0. 뒤로\n선택 > ");
        try {
            int input = Integer.parseInt(sc.nextLine().trim());
            return (input >= 0 && input <= max) ? input : -1;
        } catch (NumberFormatException e) { return -1; }
    }

    public int inputAction() {
        System.out.print("1. 예매취소  2. 리뷰작성  0. 뒤로\n선택 > ");
        try { return Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    public void printSuccess(String msg) {
        System.out.println("✓ " + msg);
    }

    public void printError(String msg) {
        System.out.println("[오류] " + msg);
    }

    private String statusLabel(BookingStatus status) {
        return switch (status) {
            case HOLD -> "결제 대기";
            case BOOKED -> "예매완료";
            case CANCELED -> "취소됨";
        };
    }
}