package service;
import dao.MemberDAO;
import dto.MemberDTO;
import dto.MemberRole;

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
    
    
    public void register(MemberDTO member) {
    	// 회원가입
    	member.setMemberRole(MemberRole.USER);
    	memberDAO.insert(member);
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
    
    
    public void addToBlacklist(String memberId) {
    	// 블랙리스트 등록 (7일)
    	
    	LocalDateTime blacklistUntil =
                LocalDateTime.now().plusDays(7);

        memberDAO.setBlacklist(memberId, blacklistUntil);
    }
    
    public void releaseBlacklist(String memberId) {
        // 블랙리스트 해제

        memberDAO.releaseBlacklist(memberId);
    }
    
}
