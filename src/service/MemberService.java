package service;
import dao.MemberDAO;
import dto.MemberDTO;
import dto.MemberRole;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
public class MemberService {
	
    private final MemberDAO memberDAO;
    
    public MemberService(MemberDAO memberDAO) { 
    	this.memberDAO = memberDAO; 
    }

    
    public MemberDTO login(String memberId, String passwd) { 
    	// 로그인 (ID/PW 검증)
    	return memberDAO.login(memberId, passwd);
    }
    
    
    public void register(String id, String pw, MemberDTO member) {
    	// 회원가입
    	member.setMemberRole(MemberRole.USER);
    	member.setMemberId(id);
    	member.setPasswd(pw);
    	memberDAO.insert(member);
    }
    
    public boolean isDuplicatedId(String memberId) throws SQLException {
    	// 회원가입 시 아이디 중복 확인 
    	
    	return memberDAO.existsById(memberId);
    }
    
    
    public boolean isBlacklisted(MemberDTO member) { 
    	// 블랙리스트 여부 확인 (로그인 시 체크)
    	
    	if (member == null) {
            return false;
        }

        return member.getBlacklistUntil() != null
                && member.getBlacklistUntil().isAfter(LocalDateTime.now());
    }
    
    
    public List<MemberDTO> getCurrentBlacklist() { 
    	// 현재 블랙리스트 목록 조회
    	return memberDAO.findCurrentBlacklist();
    }   
    
    
    public boolean addToBlacklist(String memberId) {
    	// 블랙리스트 등록 (7일)
    	
    	MemberDTO member = memberDAO.findById(memberId);
        if (member == null) {
            return false; // 존재하지 않는 회원인 경우 false 반환
        }

        LocalDateTime blacklistUntil = LocalDateTime.now().plusDays(7);
        memberDAO.setBlacklist(memberId, blacklistUntil);
        return true; // 등록 성공 시 true 반환
    }
    
    public int releaseBlacklist(String memberId) {
        // 블랙리스트 해제

    	MemberDTO member = memberDAO.findById(memberId);
    	
    	if (member == null) {
    		return 1;
    	}
    	
    	if (!isBlacklisted(member)) {
    		return 2;
    	}
    	
    	memberDAO.releaseBlacklist(memberId);
    	return 0;
    }
    
}
