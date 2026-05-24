-- =============================================================
--  ConMayo 엣지케이스 테스트 (test_edge.sql)
--  실행 순서: schema.sql → data.sql → (이 파일)
--  주석 해제 후 한 줄씩 실행 — 각각 독립적으로 테스트할 것
--
--  [실패 okay]  : 에러 발생이 정상인 케이스 (제약조건이 잘 걸려있다는 증거)
--  [코드로 차단] : DB는 허용하지만 애플리케이션에서 막아야 하는 케이스
-- =============================================================

USE conmayo;


-- =============================================================
--  MEMBER
-- =============================================================

-- [실패 okay] 전화번호 중복 (UNIQUE 위반)
-- INSERT INTO member (member_id, passwd, member_name, phone)
-- VALUES ('test01', 'pw1234', '중복폰테스트', '010-1111-1111');

-- [실패 okay] 잘못된 member_role ENUM 값
-- INSERT INTO member (member_id, passwd, member_name, phone, member_role)
-- VALUES ('test01', 'pw1234', 'ENUM테스트', '010-8888-7777', 'SUPERUSER');


-- =============================================================
--  PERFORMANCE
-- =============================================================

-- [실패 okay] 존재하지 않는 venue_id (FK 위반)
-- INSERT INTO performance (venue_id, title, category, start_time, running_time, sales_status, booking_open)
-- VALUES (999, '유령공연', '기타', '2026-09-01 19:00:00', 90, 'OPEN', '2026-08-01 10:00:00');

-- [실패 okay] running_time 음수 (CHECK 위반)
-- INSERT INTO performance (venue_id, title, category, start_time, running_time, sales_status, booking_open)
-- VALUES (1, '음수공연', '기타', '2026-09-01 19:00:00', -1, 'OPEN', '2026-08-01 10:00:00');

-- [실패 okay] 잘못된 sales_status ENUM 값
-- INSERT INTO performance (venue_id, title, category, start_time, running_time, sales_status, booking_open)
-- VALUES (1, 'ENUM테스트', '기타', '2026-09-01 19:00:00', 90, 'INVALID_STATUS', '2026-08-01 10:00:00');

-- [코드로 차단] booking_open이 start_time보다 늦음 (DB는 허용)
-- INSERT INTO performance (venue_id, title, category, start_time, running_time, sales_status, booking_open)
-- VALUES (1, '날짜역전공연', '기타', '2026-09-01 19:00:00', 90, 'OPEN', '2026-10-01 10:00:00');


-- =============================================================
--  SEAT
-- =============================================================

-- [실패 okay] 동일 공연장·구역·행·열 중복 (UNIQUE 위반)
-- INSERT INTO seat (venue_id, section, row_num, col_num) VALUES (1, 'F1', 1, 1);

-- [실패 okay] row_num = 0 (CHECK 위반)
-- INSERT INTO seat (venue_id, section, row_num, col_num) VALUES (1, 'F1', 0, 1);

-- [실패 okay] col_num 음수 (CHECK 위반)
-- INSERT INTO seat (venue_id, section, row_num, col_num) VALUES (1, 'F1', 1, -1);

-- [실패 okay] 존재하지 않는 venue_id (FK 위반)
-- INSERT INTO seat (venue_id, section, row_num, col_num) VALUES (999, 'F1', 1, 1);


-- =============================================================
--  PERFORMANCE_SEAT
-- =============================================================

-- [실패 okay] 동일 공연에 동일 좌석 중복 등록 (UNIQUE 위반)
-- INSERT INTO performance_seat (performance_id, seat_id, price) VALUES (1, 1, 999999);

-- [코드로 차단] 공연장 불일치 좌석 연결 — 공연1(KSPO DOME)에 세종문화회관 좌석(seat_id=23) 연결 (DB는 허용)
-- INSERT INTO performance_seat (performance_id, seat_id, price) VALUES (1, 23, 50000);


-- =============================================================
--  BOOKING
-- =============================================================

-- [실패 okay] 존재하지 않는 member_id (FK 위반)
-- INSERT INTO booking (member_id, performance_seat_id, booking_status, booked_at, payment)
-- VALUES ('user999', 1, 'HOLD', NOW(), 220000);

-- [실패 okay] 존재하지 않는 performance_seat_id (FK 위반)
-- INSERT INTO booking (member_id, performance_seat_id, booking_status, booked_at, payment)
-- VALUES ('user01', 9999, 'HOLD', NOW(), 220000);

-- [실패 okay] 잘못된 booking_status ENUM 값
-- INSERT INTO booking (member_id, performance_seat_id, booking_status, booked_at, payment)
-- VALUES ('user01', 4, 'PAID', NOW(), 220000);

-- [코드로 차단] HOLD 중인 좌석 중복 HOLD — ps_id=3은 data.sql에서 아직 HOLD/BOOKED 없는 좌석
-- INSERT INTO booking (member_id, performance_seat_id, booking_status, booked_at, payment)
-- VALUES ('user02', 3, 'HOLD', NOW(), 220000);
-- INSERT INTO booking (member_id, performance_seat_id, booking_status, booked_at, payment)
-- VALUES ('user05', 3, 'HOLD', NOW(), 220000);
-- (두 줄 동시 실행 시 같은 좌석에 두 명이 HOLD → DB는 허용, 트랜잭션 로직으로 차단 필요)

-- [코드로 차단] BOOKED된 좌석 HOLD 시도 — ps_id=2는 booking_id=2가 BOOKED 상태
-- INSERT INTO booking (member_id, performance_seat_id, booking_status, booked_at, payment)
-- VALUES ('user05', 2, 'HOLD', NOW(), 220000);

-- [코드로 차단] payment 음수 — DDL에 CHECK 제약 없음, 추가 권장
-- INSERT INTO booking (member_id, performance_seat_id, booking_status, booked_at, payment)
-- VALUES ('user01', 4, 'HOLD', NOW(), -5000);

-- [코드로 차단] payment가 price 초과 — ps_id=4의 price=220,000
-- INSERT INTO booking (member_id, performance_seat_id, booking_status, booked_at, payment)
-- VALUES ('user01', 4, 'HOLD', NOW(), 999999);

-- [코드로 차단] 블랙리스트 유효 회원 예매 시도 — user03은 2026-06-01까지 블랙리스트
-- INSERT INTO booking (member_id, performance_seat_id, booking_status, booked_at, payment)
-- VALUES ('user03', 4, 'HOLD', NOW(), 220000);


-- =============================================================
--  REVIEW
-- =============================================================

-- [실패 okay] 동일 booking_id 리뷰 중복 (UNIQUE 위반) — booking_id=11은 이미 리뷰 있음
-- INSERT INTO review (booking_id, seat_rating, written_at, content)
-- VALUES (11, 1, NOW(), '중복 리뷰 시도');

-- [실패 okay] seat_rating 범위 초과 (CHECK 위반)
-- INSERT INTO review (booking_id, seat_rating, written_at, content)
-- VALUES (13, 6, NOW(), '6점 시도');

-- [실패 okay] seat_rating = 0 (CHECK 위반)
-- INSERT INTO review (booking_id, seat_rating, written_at, content)
-- VALUES (13, 0, NOW(), '0점 시도');

-- [실패 okay] 존재하지 않는 booking_id (FK 위반)
-- INSERT INTO review (booking_id, seat_rating, written_at, content)
-- VALUES (9999, 4, NOW(), '없는 예매 리뷰');

-- [코드로 차단] CANCELED 예매에 리뷰 작성 — booking_id=12는 CANCELED 상태 (DB는 허용)
-- INSERT INTO review (booking_id, seat_rating, written_at, content)
-- VALUES (12, 3, NOW(), '취소했는데 리뷰 달아봄');

-- [코드로 차단] HOLD 예매에 리뷰 작성 — booking_id=14는 HOLD 상태 (DB는 허용)
-- INSERT INTO review (booking_id, seat_rating, written_at, content)
-- VALUES (14, 5, NOW(), '결제도 안 했는데 리뷰 달아봄');

-- [코드로 차단] 공연 시작 전 날짜로 리뷰 작성 — booking_id=16, 공연 start_time=2026-06-01 (DB는 허용)
-- INSERT INTO review (booking_id, seat_rating, written_at, content)
-- VALUES (16, 5, '2026-05-30 09:00:00', '공연도 안 봤는데 미리 리뷰');


-- =============================================================
--  CANCELLATION
-- =============================================================

-- [실패 okay] 이미 취소된 booking_id 중복 취소 (PK 중복) — booking_id=12는 이미 cancellation 있음
-- INSERT INTO cancellation (booking_id, refund_amount, cancellation_fee, cancel_status)
-- VALUES (12, 220000, 0, 'REQUESTED');

-- [실패 okay] 존재하지 않는 booking_id (FK 위반)
-- INSERT INTO cancellation (booking_id, refund_amount, cancellation_fee, cancel_status)
-- VALUES (9999, 100000, 0, 'REQUESTED');

-- [실패 okay] 잘못된 cancel_status ENUM 값
-- INSERT INTO cancellation (booking_id, refund_amount, cancellation_fee, cancel_status)
-- VALUES (22, 165000, 0, 'DONE');
