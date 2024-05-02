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



