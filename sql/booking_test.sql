-- booking 테스트 케이스 삽입
-- HOLD 중
INSERT INTO booking (booking_id, member_id, performance_seat_id, booking_status, booked_at, payment)
VALUES 
(1, 1, 1, 'HOLD', '2026-05-22 10:13:00', 220000),
(2, 2, 2, 'HOLD', '2026-05-22 10:30:00', 220000);

-- BOOKED 
UPDATE booking
SET booking_status='BOOKED', booked_at = '2026-05-22 10:15:30'
WHERE booking_id=1;

UPDATE booking
SET booking_status='BOOKED', booked_at = NOW()
WHERE booking_id=2;

-- 예매 후 CANCELED
UPDATE booking
SET booking_status='CANCELED'
WHERE booking_id=1;

-- 취소된 좌석 재예매
INSERT INTO booking (booking_id, member_id, performance_seat_id, booking_status, booked_at, payment)
VALUES 
(3, 2, 1, 'HOLD', '2026-05-22 10:42:00', 220000);

UPDATE booking
SET booking_status='BOOKED', booked_at = NOW()
WHERE booking_id=3;

-- HOLD 좌석 예매 시도
INSERT INTO booking (member_id, performance_seat_id, booking_status, booked_at, payment)
VALUES 
(1, 3, 'HOLD', '2026-05-22 10:47:00', 220000);

INSERT INTO booking (member_id, performance_seat_id, booking_status, booked_at, payment)
VALUES 
(2, 3, 'HOLD', '2026-05-22 10:49:00', 220000);
-- 현재는 HOLD 중인 좌석도 중복 HOLD가 됨

-- BOOKED 좌석 예매 시도
INSERT INTO booking (member_id, performance_seat_id, booking_status, booked_at, payment)
VALUES 
(1, 2, 'HOLD', '2026-05-22 10:50:00', 220000);
-- BOOKED된 자석도 HOLD됨

-- 존재하지 않는 회원이 공연 예매 시도
INSERT INTO booking (member_id, performance_seat_id, booking_status, booked_at, payment)
VALUES 
(4, 1, 'HOLD', '2026-05-22 10:51:00', 220000);

-- 존재하지 않는 공연 예매 시도
INSERT INTO booking (member_id, performance_seat_id, booking_status, booked_at, payment)
VALUES 
(1, 500, 'HOLD', '2026-05-22 10:51:00', 220000);


-- 유효하지 않은 결제 금액으로 예매 시도

INSERT INTO booking (member_id, performance_seat_id, booking_status, booked_at, payment)
VALUES 
(1, 4, 'HOLD', '2026-05-22 10:55:00', 320000),
(1, 5, 'HOLD', '2026-05-22 10:56:00', -5000);
-- 현재는 유효하지 않은 금액도 전부 추가됨.