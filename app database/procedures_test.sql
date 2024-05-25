drop procedure if exists getPayment;
delimiter $
create procedure getPayment(in service_id int)
begin
	declare pay_id int;
	
	select payment_id into pay_id from service where id=service_id;
	select payment_value,payment_method from payment where id=pay_id;
	
end$
delimiter ;
