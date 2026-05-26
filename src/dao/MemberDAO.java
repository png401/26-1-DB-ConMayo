package dao;
import dto.MemberDTO;
import java.util.List;
public interface MemberDAO {

    MemberDTO findById(String memberId);                                              // ID로 회원 1명 조회
    void insert(MemberDTO member);                                                    // 회원 가입
    void update(MemberDTO member);                                                    // 회원 정보 수정
    List<MemberDTO> findFastBookers(int performanceId, int thresholdSeconds);         // 비정상 예매속도 의심 회원 탐지
    List<MemberDTO> findHighCancelRateMembers(int minBookings, double cancelRate);    // 상습 취소자 탐지
    List<MemberDTO> findCurrentBlacklist();                                           // 현재 블랙리스트 전체 조회
    void setBlacklist(String memberId, String blacklistUntil);                        // 블랙리스트 등록 (해제일 설정)
    void releaseBlacklist(String memberId);                                           // 블랙리스트 해제 (NULL로 초기화)
}
