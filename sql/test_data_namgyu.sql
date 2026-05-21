USE conmayo;

-- ==============================
--  0. 선행 데이터
-- ==============================

INSERT INTO venue (venue_name, address) VALUES
  ('올림픽홀',   '서울시 송파구 올림픽로 424'),
  ('블루스퀘어', '서울시 용산구 이태원로 294');

INSERT INTO performance (title, category, start_time, running_time, sales_status, booking_open, venue_id) VALUES
  ('아이유 콘서트 2026', '콘서트', '2026-07-01 19:00:00', 120, 'OPEN',        '2026-05-01 10:00:00', 1),
  ('레미제라블',         '뮤지컬', '2026-08-15 18:00:00', 150, 'COMING_SOON', '2026-06-01 10:00:00', 2);

INSERT INTO member (member_id, passwd, member_name, phone, blacklist_until, member_role) VALUES
  ('user01', 'pw1234', '김철수', '010-1111-2222', NULL,                  'USER'),
  ('user02', 'pw5678', '이영희', '010-3333-4444', NULL,                  'USER'),
  ('user03', 'pw9999', '박블랙', '010-5555-6666', '2099-12-31 23:59:59', 'USER'); -- 블랙리스트 회원


-- ==============================
--  1. SEAT 정상 삽입
-- ==============================

-- 결과: 성공 okay
INSERT INTO seat (venue_id, section, row_num, col_num) VALUES
  (1, 'VIP', 1, 1),
  (1, 'VIP', 1, 2),
  (1, 'A',   2, 1),
  (1, 'A',   2, 2),
  (1, 'B',   3, 1),
  (2, 'VIP', 1, 1),
  (2, 'A',   1, 2);


-- ==============================
--  2. SEAT 엣지케이스
-- ==============================

-- 동일 공연장·구역·행·열 중복 삽입 - 실패해야함
-- 결과: 실패 okay (UNIQUE 위반)
INSERT INTO seat (venue_id, section, row_num, col_num) VALUES
  (1, 'VIP', 1, 1);

-- row_num = 0 이면 안됨 - 실패해야함
-- 결과: 실패 okay (CHECK 위반)
INSERT INTO seat (venue_id, section, row_num, col_num) VALUES
  (1, 'A', 0, 1);

-- col_num = -1 - 실패해야함
-- 결과: 실패 okay (CHECK 위반)
INSERT INTO seat (venue_id, section, row_num, col_num) VALUES
  (1, 'A', 1, -1);

-- 존재하지 않는 venue_id 참조 - 실패해야함
-- 결과: 실패 okay (FK 위반)
INSERT INTO seat (venue_id, section, row_num, col_num) VALUES
  (999, 'A', 1, 1);


-- ==============================
--  3. PERFORMANCE_SEAT
-- ==============================

-- 결과: 성공 okay
INSERT INTO performance_seat (performance_id, seat_id, price) VALUES
  (1, 1, 170000),
  (1, 2, 170000),
  (1, 3, 120000),
  (1, 4, 120000),
  (1, 5,  80000),
  (2, 6, 150000),
  (2, 7, 100000);

-- 동일 공연에 동일 좌석 중복 등록 - 실패해야함
-- 결과: 실패 okay (UNIQUE 위반)
INSERT INTO performance_seat (performance_id, seat_id, price) VALUES
  (1, 1, 999999);

-- 아이유 콘서트(올림픽홀)에 블루스퀘어 좌석 연결
-- 됨.. 코드로 막아줘야 함 (venue 교차 검증 필요)
INSERT INTO performance_seat (performance_id, seat_id, price) VALUES
  (1, 6, 50000);


-- ==============================
--  4. BOOKING 선행 데이터
-- ==============================

-- 결과: 성공 okay
INSERT INTO booking (member_id, performance_seat_id, booking_status, booked_at, payment) VALUES
  ('user01', 1, 'BOOKED',   '2026-05-10 10:05:00', 170000),  -- booking_id=1, 정상 예매
  ('user01', 2, 'CANCELED', '2026-05-10 10:10:00', 170000),  -- booking_id=2, 취소된 예매
  ('user02', 3, 'BOOKED',   '2026-05-10 10:15:00', 120000),  -- booking_id=3, 정상 예매
  ('user02', 4, 'HOLD',     '2026-05-10 10:20:00', NULL),    -- booking_id=4, 결제 미완료 (payment NULL 정상)
  ('user03', 5, 'BOOKED',   '2026-05-10 10:25:00',  80000);  -- booking_id=5, 블랙리스트 회원


-- ==============================
--  5. REVIEW 정상 케이스
-- ==============================

-- BOOKED 상태 예매에 리뷰 작성
-- 결과: 성공 okay
INSERT INTO review (booking_id, seat_rating, written_at, content) VALUES
  (1, 5, '2026-07-02 12:00:00', 'VIP석 시야 완벽합니다. 무대가 정말 잘 보였어요!'),
  (3, 4, '2026-07-02 13:00:00', 'A구역도 충분히 잘 보였어요. 가성비 최고!');


-- ==============================
--  6. REVIEW 엣지케이스
-- ==============================

-- 동일 booking_id에 리뷰 두 번 작성 - 실패해야함
-- 결과: 실패 okay (booking_id UNIQUE 위반)
INSERT INTO review (booking_id, seat_rating, written_at, content) VALUES
  (1, 1, '2026-07-03 09:00:00', '중복 리뷰 시도');

-- CANCELED 예매에 리뷰 작성
-- 됨. 코드로 막아줘야 함 (booking_status 체크 필요)
INSERT INTO review (booking_id, seat_rating, written_at, content) VALUES
  (2, 3, '2026-07-02 15:00:00', '취소했는데 리뷰 달아봄 (버그 확인용)');

-- HOLD 상태 예매에 리뷰 작성 (결제 미완료)
-- 됨. 코드로 막아줘야 함 (booking_status 체크 필요)
INSERT INTO review (booking_id, seat_rating, written_at, content) VALUES
  (4, 5, '2026-07-02 16:00:00', '결제도 안 했는데 리뷰 달아봄 (버그 확인용)');

-- 공연 시작 전 날짜로 리뷰 작성
-- booking_id=5, 공연 start_time = 2026-07-01 19:00:00
-- 삽입 성공.. 코드로 막아줘야 함 (written_at vs start_time 비교 필요)
INSERT INTO review (booking_id, seat_rating, written_at, content) VALUES
  (5, 5, '2026-06-30 09:00:00', '공연도 안 봤는데 미래 리뷰 (버그 확인용)');

-- seat_rating = 6, 범위 초과 - 실패해야함
-- 결과: 실패 okay (CHECK 위반)
INSERT INTO review (booking_id, seat_rating, written_at, content) VALUES
  (3, 6, '2026-07-02 17:00:00', '평점 6점 시도');

-- seat_rating = 0 - 실패해야함
-- 결과: 실패 okay (CHECK 위반)
INSERT INTO review (booking_id, seat_rating, written_at, content) VALUES
  (3, 0, '2026-07-02 17:30:00', '평점 0점 시도');

-- 존재하지 않는 booking_id 참조 - 실패해야함
-- 결과: 실패 okay (FK 위반)
INSERT INTO review (booking_id, seat_rating, written_at, content) VALUES
  (9999, 4, '2026-07-02 18:00:00', '없는 예매ID 리뷰');
