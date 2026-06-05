package controller;
import java.util.List;

import dto.VenueDTO;
import service.VenueService;
import view.AdminView;
import view.VenueView;
public class VenueController {
    private final VenueService venueService;
    private final AdminView adminView;
    private final VenueView venueView;
    
    public VenueController(VenueService venueService, AdminView adminView, VenueView venueView) {
        this.venueService = venueService;
        this.adminView = adminView;
        this.venueView = venueView;
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

        String name = "";
        String address = "";
        
        // 공연장 이름
        while(true) {
        	name = adminView.inputVenueName();
        	if (name!=null && !name.trim().isEmpty()) {
        		break;
        	}
        	adminView.printError("⚠ 공연장 이름은 필수 입력 사항입니다.\n");
        }
        
        //공연장 주소
        while (true) {
            address = adminView.inputVenueAddress();
            if (address != null && !address.trim().isEmpty()) {
                break;
            }
            adminView.printError("⚠ 공연장 주소는 필수 입력 사항입니다.\n");
        }

        venueService.addVenue(name, address);
        adminView.printSuccess("공연장이 성공적으로 등록되었습니다.");
        showAll(); // 등록 후 목록 갱신
    }
    
    // 3. 공연장 수정
    public void modify() {
    	if (adminView == null) return;
        showAll();
        int venueId = adminView.inputVenueId(); // View에서 입력
        
        // DB에 해당 공연장이 진짜 있는지 조회
        VenueDTO targetVenue = venueService.getVenue(venueId);
        if (targetVenue == null) {
            // 존재하지 않으면 에러 메시지만 띄우고 아래 코드를 실행하지 않고 바로 종료
            adminView.printError("수정하려는 공연장이 존재하지 않습니다. (ID: " + venueId + ")");
            return; 
        }
        
        String name = adminView.inputVenueName();
        String address = adminView.inputVenueAddress();
        venueService.modifyVenue(venueId, name, address);
        
        if (name != null && !name.trim().isEmpty() && address != null && !address.trim().isEmpty()) {
            adminView.printSuccess("공연장 정보가 수정되었습니다.");
        }
        showAll();
    }
    
    // 4. 공연장 삭제
    public void remove() {
    	if (adminView == null) return;
        showAll();
        int venueId = adminView.inputVenueId(); // View에서 입력
        
        VenueDTO targetVenue = venueService.getVenue(venueId);
        if (targetVenue == null) {
            adminView.printError("삭제하려는 공연장이 존재하지 않습니다. (ID: " + venueId + ")");
            return; 
        }
        
        venueService.removeVenue(venueId);
        adminView.printSuccess("공연장이 삭제되었습니다.");
        showAll();
    }
    
    // 5. 사용자용 공연장 목록 출력 후 venueId 반환
    public int showListForUser() {
        List<VenueDTO> list = venueService.getAllVenues();
        return venueView.showVenueList(list);
    }
}
