DROP PROCEDURE IF EXISTS insertOutCityService;
DELIMITER $
CREATE PROCEDURE insertOutCityService(IN name VARCHAR(32),IN value DECIMAL(10,2),IN method ENUM('WALLET','CASH'),IN creationDate DATETIME,IN status ENUM('ONGOING', 'COMPLETED', 'CANCELLED'),IN status_date DATETIME,IN earned_points INT,IN car_id INT)
BEGIN
    DECLARE payment_id INT UNSIGNED;
    DECLARE service_id INT UNSIGNED;

    INSERT INTO payment VALUES(null,name,value,method);
    
    SET payment_id=LAST_INSERT_ID();
    INSERT INTO service VALUES(null,creationDate,payment_id,status,status_date,earned_points);
    
    SET service_id=LAST_INSERT_ID();
    INSERT INTO out_city_service VALUES(service_id,car_id,null);

END $
DELIMITER ;
