CREATE TABLE cars (
    id REAL PRIMARY KEY,
    brand TEXT,
    model TEXT,
    price NUMERIC,
);

CREATE TABLE drivers (
    id REAL PRIMARY KEY,
    name TEXT,
    age INTEGER,
    driver_doc BOOLEAN,
    car TEXT REFERENCES cars (id)
);