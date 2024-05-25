drop procedure if exists getPayment;
delimiter $
create procedure getPayment(in service_id int)
begin
	declare pay_id int;
	
	select payment_id into pay_id from service where id=service_id;
	select payment_value,payment_method from payment where id=pay_id;
	
end$
delimiter ;
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

DROP PROCEDURE IF EXISTS insertCard;
DELIMITER $
CREATE PROCEDURE insertCard(IN user VARCHAR(32),IN cardNum VARCHAR(32),IN expDate VARCHAR(32),IN cOwner VARCHAR(32),IN cvv VARCHAR(32))
BEGIN
    DECLARE c INT UNSIGNED;
    DECLARE balance VARCHAR(32);
    SELECT count(*),owner_balance INTO c,balance FROM bank WHERE cardNum=bank.card_number AND expDate=bank.card_exp_date AND  cOwner=bank.card_owner AND cvv=bank.cvv GROUP BY owner_balance;
    IF (c=1) THEN
        INSERT INTO card VALUES(user,cardNum,expDate,cOwner,cvv,"credit");
    ELSE
        SELECT "false" AS result;
    END IF;
END $
DELIMITER ;

DROP PROCEDURE IF EXISTS chargeWallet;
DELIMITER $
CREATE PROCEDURE chargeWallet(IN user VARCHAR(32),IN val VARCHAR(32),cardNum VARCHAR(32))
BEGIN
    DECLARE bankAmount VARCHAR(32);
    SELECT owner_balance into bankAmount FROM bank INNER JOIN card ON bank.card_number=card.card_number where card.username=user;
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


DROP PROCEDURE IF EXISTS getLicense;
DELIMITER $
CREATE PROCEDURE getLicense(IN user VARCHAR(32))
BEGIN
    SELECT license_image FROM customer WHERE username=user;
END $
DELIMITER ;


-- //////////////////////BANK MOCK
INSERT INTO user VALUES("bill","123","Vasilis","Kourtakis","test@gmail.com","6911234567");
INSERT INTO customer VALUES("bill","A2","test",0);
INSERT INTO wallet VALUES("bill",100);
INSERT INTO card VALUES("bill","123","kort","123","086","credit");
INSERT INTO card VALUES("bill","124","kort2","1234","0862","credit");

-- // for taxi driver 
INSERT INTO user VALUES("bill2","1234","Vasilis2","Kourtakis2","test2@gmail.com","6911234567");
INSERT INTO wallet VALUES("bill2",100);
INSERT INTO card VALUES("bill2","1235","kort","123","086","credit");
INSERT INTO card VALUES("bill2","1246","kort2","1234","0862","credit");
INSERT INTO transport VALUES(NULL,"FORD","MONDEO","2007");
INSERT INTO taxi VALUES(1,"1234",ST_GeomFromText('POINT(1.23 4.56)'));
INSERT INTO taxi_driver VALUES("bill2",1,"TRUE");


INSERT INTO user VALUES("bill3","1235","Vasilis","Kourtakis","test@gmail.com","6911234567");
INSERT INTO customer VALUES("bill3","A2","test",0);