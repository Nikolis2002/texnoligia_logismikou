
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
create procedure unlockVehicle(in serviceId int, IN in_name VARCHAR(32))
begin
	update rental_service set unlock_vehicle=now() where service_id=serviceId;

    UPDATE wallet SET balance = balance - 1 WHERE username = in_name;
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

DROP PROCEDURE IF EXISTS withdraw_from_wallet;

DELIMITER $

CREATE PROCEDURE withdraw_from_wallet(IN in_username VARCHAR(32), IN in_amount DECIMAL(10,2))
BEGIN
    UPDATE wallet SET balance = balance - in_amount WHERE username = in_username;
END$

DELIMITER ;

-- =====================================================================================================

DROP PROCEDURE IF EXISTS add_to_wallet;

DELIMITER $

CREATE PROCEDURE add_to_wallet(IN in_username VARCHAR(32), IN in_amount DECIMAL(10,2))
BEGIN
    UPDATE wallet SET balance = balance + in_amount WHERE username = in_username;
END$

DELIMITER ;

-- =====================================================================================================

DROP PROCEDURE IF EXISTS insertFinalRentalService;
DELIMITER $

CREATE PROCEDURE insertFinalRentalService(IN in_points INT,IN userName Varchar(32),IN pValue DECIMAL(10,2),IN pMethod ENUM('WALLET','CASH'),IN serviceId INT,IN stationId INT,IN initGas INT,IN addedGas INT,IN success VARCHAR(32),IN refillDate DATETIME,IN leftimg LONGBLOB,IN rightimg LONGBLOB,IN frontimg LONGBLOB,in backimg LONGBLOB)
BEGIN

    DECLARE paymentID INT UNSIGNED;
    
    INSERT INTO payment VALUES(null,userName,pValue,pMethod);

    SET paymentID=LAST_INSERT_ID();

    UPDATE service SET payment_id=paymentID,
    service_status='COMPLETED',
    status_date=NOW(),
    earned_points=in_points
    where id=serviceId;
    
    if (refillDate is not null) then
        INSERT INTO refill VALUES(refillDate,stationId,initGas,addedGas,success);
    end if;

    UPDATE rental_service SET left_side_img=leftimg,
    right_side_img=rightimg,
    front_side_img=frontimg,
    back_side_img=backimg,
    refill_date=refillDate
    WHERE service_id=serviceId;

    UPDATE customer SET points = points + in_points;
    UPDATE wallet SET balance = balance - pValue WHERE username = userName;

END $

DELIMITER ;

-- =====================================================================================================

DELIMITER ;

DROP PROCEDURE IF EXISTS signUp;
DELIMITER $

CREATE PROCEDURE signUp(IN user VARCHAR(32),IN pass VARCHAR(32),IN name VARCHAR(32),IN lname VARCHAR(32),IN email VARCHAR(32),IN  phone VARCHAR(32),IN license LONGBLOB)
BEGIN
    INSERT INTO user VALUES(user,pass,name,lname,email,phone);
    INSERT INTO customer VALUES(user,"BOTH",license,0);
END $

DELIMITER ;

-- =====================================================================================================

DELIMITER $

DROP PROCEDURE IF EXISTS check_coupon$

CREATE PROCEDURE check_coupon(IN in_id INT)
BEGIN

    SELECT supply AS result
    FROM coupon
    WHERE id = in_id;

END$
DELIMITER ;

-- =====================================================================================================

DELIMITER $

DROP PROCEDURE IF EXISTS redeem_coupon$

CREATE PROCEDURE redeem_coupon(IN in_id INT, IN in_username VARCHAR(32))
BEGIN

    DECLARE var_id INT;
    DECLARE var_points INT;
    DECLARE var_value DECIMAL(10, 2);

    SELECT points, value
    INTO var_points, var_value
    FROM valid_coupons
    WHERE id = in_id;

    UPDATE customer SET points = points - var_points WHERE username = in_username;
    UPDATE wallet SET balance = balance + var_value WHERE username = in_username;

    UPDATE coupon
    SET supply = supply - 1
    WHERE id = in_id AND supply > -1 AND expiration_date > NOW();

END$

DELIMITER ;


-- =====================================================================================================

-- taxi procedures

-- =====================================================================================================

DROP PROCEDURE IF EXISTS cancelService;
delimiter $
create procedure cancelService(in service_id int)
begin
    update service set service_status="CANCELLED" where id=service_id;
    update service set status_date=now() where id=service_id;
end $

create procedure checkQrId(in id int,in vehicle_id int)
begin
	IF EXISTS(SELECT selected_vehicle FROM rental_service WHERE selected_vehicle=service_id and service_id=id) THEN
		SELECT "TRUE" AS result;	
	ELSE
		SELECT "FALSE" AS result;
	END IF;
end $
delimiter ;

-- =====================================================================================================

DROP PROCEDURE IF EXISTS taxiReservation;
delimiter $
create procedure taxiReservation(in payment_customer_username VARCHAR(32),in payment_method ENUM('WALLET','CASH'),
in service_creation_date DATETIME,in taxiReq_pickup_location POINT,in taxiReq_destination POINT)
begin
	declare payment_id int;
    declare service_id int;
    declare taxi_request_id int;
    
	insert into payment values(null,payment_customer_username,null,payment_method);
	set payment_id=LAST_INSERT_ID();
	
	insert into service values(null,service_creation_date,payment_id,'ONGOING',null,0);
	set service_id=LAST_INSERT_ID();
	
	insert into taxi_request values(null,taxiReq_pickup_location,taxiReq_destination,null,null,null);
	set taxi_request_id=LAST_INSERT_ID();
	
	insert into customer_history values(payment_customer_username,service_id);
	insert into taxi_service values(service_id,taxi_request_id,null);
	select service_id;
    
end$
delimiter ;

-- =====================================================================================================

DROP PROCEDURE IF EXISTS resumeService;
delimiter $
create procedure resumeService(in service_id int)
begin
	update service set service_status="ONGOING" where id=service_id;
    update service set status_date=now() where id=service_id;
end$
delimiter ;

-- =====================================================================================================

DROP PROCEDURE IF EXISTS checkTaxiReservation;
delimiter $
create procedure checkTaxiReservation(in service_id_check int)
begin
	declare taxiRequest int;
	declare date_check DATETIME;
	
	select request_id into taxiRequest from taxi_service where service_id=service_id_check;
	select assignment_time into date_check from taxi_request where id=taxiRequest;
	
	if date_check IS NULL THEN
		SELECT "FALSE" AS result;	
	ELSE
		SELECT "TRUE" AS result;
	END IF;

	
end$
delimiter ;

-- =====================================================================================================

drop procedure if exists acceptTaxiRequest;
delimiter $
create procedure acceptTaxiRequest(in request_id int, in driver_username VARCHAR(32))
begin
	
	update taxi_request set assigned_driver=driver_username, assignment_time=now() where id=request_id;

end$
delimiter ;

-- =====================================================================================================

drop procedure if exists checkTaxiPickUp;
delimiter $
create procedure checkTaxiPickUp(in service_id_in int)
begin 
	declare taxi_req_id int;
	declare pickTime DATETIME;
	
	select request_id into taxi_req_id from taxi_service where service_id=service_id_in;
	select pickup_time into pickTime from taxi_request where id=taxi_req_id;
	
	if pickTime is NULL then
		SELECT "FALSE" AS result;
	ELSE
		SELECT "TRUE" AS result;
	end if;
	
end$
delimiter ;

-- =====================================================================================================

DROP PROCEDURE IF EXISTS checkTaxiReservationSecond;
delimiter $
create procedure checkTaxiReservationSecond(in request_id_in int)
begin
	declare status_check ENUM('ONGOING', 'COMPLETED', 'CANCELLED');
	declare service_id_check int;
	
	select service_id into service_id_check from taxi_service where request_id=request_id_in;
	select service_status into status_check from service where id=service_id_check;

	if status_check='CANCELLED' THEN
		SELECT "FALSE" AS result;	
	ELSE
		SELECT "TRUE" AS result;
	END IF;
end$
delimiter ;

-- =====================================================================================================

drop procedure if exists completeTaxiRequest;
delimiter $
create procedure completeTaxiRequest(in taxi_req_id int,in payment_method ENUM('WALLET','CASH'),in payment_value DECIMAL(10,2))
begin
	declare service_id_update int;
	declare payment_id_update int;
	
	select service_id into service_id_update from taxi_service where request_id=taxi_req_id;
	select payment_id into payment_id_update from service where id=service_id_update;
	update service set service_status='COMPLETED',status_date=now() where id=service_id_update;
	update payment set payment_value=payment_value where id=payment_id_update;
	
end$
delimiter ;

-- =====================================================================================================

drop procedure if exists getPayment;
delimiter $
create procedure getPayment(in service_id int)
begin
	declare pay_id int;
	
	select payment_id into pay_id from service where id=service_id;
	select payment_value,payment_method from payment where id=pay_id;
	
end$
delimiter ;

-- =====================================================================================================

drop procedure if exists updatePickUpRequest;
delimiter $
create procedure updatePickUpRequest(in taxi_req_id int)
begin
	update taxi_request set pickup_time=now() where id=taxi_req_id;
end$
delimiter ;

-- =====================================================================================================

drop procedure if exists checkTaxiRequest;
delimiter $
create procedure checkTaxiRequest(in requestIdin int)
begin
	declare service_st  VARCHAR(32);
	declare serviceId int;
	declare taxi_req__driver VARCHAR(32);

	select service_id into serviceId from taxi_service where request_id=requestIdin;
	select service_status into service_st from service where id=serviceId;
	select assigned_driver into taxi_req__driver from taxi_request where id=serviceId;
	
	if (service_st='ONGOING' AND taxi_req__driver IS NULL) THEN
		SELECT "TRUE" AS result;
	ELSE
		SELECT "FALSE" AS result; 
	END IF;
	
end$
delimiter ;

-- =====================================================================================================

drop procedure if exists checkTaxiComplete;
delimiter $
create procedure checkTaxiComplete(in service_id int)
begin
	declare status_check ENUM('ONGOING', 'COMPLETED', 'CANCELLED');
	
	SELECT service_status into status_check
	FROM service WHERE id = service_id and service_status='COMPLETED';

	if (status_check is NOT NULL) THEN
		SELECT "TRUE" AS result;	
	ELSE
		SELECT "FALSE" AS result;
	END IF;
end$
delimiter ;

-- =====================================================================================================

drop procedure if exists updatePoints;
delimiter $
create procedure updatePoints(in service_id_in int,in points_in int,in username_in VARCHAR(32),in newPoints int)
begin

	update service set  earned_points=points_in where id=service_id_in;
	update customer set points=newPoints where username=username_in;
	
end$
delimiter ;

-- =====================================================================================================

drop procedure if exists updateWallet;
delimiter $
create procedure updateWallet(in username_in VARCHAR(32),in new_balance DECIMAL(10,2))
begin
	update wallet set balance=new_balance where username=username_in;
end $
delimiter ;

-- =====================================================================================================

-- taxi procedures

-- =====================================================================================================

DROP PROCEDURE IF EXISTS cancelService;
delimiter $
create procedure cancelService(in service_id int)
begin
    update service set service_status="CANCELLED" where id=service_id;
    update service set status_date=now() where id=service_id;
end $

create procedure checkQrId(in id int,in vehicle_id int)
begin
	IF EXISTS(SELECT selected_vehicle FROM rental_service WHERE selected_vehicle=service_id and service_id=id) THEN
		SELECT "TRUE" AS result;	
	ELSE
		SELECT "FALSE" AS result;
	END IF;
end $
delimiter ;

-- =====================================================================================================

DROP PROCEDURE IF EXISTS taxiReservation;
delimiter $
create procedure taxiReservation(in payment_customer_username VARCHAR(32),in payment_method ENUM('WALLET','CASH'),
in service_creation_date DATETIME,in taxiReq_pickup_location POINT,in taxiReq_destination POINT)
begin
	declare payment_id int;
    declare service_id int;
    declare taxi_request_id int;
    
	insert into payment values(null,payment_customer_username,null,payment_method);
	set payment_id=LAST_INSERT_ID();
	
	insert into service values(null,service_creation_date,payment_id,'ONGOING',null,0);
	set service_id=LAST_INSERT_ID();
	
	insert into taxi_request values(null,taxiReq_pickup_location,taxiReq_destination,null,null,null);
	set taxi_request_id=LAST_INSERT_ID();
	
	insert into customer_history values(payment_customer_username,service_id);
	insert into taxi_service values(service_id,taxi_request_id,null);
	select service_id;
    
end$
delimiter ;

-- =====================================================================================================

DROP PROCEDURE IF EXISTS resumeService;
delimiter $
create procedure resumeService(in service_id int)
begin
	update service set service_status="ONGOING" where id=service_id;
    update service set status_date=now() where id=service_id;
end$
delimiter ;

-- =====================================================================================================

DROP PROCEDURE IF EXISTS checkTaxiReservation;
delimiter $
create procedure checkTaxiReservation(in service_id_check int)
begin
	declare taxiRequest int;
	declare date_check DATETIME;
	
	select request_id into taxiRequest from taxi_service where service_id=service_id_check;
	select assignment_time into date_check from taxi_request where id=taxiRequest;
	
	if date_check IS NULL THEN
		SELECT "FALSE" AS result;	
	ELSE
		SELECT "TRUE" AS result;
	END IF;

	
end$
delimiter ;

-- =====================================================================================================

drop procedure if exists acceptTaxiRequest;
delimiter $
create procedure acceptTaxiRequest(in request_id int, in driver_username VARCHAR(32))
begin
	
	update taxi_request set assigned_driver=driver_username, assignment_time=now() where id=request_id;

end$
delimiter ;

-- =====================================================================================================

drop procedure if exists checkTaxiPickUp;
delimiter $
create procedure checkTaxiPickUp(in service_id_in int)
begin 
	declare taxi_req_id int;
	declare pickTime DATETIME;
	
	select request_id into taxi_req_id from taxi_service where service_id=service_id_in;
	select pickup_time into pickTime from taxi_request where id=taxi_req_id;
	
	if pickTime is NULL then
		SELECT "FALSE" AS result;
	ELSE
		SELECT "TRUE" AS result;
	end if;
	
end$
delimiter ;

-- =====================================================================================================

DROP PROCEDURE IF EXISTS checkTaxiReservationSecond;
delimiter $
create procedure checkTaxiReservationSecond(in request_id_in int)
begin
	declare status_check ENUM('ONGOING', 'COMPLETED', 'CANCELLED');
	declare service_id_check int;
	
	select service_id into service_id_check from taxi_service where request_id=request_id_in;
	select service_status into status_check from service where id=service_id_check;

	if status_check='CANCELLED' THEN
		SELECT "FALSE" AS result;	
	ELSE
		SELECT "TRUE" AS result;
	END IF;
end$
delimiter ;

-- =====================================================================================================

drop procedure if exists completeTaxiRequest;
delimiter $
create procedure completeTaxiRequest(in taxi_req_id int,in payment_method ENUM('WALLET','CASH'),in payment_value DECIMAL(10,2))
begin
	declare service_id_update int;
	declare payment_id_update int;
	
	select service_id into service_id_update from taxi_service where request_id=taxi_req_id;
	select payment_id into payment_id_update from service where id=service_id_update;
	update service set service_status='COMPLETED',status_date=now() where id=service_id_update;
	update payment set payment_value=payment_value where id=payment_id_update;
	
end$
delimiter ;

-- =====================================================================================================

drop procedure if exists getPayment;
delimiter $
create procedure getPayment(in service_id int)
begin
	declare pay_id int;
	
	select payment_id into pay_id from service where id=service_id;
	select payment_value,payment_method from payment where id=pay_id;
	
end$
delimiter ;

-- =====================================================================================================

drop procedure if exists updatePickUpRequest;
delimiter $
create procedure updatePickUpRequest(in taxi_req_id int)
begin
	update taxi_request set pickup_time=now() where id=taxi_req_id;
end$
delimiter ;

-- =====================================================================================================

drop procedure if exists checkTaxiRequest;
delimiter $
create procedure checkTaxiRequest(in requestIdin int)
begin
	declare service_st  VARCHAR(32);
	declare serviceId int;
	declare taxi_req__driver VARCHAR(32);

	select service_id into serviceId from taxi_service where request_id=requestIdin;
	select service_status into service_st from service where id=serviceId;
	select assigned_driver into taxi_req__driver from taxi_request where id=serviceId;
	
	if (service_st='ONGOING' AND taxi_req__driver IS NULL) THEN
		SELECT "TRUE" AS result;
	ELSE
		SELECT "FALSE" AS result; 
	END IF;
	
end$
delimiter ;

-- =====================================================================================================

drop procedure if exists checkTaxiComplete;
delimiter $
create procedure checkTaxiComplete(in service_id int)
begin
	declare status_check ENUM('ONGOING', 'COMPLETED', 'CANCELLED');
	
	SELECT service_status into status_check
	FROM service WHERE id = service_id and service_status='COMPLETED';

	if (status_check is NOT NULL) THEN
		SELECT "TRUE" AS result;	
	ELSE
		SELECT "FALSE" AS result;
	END IF;
end$
delimiter ;

-- =====================================================================================================

drop procedure if exists updatePoints;
delimiter $
create procedure updatePoints(in service_id_in int,in points_in int,in username_in VARCHAR(32),in newPoints int)
begin

	update service set  earned_points=points_in where id=service_id_in;
	update customer set points=newPoints where username=username_in;
	
end$
delimiter ;

-- =====================================================================================================

drop procedure if exists updateWallet;
delimiter $
create procedure updateWallet(in username_in VARCHAR(32),in new_balance DECIMAL(10,2))
begin
	update wallet set balance=new_balance where username=username_in;
end $
delimiter ;

-- =====================================================================================================
