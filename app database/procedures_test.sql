drop procedure if exists checkTaxiRequest;
delimiter $
create procedure checkTaxiRequest(in request_id int)
begin
	declare service_st  VARCHAR(32);
	declare serviceId int;
	declare taxi_req__driver VARCHAR(32);

	select service_id into serviceId from taxi_service where request_id=request_id;
	select service_status into service_st from service where id=serviceId;
	select assigned_driver into taxi_req__driver from taxi_request where id=request_id;
	
	if (service_status='ONGOING' AND assigned_driver IS NULL) THEN
		SELECT "TRUE" AS result;
	ELSE
		SELECT "FALSE" AS result; 
	END IF;
	
end$
delimiter ;