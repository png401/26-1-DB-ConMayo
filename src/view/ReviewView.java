package view;

import dto.ReviewDTO;
import javax.swing.JOptionPane;
import java.util.List;

public class ReviewView {

    // [출력] 리뷰 목록 출력 (콘솔)
    public void displayReviews(List<ReviewDTO> list) {
        System.out.println("\n═══════════════════════════════");
        System.out.println("         좌석 리뷰 목록");
        System.out.println("═══════════════════════════════");

        if (list.isEmpty()) {
            System.out.println("  등록된 리뷰가 없습니다.");
        } else {
            for (ReviewDTO r : list) {
                System.out.println("  ★ 평점: " + r.getSeatRating() + "점");
                System.out.println("  작성일: " + r.getWrittenAt());
                System.out.println("  내용: " + r.getContent());
                System.out.println("  ───────────────────────────");
            }
        }
        System.out.println("═══════════════════════════════\n");
    }

    // [입력] 별점 입력받기 (JOptionPane, 취소 시 -1 반환)
    public int inputRating() {
        String[] options = {"★1", "★★2", "★★★3", "★★★★4", "★★★★★5"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "별점을 선택하세요:",
                "리뷰 작성 - 별점",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[2] // 기본값: 3점
        );
        return (choice == JOptionPane.CLOSED_OPTION) ? -1 : choice + 1;
    }

    // [입력] 리뷰 내용 입력받기 (JOptionPane, 취소 시 null 반환)
    public String inputContent() {
        return JOptionPane.showInputDialog(
                null,
                "리뷰 내용을 입력하세요:",
                "리뷰 작성 - 내용",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    // [출력] 성공/실패 메시지 출력 (JOptionPane 팝업)
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }
    
    //[출력] 해당 좌석 리뷰 보여주기 
    public void printReviewsBySeatDialog(List<ReviewDTO> reviews) {
        if (reviews.isEmpty()) {
            JOptionPane.showMessageDialog(null, "이 좌석의 리뷰가 없습니다.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (ReviewDTO r : reviews) {
            sb.append(String.format("★%d점 | %s%n", r.getSeatRating(),
                    r.getWrittenAt().toLocalDate()));
            sb.append("  ").append(r.getContent()).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "좌석 리뷰", JOptionPane.PLAIN_MESSAGE);
    }
    
    // [출력] 내 리뷰 목록 출력 + 수정할 리뷰 ID 선택 (-1이면 취소)
    public int printMyReviewsAndSelect(List<ReviewDTO> reviews) {
        StringBuilder sb = new StringBuilder();
        sb.append("내 리뷰 목록\n");
        sb.append("═══════════════════════════════\n");
        for (ReviewDTO r : reviews) {
            sb.append(String.format("[ID:%d] ★%d점 | %s%n",
                    r.getReviewId(), r.getSeatRating(), r.getWrittenAt().toLocalDate()));
            sb.append("  ").append(r.getContent()).append("\n\n");
        }
        sb.append("═══════════════════════════════");

        String input = JOptionPane.showInputDialog(
                null,
                sb.toString() + "\n\n수정할 리뷰 ID를 입력하세요. (취소하려면 닫기)",
                "내 리뷰 조회",
                JOptionPane.PLAIN_MESSAGE
        );

        if (input == null || input.isBlank()) return -1;

        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            showMessage("올바른 ID를 입력해주세요.");
            return -1;
        }
    }
}