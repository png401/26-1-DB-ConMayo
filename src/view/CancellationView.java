package view;

import dto.BookingDTO;

import java.util.Scanner;

public class CancellationView {
    private final Scanner sc = new Scanner(System.in);

    public boolean confirmCancel(BookingDTO booking) {
        int fee = (int)(booking.getPayment() * 0.1);
        int refund = booking.getPayment() - fee;
        System.out.println("=== 예매 취소 ==============");
        System.out.printf("결제금액   : %,d원%n", booking.getPayment());
        System.out.printf("취소 수수료 : %,d원%n", fee);
        System.out.printf("환불 예정액 : %,d원%n", refund);
        System.out.println("============================");
        System.out.print("정말 취소하시겠습니까? (y/n)\n선택 > ");
        return sc.nextLine().trim().equalsIgnoreCase("y");
    }
}
