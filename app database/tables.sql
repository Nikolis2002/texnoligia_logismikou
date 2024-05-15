
DROP DATABASE IF EXISTS app_database;
CREATE DATABASE app_database;
USE app_database;

CREATE TABLE user
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    username VARCHAR(32) NOT NULL,
    name VARCHAR(32) NOT NULL,
    lname VARCHAR(32) NOT NULL,
    email VARCHAR(32) NOT NULL,

    PRIMARY KEY(id)
);


CREATE TABLE customer
(
    id INT UNSIGNED NOT NULL,
    license VARCHAR(32),
    license_image LONGBLOB,
    points INT UNSIGNED NOT NULL,

    CONSTRAINT fk_customer_id 
        FOREIGN KEY(id) REFERENCES user(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE payment
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    customer_id INT UNSIGNED NOT NULL,
    payment_value DECIMAL(10, 2) NOT NULL,
    payment_method ENUM('WALLET','CASH'),

    PRIMARY KEY(id),

    CONSTRAINT customersPaymentId
        FOREIGN KEY(customer_id) REFERENCES customer(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE

);

CREATE TABLE service
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    creation_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    payment_id INT UNSIGNED,
    service_status ENUM('ONGOING', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'ONGOING',
    status_date DATETIME, -- Cancellation or completion date. Originally NULL

    PRIMARY KEY(id),

    CONSTRAINT fk_service_payment
    FOREIGN KEY(payment_id) REFERENCES payment(id)
    ON UPDATE CASCADE
    ON DELETE SET NULL
);

CREATE TABLE customer_history
(
    customer_id INT UNSIGNED NOT NULL,
    service_id INT UNSIGNED NOT NULL,
    
    CONSTRAINT fk_customer_history_id 
        FOREIGN KEY(customer_id) REFERENCES customer(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_service_id 
        FOREIGN KEY(service_id) REFERENCES service(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE

);

CREATE TABLE transport
(
    id INT UNSIGNED NOT NULL,
    model VARCHAR(32) NOT NULL,
    manuf_year YEAR NOT NULL,
    manufacturer VARCHAR(32) NOT NULL,

    PRIMARY KEY(id)
);

CREATE TABLE taxi
(
    id INT UNSIGNED NOT NULL,
    license_plate VARCHAR(32) NOT NULL,
    coords POINT NOT NULL,

    PRIMARY KEY(id),

    CONSTRAINT fk_taxi_transport
    FOREIGN KEY(id) REFERENCES transport(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE rental
(
    id INT UNSIGNED NOT NULL,
    rate DECIMAL(5, 2) NOT NULL,
    coords POINT NOT NULL,
    free_status BOOLEAN NOT NULL,

    PRIMARY KEY(id),

    CONSTRAINT fk_rental_transport
    FOREIGN KEY(id) REFERENCES transport(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE car
(
    id INT UNSIGNED NOT NULL,
    license_plate VARCHAR(32) NOT NULL,
    gas_level INT UNSIGNED NOT NULL,
    seat_capacity INT UNSIGNED NOT NULL,

    PRIMARY KEY(id),

    CONSTRAINT fk_car_rental
    FOREIGN KEY(id) REFERENCES rental(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE motorcycle
(
    id INT UNSIGNED NOT NULL,
    license_plate VARCHAR(32) NOT NULL,
    gas_level INT UNSIGNED NOT NULL,

    PRIMARY KEY(id),

    CONSTRAINT fk_motorcycle_rental
    FOREIGN KEY(id) REFERENCES rental(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE bicycle
(
    id INT UNSIGNED NOT NULL,

    PRIMARY KEY(id),

    CONSTRAINT fk_bicycle_rental
    FOREIGN KEY(id) REFERENCES rental(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE electric_scooter
(
    id INT UNSIGNED NOT NULL,

    PRIMARY KEY(id),

    CONSTRAINT fk_electric_scooter_rental
    FOREIGN KEY(id) REFERENCES rental(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE taxi_driver
(
    id INT UNSIGNED NOT NULL,
    taxi INT UNSIGNED NOT NULL,
    free_status BOOLEAN NOT NULL,

    PRIMARY KEY(id),

    CONSTRAINT fk_taxi_driver_id 
        FOREIGN KEY(id) REFERENCES user(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_taxi_driver_taxi
    FOREIGN KEY(taxi) REFERENCES taxi(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);


CREATE TABLE wallet
(
    id INT UNSIGNED NOT NULL,
    user_id INT UNSIGNED NOT NULL,
    balance DECIMAL(10, 2) NOT NULL,

    PRIMARY KEY(id),

    CONSTRAINT fk_wallet_user
        FOREIGN KEY(user_id) REFERENCES user(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE  
);

CREATE table card
(
    wallet_id INT UNSIGNED NOT NULL,
    card_number VARCHAR(32) NOT NULL,
    card_holder VARCHAR(32) NOT NULL,
    expiration_date VARCHAR(32) NOT NULL,
    cvv VARCHAR(32) NOT NULL,
    card_type VARCHAR(32) NOT NULL,

    PRIMARY KEY(card_number),

    CONSTRAINT fk_card_wallet
        FOREIGN KEY(wallet_id) REFERENCES wallet(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE coupon
(
    id VARCHAR(32) NOT NULL,
    value DECIMAL(10, 2) NOT NULL,

    PRIMARY KEY(id)
);

CREATE TABLE customer_coupon
(
    coupon_id VARCHAR(32) NOT NULL,
    customer_id INT UNSIGNED NOT NULL,
    assignment_date DATETIME,

    PRIMARY KEY(coupon_id, customer_id),

    CONSTRAINT couponId
        FOREIGN KEY(coupon_id) REFERENCES coupon(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT customerId
        FOREIGN KEY(customer_id) REFERENCES customer(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE rental_rating
(
    id INT UNSIGNED AUTO_INCREMENT NOT NULL,
    stars TINYINT NOT NULL,
    comment TEXT,

    PRIMARY KEY(id)
);

CREATE TABLE taxi_rating
(
    id INT UNSIGNED AUTO_INCREMENT NOT NULL,
    taxi_stars TINYINT NOT NULL,
    driver_stars TINYINT NOT NULL,
    comment TEXT,

    PRIMARY KEY(id)
);

CREATE TABLE out_city_rating
(
    id INT UNSIGNED AUTO_INCREMENT NOT NULL,
    vehicle_stars TINYINT NOT NULL,
    garage_stars TINYINT NOT NULL,
    comment TEXT,

    PRIMARY KEY(id)
);

CREATE TABLE gas_station
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    coords POINT NOT NULL,
    gas_price DECIMAL(5,2) NOT NULL,

    PRIMARY KEY(id)
);

CREATE TABLE refill
(
    date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    station_id INT UNSIGNED NOT NULL,
    initial_gas_quantity INT UNSIGNED NOT NULL,
    added_gas_quantity INT UNSIGNED NOT NULL DEFAULT 0,
    success BOOLEAN NOT NULL, -- If tracker fails after refill, mark this refill session as incomplete. Return money to customer at a later date

    PRIMARY KEY(date),

    CONSTRAINT fk_refill_gas_station
    FOREIGN KEY(station_id) REFERENCES gas_station(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE rental_service
(
    service_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    selected_vehicle INT UNSIGNED NOT NULL,
    refill_date DATETIME, -- NULL, unless the customer refills the vehicle
    rating_id INT UNSIGNED,
    left_side_img LONGBLOB,
    right_side_img LONGBLOB,
    front_side_img LONGBLOB,
    back_side_img LONGBLOB,

    PRIMARY KEY(service_id),

    CONSTRAINT fk_rental_service
    FOREIGN KEY(service_id) REFERENCES service(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

    CONSTRAINT fk_rental_vehicle
    FOREIGN KEY(selected_vehicle) REFERENCES rental(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

    CONSTRAINT fk_rental_rating
    FOREIGN KEY(rating_id) REFERENCES rental_rating(id)
    ON UPDATE CASCADE
    ON DELETE SET NULL,

    CONSTRAINT fk_rental_refill
    FOREIGN KEY(refill_date) REFERENCES refill(date)
    ON UPDATE CASCADE
    ON DELETE SET NULL
);

CREATE TABLE out_city_transport
(
    id INT UNSIGNED NOT NULL,
    out_city_license VARCHAR(32) NOT NULL,
    seat_capacity INT UNSIGNED NOT NULL,
    gas INT UNSIGNED NOT NULL,
    free_status BOOLEAN NOT NULL,
    
    CONSTRAINT fk_out_city_transport 
    FOREIGN KEY(id) REFERENCES transport(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    
    PRIMARY KEY(id)
);

CREATE TABLE garage
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL,
    coords POINT NOT NULL,
    address VARCHAR(64) NOT NULL,
    available_hours TEXT NOT NULL,

    PRIMARY KEY(id)
);

CREATE TABLE out_city_service
(
    service_id INT UNSIGNED NOT NULL,
    vehicle_id INT UNSIGNED NOT NULL,
    rating_id INT UNSIGNED,

    PRIMARY KEY(service_id),

    CONSTRAINT fk_out_city_rating
    FOREIGN KEY(rating_id) REFERENCES out_city_rating(id)
    ON UPDATE CASCADE
    ON DELETE SET NULL,

    CONSTRAINT fk_out_city_vehicle
    FOREIGN KEY(vehicle_id) REFERENCES out_city_transport(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE taxi_request
(
    service_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    pickup_location POINT NOT NULL,
    destination POINT NOT NULL,
    assigned_driver INT UNSIGNED,
    assignment_time DATETIME, -- Originally NULL, until a driver is assigned
    pickup_time DATETIME, -- Originally NULL, until the driver picks up the customer
    rating_id INT UNSIGNED,

    PRIMARY KEY(service_id),

    CONSTRAINT fk_taxi_request_service
    FOREIGN KEY(service_id) REFERENCES service(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

    CONSTRAINT fk_taxi_request_driver
    FOREIGN KEY(assigned_driver) REFERENCES taxi_driver(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

    CONSTRAINT fk_taxi_request_rating
    FOREIGN KEY(rating_id) REFERENCES taxi_rating(id)
    ON UPDATE CASCADE
    ON DELETE SET NULL
);

CREATE TABLE out_city_car
(
    id INT UNSIGNED NOT NULL,
    out_city_license VARCHAR(32) NOT NULL,

    CONSTRAINT fk_out_city_car
    FOREIGN KEY(id) REFERENCES out_city_transport(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

    PRIMARY KEY(id)
);

CREATE TABLE out_city_van
(
    id INT UNSIGNED NOT NULL,

    CONSTRAINT fk_out_city_van
    FOREIGN KEY(id) REFERENCES out_city_transport(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

    PRIMARY KEY(id)
);

CREATE TABLE garage_vehicle
(
    garage_id INT UNSIGNED NOT NULL,
    vehicle_id INT UNSIGNED NOT NULL,

    PRIMARY KEY(garage_id, vehicle_id),

    CONSTRAINT fk_garage_vehicle_garage
    FOREIGN KEY(garage_id) REFERENCES garage(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

    CONSTRAINT fk_garage_vehicle_vehicle
    FOREIGN KEY(garage_id) REFERENCES out_city_transport(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

-- //////////////////////BANK MOCK


DROP TABLE IF EXISTS bank;
CREATE TABLE bank(
    id INT UNSIGNED NOT NULL,
    card_owner VARCHAR(64) NOT NULL,
    card_number VARCHAR(32) NOT NULL,
    card_exp_date VARCHAR(10) NOT NULL,
    cvv VARCHAR(8) NOT NULL,
    owner_balance INT NOT NULL default '500',
    PRIMARY KEY(id,card_number),
    CONSTRAINT fk_customer_bank_id
        FOREIGN KEY(id) REFERENCES customer(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- ////////////////////////////////////////////////////////////////////////////////////END////////////////////////////////////////////////////////////////////////////

INSERT INTO user VALUES(NULL,"bill","Vasilis","Kourtakis","test@gmail.com");
INSERT INTO customer VALUES(1,"A2","test",0);
INSERT INTO bank VALUES(1,"VASILIS KOURTAKIS","1234567891011123","05/26","888",1000);
CALL checkCredentials("VASILIS KOURTAKIS","1234567891011123","05/26","888",1000,@res);
SELECT @res;
