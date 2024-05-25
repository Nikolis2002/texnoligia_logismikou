USE app_database;

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
	
	insert into service values(null,service_creation_date,payment_id,'ONGOING',null, 0);
	set service_id=LAST_INSERT_ID();
	
	insert into taxi_request values(null,taxiReq_pickup_location,taxiReq_destination,null,null,null);
	set taxi_request_id=LAST_INSERT_ID();
	
	insert into taxi_service values(service_id,taxi_request_id,null);
    
end$
delimiter ;

DROP PROCEDURE IF EXISTS resumeService;
delimiter $
create procedure resumeService(in service_id int)
begin
	update service set service_status="ONGOING" where id=service_id;
    update service set status_date=now() where id=service_id;
end$
delimiter ;

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

DROP PROCEDURE IF EXISTS checkTaxiReservationSecond;
delimiter $
create procedure checkTaxiReservationSecond(in service_id_check int)
begin
	declare status_check VARCHAR(32);
	
	select service_status into status from service where id=service_id_check;

	
	if service_status='CANCELLED' THEN
		SELECT "FALSE" AS result;	
	ELSE
		SELECT "TRUE" AS result;
	END IF;

	
end$
delimiter ;

drop procedure if exists acceptTaxiRequest;
delimiter $
create procedure acceptTaxiRequest(in request_id int, in driver_username VARCHAR(32))
begin
	
	update taxi_request set assigned_driver=driver_username, assignment_time=now() where id=request_id;

end$
delimiter ;

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

DROP PROCEDURE IF EXISTS checkTaxiReservationSecond;
delimiter $
create procedure checkTaxiReservationSecond(in service_id_check int)
begin
	declare status_check ENUM('ONGOING', 'COMPLETED', 'CANCELLED');
	
	select service_status into status_check from service where id=service_id_check;

	
	if service_status='CANCELLED' THEN
		SELECT "FALSE" AS result;	
	ELSE
		SELECT "TRUE" AS result;
	END IF;

	
end$
delimiter ;

drop procedure if exists completeTaxiRequest;
delimiter $
create procedure completeTaxiRequest(in taxi_req_id int,in payment_method ENUM('WALLET','CASH'),in payment_value DECIMAL(10,2))
begin
	declare service_id_update int;
	declare username VARCHAR(32);
	
	select service_id into service_id_update from taxi_service where request_id=taxi_req_id;
	select customer_username into username from customer_history where service_id=service_id_update;
	update service set service_status='COMPLETED' where id=service_id_update;
	insert into payment values(null,username,payment_value,payment_method);
	
end$
delimiter ;

drop procedure if exists getPayment;
delimiter $
create procedure getPayment(in service_id int)
begin
	declare pay_id int;
	
	select payment_id into pay_id from service where id=service_id;
	select payment_value,payment_method from payment where id=pay_id;
	
end$
delimiter ;

drop procedure if exists updatePickUpRequest;
delimiter $
create procedure updatePickUpRequest(in taxi_req_id int)
begin
	update taxi_request set pickup_time=now() where id=taxi_req_id;
end$
delimiter ;

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