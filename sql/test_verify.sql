-- =============================================================
--  ConMayo 검증 쿼리 (test_verify.sql)
--  실행 순서: schema.sql → data.sql → (이 파일)
--  각 쿼리마다 기대 결과 명시
-- =============================================================

USE conmayo;


-- =============================================================
--  정규화 검증
-- =============================================================

-- [정규화] venue 주소 변경 시 공연 전체에 반영되는지 확인 (갱신 이상 없음)
-- 기대: 엔하이픈 공연 주소가 같이 바뀌면 정규화 정상
UPDATE venue SET address = '서울특별시 송파구 테스트로 999' WHERE venue_id = 1;
SELECT p.title, v.address FROM performance p JOIN venue v ON v.venue_id = p.venue_id;
UPDATE venue SET address = '서울특별시 송파구 올림픽로 424' WHERE venue_id = 1;  -- 원복

-- [정규화] 공연 없는 좌석에 평점만 덩그러니 생기는 삽입 이상 확인
-- 기대: 빈 칸 (삽입 이상 없음)
SELECT s.seat_id, AVG(r.seat_rating)
FROM seat s
LEFT JOIN performance_seat ps ON ps.seat_id = s.seat_id
LEFT JOIN booking b           ON b.performance_seat_id = ps.performance_seat_id
LEFT JOIN review r            ON r.booking_id = b.booking_id
WHERE ps.performance_seat_id IS NULL
GROUP BY s.seat_id;

-- [정규화] review에서 booking JOIN 한 번으로 member_id 도달 가능한지 확인 (3NF)
-- 기대: review_id마다 member_id 유일하게 조회됨
SELECT r.review_id, b.member_id, b.booking_id
FROM review r
JOIN booking b ON b.booking_id = r.booking_id;

-- [정규화] 정가(performance_seat.price)와 실결제(booking.payment) 분리 확인
-- 기대: booking_id=14(HOLD)만 payment=NULL, 나머지는 정상값
SELECT b.booking_id, b.booking_status, ps.price, b.payment
FROM performance_seat ps
JOIN booking b ON b.performance_seat_id = ps.performance_seat_id
ORDER BY b.booking_id;


-- =============================================================
--  핵심 기능 검증
-- =============================================================

-- [핵심] 좌석별 평균 평점 조회
-- 기대: booking_id=11(F2석) 5.0, booking_id=13(1A석) 4.0 순으로 조회
SELECT
    s.seat_id,
    s.section,
    s.row_num,
    s.col_num,
    COUNT(r.review_id) AS review_count,
    AVG(r.seat_rating)  AS avg_rating
FROM seat s
JOIN performance_seat ps ON ps.seat_id = s.seat_id
JOIN booking b           ON b.performance_seat_id = ps.performance_seat_id
                        AND b.booking_status = 'BOOKED'
LEFT JOIN review r       ON r.booking_id = b.booking_id
GROUP BY s.seat_id, s.section, s.row_num, s.col_num
ORDER BY avg_rating DESC;

-- [핵심] 공연별 예매 현황 집계
-- 기대: 공연별 전체/BOOKED/CANCELED/HOLD 수 확인
SELECT
    p.title,
    COUNT(*)                             AS total,
    SUM(b.booking_status = 'BOOKED')   AS booked,
    SUM(b.booking_status = 'CANCELED') AS canceled,
    SUM(b.booking_status = 'HOLD')     AS hold
FROM booking b
JOIN performance_seat ps ON b.performance_seat_id = ps.performance_seat_id
JOIN performance p        ON ps.performance_id = p.performance_id
GROUP BY p.performance_id, p.title;

-- [핵심] 취소 내역 전체 조회
-- 기대: booking_id=1, 12 두 건 조회 (data.sql 선행 데이터)
SELECT
    c.booking_id,
    b.member_id,
    b.payment,
    c.cancellation_fee,
    c.refund_amount,
    c.cancel_status,
    c.canceled_at
FROM cancellation c
JOIN booking b ON b.booking_id = c.booking_id
ORDER BY c.canceled_at;


-- =============================================================
--  버그 탐지 검증
-- =============================================================

-- [버그] 공연 전 작성된 리뷰 탐지
-- 기대: booking_id=16 → '⚠ 공연 전 리뷰' 탐지 (코드로 막아야 함)
SELECT
    r.review_id,
    r.booking_id,
    r.written_at,
    p.start_time,
    CASE WHEN r.written_at < p.start_time THEN '⚠ 공연 전 리뷰' ELSE 'OK' END AS result
FROM review r
JOIN booking b           ON b.booking_id = r.booking_id
JOIN performance_seat ps ON ps.performance_seat_id = b.performance_seat_id
JOIN performance p        ON p.performance_id = ps.performance_id;

-- [버그] CANCELED / HOLD 예매에 달린 리뷰 탐지
-- 기대: booking_id=12(CANCELED), 14(HOLD) → '⚠ 비정상 상태 리뷰' 탐지 (코드로 막아야 함)
SELECT
    r.review_id,
    b.booking_id,
    b.booking_status,
    CASE WHEN b.booking_status != 'BOOKED' THEN '⚠ 비정상 상태 리뷰' ELSE 'OK' END AS result
FROM review r
JOIN booking b ON b.booking_id = r.booking_id;

-- [버그] 공연장 불일치 좌석 탐지
-- 기대: 모두 'OK' (test_edge.sql에서 불일치 데이터 삽입 시 '⚠ 공연장 불일치' 탐지)
SELECT
    ps.performance_seat_id,
    ps.performance_id,
    ps.seat_id,
    p.venue_id  AS perf_venue,
    s.venue_id  AS seat_venue,
    CASE WHEN p.venue_id != s.venue_id THEN '⚠ 공연장 불일치' ELSE 'OK' END AS result
FROM performance_seat ps
JOIN performance p ON p.performance_id = ps.performance_id
JOIN seat s        ON s.seat_id = ps.seat_id;

-- [버그] CANCELED인데 cancellation 레코드 없는 경우 탐지
-- 기대: 빈 칸 (booking_id=1,12 모두 cancellation 있으므로)
-- (test_edge.sql에서 cancellation 없이 booking만 CANCELED 처리 시 탐지됨)
SELECT b.booking_id, b.booking_status
FROM booking b
LEFT JOIN cancellation c ON c.booking_id = b.booking_id
WHERE b.booking_status = 'CANCELED'
  AND c.booking_id IS NULL;

-- [버그] 중복 리뷰 탐지
-- 기대: 빈 칸 (UNIQUE 제약으로 막혀 있어야 함)
SELECT booking_id, COUNT(*) AS cnt
FROM review
GROUP BY booking_id
HAVING cnt > 1;

-- [버그] BOOKED인데 payment != price 탐지
-- 기대: 빈 칸 (정상이면 price=payment)
SELECT b.booking_id, ps.price, b.payment, (b.payment - ps.price) AS diff
FROM booking b
JOIN performance_seat ps ON ps.performance_seat_id = b.performance_seat_id
WHERE b.booking_status = 'BOOKED'
  AND b.payment != ps.price;


-- =============================================================
--  취소 수수료 트랜잭션 테스트
--  booking_id 21~27 중 하나씩 @target_booking_id 바꿔가며 테스트
--
--  booking_id | 취소일      | 수수료율        | 수수료(165,000원 기준) | 환불액
--  -----------|-------------|----------------|----------------------|--------
--  21         | 2026-05-06  | 0%  (7일 이내)  |                    0 | 165,000
--  22         | 2026-05-20  | 7%             |               11,550 | 153,450
--  23         | 2026-05-24  | 10% (7~9일전)   |               16,500 | 148,500
--  24         | 2026-05-27  | 20% (3~6일전)   |               33,000 | 132,000
--  25         | 2026-05-31  | 30% (1~2일전)   |               49,500 | 115,500
--  26         | 2026-06-01  | 70% (당일)      |              115,500 |  49,500
--  27         | 2026-06-03  | 100% (관람 후)  |              165,000 |       0
-- =============================================================

START TRANSACTION;

SET @target_booking_id = 23;           -- 테스트할 booking_id로 변경
SET @test_cancel_date  = '2026-05-24 14:00:00';  -- 해당 booking_id의 취소일로 변경

UPDATE booking SET booking_status = 'CANCELED' WHERE booking_id = @target_booking_id;

INSERT INTO cancellation (booking_id, cancellation_fee, refund_amount, canceled_at, cancel_status)
SELECT
    b.booking_id,
    CAST(b.payment * (
        CASE
            WHEN DATEDIFF(p.start_time, @test_cancel_date) < 0              THEN 1.0
            WHEN DATEDIFF(p.start_time, @test_cancel_date) = 0              THEN 0.7
            WHEN DATEDIFF(p.start_time, @test_cancel_date) BETWEEN 1 AND 2  THEN 0.3
            WHEN DATEDIFF(p.start_time, @test_cancel_date) BETWEEN 3 AND 6  THEN 0.2
            WHEN DATEDIFF(p.start_time, @test_cancel_date) BETWEEN 7 AND 9  THEN 0.1
            WHEN DATEDIFF(@test_cancel_date, b.booked_at)  <= 7             THEN 0.0
            ELSE 0.07
        END
    ) AS SIGNED) AS cancellation_fee,
    b.payment - CAST(b.payment * (
        CASE
            WHEN DATEDIFF(p.start_time, @test_cancel_date) < 0              THEN 1.0
            WHEN DATEDIFF(p.start_time, @test_cancel_date) = 0              THEN 0.7
            WHEN DATEDIFF(p.start_time, @test_cancel_date) BETWEEN 1 AND 2  THEN 0.3
            WHEN DATEDIFF(p.start_time, @test_cancel_date) BETWEEN 3 AND 6  THEN 0.2
            WHEN DATEDIFF(p.start_time, @test_cancel_date) BETWEEN 7 AND 9  THEN 0.1
            WHEN DATEDIFF(@test_cancel_date, b.booked_at)  <= 7             THEN 0.0
            ELSE 0.07
        END
    ) AS SIGNED) AS refund_amount,
    @test_cancel_date AS canceled_at,
    'REFUNDED'        AS cancel_status
FROM booking b
JOIN performance_seat ps ON b.performance_seat_id = ps.performance_seat_id
JOIN performance p        ON ps.performance_id    = p.performance_id
WHERE b.booking_id = @target_booking_id;

COMMIT;

-- 트랜잭션 실행 후 결과 확인
SELECT
    c.booking_id,
    b.payment,
    c.cancellation_fee,
    c.refund_amount,
    c.cancel_status,
    c.canceled_at
FROM cancellation c
JOIN booking b ON b.booking_id = c.booking_id
ORDER BY c.canceled_at;
