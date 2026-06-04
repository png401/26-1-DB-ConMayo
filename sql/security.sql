-- 데이터베이스 보안: 사용자 계정 및 권한 관리

-- 일반 회원용 계정
CREATE USER IF NOT EXISTS 'conmayo_user'@'localhost' IDENTIFIED BY 'user_pw123';
GRANT SELECT ON conmayo.* TO 'conmayo_user'@'localhost';
GRANT INSERT, UPDATE ON conmayo.booking TO 'conmayo_user'@'localhost';
GRANT INSERT, UPDATE ON conmayo.review TO 'conmayo_user'@'localhost';
GRANT INSERT ON conmayo.cancellation TO 'conmayo_user'@'localhost';
GRANT UPDATE ON conmayo.member TO 'conmayo_user'@'localhost';

-- 관리자용 계정
CREATE USER IF NOT EXISTS 'conmayo_admin'@'localhost' IDENTIFIED BY 'admin_pw123';
GRANT ALL PRIVILEGES ON conmayo.* TO 'conmayo_admin'@'localhost' WITH GRANT OPTION;

FLUSH PRIVILEGES;