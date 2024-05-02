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

CREATE TABLE Taxi_driver
(
    id INT UNSIGNED NOT NULL,

    CONSTRAINT fk_taxi_driver_id 
        FOREIGN KEY(id) REFERENCES user(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE

);


CREATE TABLE Wallet
(
    id INT UNSIGNED NOT NULL,
    user_id INT UNSIGNED NOT NULL,
    balance DECIMAL(10, 2) NOT NULL,

    CONSTRAINT userId
        FOREIGN KEY(user_id) REFERENCES user(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE

    PRIMARY KEY(id)
);

CREATE table Card
(
    wallet_id INT UNSIGNED NOT NULL,
    card_number STRING NOT NULL,
    card_holder STRING NOT NULL,
    expiration_date STRING NOT NULL,
    cvv STRING NOT NULL,
    card_type STRING NOT NULL

    CONSTRAINT walletId
        FOREIGN KEY(wallet_id) REFERENCES Wallet(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE

    PRIMARY KEY(wallet_id)
);

CREATE TABLE Coupon
(
    id STRING NOT NULL,
    value DECIMAL(10, 2) NOT NULL,

    PRIMARY KEY(id)
);

CREATE TABLE Customer_Coupon
(
    coupon_id STRING NOT NULL,
    customer_id INT UNSIGNED NOT NULL,
    assignment_date DATETIME,

    CONSTRAINT couponId
        FOREIGN KEY(coupon_id) REFERENCES Coupon(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE

    CONSTRAINT customerId
        FOREIGN KEY(customer_id) REFERENCES customer(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE

);

CREATE TABLE Payment
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    customer_id INT UNSIGNED NOT NULL,
    payment_value DECIMAL(10, 2) NOT NULL,
    payment_method ENUM('Wallet','Cash')

    CONSTRAINT customersPaymentId
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
    refill_date DATETIME 

    PRIMARY

    FOREIGN KEY(rating) REFERENCES rental_rating(id)
    ON UPDATE CASCADE
    ON DELETE SET NULL
)

CREATE TABLE out_city_service
(
    rating_id INT UNSIGNED NOT NULL,

    FOREIGN KEY(rating_id) REFERENCES rental_rating(id)
    ON UPDATE CASCADE
    ON DELETE SET NULL
)

CREATE TABLE taxi_request
(
    service_id INT NOT NULL AUTO_INCREMENT,
    pickup_location POINT NOT NULL,
    destination POINT NOT NULL,
    assigned_driver INT UNSIGNED,
    assignment_time DATETIME, -- Originally NULL, until a driver is assigned
    pickup_time DATETIME, -- Originally NULL, until the driver picks up the customer
    rating_id INT UNSIGNED NOT NULL,

    PRIMARY KEY(service_id)

    CONSTRAINT fk_taxi_request_service
    FOREIGN KEY(service_id) REFERENCES service(id)
    ON UPDATE CASCADE,
    ON DELETE CASCADE

    CONSTRAINT fk_taxi_request_driver
    FOREIGN KEY(assigned_driver) REFERENCES taxi_driver(id)
    ON UPDATE CASCADE,
    ON DELETE CASCADE

    CONSTRAINT fk_taxi_request_rating
    FOREIGN KEY(rating_id) REFERENCES taxi_rating(id)
    ON UPDATE CASCADE,
    ON DELETE CASCADE
)