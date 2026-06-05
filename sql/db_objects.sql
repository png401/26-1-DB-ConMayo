USE conmayo;

-- FUNCTION

-- 잔여석 계산
DROP FUNCTION IF EXISTS get_remaining_seats;
DELIMITER //
CREATE FUNCTION get_remaining_seats(pid INT)
RETURNS INT READS SQL DATA
BEGIN
    DECLARE total INT;
    DECLARE booked INT;
    SELECT COUNT(*) INTO total
    FROM performance_seat
    WHERE performance_id = pid;
    SELECT COUNT(*) INTO booked
    FROM booking b
    JOIN performance_seat ps ON b.performance_seat_id = ps.performance_seat_id
    WHERE ps.performance_id = pid
      AND b.booking_status IN ('BOOKED', 'HOLD');
    RETURN total - booked;
END //
DELIMITER ;


-- PROCEDURE

-- 1. 예매 처리 (좌석 락 + 중복 체크 + INSERT)
DROP PROCEDURE IF EXISTS proc_book_seat;
DELIMITER //
CREATE PROCEDURE proc_book_seat(
    IN  p_member_id           VARCHAR(20),
    IN  p_performance_seat_id INT,
    IN  p_payment             INT,
    OUT p_result              VARCHAR(50)
)
BEGIN
    DECLARE v_count INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_result = 'ERROR';
    END;

    START TRANSACTION;

    -- 좌석 락
    SELECT COUNT(*) INTO v_count
    FROM performance_seat
    WHERE performance_seat_id = p_performance_seat_id FOR UPDATE;

    -- 중복 체크
    SELECT COUNT(*) INTO v_count
    FROM booking
    WHERE performance_seat_id = p_performance_seat_id
      AND booking_status IN ('BOOKED', 'HOLD');

    IF v_count > 0 THEN
        ROLLBACK;
        SET p_result = 'ALREADY_BOOKED';
    ELSE
        INSERT INTO booking (member_id, performance_seat_id, booking_status, payment)
        VALUES (p_member_id, p_performance_seat_id, 'BOOKED', p_payment);
        COMMIT;
        SET p_result = 'SUCCESS';
    END IF;
END //
DELIMITER ;

-- 2. 취소 처리 (booking CANCELED + cancellation INSERT)
DROP PROCEDURE IF EXISTS proc_cancel_booking;
DELIMITER //
CREATE PROCEDURE proc_cancel_booking(
    IN p_booking_id INT,
    IN p_refund     INT,
    IN p_fee        INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
    END;

    START TRANSACTION;
    UPDATE booking
    SET booking_status = 'CANCELED'
    WHERE booking_id = p_booking_id;

    INSERT INTO cancellation (booking_id, refund_amount, cancellation_fee, cancel_status)
    VALUES (p_booking_id, p_refund, p_fee, 'REQUESTED');
    COMMIT;
END //
DELIMITER ;

-- TRIGGER

-- 1. 매진 상태 자동 변경 (잔여석 0 되면 SOLD_OUT)
DROP TRIGGER IF EXISTS trg_check_sold_out;
DELIMITER //
CREATE TRIGGER trg_check_sold_out
AFTER INSERT ON booking
FOR EACH ROW
BEGIN
    DECLARE remaining        INT;
    DECLARE v_performance_id INT;

    SELECT performance_id INTO v_performance_id
    FROM performance_seat
    WHERE performance_seat_id = NEW.performance_seat_id;

    SET remaining = get_remaining_seats(v_performance_id);

    IF remaining = 0 THEN
        UPDATE performance
        SET sales_status = 'SOLD_OUT'
        WHERE performance_id = v_performance_id;
    END IF;
END //
DELIMITER ;

-- 2. 리뷰 작성 차단 (취소/HOLD 예매, 공연 시작 전 차단)
DROP TRIGGER IF EXISTS trg_review_insert_guard;
DELIMITER //
CREATE TRIGGER trg_review_insert_guard
BEFORE INSERT ON review
FOR EACH ROW
BEGIN
    DECLARE v_status VARCHAR(10);
    DECLARE v_start  DATETIME;

    SELECT b.booking_status, p.start_time
    INTO   v_status, v_start
    FROM   booking b
    JOIN   performance_seat ps ON b.performance_seat_id = ps.performance_seat_id
    JOIN   performance p       ON ps.performance_id     = p.performance_id
    WHERE  b.booking_id = NEW.booking_id;

    IF v_status = 'CANCELED' THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = '취소된 예매에는 리뷰를 작성할 수 없습니다.';
    END IF;

    IF v_status = 'HOLD' THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = '결제가 완료되지 않은 예매에는 리뷰를 작성할 수 없습니다.';
    END IF;

    IF NEW.written_at < v_start THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = '공연 시작 전에는 리뷰를 작성할 수 없습니다.';
    END IF;
END //
DELIMITER ;