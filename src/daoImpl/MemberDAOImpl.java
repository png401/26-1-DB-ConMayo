package daoImpl;

import dao.MemberDAO;
import dto.MemberDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberDAOImpl implements MemberDAO {
    private final Connection conn; // Main에서 주입받은 DB 연결

    public MemberDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public MemberDTO findById(String memberId) {
        // SELECT * FROM member WHERE member_id = ?
        // → 로그인 시 ID/PW 검증, 블랙리스트 체크에 사용
        return null; // TODO
    }

    @Override
    public void insert(MemberDTO member) {
        // INSERT INTO member (member_id, passwd, member_name, phone, member_role)
        // → 회원가입 시 호출
    } // TODO

    @Override
    public void update(MemberDTO member) {
        // UPDATE member SET ... WHERE member_id = ?
        // → 회원 정보 수정 시 호출
    } // TODO

    @Override
    public List<MemberDTO> findFastBookers(int performanceId, int thresholdSeconds) {
        // 예매 오픈 후 N초 이내에 BOOKED 완료한 회원 탐지
        // TIMESTAMPDIFF(SECOND, p.booking_open, b.booked_at) <= thresholdSeconds
        return new ArrayList<>(); // TODO
    }

    @Override
    public List<MemberDTO> findHighCancelRateMembers(int minBookings, double cancelRate) {
        // 총 예매 건수 >= minBookings AND 취소율 > cancelRate 인 회원 탐지
        // 기준: 예매 10건 이상 + 취소율 70% 초과
        return new ArrayList<>(); // TODO
    }

    @Override
    public List<MemberDTO> findCurrentBlacklist() {
        // SELECT WHERE blacklist_until IS NOT NULL AND blacklist_until > NOW()
        // → 현재 블랙리스트 상태인 회원만 조회
        return new ArrayList<>(); // TODO
    }

    @Override
    public void setBlacklist(String memberId, String blacklistUntil) {
        // UPDATE member SET blacklist_until = ? WHERE member_id = ?
        // → 블랙리스트 등록 (해제 일시 설정)
    } // TODO

    @Override
    public void releaseBlacklist(String memberId) {
        // UPDATE member SET blacklist_until = NULL WHERE member_id = ?
        // → 블랙리스트 해제
    } // TODO
}
