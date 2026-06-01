package controller;
import java.util.List;

import dto.VenueDTO;
import service.VenueService;
import view.AdminView;
public class VenueController {
    private final VenueService venueService;
    private final AdminView adminView;
    public VenueController(VenueService venueService, AdminView adminView) {
        this.venueService = venueService;
        this.adminView = adminView;
    }

    // 1. 공연장 전체 목록 출력
    public void showAll() {
        List<VenueDTO> list = venueService.getAllVenues();
        
        if (adminView != null) {
            adminView.printVenueList(list);
        }
    }
    
    // 2. 공연장 등록
    public void add() {
        if (adminView == null) return;

        String name = adminView.inputVenueName();
        String address = adminView.inputVenueAddress();

        venueService.addVenue(name, address);
        adminView.printSuccess("공연장이 성공적으로 등록되었습니다.");
        showAll(); // 등록 후 목록 갱신
    }
    
    // 3. 공연장 수정
    public void modify() {
    	if (adminView == null) return;
        showAll();
        int venueId = adminView.inputVenueId(); // View에서 입력
        String name = adminView.inputVenueName();
        String address = adminView.inputVenueAddress();
        venueService.modifyVenue(venueId, name, address);
        adminView.printSuccess("공연장 정보가 수정되었습니다.");
        showAll();
    }
    
    // 4. 공연장 삭제
    public void remove() {
    	if (adminView == null) return;
        showAll();
        int venueId = adminView.inputVenueId(); // View에서 입력
        venueService.removeVenue(venueId);
        adminView.printSuccess("공연장이 삭제되었습니다.");
        showAll();
    }
}
