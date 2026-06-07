package dao;
import dto.VenueDTO;
import java.util.List;
public interface VenueDAO {
    List<VenueDTO> findAll();               // 공연장 전체 목록 조회
    VenueDTO findById(int venueId);         // ID로 공연장 1개 조회
    int insert(VenueDTO venue);            // 공연장 등록 (관리자)
    									   // 수정 void->int: insert 시 공연장아이디 받기
    void update(VenueDTO venue);            // 공연장 정보 수정 (관리자)
    void delete(int venueId);              // 공연장 삭제 (관리자)
}
