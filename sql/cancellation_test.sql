-- 취소 테이블 테스트 케이스 삽입

-- 수수료를 고려하지 않은 가장 간단한 예메 취소 트랜잭션
-- 트랜잭션 시작

START TRANSACTION;

-- 1) booking 테이블에서 3번 예매의 상태를 'CANCELLED'로 변경
UPDATE booking 
SET booking_status = 'CANCELED' 
WHERE booking_id = 3;

-- 2) cancellation 테이블에 취소 내역 추가
-- (booking_id, 환불금액, 취소시각, 수수료) 취소상태는 default로 'REQUESTED'
INSERT INTO cancellation (booking_id, refund_amount, cancellation_fee)
VALUES (3, 220000, 0);

-- 모든 쿼리가 에러 없이 실행되었다면 반영
COMMIT;


-- 이미 취소된 예매에 대한 중복 취소 요청
-- 1번 예매 건에 대한 취소 레코드 최초 삽입

INSERT INTO cancellation (booking_id, refund_amount, cancellation_fee, cancel_status)
VALUES (1, 220000, 0, 'REFUNDED');


-- [Test 2] 동일한 1번 건으로 한 번 더 삽입 시도 (UNIQUE 제약 조건 -> 에러 발생해야 함)
-- 에러 메시지 예시: Duplicate entry '1' for key 'PRIMARY'
INSERT INTO cancellation (booking_id, refund_amount, cancellation_fee, cancel_status)
VALUES (1, 220000, 0, 'REQUESTED');


-- 수수료 로직 점검
-- 공연: 6/1

INSERT INTO booking (member_id, performance_seat_id, booking_status, booked_at, payment)
VALUES 
(1, 11, 'BOOKED', '2026-05-05 10:13:00', 220000), -- 예매 후 7일 이내 취소: 5/6
(1, 12, 'BOOKED', '2026-05-05 10:30:00', 220000), -- 예매 후 8일 ~ 관람일 10일 전 취소: 5/20
(1, 13, 'BOOKED', '2026-05-22 10:30:00', 220000), -- 관람일 7일~9일전 취소: 5/24
(1, 14, 'BOOKED', '2026-05-22 10:30:00', 220000), -- 관람일 3일~6일전 취소: 5/27
(1, 15, 'BOOKED', '2026-05-22 10:30:00', 220000), -- 관람일 1일~ 2일 전 취소: 5/31
(1, 16, 'BOOKED', '2026-05-22 10:30:00', 220000), -- 관람일 당일 취소: 6/1
(1, 17, 'BOOKED', '2026-05-22 10:30:00', 220000); -- 관람 이후 취소 : 6/3


START TRANSACTION;

SET @target_booking_id = 22; 
SET @test_cancel_date = '2026-05-24 14:00:00';

-- 1) 예매 상태 취소로 변경
UPDATE booking 
SET booking_status = 'CANCELLED' 
WHERE booking_id = @target_booking_id;

-- 2) booking -> performance_seat -> performance 3개 테이블 조인 후 자동 계산 및 삽입
INSERT INTO cancellation (booking_id, cancellation_fee, refund_amount, canceled_at, cancel_status)
SELECT 
    b.booking_id,
    
    -- [A] 수수료 금액 자동 계산
    CAST(b.payment * (
        CASE 
            -- 규칙 1: 관람 이후 취소 불가 대책 (마이너스 일수)
            WHEN DATEDIFF(p.start_time, @test_cancel_date) < 0 THEN 1.0  
            -- 규칙 2: 관람일 당일 취소 (70%)
            WHEN DATEDIFF(p.start_time, @test_cancel_date) = 0 THEN 0.7
            -- 규칙 3: 관람일 1일 전 ~ 2일 전 (30%)
            WHEN DATEDIFF(p.start_time, @test_cancel_date) BETWEEN 1 AND 2 THEN 0.3
            -- 규칙 4: 관람일 3일 전 ~ 6일 전 (20%)
            WHEN DATEDIFF(p.start_time, @test_cancel_date) BETWEEN 3 AND 6 THEN 0.2
            -- 규칙 5: 관람일 7일 전 ~ 9일 전 (10%)
            WHEN DATEDIFF(p.start_time, @test_cancel_date) BETWEEN 7 AND 9 THEN 0.1
            -- 규칙 6: 예매 후 7일 이내 취소 (수수료 없음)
            WHEN DATEDIFF(@test_cancel_date, b.booked_at) <= 7 THEN 0.0
            -- 규칙 7: 예매 후 8일 이후 ~ 관람일 10일 전까지 (7%)
            ELSE 0.07
        END
    ) AS SIGNED) AS cancellation_fee,

    -- [B] 최종 환불 금액 자동 계산 (원금 - 자동 계산된 수수료)
    b.payment - CAST(b.payment * (
        CASE 
            WHEN DATEDIFF(p.start_time, @test_cancel_date) < 0 THEN 1.0
            WHEN DATEDIFF(p.start_time, @test_cancel_date) = 0 THEN 0.7
            WHEN DATEDIFF(p.start_time, @test_cancel_date) BETWEEN 1 AND 2 THEN 0.3
            WHEN DATEDIFF(p.start_time, @test_cancel_date) BETWEEN 3 AND 6 THEN 0.2
            WHEN DATEDIFF(p.start_time, @test_cancel_date) BETWEEN 7 AND 9 THEN 0.1
            WHEN DATEDIFF(@test_cancel_date, b.booked_at) <= 7 THEN 0.0
            ELSE 0.07
        END
    ) AS SIGNED) AS refund_amount,
    
    @test_cancel_date AS canceled_at,
    'REFUNDED' AS cancel_status
FROM booking b

INNER JOIN performance_seat ps ON b.performance_seat_id = ps.performance_seat_id
INNER JOIN performance p ON ps.performance_id = p.performance_id
WHERE b.booking_id = @target_booking_id;

COMMIT;