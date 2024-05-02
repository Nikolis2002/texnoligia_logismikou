DROP DATABASE IF EXISTS app_database
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
    points INT UNSIGNED NOT NULL,

    CONSTRAINT fk_customer_id 
        FOREIGN KEY(id) REFERENCES user(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE rental_rating
(
    id INT UNSIGNED AUTO_INCREMENT NOT NULL,
    stars TINYINT NOT NULL,
    comment TEXT,

    PRIMARY KEY(id)
)

CREATE TABLE taxi_rating
(
    id INT UNSIGNED AUTO_INCREMENT NOT NULL,
    taxi_stars TINYINT NOT NULL,
    driver_stars TINYINT NOT NULL,
    comment TEXT,

    PRIMARY KEY(id)
)

CREATE TABLE out_city_rating
(
    id INT UNSIGNED AUTO_INCREMENT NOT NULL,
    vehicle_stars TINYINT NOT NULL,
    garage_stars TINYINT NOT NULL,
    comment TEXT,

    PRIMARY KEY(id)
)

CREATE TABLE service
(
    id INT NOT NULL AUTO_INCREMENT,
    creation_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    payment_id INT,
    service_status ENUM('ONGOING', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'ONGOING',
    status_date DATETIME, -- Cancellation or completion date. Originally NULL,

    PRIMARY KEY(id)

    CONSTRAINT fk_service_payment
    FOREIGN KEY(payment_id) REFERENCES payment(id)
    ON UPDATE CASCADE,
    ON DELETE SET NULL
)

CREATE TABLE rental_service
(
    rating_id INT UNSIGNED NOT NULL,

    FOREIGN KEY(rating) REFERENCES rating(id)
    ON UPDATE CASCADE
    ON DELETE SET NULL
)

CREATE TABLE out_city_reservation
(

)

CREATE TABLE taxi_request
(
    service_id INT NOT NULL AUTO_INCREMENT,
    pickup POINT NOT NULL,
    destination POINT NOT NULL,
    assigned_driver INT UNSIGNED,
    assignment_time DATETIME, -- Originally NULL, until a driver is assigned
    pickup_time DATETIME, -- Originally NULL, until the driver picks up the customer

    CONSTRAINT fk_taxi_request_service
    FOREIGN KEY(service_id) REFERENCES service(id)
    ON UPDATE CASCADE,
    ON DELETE CASCADE

    CONSTRAINT fk_taxi_request_driver
    FOREIGN KEY(service_id) REFERENCES service(id)
    ON UPDATE CASCADE,
    ON DELETE CASCADE
)