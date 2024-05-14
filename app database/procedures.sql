DROP PROCEDURE IF EXISTS checkCredentials;
DELIMITER $
CREATE PROCEDURE checkCredentials(IN c_own VARCHAR(64),IN c_number VARCHAR(32),IN c_exp_date VARCHAR(10),IN c_cvv VARCHAR(8),IN price INT,OUT res SMALLINT)
BEGIN
    DECLARE c_id INT UNSIGNED;
    DECLARE balance INT UNSIGNED;
    SELECT count(*),id,MAX(owner_balance) INTO res,c_id,balance FROM bank WHERE c_own=bank.card_owner AND c_number=bank.card_number AND c_exp_date=bank.card_exp_date AND c_cvv=bank.cvv GROUP BY bank.id;
    IF (res=1 AND balance>=price) THEN
        UPDATE bank SET owner_balance=owner_balance-price WHERE c_id=bank.id;
    ELSEIF(balance<price) THEN
        SET res=-1;
    ELSEIF(res=0) THEN
        SET res=-2;
    END IF;
END $
DELIMITER ;

DROP PROCEDURE IF EXISTS getCarsAndGarage;
DELIMITER $
CREATE PROCEDURE getCarsAndGarage()
BEGIN
    SELECT
    FROM
END $
DELIMITER ;