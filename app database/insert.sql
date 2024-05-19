INSERT INTO user VALUES
    ("customer", "password", "Customer", "Customer", "customer@gmail.com", "6970000000"),
    ("driver", "password", "Driver", "Driver", "driver@gmail.com", "6970000001"),
    ("pipis", "password", "Mitsos", "Pippis", "pipis@gmail.com", "6970000002"),
    ("souvlakigamer2024", "password", "Babis", "Valtas", "souvlakigamer@gmail.com", "6970000003"),
    ("magic17", "password", "Nikos", "Hatzigeorgokostomanolopoulos", "magic@gmail.com", "6970000004"),
    ("FIRE_IT_UP", "password", "Kostas", "Kostopoulos", "fireitup@hotmail.com", "6970000005");


INSERT INTO customer VALUES
    ("customer", "LICENSE", NULL, 16),
    ("pipis", "LICENSE", NULL, 1997),
    ("souvlakigamer2024", "LICENSE", NULL, 64),
    ("magic17", "LICENSE", NULL, 128);

INSERT INTO transport VALUES
    (1, "Honda", "Civic", 2006),
    (2, "Ford", "Focus", 2018);

INSERT INTO taxi VALUES
    (1, "TAX-1234", POINT(38.2406297, 21.7409815)),
    (2, "TAX-5678", POINT(38.2676396, 21.7536670));

INSERT INTO taxi_driver VALUES
    ("driver", 1, "TRUE"),
    ("FIRE_IT_UP", 2, "TRUE");
    
INSERT INTO wallet VALUES
    ("customer", "22.64"),
    ("driver", "251.31");

INSERT INTO card VALUES
    ("customer", "1234 5678 1234 5678", "CUSTOMER CUSTOMER", "12/27", "999", "DEBIT"),
    ("driver", "5678 1234 5678 1234", "DRIVER DRIVER", "12/27", "111", "DEBIT");