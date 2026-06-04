package view;

import dto.VenueDTO;
import java.util.List;
import java.util.Scanner;

public class VenueView {
    private final Scanner sc = new Scanner(System.in);

    public int showVenueList(List<VenueDTO> list) {
        System.out.println("\n====== 공연장 목록 ======");
        for (VenueDTO v : list) {
            System.out.printf("[%d] %s | %s%n",
                    v.getVenueId(), v.getVenueName(), v.getAddress());
        }
        System.out.println("0. 뒤로");
        System.out.println();

        while (true) {
            System.out.print("선택 > ");
            try {
                int choice = sc.nextInt();
                sc.nextLine();
                // 0이거나 목록에 있는 venueId면 반환
                if (choice == 0 || list.stream().anyMatch(v -> v.getVenueId() == choice)) {
                    return choice;
                }
                System.out.println("\n⚠ 목록에 없는 공연장입니다.\n");
            } catch (Exception e) {
                sc.nextLine(); // 버퍼 비우기
                System.out.println("\n⚠ 숫자를 입력해주세요.\n");
            }
        }
    }
}
