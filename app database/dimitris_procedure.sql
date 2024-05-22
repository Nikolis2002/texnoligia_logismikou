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

create procedure taxiReservation(in payment_customer_username VARCHAR(32),in payment_method ENUM,
in service_creation_date DATETIME,in taxiReq_pickup_location POINT,in taxiReq_destination POINT,
out payment_id int,out service_id int,out taxi_request_id int)
begin
	
	insert into payment values(null,payment_customer_username,null,payment_method);
	set payment_id=LAST_INSERT_ID();
	
	insert into service(null,service_creation_date,payment_id,'ONGOING',null);
	set service_id=LAST_INSERT_ID();
	
	insert into taxi_request values(null,taxiReq_pickup_location,taxiReq_destination,null,null,null);
	set taxi_request_id=LAST_INSERT_ID();
	
	insert into taxi_service(service_id,taxi_request_id,null);
end$

delimiter ;