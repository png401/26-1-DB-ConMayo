USE conmayo;

-- 기존 트리거가 있다면 삭제
DROP TRIGGER IF EXISTS trg_auto_blacklist_on_cancellation;

DELIMITER //

CREATE DEFINER=`root`@`localhost` TRIGGER trg_auto_blacklist_on_cancellation
AFTER INSERT ON cancellation
FOR EACH ROW
BEGIN
    DECLARE v_member_id VARCHAR(20);
    DECLARE v_cancel_count INT;

    SELECT member_id
      INTO v_member_id
    FROM booking
    WHERE booking_id = NEW.booking_id;

    SELECT COUNT(*)
      INTO v_cancel_count
    FROM booking b
    JOIN cancellation c
      ON b.booking_id = c.booking_id
    WHERE b.member_id = v_member_id
      AND c.canceled_at >= DATE_SUB(NOW(), INTERVAL 7 DAY);

    IF v_cancel_count >= 3 THEN
        UPDATE member
        SET blacklist_until = NOW() + INTERVAL 7 DAY
        WHERE member_id = v_member_id;
    END IF;
END //
DELIMITER ;