package service;
import dao.VenueDAO;
import dto.VenueDTO;
import java.util.List;
public class VenueService {
    private final VenueDAO venueDAO;
    public VenueService(VenueDAO venueDAO) { this.venueDAO = venueDAO; }

    public List<VenueDTO> getAllVenues() { return null; }                                         // 공연장 전체 목록
    public VenueDTO getVenue(int venueId) { return null; }                                        // 공연장 1개 조회
    public void addVenue(String venueName, String address) {}                                     // 공연장 등록 (빈값 검증 포함)
    public void modifyVenue(int venueId, String venueName, String address) {}                     // 공연장 수정 (존재 여부 확인 후)
    public void removeVenue(int venueId) {}                                                       // 공연장 삭제 (존재 여부 확인 후)
}