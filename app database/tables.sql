
DROP DATABASE IF EXISTS app_database;
CREATE DATABASE app_database;
USE app_database;

CREATE TABLE user
(
    username VARCHAR(32) NOT NULL UNIQUE,
    password VARCHAR(32) NOT NULL,
    name VARCHAR(32) NOT NULL,
    lname VARCHAR(32) NOT NULL,
    email VARCHAR(32) NOT NULL,
    phone VARCHAR(32) NOT NULL,

    PRIMARY KEY(username)
);


CREATE TABLE customer
(
    username VARCHAR(32) NOT NULL UNIQUE,
    license VARCHAR(32),
    license_image LONGBLOB,
    points INT UNSIGNED NOT NULL,
    
    PRIMARY KEY(username),
    
    CONSTRAINT fk_customer_username
        FOREIGN KEY(username) REFERENCES user(username)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE payment
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    customer_username VARCHAR(32) NOT NULL ,
    payment_value DECIMAL(10, 2),
    payment_method ENUM('WALLET','CASH') NOT NULL,

    PRIMARY KEY(id),

    CONSTRAINT customersPaymentId
        FOREIGN KEY(customer_username) REFERENCES customer(username)
        ON UPDATE CASCADE
        ON DELETE CASCADE

);

CREATE TABLE service
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    creation_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    payment_id  INT UNSIGNED,
    service_status ENUM('ONGOING', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'ONGOING',
    status_date DATETIME, -- Cancellation or completion date. Originally NULL
    earned_points INT UNSIGNED NOT NULL DEFAULT 0,

    PRIMARY KEY(id),

    CONSTRAINT fk_service_payment
    FOREIGN KEY(payment_id) REFERENCES payment(id)
    ON UPDATE CASCADE
    ON DELETE SET NULL
);

CREATE TABLE customer_history
(
    customer_username VARCHAR(32) NOT NULL,
    service_id INT UNSIGNED NOT NULL,
    
    CONSTRAINT fk_customer_history_id 
        FOREIGN KEY(customer_username) REFERENCES customer(username)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_service_id 
        FOREIGN KEY(service_id) REFERENCES service(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE

);

CREATE TABLE transport
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    manufacturer VARCHAR(32) NOT NULL,
    model VARCHAR(32) NOT NULL,
    manuf_year YEAR NOT NULL,

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
    free_status VARCHAR(32) NOT NULL,

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
    username VARCHAR(32) NOT NULL UNIQUE,
    taxi INT UNSIGNED NOT NULL,
    free_status VARCHAR(32) NOT NULL,

    PRIMARY KEY(username),

    CONSTRAINT fk_taxi_driver_id 
        FOREIGN KEY(username) REFERENCES user(username)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_taxi_driver_taxi
    FOREIGN KEY(taxi) REFERENCES taxi(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);


CREATE TABLE wallet
(
    username VARCHAR(32) NOT NULL UNIQUE,
    balance DECIMAL(10, 2) NOT NULL,

    PRIMARY KEY(username),

    CONSTRAINT fk_wallet_user
        FOREIGN KEY(username) REFERENCES user(username)
        ON UPDATE CASCADE
        ON DELETE CASCADE  
);

CREATE table card
(
    username VARCHAR(32) NOT NULL,
    card_number VARCHAR(32) NOT NULL,
    card_holder VARCHAR(32) NOT NULL,
    expiration_date VARCHAR(32) NOT NULL,
    cvv VARCHAR(32) NOT NULL,
    card_type VARCHAR(32) NOT NULL,

    PRIMARY KEY(card_number),

    CONSTRAINT fk_card_wallet
        FOREIGN KEY(username) REFERENCES wallet(username)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE coupon
(
    id VARCHAR(32) NOT NULL,
    name VARCHAR(32) NOT NULL,
    value DECIMAL(10, 2) NOT NULL,
    points INT UNSIGNED NOT NULL,
    expires DATE,

    PRIMARY KEY(id)
);

CREATE TABLE customer_coupon
(
    coupon_id VARCHAR(32) NOT NULL,
    customer_username VARCHAR(32) NOT NULL,
    assignment_date DATETIME,

    PRIMARY KEY(coupon_id, customer_username),

    CONSTRAINT couponId
        FOREIGN KEY(coupon_id) REFERENCES coupon(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT customerId
        FOREIGN KEY(customer_username) REFERENCES customer(username)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE rental_rating
(
    id INT UNSIGNED AUTO_INCREMENT NOT NULL,
    vehicle_stars TINYINT NOT NULL,
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
    success VARCHAR(32) NOT NULL, -- If tracker fails after refill, mark this refill session as incomplete. Return money to customer at a later date

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
	unlock_vehicle DATETIME,
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

CREATE TABLE garage
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL,
    coords POINT NOT NULL,
    address VARCHAR(64) NOT NULL,
    available_hours TEXT NOT NULL, -- A simple string like this: Mon-Fri 08:00-20:00. It's good enough for now

    PRIMARY KEY(id)
);

CREATE TABLE out_city_transport
(
    id INT UNSIGNED NOT NULL,
    out_city_license VARCHAR(32) NOT NULL,
    seat_capacity INT UNSIGNED NOT NULL,
    gas INT UNSIGNED NOT NULL,
    garage INT UNSIGNED NOT NULL,
    free_status VARCHAR(32) NOT NULL,
    rate DECIMAL(5, 2) NOT NULL,

    PRIMARY KEY(id),
    
    CONSTRAINT fk_out_city_transport_transport
    FOREIGN KEY(id) REFERENCES transport(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

    CONSTRAINT fk_out_city_transport_garage
    FOREIGN KEY(garage) REFERENCES garage(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE out_city_service
(
    service_id INT UNSIGNED NOT NULL,
    vehicle_id INT UNSIGNED NOT NULL,
    rating_id INT UNSIGNED,

    PRIMARY KEY(service_id),

    CONSTRAINT fk_out_city_service_service
    FOREIGN KEY(service_id) REFERENCES service(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

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
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    pickup_location POINT NOT NULL,
    destination POINT NOT NULL,
    assigned_driver VARCHAR(32),
    assignment_time DATETIME, -- Originally NULL, until a driver is assigned
    pickup_time DATETIME, -- Originally NULL, until the driver picks up the customer

    PRIMARY KEY(id),

    CONSTRAINT fk_taxi_request_driver
    FOREIGN KEY(assigned_driver) REFERENCES taxi_driver(username)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE taxi_service
(
    service_id INT UNSIGNED NOT NULL,
    request_id INT UNSIGNED NOT NULL,
    rating_id INT UNSIGNED,

    PRIMARY KEY(service_id),

    CONSTRAINT fk_taxi_service_service
    FOREIGN KEY(service_id) REFERENCES service(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

    CONSTRAINT fk_taxi_service_taxi_request
    FOREIGN KEY(request_id) REFERENCES taxi_request(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,

    CONSTRAINT fk_taxi_service_rating
    FOREIGN KEY(rating_id) REFERENCES taxi_rating(id)
    ON UPDATE CASCADE
    ON DELETE SET NULL
);

CREATE TABLE out_city_car
(
    id INT UNSIGNED NOT NULL,

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

-- //////////////////////BANK MOCK


DROP TABLE IF EXISTS bank;
CREATE TABLE bank(
    card_owner VARCHAR(64) NOT NULL,
    card_number VARCHAR(32) NOT NULL,
    card_exp_date VARCHAR(10) NOT NULL,
    cvv VARCHAR(8) NOT NULL,
    owner_balance INT NOT NULL default '500',
    PRIMARY KEY(card_number)
);

-- ////////////////////////////////////////////////////////////////////////////////////END////////////////////////////////////////////////////////////////////////////



-- Views
-- ==========================================================================================================================

-- Full information for all the free cars
-- Displayed on the rental map

DROP VIEW IF EXISTS rental_cars;

CREATE VIEW rental_cars AS
SELECT license_plate, car.id, manufacturer, model, manuf_year, rate, coords, gas_level
FROM car
INNER JOIN rental ON car.id = rental.id
INNER JOIN transport on car.id = transport.id
WHERE free_status = "TRUE";

-- =========================================================================================================================

-- Full information for all the free motorcycles
-- Displayed on the rental map

DROP VIEW IF EXISTS rental_motorcycles;

CREATE VIEW rental_motorcycles AS
SELECT license_plate, motorcycle.id, manufacturer, model, manuf_year, rate, coords, gas_level
FROM motorcycle
INNER JOIN rental ON motorcycle.id = rental.id
INNER JOIN transport on motorcycle.id = transport.id
WHERE free_status = "TRUE";

-- =========================================================================================================================

-- Full information for all the free bikes
-- Displayed on the rental map

DROP VIEW IF EXISTS rental_bikes;

CREATE VIEW rental_bikes AS
SELECT bicycle.id, manufacturer, model, manuf_year, rate, coords
FROM bicycle
INNER JOIN rental ON bicycle.id = rental.id
INNER JOIN transport on bicycle.id = transport.id
WHERE free_status = "TRUE";

-- =========================================================================================================================

-- Full information for all the free scooters
-- Displayed on the rental map

DROP VIEW IF EXISTS rental_scooters;

CREATE VIEW rental_scooters AS
SELECT electric_scooter.id, manufacturer, model, manuf_year, rate, coords
FROM electric_scooter
INNER JOIN rental ON electric_scooter.id = rental.id
INNER JOIN transport on electric_scooter.id = transport.id
WHERE free_status = "TRUE";

-- =========================================================================================================================

-- All the information for all the out city vehicles, along with their type
-- From here we retrieve the vehicle associated with each out city history entry

DROP VIEW IF EXISTS out_city_vehicles;

CREATE VIEW out_city_vehicles AS
SELECT "van" AS "type", t.id, t.manufacturer, t.model, t.manuf_year, oct.out_city_license AS "license_plate", oct.rate, oct.seat_capacity AS "seats", oct.gas, g.id AS "garage_id", g.name AS "garage_name"
FROM transport t
INNER JOIN out_city_transport oct ON t.id = oct.id
INNER JOIN out_city_van ocv ON ocv.id = oct.id
INNER JOIN garage g ON oct.garage = g.id
UNION
SELECT "car" AS "type", t.id, t.manufacturer, t.model, t.manuf_year, oct.out_city_license AS "license_plate", oct.rate, oct.seat_capacity AS "seats", oct.gas, g.id AS "garage_id", g.name AS "garage_name"
FROM transport t
INNER JOIN out_city_transport oct ON t.id = oct.id
INNER JOIN out_city_car occ ON occ.id = oct.id
INNER JOIN garage g ON oct.garage = g.id;

-- =========================================================================================================================

-- All the information for all the rentals, along with their type
-- From here we retrieve the vehicle associated with each rental history entry

DROP VIEW IF EXISTS rental_vehicles;

CREATE VIEW rental_vehicles AS
SELECT "car" AS "type", license_plate, car.id, manufacturer, model, manuf_year, rate, coords, gas_level
FROM car
INNER JOIN rental ON car.id = rental.id
INNER JOIN transport on car.id = transport.id
UNION
SELECT "motorcycle" AS "type", license_plate, motorcycle.id, manufacturer, model, manuf_year, rate, coords, gas_level
FROM motorcycle
INNER JOIN rental ON motorcycle.id = rental.id
INNER JOIN transport on motorcycle.id = transport.id
UNION
SELECT "bicycle" AS "type", NULL AS "license_plate", bicycle.id, manufacturer, model, manuf_year, rate, coords, NULL AS "gas_level"
FROM bicycle
INNER JOIN rental ON bicycle.id = rental.id
INNER JOIN transport on bicycle.id = transport.id
UNION
SELECT "scooter" AS "type", NULL AS "license_plate", electric_scooter.id, manufacturer, model, manuf_year, rate, coords, NULL AS "gas_level"
FROM electric_scooter
INNER JOIN rental ON electric_scooter.id = rental.id
INNER JOIN transport on electric_scooter.id = transport.id;

-- =========================================================================================================================

-- Full info of each taxi
-- From here we retrieve the vehicle associated with each taxi history entry

DROP VIEW IF EXISTS taxi_vehicles;

CREATE VIEW taxi_vehicles AS
SELECT taxi.id, license_plate, coords, manufacturer, model, manuf_year
FROM taxi
INNER JOIN transport ON taxi.id = transport.id;

-- =========================================================================================================================

-- Rental history

DROP VIEW IF EXISTS rental_history;

CREATE VIEW rental_history AS
SELECT
    "rental" AS "type",
    p.customer_username AS "user",
    s.id, s.creation_date,
    s.status_date AS "completion_date",
    s.earned_points,
    p.payment_value AS "amount",
    p.payment_method,
    rs.selected_vehicle AS "other_id", -- here it is vehicle id
    rs.rating_id
FROM service s
INNER JOIN rental_service rs ON s.id = rs.service_id
INNER JOIN payment p ON s.payment_id = p.id
WHERE s.service_status = "COMPLETED";

-- =========================================================================================================================

-- Taxi history

DROP VIEW IF EXISTS taxi_history;

CREATE VIEW taxi_history AS
SELECT
    "taxi" AS "type",
    p.customer_username AS "user",
    s.id, s.creation_date,
    s.status_date AS "completion_date",
    s.earned_points,
    p.payment_value AS "amount",
    p.payment_method,
    ts.request_id AS "other_id", -- here it is taxi request id
    ts.rating_id
FROM service s
INNER JOIN taxi_service ts ON s.id = ts.service_id
INNER JOIN payment p ON s.payment_id = p.id
WHERE s.service_status = "COMPLETED";

-- =========================================================================================================================

-- Out city history

DROP VIEW IF EXISTS out_city_history;

CREATE VIEW out_city_history AS
SELECT
    "out_city" AS "type",
    p.customer_username AS "user",
    s.id, s.creation_date,
    s.status_date AS "completion_date",
    s.earned_points,
    p.payment_value AS "amount",
    p.payment_method,
    ocs.vehicle_id AS "other_id", -- here it is vehicle id
    ocs.rating_id
FROM service s
INNER JOIN out_city_service ocs ON s.id = ocs.service_id
INNER JOIN payment p ON s.payment_id = p.id
WHERE s.service_status = "COMPLETED";

-- =========================================================================================================================

-- Shows which taxi was used for each request

DROP VIEW IF EXISTS taxi_request_vehicle;

CREATE VIEW taxi_request_vehicle AS
SELECT taxi_request.id AS "request_id", taxi_vehicles.id, license_plate, coords, manufacturer, model, manuf_year
FROM taxi_request
INNER JOIN taxi_driver ON assigned_driver = username
INNER JOIN taxi_vehicles ON taxi_driver.taxi = taxi_vehicles.id;

-- =========================================================================================================================

-- All the services along with their type

DROP VIEW IF EXISTS history;

CREATE VIEW history AS
SELECT *
FROM
(
    SELECT * FROM rental_history
    UNION
    SELECT * FROM taxi_history
    UNION
    SELECT * FROM out_city_history
) t
ORDER BY creation_date DESC;