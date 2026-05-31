package dao;
import dto.MemberDTO;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
public interface MemberDAO {

    MemberDTO findById(String memberId);		// ID로 회원 1명 조회
    MemberDTO login(String memberId, String passwd);	// 로그인 
    void insert(MemberDTO member);				// 회원 가입
    void update(MemberDTO member);				// 회원 정보 
    List<MemberDTO> findCurrentBlacklist();		// 현재 블랙리스트 전체 조회
    void setBlacklist(String memberId, LocalDateTime blacklistUntil);	// 블랙리스트 등록 (해제일 설정)
    void releaseBlacklist(String memberId);		// 블랙리스트 해제 (NULL로 초기화)
	boolean existsById(String memberId) throws SQLException;
}
