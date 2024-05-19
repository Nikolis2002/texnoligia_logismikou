USE app_database;

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


delimiter $
CREATE PROCEDURE test()
BEGIN
	SELECT "TRUE" AS result;
END$
delimiter ;