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

DROP PROCEDURE IF EXISTS checkDriver;
DELIMITER $

CREATE PROCEDURE checkDriver(IN username VARCHAR(32), IN password VARCHAR(32))
BEGIN
    DECLARE driver_id INT UNSIGNED;
    DECLARE driver_username VARCHAR(32);
    DECLARE driver_password VARCHAR(32);
    DECLARE driver_name VARCHAR(32);
    DECLARE driver_lname VARCHAR(32);
    DECLARE email VARCHAR(32);
    DECLARE taxi_id INT;
    DECLARE taxi_model VARCHAR(32);
    DECLARE taxi_year YEAR;
    DECLARE manuf VARCHAR(32);
    DECLARE licensePlate VARCHAR(32);
    DECLARE taxi_coords POINT;
    DECLARE not_found BOOLEAN DEFAULT TRUE;

    DECLARE cur CURSOR FOR 
        SELECT u.id, u.username, u.password, u.name, u.lname,u.email,t.id,tr.model,tr.manuf_year,tr.manufacturer,t.license_plate,t.coords
        FROM taxi_driver taxiD
        INNER JOIN user u ON u.id = taxiD.id
        INNER JOIN taxi t ON t.id =taxiD.taxi
        INNER JOIN transport tr ON t.id=tr.id
        WHERE u.username = username AND u.password = password;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET not_found = TRUE;

    OPEN cur;
    FETCH cur INTO driver_id, driver_username, driver_password, driver_name, driver_lname,email,taxi_id,taxi_model,taxi_year,manuf,licensePlate,taxi_coords;
    
    IF not_found THEN
        SELECT FALSE AS result;
    ELSE
        SELECT "taxi driver" AS type,driver_id AS id, driver_username AS username, driver_password AS password, driver_name AS name, driver_lname AS lname, email,taxi_id,taxi_model,taxi_year,manuf,licensePlate,taxi_coords;
    END IF;

    CLOSE cur;
END $
DELIMITER ;

DROP PROCEDURE IF EXISTS checkCustomer;
DELIMITER $
CREATE PROCEDURE checkCustomer(IN username VARCHAR(32),IN password VARCHAR(32))
BEGIN
    DECLARE cus_id INT UNSIGNED;
    DECLARE cus_username VARCHAR(32);
    DECLARE cus_password VARCHAR(32);
    DECLARE cus_name VARCHAR(32);
    DECLARE cus_lname VARCHAR(32);
    DECLARE email VARCHAR(32);
    DECLARE cus_licence VARCHAR(32);
    DECLARE img LONGBLOB;
    DECLARE cus_points INT UNSIGNED;
    DECLARE wallet_balance DECIMAL(10,2);
    DECLARE crd_number VARCHAR(32);
    DECLARE crd_holder VARCHAR(32);
    DECLARE exp_date VARCHAR(32);
    DECLARE crd_cvv VARCHAR(32);
    DECLARE crd_type VARCHAR(32);
    DECLARE not_found BOOLEAN DEFAULT TRUE;


    DECLARE cur CURSOR FOR 
        SELECT u.id,u.username,u.password,u.name,u.lname,u.email,c.license,c.license_image,c.points,w.balance,ca.card_number,ca.card_holder,ca.expiration_date,ca.cvv,ca.card_type
        FROM customer c 
        INNER JOIN user u on u.id=c.id 
        RIGHT JOIN wallet w on w.user_id=u.id 
        RIGHT JOIN card ca on  ca.wallet_id=w.id
    WHERE u.username = username AND u.password= password;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET not_found = TRUE;

    OPEN cur ;
    FETCH cur INTO cus_id,cus_username,cus_password,cus_name,cus_lname,email,cus_licence,img,cus_points,wallet_balance,crd_number,crd_holder,exp_date,crd_cvv,crd_type;

    IF not_found THEN
        SELECT FALSE AS result;
    ELSE
        SELECT "customer" AS type,cus_id,cus_username,cus_password,cus_name,cus_lname,email,cus_licence,img,cus_points,wallet_balance,crd_number,crd_holder,exp_date,crd_cvv,crd_type;

    END IF;

    CLOSE cur;


END $
DELIMITER ;