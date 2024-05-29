
DROP PROCEDURE IF EXISTS checkDriver;
DELIMITER $
CREATE PROCEDURE checkDriver(IN username VARCHAR(32), IN password VARCHAR(32))
BEGIN 
    DECLARE driver_username VARCHAR(32);
    DECLARE driver_password VARCHAR(32);
    DECLARE driver_name VARCHAR(32);
    DECLARE driver_lname VARCHAR(32);
    DECLARE email VARCHAR(32);
    Declare taxi_status VARCHAR(32);
    DECLARE taxi_id INT;
    DECLARE taxi_model VARCHAR(32);
    DECLARE taxi_year YEAR;
    DECLARE manuf VARCHAR(32);
    DECLARE licensePlate VARCHAR(32);
    DECLARE taxi_coords POINT;
    DECLARE wallet_balance DECIMAL(10,2);
    DECLARE not_found BOOLEAN DEFAULT FALSE;

    DECLARE cur CURSOR FOR 
        SELECT u.username, u.password, u.name, u.lname, u.email,taxiD.free_status ,t.id, tr.model, tr.manuf_year, tr.manufacturer, t.license_plate, t.coords,w.balance
        FROM taxi_driver taxiD
        INNER JOIN user u ON u.username = taxiD.username
        INNER JOIN taxi t ON t.id = taxiD.taxi
        INNER JOIN transport tr ON t.id = tr.id
        LEFT JOIN wallet w on w.username= u.username
        WHERE u.username = username AND u.password = password;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET not_found = TRUE;

    OPEN cur;
    FETCH cur INTO driver_username, driver_password, driver_name, driver_lname, email,taxi_status,taxi_id, taxi_model, taxi_year, manuf, licensePlate, taxi_coords,wallet_balance;

    IF not_found THEN
        SELECT FALSE AS result;
    ELSE
        SELECT 'taxi driver' AS type,driver_username AS username, driver_password AS password, driver_name AS name, driver_lname AS lname, email,taxi_status,taxi_id, taxi_model, taxi_year, manuf, licensePlate, taxi_coords,wallet_balance;

        SELECT 'card' AS type, ca.card_number, ca.card_holder, ca.expiration_date, ca.cvv, ca.card_type
        FROM card ca
        LEFT JOIN wallet w ON ca.username = w.username
        WHERE w.username = driver_username;
    END IF;

    CLOSE cur;
END $
DELIMITER ;

-- =====================================================================================================

DROP PROCEDURE IF EXISTS checkCustomer;
DELIMITER $$

CREATE PROCEDURE checkCustomer(IN username VARCHAR(32), IN password VARCHAR(32))
BEGIN
    DECLARE cus_username VARCHAR(32);
    DECLARE cus_password VARCHAR(32);
    DECLARE cus_name VARCHAR(32);
    DECLARE cus_lname VARCHAR(32);
    DECLARE email VARCHAR(32);
    DECLARE cus_licence VARCHAR(32);
    DECLARE img LONGBLOB;
    DECLARE cus_points INT UNSIGNED;
    DECLARE wallet_balance DECIMAL(10,2);
    DECLARE not_found BOOLEAN DEFAULT FALSE;

    DECLARE cur CURSOR FOR 
    SELECT u.username, u.password, u.name, u.lname, u.email, c.license, c.license_image, c.points, w.balance
    FROM user u
    JOIN customer c ON c.username = u.username
    LEFT JOIN wallet w ON w.username = u.username
    WHERE u.username = username AND u.password = password;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET not_found = TRUE;

    OPEN cur;
    FETCH cur INTO cus_username, cus_password, cus_name, cus_lname, email, cus_licence, img, cus_points, wallet_balance;

    IF not_found THEN
        SELECT FALSE AS result;
    ELSE
        SELECT 'customer' AS type, cus_username, cus_password, cus_name, cus_lname, email, cus_licence, img, cus_points, wallet_balance;

        SELECT 'card' AS type, ca.card_number, ca.card_holder, ca.expiration_date, ca.cvv,ca.card_type
        FROM card ca
        LEFT JOIN wallet w ON ca.username = w.username
        WHERE w.username = cus_username;
    END IF;

    CLOSE cur;

END $$

DELIMITER ;

-- =====================================================================================================

DROP PROCEDURE IF EXISTS insertCard;
DELIMITER $
CREATE PROCEDURE insertCard(IN user VARCHAR(32),IN cardNum VARCHAR(32),IN expDate VARCHAR(32),IN cOwner VARCHAR(32),IN cvv VARCHAR(32))
BEGIN
    DECLARE c INT UNSIGNED;
    DECLARE balance VARCHAR(32);
    SELECT count(*),owner_balance INTO c,balance FROM bank WHERE cardNum=bank.card_number AND expDate=bank.card_exp_date AND  cOwner=bank.card_owner AND cvv=bank.cvv GROUP BY owner_balance;
    IF (c=1) THEN
        SELECT "true" AS result;
        INSERT INTO card VALUES(user,cardNum,cOwner,expDate,cvv,"credit");
    ELSE
        SELECT "false" AS result;
    END IF;
END $
DELIMITER ;

-- =====================================================================================================

DROP PROCEDURE IF EXISTS chargeWallet;
DELIMITER $
CREATE PROCEDURE chargeWallet(IN user VARCHAR(32),IN val DECIMAL(10,2),cardNum VARCHAR(32))
BEGIN
    DECLARE bankAmount VARCHAR(32);

    SELECT owner_balance into bankAmount FROM bank INNER JOIN card ON bank.card_number=card.card_number where card.username=user AND card.card_number=cardNum;
    IF(bankAmount>=val) THEN
        UPDATE wallet SET balance=balance+val WHERE username=user;
        UPDATE bank SET owner_balance=bankAmount-val WHERE card_number=cardNum;
        SELECT "true" AS result;
    ELSE
        SELECT "false" AS result;
    END IF;
END $

DELIMITER ;

DROP PROCEDURE IF EXISTS insertLicense;
DELIMITER $
CREATE PROCEDURE insertLicense(IN user VARCHAR(32),IN imagebyte LONGBLOB)
BEGIN
    UPDATE customer SET license_image=imagebyte WHERE username=user;
END $
DELIMITER ;

-- =====================================================================================================

DROP PROCEDURE IF EXISTS check_rental_available;
DELIMITER $

CREATE PROCEDURE check_rental_available(IN in_id INT UNSIGNED)
BEGIN
    SELECT COUNT(*) AS result
    FROM rental
    WHERE id = in_id AND free_status = 'TRUE';
END$

DELIMITER ;

-- =====================================================================================================

DROP procedure IF EXISTS cancelReservation;
delimiter $
create procedure cancelReservation(in serviceId int,in rentalId int)
begin
	update service set service_status='CANCELLED',status_date=now() where id=serviceId;
	update rental set free_status='TRUE' where id=rentalId;
end$
delimiter ;

-- =====================================================================================================

drop procedure if EXISTS checkVehicleId;
delimiter $
create procedure checkVehicleId(in serviceId int,in vehicle_id int)
begin
	declare check_id int;
	
	select selected_vehicle into check_id from rental_service where service_id=serviceId;
	
	if vehicle_id=check_id then
		Select 'TRUE' as result;
	else
		select 'FALSE' as result;
	end if;
	
end$
delimiter ;

-- =====================================================================================================

drop procedure if exists unlockVehicle;
delimiter $
create procedure unlockVehicle(in serviceId int)
begin
	update rental_service set unlock_vehicle=now() where service_id=serviceId;
end$
delimiter ;

drop procedure if exists rentalService;
delimiter $
create procedure rentalService(in serviceId int)
begin
	select creation_date from service where id=serviceId;
end$
delimiter ;

-- =====================================================================================================

DROP PROCEDURE IF EXISTS insertOutCityService;
DELIMITER $
CREATE PROCEDURE insertOutCityService(IN name VARCHAR(32),IN value DECIMAL(10,2),IN method ENUM('WALLET','CASH'),IN creationDate DATETIME,IN status ENUM('ONGOING', 'COMPLETED', 'CANCELLED'),IN status_date DATETIME,IN earned_points INT,IN car_id INT, IN num_days INT)
BEGIN
    DECLARE payment_id INT UNSIGNED;
    DECLARE service_id INT UNSIGNED;

    INSERT INTO payment VALUES(null,name,value,method);
    
    SET payment_id=LAST_INSERT_ID();
    INSERT INTO service VALUES(null,creationDate,payment_id,status,status_date,earned_points);

    UPDATE wallet SET balance = balance - value WHERE username = name;
    
    SET service_id=LAST_INSERT_ID();
    INSERT INTO out_city_service VALUES(service_id,car_id,null,num_days);

    UPDATE out_city_transport SET free_status = "FALSE" WHERE id = car_id;
END $
DELIMITER ;

-- =====================================================================================================

DROP PROCEDURE IF EXISTS rate_rental_service;

DELIMITER $

CREATE PROCEDURE rate_rental_service(IN in_service_id INT UNSIGNED, IN vehicle_stars TINYINT, IN comment TEXT)
BEGIN
    INSERT INTO rental_rating VALUES(NULL, vehicle_stars, comment);
    UPDATE rental_service SET rating_id = LAST_INSERT_ID() WHERE service_id = in_service_id;
END$

DELIMITER ;

-- =====================================================================================================

DROP PROCEDURE IF EXISTS rate_taxi_service;

DELIMITER $

CREATE PROCEDURE rate_taxi_service(IN in_service_id INT UNSIGNED, IN vehicle_stars TINYINT, IN driver_stars TINYINT, IN comment TEXT)
BEGIN
    INSERT INTO taxi_rating VALUES(NULL, vehicle_stars, driver_stars, comment);
    UPDATE taxi_service SET rating_id = LAST_INSERT_ID() WHERE service_id = in_service_id;
END$

DELIMITER ;

-- =====================================================================================================

DROP PROCEDURE IF EXISTS rate_out_city_service;

DELIMITER $

CREATE PROCEDURE rate_out_city_service(IN in_service_id INT UNSIGNED, IN vehicle_stars TINYINT, IN garage_stars TINYINT, IN comment TEXT)
BEGIN
    INSERT INTO out_city_rating VALUES(NULL, vehicle_stars, garage_stars, comment);
    UPDATE out_city_service SET rating_id = LAST_INSERT_ID() WHERE service_id = in_service_id;
END$

DELIMITER ;

-- =====================================================================================================