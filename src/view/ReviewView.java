package view;
import dto.ReviewDTO;
import java.util.List;
import java.util.Scanner;
public class ReviewView {
    private final Scanner sc = new Scanner(System.in);

    public void printReviews(List<ReviewDTO> list) {}  // 리뷰 목록 출력
    public int inputRating() { return 0; }             // 별점 입력받기 (1~5)
    public String inputContent() { return null; }      // 리뷰 내용 입력받기
    public void printSuccess(String msg) {}            // 성공 메시지 출력
}
