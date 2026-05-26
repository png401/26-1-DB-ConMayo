// MemberService.java
package service;
import dao.MemberDAO;
import dto.MemberDTO;
import java.util.List;
public class MemberService {
    private final MemberDAO memberDAO;
    public MemberService(MemberDAO memberDAO) { this.memberDAO = memberDAO; }

    public MemberDTO login(String memberId, String passwd) { return null; }           // 로그인 (ID/PW 검증)
    public void register(MemberDTO member) {}                                         // 회원가입
    public boolean isBlacklisted(MemberDTO member) { return false; }                  // 블랙리스트 여부 확인 (로그인 시 체크)
    public List<MemberDTO> detectFastBookers(int performanceId) { return null; }      // 비정상 예매속도 의심 회원 탐지
    public List<MemberDTO> detectHighCancelRateMembers() { return null; }             // 상습 취소자 탐지 (10건↑, 70%↑)
    public List<MemberDTO> getCurrentBlacklist() { return null; }                     // 현재 블랙리스트 목록 조회
    public void addToBlacklist(String memberId) {}                                    // 블랙리스트 등록 (30일)
}
