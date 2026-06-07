package service;
import dao.SeatDAO; //import 문 추가
import dao.VenueDAO;
import dto.VenueDTO;
import java.util.List;
public class VenueService {
    private final VenueDAO venueDAO;
    private final SeatDAO seatDAO; //필드 추가
    public VenueService(VenueDAO venueDAO, SeatDAO seatDAO) { //생성자 변경
    	this.venueDAO = venueDAO;
    	this.seatDAO = seatDAO;
    }

    // 1. 공연장 전체 목록
    public List<VenueDTO> getAllVenues() { 
        return venueDAO.findAll(); 
    }
    
    // 2. 공연장 1개 조회
    public VenueDTO getVenue(int venueId) { 
        return venueDAO.findById(venueId); 
    }
    
    
    // 3. 공연장 등록 (빈값 검증 포함)
    // 반환형 수정
    public int addVenue(String venueName, String address) {
        // 빈값 및 공백 문자열 검증
        if (venueName == null || venueName.trim().isEmpty() || address == null || address.trim().isEmpty()) {
            System.out.println("오류: 공연장 이름과 주소는 필수 입력 사항입니다.");
            return -1; //int 반환 위해 -1 반환
        }

        // 알맞은 DTO 박스를 생성해서 DAO로 전달 (ID는 AUTO_INCREMENT이므로 0 대입)
        VenueDTO venue = new VenueDTO(0, venueName.trim(), address.trim());
        // return 추가
        return venueDAO.insert(venue);
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
    
    // 추가 - 공연장 추가 시, 공연장의 좌석 생성
    public void createSeatsForVenue(int venueId, String section, int rows, int cols) {
        seatDAO.createSeatsForVenue( venueId, section, rows, cols);
    }
    
    // 추가 - 공연장에 등록된 공연이 있으면 true 반환
    public boolean hasPerformance(int venueId) {
        return venueDAO.hasPerformance(venueId);
    }
}