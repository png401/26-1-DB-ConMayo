package service;
import dao.VenueDAO;
import dto.VenueDTO;
import java.util.List;
public class VenueService {
    private final VenueDAO venueDAO;
    public VenueService(VenueDAO venueDAO) { this.venueDAO = venueDAO; }

    // 1. 공연장 전체 목록
    public List<VenueDTO> getAllVenues() { 
        return venueDAO.findAll(); 
    }
    
    // 2. 공연장 1개 조회
    public VenueDTO getVenue(int venueId) { 
        return venueDAO.findById(venueId); 
    }
    
    
    // 3. 공연장 등록 (빈값 검증 포함)
    public void addVenue(String venueName, String address) {
        // 빈값 및 공백 문자열 검증
        if (venueName == null || venueName.trim().isEmpty() || address == null || address.trim().isEmpty()) {
            System.out.println("오류: 공연장 이름과 주소는 필수 입력 사항입니다.");
            return;
        }

        // 알맞은 DTO 박스를 생성해서 DAO로 전달 (ID는 AUTO_INCREMENT이므로 0 대입)
        VenueDTO venue = new VenueDTO(0, venueName.trim(), address.trim());
        venueDAO.insert(venue);
    }
    
    // 4. 공연장 수정 (존재 여부 확인 후)
    public void modifyVenue(int venueId, String venueName, String address) {
        // 1. 존재 여부 확인
        VenueDTO exist = venueDAO.findById(venueId);
        if (exist == null) {
            System.out.println("오류: 수정하려는 공연장이 존재하지 않습니다. (ID: " + venueId + ")");
            return;
        }

        // 2. 빈값 검증
        if (venueName == null || venueName.trim().isEmpty() || address == null || address.trim().isEmpty()) {
            System.out.println("오류: 수정할 내용에 빈값을 입력할 수 없습니다.");
            return;
        }

        VenueDTO venue = new VenueDTO(venueId, venueName.trim(), address.trim());
        venueDAO.update(venue);
    }
    
    // 5. 공연장 삭제 (존재 여부 확인 후)
    public void removeVenue(int venueId) { 
        // 존재 여부 확인
        VenueDTO exist = venueDAO.findById(venueId);
        if (exist == null) {
            System.out.println("오류: 삭제하려는 공연장이 존재하지 않습니다. (ID: " + venueId + ")");
            return;
        }

        venueDAO.delete(venueId);
    }
}