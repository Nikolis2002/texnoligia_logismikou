USE app_database;

INSERT INTO user VALUES
    ("a", "a", "Name", "Lastname", "myemail@gmail.com", "6970101010"),
    ("customer", "password", "Customer", "Customer", "customer@gmail.com", "6970000000"),
    ("driver", "password", "Driver", "Driver", "driver@gmail.com", "6970000001"),
    ("pipis", "password", "Mitsos", "Pippis", "pipis@gmail.com", "6970000002"),
    ("souvlakigamer2024", "password", "Babis", "Valtas", "souvlakigamer@gmail.com", "6970000003"),
    ("magic17", "password", "Nikos", "Hatzigeorgokostomanolopoulos", "magic@gmail.com", "6970000004"),
    ("FIRE_IT_UP", "password", "Kostas", "Kostopoulos", "fireitup@hotmail.com", "6970000005");


INSERT INTO customer VALUES
    ("a", "BOTH", NULL, 1),
    ("customer", "BOTH", NULL, 16),
    ("pipis", "BOTH", NULL, 1997),
    ("souvlakigamer2024", "MOTORCYCLE", NULL, 64),
    ("magic17", "CAR", NULL, 128);

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
    (20, "Mercedes-Benz", "Transporter", 2023);

INSERT INTO taxi VALUES
    (5, "TAX-1234", POINT(38.2406297, 21.7409815)),
    (6, "TAX-5678", POINT(38.2406297, 21.7409815));

INSERT INTO taxi_driver VALUES
    ("driver", 5, "TRUE"),
    ("FIRE_IT_UP", 6, "TRUE");
    
INSERT INTO wallet VALUES
    ("a", "57.82"),
    ("customer", "22.64"),
    ("driver", "251.31");

INSERT INTO card VALUES
    ("customer", "1234 5678 1234 5678", "CUSTOMER CUSTOMER", "12/27", "999", "DEBIT"),
    ("customer", "5678 1234 5678 1232", "CUSTOMER CUSTOMER", "12/27", "222", "DEBIT"),
    ("driver", "5678 1234 5678 1234", "DRIVER DRIVER", "12/27", "111", "DEBIT");

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
    (4, 'a', '23.06', 'WALLET');

INSERT INTO service VALUES
    (1, '2024-05-24 20:21:26', 1, 'COMPLETED', '2024-05-24 21:21:26', 30),
    (2, '2023-02-04 18:07:14', 2, 'COMPLETED', '2023-02-04 19:07:14', 20),
    (3, '2024-05-11 19:15:59', 3, 'COMPLETED', '2024-05-11 20:15:59', 45),
    (4, '2024-04-13 19:36:52', 4, 'COMPLETED', '2024-04-13 20:36:52', 25);

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
    (1, POINT(38.2442870,21.7326153), POINT(38.2466208,21.7325087), "FIRE_IT_UP", '2024-05-24 21:30:22', '2024-05-24 21:30:39');

INSERT INTO taxi_service VALUES
    (3, 1, null);
