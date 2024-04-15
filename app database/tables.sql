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


