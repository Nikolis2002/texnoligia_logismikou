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

        SELECT 'card' AS type, ca.card_number, ca.card_holder, ca.expiration_date, ca.cvv, ca.card_type
        FROM card ca
        LEFT JOIN wallet w ON ca.username = w.username
        WHERE w.username = cus_username;
    END IF;

    CLOSE cur;

END $$

DELIMITER ;

DROP PROCEDURE IF EXISTS getCarsAndGarage;
DELIMITER $$
CREATE PROCEDURE getCarsAndGarage()
BEGIN
    SELECT
    FROM garage g 
    INNER  JOIN out_city_transport ot on g.id=ot.garage_id
    LEFT JOIN out_city_car oc on oc.id=ot.id
    LEFT JOIN out_city_van ov ON ov.id=ot.id;
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
        SELECT FALSE AS result;
    END IF;
END $
DELIMITER ;


INSERT INTO bank VALUES("Billkort","072","123","999",default);
CALL insertCard("Bill","072","123","Billkort","999");
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
INSERT INTO transport VALUES(NULL,"MONDEO","2007","FORD");
INSERT INTO taxi VALUES(1,"1234",ST_GeomFromText('POINT(1.23 4.56)'));
INSERT INTO taxi_driver VALUES("bill2",1,"TRUE");


INSERT INTO user VALUES("bill3","1235","Vasilis","Kourtakis","test@gmail.com","6911234567");
INSERT INTO customer VALUES("bill3","A2","test",0);