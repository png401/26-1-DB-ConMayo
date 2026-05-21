-- ConMayo 공연 예매 관리 데이터베이스

CREATE DATABASE IF NOT EXISTS conmayo
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE conmayo;


-- 회원 테이블
CREATE TABLE IF NOT EXISTS member (
	member_id VARCHAR(20) PRIMARY KEY,
	passwd VARCHAR(20) NOT NULL,
	member_name VARCHAR(20) NOT NULL,
	phone VARCHAR(15) UNIQUE,
	blacklist_until DATETIME DEFAULT NULL,
	member_role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER'
	);


-- 공연장 테이블
CREATE TABLE IF NOT EXISTS venue (
	venue_id INT AUTO_INCREMENT PRIMARY KEY,
	venue_name VARCHAR(100),
	address VARCHAR(255)
	);


-- 공연 테이블 
CREATE TABLE IF NOT EXISTS performance (
	performance_id INT AUTO_INCREMENT PRIMARY KEY,
	title VARCHAR(255) NOT NULL,
	category VARCHAR(20) NOT NULL,
	start_time DATETIME NOT NULL,
	running_time INT NOT NULL,
	sales_status ENUM(
		'OPEN', 'CLOSED', 'SOLD_OUT', 'COMING_SOON'
		) NOT NULL,
	booking_open DATETIME NOT NULL,
	venue_id INT NOT NULL,
	FOREIGN KEY(venue_id) REFERENCES venue(venue_id)
	);
	

-- 좌석 테이블
CREATE TABLE IF NOT EXISTS seat (
	seat_id INT AUTO_INCREMENT PRIMARY KEY,
	venue_id INT NOT NULL,
	section VARCHAR(10) NOT NULL,
	row_num INT NOT NULL CHECK (row_num >= 1),
	col_num INT NOT NULL CHECK (col_num >= 1),
	FOREIGN KEY(venue_id) REFERENCES venue(venue_id),
	UNIQUE (venue_id, section, row_num, col_num)
	);
	
	
-- 공연좌석 테이블
CREATE TABLE IF NOT EXISTS performance_seat (
	performance_seat_id INT AUTO_INCREMENT PRIMARY KEY,
	performance_id INT NOT NULL,
	seat_id INT NOT NULL,
	price INT NOT NULL,
	FOREIGN KEY(performance_id) REFERENCES performance(performance_id),
	FOREIGN KEY(seat_id) REFERENCES seat(seat_id),
	UNIQUE (performance_id, seat_id)
	);
	
	
-- 예매 테이블
CREATE TABLE IF NOT EXISTS booking (
	booking_id INT AUTO_INCREMENT PRIMARY KEY,
	member_id VARCHAR(20) NOT NULL,
	performance_seat_id INT NOT NULL,
	booking_status ENUM(
		'HOLD', 'BOOKED', 'CANCELED'
		) NOT NULL DEFAULT 'HOLD',
	booked_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	payment INT,
	FOREIGN KEY(member_id) REFERENCES member(member_id),
	FOREIGN KEY(performance_seat_id) REFERENCES performance_seat(performance_seat_id)
	);
	
	
-- 리뷰 테이블
CREATE TABLE IF NOT EXISTS review (
	review_id INT AUTO_INCREMENT PRIMARY KEY,
	booking_id INT NOT NULL UNIQUE,
	seat_rating INT NOT NULL CHECK(
		seat_rating BETWEEN 1 AND 5
		),
	written_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	content TEXT NOT NULL,
	FOREIGN KEY(booking_id) REFERENCES booking(booking_id)
	);
	

-- 취소 테이블
CREATE TABLE IF NOT EXISTS cancellation (
	booking_id INT PRIMARY KEY,
	refund_amount INT NOT NULL,
	canceled_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	cancellation_fee INT NOT NULL DEFAULT 0,
	cancel_status ENUM(
		'REQUESTED', 'PENDING_REFUND', 'REFUNDED'
		),
	FOREIGN KEY(booking_id) REFERENCES booking(booking_id)
	);