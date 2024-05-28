USE app_database;

DELETE FROM gas_station;
DELETE FROM taxi_service;
DELETE FROM taxi_request;
DELETE FROM out_city_service;
DELETE FROM rental_service;
DELETE FROM rental_rating;
DELETE FROM customer_history;
DELETE FROM service;
DELETE FROM payment;
DELETE FROM out_city_van;
DELETE FROM out_city_car;
DELETE FROM out_city_transport;
DELETE FROM garage;
DELETE FROM electric_scooter;
DELETE FROM bicycle;
DELETE FROM motorcycle;
DELETE FROM car;
DELETE FROM rental;
DELETE FROM card;
DELETE FROM wallet;
DELETE FROM taxi_driver;
DELETE FROM taxi;
DELETE FROM transport;
DELETE FROM bank;
DELETE FROM customer;
DELETE FROM user;

INSERT INTO user VALUES
    ("a", "a", "Name", "Lastname", "myemail@gmail.com", "6970101010"),
    ("customer", "password", "Customer", "Customer", "customer@gmail.com", "6970000000"),
    ("driver", "password", "Driver", "Driver", "driver@gmail.com", "6970000001"),
    ("pipis", "password", "Mitsos", "Pippis", "pipis@gmail.com", "6970000002"),
    ("souvlakigamer2024", "password", "Babis", "Valtas", "souvlakigamer@gmail.com", "6970000003"),
    ("magic17", "password", "Nikos", "Hatzigeorgokostomanolopoulos", "magic@gmail.com", "6970000004"),
    ("FIRE_IT_UP", "password", "Kostas", "Kostopoulos", "fireitup@hotmail.com", "6970000005"),
    
    ("bill","123","Vasilis","Kourtakis","test@gmail.com","6911234567"),
    ("bill2","1234","Vasilis2","Kourtakis2","test2@gmail.com","6911234567"),
    ("bill3","1235","Vasilis","Kourtakis","test@gmail.com","6911234567"),
    ("Dista","dim123","Dimitris","Stasinos","dimitris@gmail.com","6981472583"),
    ("Nikolis","nik789","Nikolaos","Andrianopoulos","nikolis@gmail.com","6987894561"),
    ("Zoukos","zouk741","Panagiotis","Kalozoumis","panos@gmail.com","6988521346"),
    ("bill4","12356","Vasilis","Kourtakis","test@gmail2.com","6911234567");

INSERT INTO customer VALUES
    ("a", "BOTH", NULL, 1),
    ("customer", "BOTH", NULL, 16),
    ("pipis", "BOTH", NULL, 1997),
    ("souvlakigamer2024", "MOTORCYCLE", NULL, 64),
    ("magic17", "CAR", NULL, 128),

    ("bill","BOTH","test",0),
    ("bill3","BOTH","test",0),
    ("Dista","BOTH",null,0),
    ("Nikolis","BOTH",null,0),
    ("Zoukos","BOTH",null,0),
    ("bill4","BOTH","test",0);

INSERT INTO bank VALUES("kort","123","123","086",500);

INSERT INTO transport VALUES
    (1, "Ford", "Mondeo", 1993),
    (2, "Honda", "Civic", 2006),
    (3, "Pontiac", "Aztek", 2004),
    (4, "Suzuki", "Esteem", 1998),

    (5, "Ford", "Focus", 2018),
    (6, "Volvo", "V90", 2016),

    (7, "Yamaha", "MT-09", 2021),
    (8, "Honda", "CB750", 2003),
    (9, "BMW", "Motorrad R1250GS", 2019),

    (10, "Fuji", "SL", 2023),
    (11, "Mongoose", "Legion BMX L20", 2023),
    (12, "Scott", "Addict RC 40", 2023),

    (13, "Segway", "Ninebot MAX", 2022),
    (14, "Xiaomi", "Mi Electric Scooter Pro 2", 2023),
    (15, "GoTrax", "GXL V2", 2022),

    (16, "Tesla", "Model S", 2023),
    (17, "Mercedes-Benz", "S-Class", 2022),
    (18, "Toyota", "Corolla", 2022),

    (19, "Ford", "Transit", 2024),
    (20, "Mercedes-Benz", "Transporter", 2023),

    (21,"Ford","Mondeo","2007");

INSERT INTO taxi VALUES
    (5, "TAX-1234", POINT(38.2406297, 21.7409815)),
    (6, "TAX-5678", POINT(38.2406297, 21.7409815)),

    (21,"1234",ST_GeomFromText('POINT(1.23 4.56)'));

INSERT INTO taxi_driver VALUES
    ("driver", 5, "TRUE"),
    ("FIRE_IT_UP", 6, "TRUE"),

    ("bill2",21,"TRUE");
    
INSERT INTO wallet VALUES
    ("a", "57.82"),
    ("customer", "22.64"),
    ("driver", "251.31"),

    ("bill",100),
    ("bill2",100),
    ("bill3",50),
    ("Dista",10),
    ("Nikolis",20),
    ("Zoukos",10),
    ("bill4",50);

INSERT INTO card VALUES
    ("customer", "1234 5678 1234 5678", "CUSTOMER CUSTOMER", "12/27", "999", "DEBIT"),
    ("customer", "5678 1234 5678 1232", "CUSTOMER CUSTOMER", "12/27", "222", "DEBIT"),
    ("driver", "5678 1234 5678 1234", "DRIVER DRIVER", "12/27", "111", "DEBIT"),

    ("bill","123","kort","123","086","CREDIT"),
    ("bill","124","kort2","1234","0862","CREDIT"),
    ("bill2","1235","kort","123","086","CREDIT"),
    ("bill2","1246","kort2","1234","0862","CREDIT"),
    ("Dista","456","dimitris","123","086","CREDIT"),
    ("Nikolis","458","nikolaos","123","086","CREDIT"),
    ("Zoukos","125","panagiotis","123","086","CREDIT");

INSERT INTO rental VALUES
    (1, 1.40, POINT(38.2442870,21.7326153), "TRUE"),
    (2, 1.30, POINT(38.2466208,21.7325087), "TRUE"),
    (3, 1.20, POINT(38.2481327,21.7374738), "TRUE"),
    (4, 1.00, POINT(38.2442388,21.7405935), "TRUE"),

    (7, 1.40, POINT(38.2442870,21.7326153), "TRUE"),
    (8, 1.30, POINT(38.2466208,21.7325087), "TRUE"),
    (9, 1.20, POINT(38.2481327,21.7374738), "TRUE"),

    (10, 1.40, POINT(38.2442870,21.7326153), "TRUE"),
    (11, 1.30, POINT(38.2466208,21.7325087), "TRUE"),
    (12, 1.20, POINT(38.2481327,21.7374738), "TRUE"),

    (13, 1.40, POINT(38.2442870,21.7326153), "TRUE"),
    (14, 1.30, POINT(38.2466208,21.7325087), "TRUE"),
    (15, 1.20, POINT(38.2481327,21.7374738), "TRUE");

INSERT INTO car VALUES
    (1, "ABC-1234", 30, 4),
    (2, "DEF-5678", 25, 4),
    (3, "GHI-9012", 40, 4),
    (4, "JKL-3456", 50, 4);

INSERT INTO motorcycle VALUES
    (7, "MNO-7890", 10),
    (8, "PQR-1234", 7),
    (9, "STU-5678", 8);

INSERT INTO bicycle VALUES
    (10),
    (11),
    (12);

INSERT INTO electric_scooter VALUES
    (13),
    (14),
    (15);

INSERT INTO garage VALUES
    (1, "Kort Rentals", POINT(38.2442870,21.7326153), "Mitsou 17", "Mon-Fri 08:00-20:00"),
    (2, "TurboRide Rentals", POINT(38.2466208,21.7325087), "Dista 1", "Mon-Fri 10:00-19:00"),
    (3, "Movfast Official", POINT(38.2481327,21.7374738), "Thiseos 64", "Mon-Sat 09:00-17:00"),
    (4, "GearShift Garage", POINT(38.2442388,21.7405935), "Meg. Alexandrou 32", "Mon-Fri 08:00-20:00"),
    (5, "AutoHaven", POINT(38.2442388,21.7405935), "Ikarou 99", "Mon-Fri 08:00-20:00");

INSERT INTO out_city_transport VALUES
    (16, "VWX-9012", 4, 50, 1, "TRUE", 40),
    (17,"YZA-3456", 4, 64, 1, "TRUE", 50),
    (18, "BCD-7890", 4, 60, 1, "TRUE", 55),

    (19, "EFG-1234", 15, 70, 1, "TRUE", 80),
    (20,"HIJ-5678", 12, 80, 1, "TRUE", 90);

INSERT INTO out_city_car VALUES
    (16),
    (17),
    (18);

INSERT INTO out_city_van VALUES
    (19),
    (20);

INSERT INTO payment VALUES
    (1, 'a', '40.00', 'WALLET'),
    (2, 'a', '15.76', 'WALLET'),
    (3, 'a', '14.28', 'CASH'),
    (4, 'a', '23.06', 'WALLET'),

    (5,'bill', 50.00, 'WALLET'),
    (6,'bill3', 30.50, 'CASH'),
    (7,'bill4', 30.50, 'CASH');

INSERT INTO service VALUES
    (1, '2024-05-24 20:21:26', 1, 'COMPLETED', '2024-05-24 21:21:26', 30),
    (2, '2023-02-04 18:07:14', 2, 'COMPLETED', '2023-02-04 19:07:14', 20),
    (3, '2024-05-11 19:15:59', 3, 'COMPLETED', '2024-05-11 20:15:59', 45),
    (4, '2024-04-13 19:36:52', 4, 'COMPLETED', '2024-04-13 20:36:52', 25),

    (5, CURRENT_TIMESTAMP, 5, 'ONGOING', NULL, 0),
    (6, CURRENT_TIMESTAMP, 6, 'ONGOING', NULL, 0),
    (7, CURRENT_TIMESTAMP, 7, 'ONGOING', NULL, 0);

INSERT INTO customer_history VALUES
    ("a", 1),
    ("a", 2),
    ("a", 3),
    ("a", 4);

INSERT INTO rental_rating VALUES
    (1, 1, "an mporousa na balo 0 asteria tha to ekana!! pragmatika den exo ksanadei xeirotero autokinhto sti zoi mou. kat arxas to vrikame xoris katholoy venzini, kyriolektika mas emeine ston dromo. LIGO ELEOS DHLADH, DEN MPOREITE KAN NA GEMIZETE TA OXHMATA SAS?? epipleon afou to gemisame, pali den empaine bros ekane enan ixo 'ASIIII' kai meta esbhnea amesos. gia na mhn anafero thn katastasi toy esoterikou. akoma prospathoyme na katalaboume ti htan ekeinoi oi lekedes sta piso kathismata... genika exo pei se olous moy tous gnostous na min kleinoun amaxia apo authn thn efarmogh, oxi tipota allo mhn kollhsoume kai tipota. POTE KSANA!!! movfast, more like moufa!!!");

INSERT INTO rental_service VALUES
    (1, 7, null, null, null, null, null, null, null),
    (4, 1, null, null, 1, null, null, null, null);

INSERT INTO out_city_service VALUES
    (2, 19, null, 3);

INSERT INTO taxi_request VALUES
    (1, POINT(38.2442870,21.7326153), POINT(38.2466208,21.7325087), "FIRE_IT_UP", '2024-05-24 21:30:22', '2024-05-24 21:30:39'),

    (2, ST_GeomFromText('POINT(40.7128 -74.0060)'), ST_GeomFromText('POINT(34.0522 -118.2437)'), 'bill2', '2024-05-20 08:30:00', NULL),
    (3, ST_GeomFromText('POINT(37.7749 -122.4194)'), ST_GeomFromText('POINT(36.1699 -115.1398)'), NULL, NULL, NULL),
    (4, ST_GeomFromText('POINT(37.7749 -122.4194)'), ST_GeomFromText('POINT(36.1699 -115.1398)'), NULL, NULL, NULL);

INSERT INTO taxi_service VALUES
    (3, 1, null),

    (5, 2, NULL),
    (6, 3, NULL),
    (7, 4, NULL);

INSERT INTO gas_station VALUES
    (NULL, ST_GeomFromText('POINT(38.256422 21.743256)'), 3.2),
    (NULL, ST_GeomFromText('POINT(38.262861 21.751035)'), 1.2),
    (NULL, ST_GeomFromText('POINT(38.269737 21.746496)'), 3.7),
    (NULL, ST_GeomFromText('POINT(38.278801 21.766688)'), 3.7),
    (NULL, ST_GeomFromText('POINT(38.255465 21.747013)'), 4.5),
    (NULL, ST_GeomFromText('POINT(38.243420 21.754333)'), 6.6),
    (NULL, ST_GeomFromText('POINT(38.234517 21.746291)'), 7.9);
