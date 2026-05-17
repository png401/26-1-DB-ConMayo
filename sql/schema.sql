-- ConMayo 공연 예매 관리 데이터베이스
-- schema.sql

CREATE DATABASE IF NOT EXISTS conmayo
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE conmayo;

-- 회원 테이블
CREATE TABLE IF NOT EXISTS member (
                                      member_id VARCHAR(20) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    name      VARCHAR(20) NOT NULL,
    phone     VARCHAR(15),
    email     VARCHAR(50),
    PRIMARY KEY (member_id)
    );