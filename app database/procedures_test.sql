DROP PROCEDURE IF EXISTS insertFinalRentalService;
DELIMITER $

CREATE PROCEDURE insertFinalRentalService(IN id INT,IN stationId INT,IN initGas INT,IN addedGas INT,IN success VARCHAR(32),IN refillDate DATETIME,IN left LONGBLOB,IN right LONGBLOB,IN front LONGBLOB,in back LONGBLOB)
BEGIN
    INSERT INTO refill VALUES(refillDate,stationId,initGas,addedGas,success);

    UPDATE rental_service SET left_side_img=left,
    right_side_img=right,
    front_side_img=front,
    back_side_img=back,
    refill_date=refillDate
    WHERE service_id=id;
END $

DELIMITER ;