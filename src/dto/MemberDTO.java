package dto;

import java.time.LocalDateTime;

public class MemberDTO {
	
	private String memberId;	// 회원 ID (PK)
	private String passwd;		// 비밀번호
	private String memberName;	// 회원 이름
	private String phone;		// 전화번호 (UNIQUE)
	private LocalDateTime blacklistUntil;	// 블랙리스트 해제 일시 (NULL이면 정상)
	private MemberRole memberRole;			// 권한 (USER / ADMIN)
	
	public MemberDTO() {}

	public MemberDTO(String memberId, String passwd, 
			String memberName, String phone, 
			LocalDateTime blacklistUntil,
			MemberRole memberRole) {
		this.memberId = memberId;
		this.passwd = passwd;
		this.memberName = memberName;
		this.phone = phone;
		this.blacklistUntil = blacklistUntil;
		this.memberRole = memberRole;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public LocalDateTime getBlacklistUntil() {
		return blacklistUntil;
	}

	public void setBlacklistUntil(LocalDateTime blacklistUntil) {
		this.blacklistUntil = blacklistUntil;
	}

	public MemberRole getMemberRole() {
		return memberRole;
	}

	public void setMemberRole(MemberRole memberRole) {
		this.memberRole = memberRole;
	}

}