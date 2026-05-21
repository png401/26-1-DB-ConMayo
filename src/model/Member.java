package model;

import java.time.LocalDateTime;

public class Member {
	
	private String memberId;
	private String passwd;
	private String memberName;
	private String phone;
	private LocalDateTime blacklistUntil;
	private MemberRole memberRole;
	
	public Member() {}

	public Member(String memberId, String passwd, 
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