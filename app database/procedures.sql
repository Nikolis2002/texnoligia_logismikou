
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
        SELECT TRUE AS result;
        INSERT INTO card VALUES(user,cardNum,cOwner,expDate,cvv,"credit");
    ELSE
        SELECT FALSE AS result;
    END IF;
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

DROP VIEW IF EXISTS selectTaxiRequests;
CREATE VIEW selectTaxiRequests AS
SELECT tr.id as id,tr.pickup_location as pickup_location,tr.destination as destination,p.payment_method as payment_method
FROM taxi_request tr 
INNER JOIN taxi_service ts ON tr.id=ts.request_id
INNER JOIN service ser  ON ts.request_id=ser.id
INNER JOIN payment p  ON  p.id=ser.payment_id
WHERE tr.assignment_time is NULL and ser.service_status='ONGOING';

-- =====================================================================================================

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
INSERT INTO wallet VALUES("bill3",50);
CALL insertCard("Bill","072","123","Billkort","999");

insert into user values("Dista","dim123","Dimitris","Stasinos","dimitris@gmail.com","6981472583");
INSERT INTO customer VALUES("Dista","A2",null,0);
INSERT INTO wallet VALUES("Dista",10);
INSERT INTO card VALUES("Dista","456","dimitris","123","086","credit");

insert into user values("Nikolis","nik789","Nikolaos","Andrianopoulos","nikolis@gmail.com","6987894561");
INSERT INTO customer VALUES("Nikolis","A2",null,0);
INSERT INTO wallet VALUES("Nikolis",20);
INSERT INTO card VALUES("Nikolis","458","nikolaos","123","086","credit");

insert into user values("Zoukos","zouk741","Panagiotis","Kalozoumis","panos@gmail.com","6988521346");
INSERT INTO customer VALUES("Zoukos","A2",null,0);
INSERT INTO wallet VALUES("Zoukos",10);
INSERT INTO card VALUES("Zoukos","125","panagiotis","123","086","credit");
INSERT INTO user VALUES("bill4","12356","Vasilis","Kourtakis","test@gmail2.com","6911234567");
INSERT INTO customer VALUES("bill4","A2","test",0);
INSERT INTO wallet VALUES("bill4",50);


-- Insert into taxi_request
INSERT INTO taxi_request
VALUES 
    (null,ST_GeomFromText('POINT(40.7128 -74.0060)'), ST_GeomFromText('POINT(34.0522 -118.2437)'), 'bill2', '2024-05-20 08:30:00', NULL),
    (null,ST_GeomFromText('POINT(37.7749 -122.4194)'), ST_GeomFromText('POINT(36.1699 -115.1398)'), NULL, NULL, NULL),
    (null,ST_GeomFromText('POINT(37.7749 -122.4194)'), ST_GeomFromText('POINT(36.1699 -115.1398)'), NULL, NULL, NULL);

-- Insert into payment
INSERT INTO payment 
VALUES 
    (null,'bill', 50.00, 'WALLET'),
    (null,'bill3', 30.50, 'CASH'),
    (null,'bill4', 30.50, 'CASH');

-- Insert into service
INSERT INTO service 
VALUES 
    (null,CURRENT_TIMESTAMP, 1, 'ONGOING', NULL),
    (null,CURRENT_TIMESTAMP, 2, 'ONGOING', NULL),
    (null,CURRENT_TIMESTAMP, 3, 'ONGOING', NULL);

-- Insert into taxi_service
INSERT INTO taxi_service (service_id, request_id, rating_id)
VALUES 
    (1, 1, NULL),
    (2, 2, NULL),
    (3,3, NULL);


INSERT INTO gas_station VALUES
(NULL,ST_GeomFromText('POINT(38.256422 21.743256)'),3.2),
(NULL,ST_GeomFromText('POINT(38.262861 21.751035)'),1.2),
(NULL,ST_GeomFromText('POINT(38.269737 21.746496)'),3.7),
(NULL,ST_GeomFromText('POINT(38.278801 21.766688)'),3.7),
(NULL,ST_GeomFromText('POINT(38.255465 21.747013)'),4.5),
(NULL,ST_GeomFromText('POINT(38.243420 21.754333)'),6.6),
(NULL,ST_GeomFromText('POINT(38.234517 21.746291)'),7.9);
