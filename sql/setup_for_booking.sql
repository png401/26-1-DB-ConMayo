-- member 테스트 케이스 삽입
INSERT INTO member (member_id, passwd, member_name, phone, blacklist_until, member_role) 
VALUES
(1, 'yjs1234!', '유재석', '010-1111-1111', NULL, 'USER'),
(2, 'mrJung', '정준하', '010-2222-2222', NULL, 'USER'),
(3, 'badguy', '박명수', '010-3333-3333', '2026-06-01 00:00:00', 'USER');


-- venue 테스트 케이스 삽입
/*
INSERT INTO venue (venue_id, venue_name, address)
VALUES
(1, 'KSPO DOME', '서울특별시 송파구'),
(2, '고척스카이돔', '서울특별시 구로구'),
(3, '세종문화회관', '서울특별시 종로구');
*/

-- performance 테스트 케이스 삽입
INSERT INTO performance (performance_id, venue_id, title, category, start_time, running_time, sales_status, booking_open)
VALUES
(1, 1, '엔하이픈 투어 BLOOD SAGA', 'K-POP', '2026-06-01 19:00:00', 150, 'OPEN', '2026-05-01 20:00:00'),
(2, 3, '위키드', '뮤지컬', '2026-07-25 14:00:00', 180, 'COMING_SOON', '2026-06-10 20:00:00');

-- performance_seat 테스트 케이스 삽입
-- 1번 공연장
-- FLOOR(1번~10번): 220,000원
-- 1층(11번~22번): 165,000원
INSERT INTO performance_seat (performance_seat_id, performance_id, seat_id, price)
VALUES
(1, 1, 1, 220000),
(2, 1, 2, 220000),
(3, 1, 3, 220000),
(4, 1, 4, 220000),
(5, 1, 5, 220000),
(6, 1, 6, 220000),
(7, 1, 7, 220000),
(8, 1, 8, 220000),
(9, 1, 9, 220000),
(10, 1, 10, 220000),
(11, 1, 11, 165000),
(12, 1, 12, 165000),
(13, 1, 13, 165000),
(14, 1, 14, 165000),
(15, 1, 15, 165000),
(16, 1, 16, 165000),
(17, 1, 17, 165000),
(18, 1, 18, 165000),
(19, 1, 19, 165000),
(20, 1, 20, 165000),
(21, 1, 21, 165000),
(22, 1, 22, 165000);