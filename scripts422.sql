CREATE TABLE cars (
    id REAL PRIMARY KEY,
    brand TEXT,
    model TEXT,
    price NUMERIC,
    driver TEXT REFERENCES drivers (id)
);

CREATE TABLE drivers (
    id REAL PRIMARY KEY,
    name TEXT,
    age INTEGER,
    driver_doc BOOLEAN,
    car TEXT REFERENCES cars (id)
);

1) Нарушение нормализации 1НФ в первой таблице. Правильно ли это? Про соблюдение не говорилось в задании
2) В условиях задания ничего не говорилось об id (добавил самостоятельно, так как ни марка, ни модель, ни стоимость не
обеспечивают уникальность, как и поля другой таблицы)
3) В шпаргалке для связи использовалось "department_id TEXT REFERENCES departments (id)" Почему TEXT, если id имеет не
текстовое значение?