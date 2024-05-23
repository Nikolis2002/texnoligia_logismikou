INSERT INTO user VALUES
    ("a", "a", "Name", "Lastname", "myemail@gmail.com", "6970101010"),
    ("customer", "password", "Customer", "Customer", "customer@gmail.com", "6970000000"),
    ("driver", "password", "Driver", "Driver", "driver@gmail.com", "6970000001"),
    ("pipis", "password", "Mitsos", "Pippis", "pipis@gmail.com", "6970000002"),
    ("souvlakigamer2024", "password", "Babis", "Valtas", "souvlakigamer@gmail.com", "6970000003"),
    ("magic17", "password", "Nikos", "Hatzigeorgokostomanolopoulos", "magic@gmail.com", "6970000004"),
    ("FIRE_IT_UP", "password", "Kostas", "Kostopoulos", "fireitup@hotmail.com", "6970000005");


INSERT INTO customer VALUES
    ("a", "LICENSE", NULL, 1),
    ("customer", "LICENSE", NULL, 16),
    ("pipis", "LICENSE", NULL, 1997),
    ("souvlakigamer2024", "LICENSE", NULL, 64),
    ("magic17", "LICENSE", NULL, 128);

INSERT INTO transport VALUES
    (1, "Ford", "Mondeo", 1993),
    (2, "Honda", "Civic", 2006),
    (3, "Pontiac", "Aztek", 2004),
    (4, "Suzuki", "Esteem", 1998),
    (5, "Ford", "Focus", 2018);

INSERT INTO taxi VALUES
    (5, "TAX-1234", POINT(38.2406297, 21.7409815));

INSERT INTO taxi_driver VALUES
    ("driver", 5, "TRUE");
    
INSERT INTO wallet VALUES
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
    (4, 1.00, POINT(38.2442388,21.7405935), "TRUE");

INSERT INTO car VALUES
    (1, "ABC-1234", 30, 4),
    (2, "DEF-1234", 25, 4),
    (3, "GHI-1234", 40, 4),
    (4, "JKL-1234", 50, 4);

INSERT INTO service VALUES
    (1, '2024-05-23 10:48:59', 1, 'ONGOING', )