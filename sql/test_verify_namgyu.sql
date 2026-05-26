USE conmayo;

-- ==============================
--  정규화 검증
-- ==============================

-- [갱신 이상] venue 주소 한 곳만 바꿔도 performance 전체에 반영되는지 확인
-- 결과: 아이유 콘서트 주소 바뀌었음. 정규화 잘 된 것
UPDATE venue SET address = '서울시 송파구 테스트로 999' WHERE venue_id = 1;
SELECT p.title, v.address FROM performance p JOIN venue v ON v.venue_id = p.venue_id;
-- 확인 후 원복
UPDATE venue SET address = '서울시 송파구 올림픽로 424' WHERE venue_id = 1;

-- [삽입 이상] 공연 없는 좌석에 평점만 덩그러니 생기는 경우 확인
-- 결과: 빈 칸 나왔음. okay (삽입 이상 없음)
SELECT s.seat_id, AVG(r.seat_rating)
FROM seat s
LEFT JOIN performance_seat ps ON ps.seat_id = s.seat_id
LEFT JOIN booking b ON b.performance_seat_id = ps.performance_seat_id
LEFT JOIN review r ON r.booking_id = b.booking_id
WHERE ps.performance_seat_id IS NULL
GROUP BY s.seat_id;

-- [3NF] review에 member_id 없어도 JOIN 한 번으로 도달 가능한지 확인
-- 결과: review_id마다 member_id 유일하게 조회됐음. okay
SELECT r.review_id, b.member_id, b.booking_id
FROM review r
JOIN booking b ON b.booking_id = r.booking_id;

-- [price/payment 분리] 정가는 performance_seat에, 실결제는 booking에만 있는지 확인
-- HOLD(booking_id=4)만 payment=NULL, 나머지는 price=payment면 okay
SELECT ps.price, b.payment, b.booking_id
FROM performance_seat ps
JOIN booking b ON b.performance_seat_id = ps.performance_seat_id;


-- ==============================
--  버그 탐지 쿼리
-- ==============================

-- 좌석별 평균 평점 조회 (핵심 기능)
-- seat_id=1(VIP) 5.0, seat_id=5(B) 5.0, seat_id=3(A) 4.0 나오면 okay
-- 오케이
SELECT
    s.seat_id,
    s.section,
    s.row_num,
    s.col_num,
    COUNT(r.review_id) AS review_count,
    AVG(r.seat_rating) AS avg_rating
FROM seat s
JOIN performance_seat ps ON ps.seat_id = s.seat_id
JOIN booking b           ON b.performance_seat_id = ps.performance_seat_id
                        AND b.booking_status = 'BOOKED'
LEFT JOIN review r       ON r.booking_id = b.booking_id
GROUP BY s.seat_id, s.section, s.row_num, s.col_num
ORDER BY avg_rating DESC;

-- 중복 리뷰 탐지 - cnt > 1 이면 버그
-- 결과: 빈 칸 나왔음. okay
SELECT booking_id, COUNT(*) AS cnt
FROM review
GROUP BY booking_id
HAVING cnt > 1;

-- 공연 전 작성된 리뷰 탐지
-- review_id=6 → '공연 전 리뷰' 탐지되면 okay (코드로 막아야 함)
SELECT
    r.review_id,
    r.booking_id,
    r.written_at,
    p.start_time,
    CASE WHEN r.written_at < p.start_time THEN '⚠ 공연 전 리뷰' ELSE 'OK' END AS check_result
FROM review r
JOIN booking b           ON b.booking_id = r.booking_id
JOIN performance_seat ps ON ps.performance_seat_id = b.performance_seat_id
JOIN performance p       ON p.performance_id = ps.performance_id;

-- CANCELED / HOLD 예매의 리뷰 탐지
-- booking_id=2(CANCELED), booking_id=4(HOLD) 탐지되면 okay (코드로 막아야 함)
SELECT
    r.review_id,
    b.booking_id,
    b.booking_status,
    CASE WHEN b.booking_status != 'BOOKED' THEN '⚠ 비정상 상태 리뷰' ELSE 'OK' END AS check_result
FROM review r
JOIN booking b ON b.booking_id = r.booking_id;

-- 공연장 불일치 좌석 탐지
-- '공연장 불일치' 탐지되면 okay (코드로 막아야 함)
SELECT
    ps.performance_seat_id,
    ps.performance_id,
    ps.seat_id,
    p.venue_id AS perf_venue,
    s.venue_id AS seat_venue,
    CASE WHEN p.venue_id != s.venue_id THEN '⚠ 공연장 불일치' ELSE 'OK' END AS check_result
FROM performance_seat ps
JOIN performance p ON p.performance_id = ps.performance_id
JOIN seat s        ON s.seat_id = ps.seat_id;

-- CANCELED인데 cancellation 없는 경우 탐지
-- booking_id=2 탐지됨 → 취소 트랜잭션에서 booking UPDATE + cancellation INSERT 같이 묶어야 함
SELECT b.booking_id, b.booking_status
FROM booking b
LEFT JOIN cancellation c ON c.booking_id = b.booking_id
WHERE b.booking_status = 'CANCELED'
  AND c.booking_id IS NULL;

-- 결제금액 이상 탐지 - BOOKED인데 payment != price면 버그
-- 결과: 빈 칸 나왔음. okay
SELECT b.booking_id, ps.price, b.payment, (b.payment - ps.price) AS diff
FROM booking b
JOIN performance_seat ps ON ps.performance_seat_id = b.performance_seat_id
WHERE b.booking_status = 'BOOKED'
  AND b.payment != ps.price;
