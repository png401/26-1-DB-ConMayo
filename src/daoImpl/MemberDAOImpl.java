package daoImpl;

import dao.MemberDAO;
import db.DatabaseConnector;
import dto.MemberDTO;
import dto.MemberRole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MemberDAOImpl implements MemberDAO {

    // conn 필드 제거 — 매번 getConnection() 호출

    @Override
    public MemberDTO findById(String memberId) {
        // 로그인 시 ID/PW 검증, 블랙리스트 체크에 사용
        String sql = "SELECT * FROM member WHERE member_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, memberId);
            ResultSet rs = stmt.executeQuery();

            // 조회 결과가 존재하면
            if (rs.next()) {
                MemberDTO member = new MemberDTO();
                member.setMemberId(rs.getString("member_id"));
                member.setPasswd(rs.getString("passwd"));
                member.setMemberName(rs.getString("member_name"));
                member.setPhone(rs.getString("phone"));
                member.setMemberRole(MemberRole.valueOf(rs.getString("member_role")));
                Timestamp ts = rs.getTimestamp("blacklist_until");
                if (ts != null) {
                    member.setBlacklistUntil(ts.toLocalDateTime());
                }
                return member;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public boolean existsById(String memberId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM member WHERE member_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, memberId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }


    @Override
    public MemberDTO login(String memberId, String passwd) {
        String sql = "SELECT * FROM member " +
                "WHERE member_id = ? AND passwd = SHA2(?, 256)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, memberId);
            stmt.setString(2, passwd);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                MemberDTO member = new MemberDTO();
                member.setMemberId(rs.getString("member_id"));
                member.setPasswd(rs.getString("passwd"));
                member.setMemberName(rs.getString("member_name"));
                member.setPhone(rs.getString("phone"));
                member.setMemberRole(MemberRole.valueOf(rs.getString("member_role")));
                Timestamp ts = rs.getTimestamp("blacklist_until");
                if (ts != null) {
                    member.setBlacklistUntil(ts.toLocalDateTime());
                }
                return member;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void insert(MemberDTO member) throws SQLException {
        // 회원가입 시 호출
        String sql = "INSERT INTO member " +
                "(member_id, passwd, member_name, phone, member_role) " +
                "VALUES (?, SHA2(?, 256), ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, member.getMemberId());
            stmt.setString(2, member.getPasswd());
            stmt.setString(3, member.getMemberName());
            stmt.setString(4, member.getPhone());
            stmt.setString(5, member.getMemberRole().name());
            stmt.executeUpdate();

        } 
    }


    @Override
    public void update(MemberDTO member) {
        // 회원 정보 수정 시 호출
        String sql = "UPDATE member " +
                "SET passwd = SHA2(?, 256), member_name = ?, phone = ? " +
                "WHERE member_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, member.getPasswd());
            stmt.setString(2, member.getMemberName());
            stmt.setString(3, member.getPhone());
            stmt.setString(4, member.getMemberId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<MemberDTO> findCurrentBlacklist() {
        // 현재 블랙리스트 상태인 회원 조회
        String sql = "SELECT * FROM member " +
                "WHERE blacklist_until IS NOT NULL AND blacklist_until > NOW()";

        List<MemberDTO> list = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MemberDTO member = new MemberDTO();
                member.setMemberId(rs.getString("member_id"));
                member.setPasswd(rs.getString("passwd"));
                member.setMemberName(rs.getString("member_name"));
                member.setPhone(rs.getString("phone"));
                member.setMemberRole(MemberRole.valueOf(rs.getString("member_role")));
                Timestamp ts = rs.getTimestamp("blacklist_until");
                if (ts != null) {
                    member.setBlacklistUntil(ts.toLocalDateTime());
                }
                list.add(member);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    @Override
    public void setBlacklist(String memberId, LocalDateTime blacklistUntil) {
        // 블랙리스트 등록
        String sql = "UPDATE member SET blacklist_until = ? WHERE member_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, blacklistUntil);
            stmt.setString(2, memberId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean releaseBlacklist(String memberId) {
        // 블랙리스트 해제
        String sql = "UPDATE member SET blacklist_until = NULL WHERE member_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, memberId);
            int result = stmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // 추가
    // 최근 7일간 취소 횟수 리턴
    @Override
    public List<Object[]> getRecentCancellationCounts() {

        List<Object[]> result = new ArrayList<>();

        String sql =
                "SELECT b.member_id, COUNT(*) AS cancel_count " +
                "FROM booking b " +
                "JOIN cancellation c " +
                "ON b.booking_id = c.booking_id " +
                "WHERE c.canceled_at >= DATE_SUB(NOW(), INTERVAL 7 DAY) " +
                "GROUP BY b.member_id " +
                "ORDER BY cancel_count DESC";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {

                result.add(new Object[] {
                        rs.getString("member_id"),
                        rs.getInt("cancel_count")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}