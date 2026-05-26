package controller;
import service.VenueService;
import view.AdminView;
public class VenueController {
    private final VenueService venueService;
    private final AdminView adminView;
    public VenueController(VenueService venueService, AdminView adminView) {
        this.venueService = venueService;
        this.adminView = adminView;
    }

    public void showAll() {}  // 공연장 전체 목록 출력
    public void add() {}      // 공연장 등록
    public void modify() {}   // 공연장 수정
    public void remove() {}   // 공연장 삭제
}
