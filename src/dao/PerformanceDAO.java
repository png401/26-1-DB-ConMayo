// PerformanceDAO.java
package dao;
import dto.PerformanceDTO;
import java.util.List;
public interface PerformanceDAO {
    List<PerformanceDTO> findAll();                              // 공연 전체 목록 조회
    List<PerformanceDTO> findByCategory(String category);        // 카테고리별 공연 조회 (콘서트/뮤지컬/스포츠)
    PerformanceDTO findById(int performanceId);                  // ID로 공연 1개 조회
    void insert(PerformanceDTO performance);                     // 공연 등록 (관리자)
    void update(PerformanceDTO performance);                     // 공연 정보 수정 (관리자)
    void delete(int performanceId);                             // 공연 삭제 (관리자)
}