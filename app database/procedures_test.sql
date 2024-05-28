DROP PROCEDURE IF EXISTS insertFinalRentalService;
DELIMITER $

CREATE PROCEDURE insertFinalRentalService(IN id INT,IN stationId INT,IN initGas INT,IN addedGas INT,IN success VARCHAR(32),IN refillDate DATETIME,IN leftimg LONGBLOB,IN rightimg LONGBLOB,IN frontimg LONGBLOB,in backimg LONGBLOB)
BEGIN
    INSERT INTO refill VALUES(refillDate,stationId,initGas,addedGas,success);

    UPDATE rental_service SET left_side_img=leftimg,
    right_side_img=rightimg,
    front_side_img=frontimg,
    back_side_img=backimg,
    refill_date=refillDate
    WHERE service_id=id;
END $

DELIMITER ;