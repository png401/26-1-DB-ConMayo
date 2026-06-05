-- 데이터베이스 보안: 사용자 계정 및 권한 관리

-- 일반 회원용 계정
CREATE USER IF NOT EXISTS 'conmayo_user'@'localhost' IDENTIFIED BY 'user_pw123';
GRANT SELECT ON conmayo.* TO 'conmayo_user'@'localhost'; -- 전체 테이블 조회
GRANT INSERT, UPDATE ON conmayo.booking TO 'conmayo_user'@'localhost'; -- 예매 생성 및 상태 변경
GRANT INSERT, UPDATE ON conmayo.review TO 'conmayo_user'@'localhost'; -- 리뷰 작성 및 수정
GRANT INSERT ON conmayo.cancellation TO 'conmayo_user'@'localhost'; -- 취소 요청 생성
GRANT UPDATE ON conmayo.member TO 'conmayo_user'@'localhost'; -- blacklist_until 갱신 (트리거 보조)
GRANT EXECUTE ON conmayo.* TO 'conmayo_user'@'localhost'; -- 함수 실행 시 필요

-- 관리자용 계정
CREATE USER IF NOT EXISTS 'conmayo_admin'@'localhost' IDENTIFIED BY 'admin_pw123';
GRANT ALL PRIVILEGES ON conmayo.* TO 'conmayo_admin'@'localhost' WITH GRANT OPTION;

FLUSH PRIVILEGES;
