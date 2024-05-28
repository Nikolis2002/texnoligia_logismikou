DROP PROCEDURE IF EXISTS insertFinalRentalService;
DELIMITER $

CREATE PROCEDURE insertFinalRentalService(IN points INT,IN userName Varchar(32),IN pValue DECIMAL(10,2),IN pMethod ENUM('WALLET','CASH'),IN rentalId INT,IN stationId INT,IN initGas INT,IN addedGas INT,IN success VARCHAR(32),IN refillDate DATETIME,IN leftimg LONGBLOB,IN rightimg LONGBLOB,IN frontimg LONGBLOB,in backimg LONGBLOB)
BEGIN

    DECLARE paymentID INT UNSIGNED;
    
    INSERT INTO payment VALUES(null,userName,pValue,pMethod);

    SET paymentID=LAST_INSERT_ID();

    UPDATE service SET payment_id=paymentID,
    service_status='COMPLETED',
    status_date=NOW(),
    earned_points=points
    where id=rentalId;

    UPDATE wallet SET balance=balance-pValue
    WHERE username=userName;
    
    INSERT INTO refill VALUES(refillDate,stationId,initGas,addedGas,success);

    UPDATE rental_service SET left_side_img=leftimg,
    right_side_img=rightimg,
    front_side_img=frontimg,
    back_side_img=backimg,
    refill_date=refillDate
    WHERE service_id=rentalId;
END $

DELIMITER ;