-- =============================================================
--  ConMayo 테스트 데이터
--  실행 순서: schema.sql → data.sql
--  삽입 순서: venue → member → performance → seat
--             → performance_seat → booking → review
-- =============================================================

USE conmayo;

-- =============================================================
--  1. VENUE
-- =============================================================

INSERT INTO venue (venue_id, venue_name, address) VALUES
(1, '예술의전당 오페라극장', '서울특별시 서초구 남부순환로 2406'),
(2, '세종문화회관 대극장', '서울특별시 종로구 세종대로 175'),
(3, '블루스퀘어 신한카드홀', '서울특별시 용산구 이태원로 294'),
(4, 'KSPO DOME (올림픽체조경기장)', '서울특별시 송파구 올림픽로 424'),
(5, '고척스카이돔', '서울특별시 구로구 경인로 430');


-- =============================================================
--  2. MEMBER
-- =============================================================

INSERT INTO member (member_id, passwd, member_name, phone, blacklist_until, member_role) VALUES
('admin01', 'adminpw!', '관리자', '010-9999-0000', NULL, 'ADMIN'),
('user01', 'pw01', '강민지', '010-1000-0001', NULL, 'USER'),
('user02', 'pw02', '박남규', '010-1000-0002', NULL, 'USER'),
('user03', 'pw03', '이채빈', '010-1000-0003', NULL, 'USER'),
('user04', 'pw04', '하나경', '010-1000-0004', NULL, 'USER'),
('user05', 'pw05', '유재석', '010-1000-0005', NULL, 'USER'),
('user06', 'pw06', '박명수', '010-1000-0006', NULL, 'USER'),
('user07', 'pw07', '한로로', '010-1000-0007', NULL, 'USER'),
('user08', 'pw08', '윤도현', '010-1000-0008', NULL, 'USER'),
('user09', 'pw09', '장나라', '010-1000-0009', NULL, 'USER'),
('user10', 'pw10', '임영웅', '010-1000-0010', NULL, 'USER'),
('user11', 'pw11', '한소희', '010-1000-0011', NULL, 'USER'),
('user12', 'pw12', '송중기', '010-1000-0012', NULL, 'USER'),
('user13', 'pw13', '오연서', '010-1000-0013', NULL, 'USER'),
('user14', 'pw14', '류준열', '010-1000-0014', NULL, 'USER'),
('user15', 'pw15', '신세경', '010-1000-0015', NULL, 'USER'),
('user16', 'pw16', '진용훈', '010-1000-0016', NULL, 'USER'),
('user17', 'pw17', '박보영', '010-1000-0017', NULL, 'USER'),
('user18', 'pw18', '이종석', '010-1000-0018', NULL, 'USER'),
('user19', 'pw19', '배수지', '010-1000-0019', NULL, 'USER'),
('user20', 'pw20', '공유', '010-1000-0020', NULL, 'USER'),
('user21', 'pw21', '김태리', '010-1000-0021', NULL, 'USER'),
('user22', 'pw22', '박서준', '010-1000-0022', NULL, 'USER'),
('user23', 'pw23', '아이유', '010-1000-0023', NULL, 'USER'),
('user24', 'pw24', '현빈', '010-1000-0024', NULL, 'USER'),
('user25', 'pw25', '손예진', '010-1000-0025', NULL, 'USER'),
('user26', 'pw26', '남주혁', '010-1000-0026', NULL, 'USER'),
('user27', 'pw27', '전지현', '010-1000-0027', NULL, 'USER'),
('user28', 'pw28', '이동욱', '010-1000-0028', NULL, 'USER'),
('user29', 'pw29', '김지원', '010-1000-0029', NULL, 'USER'),
('user30', 'pw30', '강현구', '010-1000-0030', NULL, 'USER'),
('user31', 'pw31', '한지민', '010-1000-0031', NULL, 'USER'),
('user32', 'pw32', '유아인', '010-1000-0032', NULL, 'USER'),
('user33', 'pw33', '정유미', '010-1000-0033', NULL, 'USER'),
('user34', 'pw34', '박형식', '010-1000-0034', NULL, 'USER'),
('user35', 'pw35', '김유정', '010-1000-0035', NULL, 'USER'),
('user36', 'pw36', '서강준', '010-1000-0036', NULL, 'USER'),
('user37', 'pw37', '이성경', '010-1000-0037', NULL, 'USER'),
('user38', 'pw38', '지창욱', '010-1000-0038', NULL, 'USER'),
('user39', 'pw39', '박은빈', '010-1000-0039', NULL, 'USER'),
('user40', 'pw40', '안효섭', '010-1000-0040', NULL, 'USER'),
('user41', 'pw41', '김다미', '010-1000-0041', NULL, 'USER'),
('user42', 'pw42', '최우식', '010-1000-0042', NULL, 'USER'),
('user43', 'pw43', '문가영', '010-1000-0043', NULL, 'USER'),
('user44', 'pw44', '이도현', '010-1000-0044', NULL, 'USER'),

-- 블랙리스트 만료 회원 
('user45', 'pw45', '고민시', '010-1000-0045', '2026-01-01 21:00:00', 'USER'),
('user46', 'pw46', '송강', '010-1000-0046', '2026-02-02 13:30:00', 'USER'),

-- 최근 일주일 2회 취소자 (위험군 - 블랙리스트 아님)
('user47', 'pw47', '김경고', '010-1000-0047', NULL, 'USER'),
('user48', 'pw48', '이주의', '010-1000-0048', NULL, 'USER'),

-- 최근 일주일 3회 취소자 (블랙리스트 적용: 현재(5/31) 기준 + 7일)
('user49', 'pw49', '박블랙', '010-1000-0049', '2026-06-06 15:30:00', 'USER'),
('user50', 'pw50', '최차단', '010-1000-0050', '2026-06-07 10:00:00', 'USER');


-- =============================================================
--  3. PERFORMANCE
-- =============================================================

INSERT INTO performance (performance_id, venue_id, title, category, start_time, running_time, sales_status, booking_open) VALUES
-- 예술의전당 오페라극장
(1, 1, '오페라 <마술피리>', '클래식', '2026-06-15 19:30:00', 160, 'OPEN', '2026-05-01 14:00:00'),
(2, 1, '국립발레단 <백조의 호수>', '무용', '2026-06-20 15:00:00', 150, 'SOLD_OUT', '2026-05-15 14:00:00'),
(3, 1, '오페라 <라 트라비아타>', '클래식', '2026-07-05 19:30:00', 170, 'OPEN', '2026-05-20 14:00:00'),
(4, 1, '유니버설발레단 <호두까기 인형>', '무용', '2026-07-12 14:00:00', 120, 'COMING_SOON', '2026-06-10 14:00:00'),
(5, 1, '조성진 피아노 리사이틀', '클래식', '2026-07-20 20:00:00', 100, 'SOLD_OUT', '2026-05-25 14:00:00'),
(6, 1, '오페라 <카르멘>', '클래식', '2026-08-01 19:30:00', 165, 'COMING_SOON', '2026-07-01 14:00:00'),
(7, 1, '임윤찬 피아노 리사이틀', '클래식', '2026-08-10 20:00:00', 110, 'COMING_SOON', '2026-07-15 14:00:00'),
(8, 1, '국립오페라단 <투란도트>', '클래식', '2026-08-20 19:30:00', 180, 'COMING_SOON', '2026-07-20 14:00:00'),
(9, 1, '클라라 주미 강 바이올린 독주회', '클래식', '2026-09-05 20:00:00', 90, 'COMING_SOON', '2026-08-01 14:00:00'),
(10, 1, '발레 <지젤>', '무용', '2026-09-15 15:00:00', 140, 'COMING_SOON', '2026-08-10 14:00:00'),

-- 세종문화회관 대극장
(11, 2, '뮤지컬 <영웅>', '뮤지컬', '2026-06-18 19:30:00', 160, 'OPEN', '2026-05-10 14:00:00'),
(12, 2, '뮤지컬 <모차르트!>', '뮤지컬', '2026-06-25 19:30:00', 175, 'OPEN', '2026-05-12 14:00:00'),
(13, 2, '서울시향 정기연주회', '클래식', '2026-07-08 20:00:00', 120, 'OPEN', '2026-05-15 14:00:00'),
(14, 2, '뮤지컬 <노트르담 드 파리>', '뮤지컬', '2026-07-15 19:30:00', 150, 'COMING_SOON', '2026-06-05 14:00:00'),
(15, 2, '조수미 콘서트', '클래식', '2026-07-28 20:00:00', 120, 'SOLD_OUT', '2026-05-20 14:00:00'),
(16, 2, '뮤지컬 <명성황후>', '뮤지컬', '2026-08-05 19:30:00', 160, 'COMING_SOON', '2026-07-01 14:00:00'),
(17, 2, '히사이시 조 영화음악 콘서트', '클래식', '2026-08-15 17:00:00', 130, 'COMING_SOON', '2026-07-10 14:00:00'),
(18, 2, '뮤지컬 <엑스칼리버>', '뮤지컬', '2026-08-25 19:30:00', 170, 'COMING_SOON', '2026-07-20 14:00:00'),
(19, 2, '송가인 단독 콘서트', '콘서트', '2026-09-10 19:00:00', 150, 'COMING_SOON', '2026-08-05 14:00:00'),
(20, 2, '뮤지컬 <레베카>', '뮤지컬', '2026-09-20 19:30:00', 170, 'COMING_SOON', '2026-08-15 14:00:00'),

-- 블루스퀘어 신한카드홀
(21, 3, '뮤지컬 <지킬 앤 하이드>', '뮤지컬', '2026-06-10 19:30:00', 170, 'OPEN', '2026-04-30 14:00:00'),
(22, 3, '뮤지컬 <위키드>', '뮤지컬', '2026-06-22 19:30:00', 165, 'SOLD_OUT', '2026-05-05 14:00:00'),
(23, 3, '뮤지컬 <프랑켄슈타인>', '뮤지컬', '2026-07-02 19:30:00', 175, 'OPEN', '2026-05-15 14:00:00'),
(24, 3, '뮤지컬 <시카고>', '뮤지컬', '2026-07-18 14:00:00', 145, 'OPEN', '2026-05-20 14:00:00'),
(25, 3, '뮤지컬 <데스노트>', '뮤지컬', '2026-07-30 19:30:00', 160, 'COMING_SOON', '2026-06-15 14:00:00'),
(26, 3, '뮤지컬 <드라큘라>', '뮤지컬', '2026-08-12 19:30:00', 165, 'COMING_SOON', '2026-07-05 14:00:00'),
(27, 3, '뮤지컬 <아이다>', '뮤지컬', '2026-08-22 14:00:00', 160, 'COMING_SOON', '2026-07-15 14:00:00'),
(28, 3, '뮤지컬 <마틸다>', '뮤지컬', '2026-09-01 19:30:00', 155, 'COMING_SOON', '2026-08-01 14:00:00'),
(29, 3, '뮤지컬 <킹키부츠>', '뮤지컬', '2026-09-12 19:30:00', 140, 'COMING_SOON', '2026-08-10 14:00:00'),
(30, 3, '뮤지컬 <오페라의 유령>', '뮤지컬', '2026-09-25 19:30:00', 170, 'COMING_SOON', '2026-08-20 14:00:00'),

-- KSPO DOME
(31, 4, '아이유(IU) 월드투어 [HERE]', '콘서트', '2026-06-27 18:00:00', 180, 'SOLD_OUT', '2026-05-25 20:00:00'),
(32, 4, 'BLACKPINK WORLD TOUR [BORN PINK]', '콘서트', '2026-07-04 18:00:00', 150, 'OPEN', '2026-05-28 20:00:00'),
(33, 4, '원위(ONEWE) O! NEW E!volution V', '콘서트', '2026-07-11 18:00:00', 180, 'SOLD_OUT', '2026-06-01 20:00:00'),
(34, 4, 'HANRORO 4th SOLO CONCERT 자몽살구클럽', '콘서트', '2026-07-25 18:42:00', 240, 'COMING_SOON', '2026-06-15 20:00:00'),
(35, 4, '뉴진스(NewJeans) 1st FANMEETING', '콘서트', '2026-08-08 17:00:00', 120, 'COMING_SOON', '2026-07-01 20:00:00'),
(36, 4, '에스파(aespa) LIVE TOUR 2026', '콘서트', '2026-08-15 18:00:00', 150, 'COMING_SOON', '2026-07-10 20:00:00'),
(37, 4, 'NCT 127 4TH TOUR [NEO CITY]', '콘서트', '2026-08-29 18:00:00', 160, 'COMING_SOON', '2026-07-20 20:00:00'),
(38, 4, '한화 vs LG', '스포츠', '2026-09-05 18:00:00', 150, 'COMING_SOON', '2026-08-01 20:00:00'),
(39, 4, '삼성 vs LG', '스포츠', '2026-09-19 18:00:00', 160, 'COMING_SOON', '2026-08-15 20:00:00'),
(40, 4, 'SSG vs LG', '스포츠', '2026-09-26 19:00:00', 180, 'COMING_SOON', '2026-08-20 20:00:00'),

-- 고척스카이돔
(41, 5, '두산 vs 키움', '스포츠', '2026-06-13 18:30:00', 180, 'SOLD_OUT', '2026-05-15 20:00:00'),
(42, 5, '키움 vs KIA', '스포츠', '2026-07-18 19:00:00', 180, 'COMING_SOON', '2026-06-10 20:00:00'),
(43, 5, '한화 vs 키움', '스포츠', '2026-08-01 19:00:00', 120, 'COMING_SOON', '2026-06-25 12:00:00'),
(44, 5, '브루노 마스(Bruno Mars) 내한공연', '콘서트', '2026-08-22 19:00:00', 120, 'COMING_SOON', '2026-07-15 12:00:00'),
(45, 5, '콜드플레이(Coldplay) MUSIC OF THE SPHERES', '콘서트', '2026-09-12 20:00:00', 150, 'COMING_SOON', '2026-08-01 12:00:00'),
(46, 5, '기프트 단독공연 〈Wonderland〉', '콘서트', '2026-09-26 19:00:00', 180, 'COMING_SOON', '2026-08-15 20:00:00'),
(47, 5, '투모로우바이투게더(TXT) WORLD TOUR', '콘서트', '2026-10-03 18:00:00', 150, 'COMING_SOON', '2026-09-01 20:00:00'),
(48, 5, '아이브(IVE) THE 2ND WORLD TOUR', '콘서트', '2026-10-17 18:00:00', 150, 'COMING_SOON', '2026-09-10 20:00:00'),
(49, 5, '엔하이픈(ENHYPEN) WORLD TOUR BLOOD SAGA', '콘서트', '2026-10-31 18:00:00', 150, 'COMING_SOON', '2026-09-20 20:00:00'),
(50, 5, '테일러 스위프트(Taylor Swift) THE ERAS TOUR', '콘서트', '2026-11-14 19:00:00', 210, 'COMING_SOON', '2026-10-01 12:00:00');


-- =============================================================
--  4. SEAT
-- =============================================================

-- 1. 예술의전당 (10x10 = 100석)
INSERT INTO seat (venue_id, section, row_num, col_num)
WITH RECURSIVE R AS (SELECT 1 AS n UNION ALL SELECT n+1 FROM R WHERE n < 10),
               C AS (SELECT 1 AS n UNION ALL SELECT n+1 FROM C WHERE n < 10)
SELECT 1, CASE WHEN R.n <= 3 THEN 'VIP' WHEN R.n <= 6 THEN 'R' ELSE 'S' END, R.n, C.n FROM R CROSS JOIN C;

-- 2. 세종문화회관 (12x8 = 96석)
INSERT INTO seat (venue_id, section, row_num, col_num)
WITH RECURSIVE R AS (SELECT 1 AS n UNION ALL SELECT n+1 FROM R WHERE n < 12),
               C AS (SELECT 1 AS n UNION ALL SELECT n+1 FROM C WHERE n < 8)
SELECT 2, CASE WHEN R.n <= 4 THEN 'VIP' WHEN R.n <= 8 THEN 'R' ELSE 'A' END, R.n, C.n FROM R CROSS JOIN C;

-- 3. 블루스퀘어 (8x12 = 96석)
INSERT INTO seat (venue_id, section, row_num, col_num)
WITH RECURSIVE R AS (SELECT 1 AS n UNION ALL SELECT n+1 FROM R WHERE n < 8),
               C AS (SELECT 1 AS n UNION ALL SELECT n+1 FROM C WHERE n < 12)
SELECT 3, CASE WHEN R.n <= 3 THEN 'VIP' WHEN R.n <= 5 THEN 'R' ELSE 'S' END, R.n, C.n FROM R CROSS JOIN C;

-- 4. KSPO DOME (10x12 = 120석)
INSERT INTO seat (venue_id, section, row_num, col_num)
WITH RECURSIVE R AS (SELECT 1 AS n UNION ALL SELECT n+1 FROM R WHERE n < 10),
               C AS (SELECT 1 AS n UNION ALL SELECT n+1 FROM C WHERE n < 12)
SELECT 4, CASE WHEN R.n <= 2 THEN 'STANDING' WHEN R.n <= 5 THEN 'R' ELSE 'S' END, R.n, C.n FROM R CROSS JOIN C;

-- 5. 고척스카이돔 (12x10 = 120석)
INSERT INTO seat (venue_id, section, row_num, col_num)
WITH RECURSIVE R AS (SELECT 1 AS n UNION ALL SELECT n+1 FROM R WHERE n < 12),
               C AS (SELECT 1 AS n UNION ALL SELECT n+1 FROM C WHERE n < 10)
SELECT 5, CASE WHEN R.n <= 3 THEN 'GROUND' WHEN R.n <= 7 THEN '1층' ELSE '2층' END, R.n, C.n FROM R CROSS JOIN C;


-- =============================================================
--  5. PERFORMANCE_SEAT
--  공연1 FLOOR(ps_id 1~10): 220,000원
--  공연1 1층(ps_id 11~22) : 165,000원
--  공연2 VIP(ps_id 23~32) : 150,000원
-- =============================================================

INSERT INTO performance_seat (performance_id, seat_id, price)
SELECT 
    p.performance_id, 
    s.seat_id,
    CASE 
        WHEN s.section IN ('VIP', 'STANDING', 'GROUND') THEN 180000
        WHEN s.section IN ('R', '1층') THEN 150000
        WHEN s.section IN ('S', '2층') THEN 120000
        ELSE 99000
    END AS price
FROM performance p
JOIN seat s ON p.venue_id = s.venue_id;


-- =============================================================
--  6. BOOKING
-- =============================================================

-- [시나리오 1] user47 (위험군): 최근 7일 내 2회 취소 대상 예매 건
INSERT INTO booking (booking_id, member_id, performance_seat_id, booking_status, booked_at, payment) VALUES
(101, 'user47', 1, 'CANCELED', '2026-05-25 10:00:00', 180000),
(102, 'user47', 2, 'CANCELED', '2026-05-26 11:00:00', 180000);

-- [시나리오 2] user48 (위험군): 최근 7일 내 2회 취소 대상 예매 건
INSERT INTO booking (booking_id, member_id, performance_seat_id, booking_status, booked_at, payment) VALUES
(103, 'user48', 3, 'CANCELED', '2026-05-25 12:00:00', 180000),
(104, 'user48', 4, 'CANCELED', '2026-05-27 13:00:00', 180000);

-- [시나리오 3] user49 (블랙리스트): 최근 7일 내 3회 취소 대상 예매 건
INSERT INTO booking (booking_id, member_id, performance_seat_id, booking_status, booked_at, payment) VALUES
(105, 'user49', 5, 'CANCELED', '2026-05-24 09:00:00', 180000),
(106, 'user49', 6, 'CANCELED', '2026-05-24 09:05:00', 180000),
(107, 'user49', 7, 'CANCELED', '2026-05-24 09:10:00', 180000);

-- [시나리오 4] user50 (블랙리스트): 최근 7일 내 3회 취소 대상 예매 건
INSERT INTO booking (booking_id, member_id, performance_seat_id, booking_status, booked_at, payment) VALUES
(108, 'user50', 8,  'CANCELED', '2026-05-25 10:00:00', 180000),
(109, 'user50', 9,  'CANCELED', '2026-05-25 10:05:00', 180000),
(110, 'user50', 10, 'CANCELED', '2026-05-25 10:10:00', 180000);

-- [일반 데이터] 정상 예매 건
INSERT INTO booking (booking_id, member_id, performance_seat_id, booking_status, booked_at, payment) VALUES
(201, 'user01', 11, 'BOOKED', '2026-05-02 14:05:00', 150000),
(202, 'user02', 12, 'BOOKED', '2026-05-02 14:06:00', 150000),
(203, 'user03', 13, 'HOLD',   '2026-05-31 10:40:00', NULL); -- 방금 전 예매 시도중

-- [일반 데이터] 리뷰 작성을 위한 과거 완료된 예매 건
INSERT INTO booking (booking_id, member_id, performance_seat_id, booking_status, booked_at, payment) VALUES
(204, 'user10', 999, 'BOOKED', '2026-04-01 10:00:00', 120000),
(205, 'user11', 1000, 'BOOKED', '2026-04-01 10:05:00', 120000);


-- =============================================================
--  7. REVIEW
-- =============================================================

-- 과거 예매 건(booking_id: 204, 205)에 대한 관람 후 리뷰
INSERT INTO review (booking_id, seat_rating, written_at, content) VALUES
(204, 5, '2026-04-15 23:00:00', '음향도 좋고 좌석 시야도 너무 훌륭했습니다!'),
(205, 4, '2026-04-16 10:30:00', '조금 멀긴 했지만 전체적인 무대를 보기에는 좋았어요.');


-- =============================================================
--  8. CANCELLATION
-- =============================================================

-- [시나리오 1] user47 (위험군) 취소 내역
INSERT INTO cancellation (booking_id, refund_amount, cancellation_fee, canceled_at, cancel_status) VALUES
(101, 180000, 0, '2026-05-28 14:00:00', 'REFUNDED'),
(102, 180000, 0, '2026-05-30 14:00:00', 'REFUNDED');

-- [시나리오 2] user48 (위험군) 취소 내역
INSERT INTO cancellation (booking_id, refund_amount, cancellation_fee, canceled_at, cancel_status) VALUES
(103, 180000, 0, '2026-05-29 15:00:00', 'REFUNDED'),
(104, 180000, 0, '2026-05-30 16:00:00', 'REFUNDED');

-- [시나리오 3] user49 (블랙리스트) 취소 내역
INSERT INTO cancellation (booking_id, refund_amount, cancellation_fee, canceled_at, cancel_status) VALUES
(105, 180000, 0, '2026-05-26 15:30:00', 'REFUNDED'),
(106, 180000, 0, '2026-05-28 15:30:00', 'REFUNDED'),
(107, 180000, 0, '2026-05-30 15:30:00', 'REFUNDED'); -- 3번째 취소 시점 (블랙리스트 등록)

-- [시나리오 4] user50 (블랙리스트) 취소 내역
INSERT INTO cancellation (booking_id, refund_amount, cancellation_fee, canceled_at, cancel_status) VALUES
(108, 180000, 0, '2026-05-27 10:00:00', 'REFUNDED'),
(109, 180000, 0, '2026-05-29 10:00:00', 'REFUNDED'),
(110, 180000, 0, '2026-05-31 10:00:00', 'REFUNDED'); -- 3번째 취소 시점 (블랙리스트 등록)
