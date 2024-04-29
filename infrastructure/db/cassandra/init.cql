CREATE KEYSPACE IF NOT EXISTS test_keyspace
    WITH REPLICATION = {
        'class' : 'SimpleStrategy',
        'replication_factor' : 1
        }
     AND DURABLE_WRITES = true;

CREATE TABLE IF NOT EXISTS test_keyspace.currency_rates
(
    symbol     varchar,
    rate       decimal,
    time_stamp timestamp,
    close      boolean,
    PRIMARY KEY ( symbol, time_stamp )
);

CREATE TABLE IF NOT EXISTS test_keyspace.holidays
(
    country varchar,
    month   int,
    day     int,
    name    varchar,
    PRIMARY KEY ( country, month, day )
);

-- Currency rates insert --

INSERT INTO test_keyspace.currency_rates (symbol, rate, time_stamp, close)
VALUES ('USD/KZT', 442.47787, 1714137360000, false) IF NOT EXISTS;

INSERT INTO test_keyspace.currency_rates (symbol, rate, time_stamp, close)
VALUES ('USD/RUB', 92.28, 1714137360000, false) IF NOT EXISTS;

INSERT INTO test_keyspace.currency_rates (symbol, rate, time_stamp, close)
VALUES ('RUB/KZT', 4.82981, 1714137360000, false) IF NOT EXISTS;

INSERT INTO test_keyspace.currency_rates (symbol, rate, time_stamp, close)
VALUES ('RUB/USD', 0.01083, 1714137360000, false) IF NOT EXISTS;

INSERT INTO test_keyspace.currency_rates (symbol, rate, time_stamp, close)
VALUES ('KZT/USD', 0.00226, 1714137360000, false) IF NOT EXISTS;

INSERT INTO test_keyspace.currency_rates (symbol, rate, time_stamp, close)
VALUES ('KZT/RUB', 0.20704, 1714137360000, false) IF NOT EXISTS;

-- Holidays insert --

INSERT INTO test_keyspace.holidays (country, month, day, name)
VALUES ('KZ', 1, 1, 'New Year`s Day') IF NOT EXISTS;

INSERT INTO test_keyspace.holidays (country, month, day, name)
VALUES ('KZ', 1, 2, 'Second Day of the New Year') IF NOT EXISTS;

INSERT INTO test_keyspace.holidays (country, month, day, name)
VALUES ('KZ', 1, 7, 'Orthodox Christmas Day') IF NOT EXISTS;

INSERT INTO test_keyspace.holidays (country, month, day, name)
VALUES ('KZ', 3, 8, 'International Women`s Day') IF NOT EXISTS;

INSERT INTO test_keyspace.holidays (country, month, day, name)
VALUES ('KZ', 3, 21, 'Nauryz') IF NOT EXISTS;

INSERT INTO test_keyspace.holidays (country, month, day, name)
VALUES ('KZ', 5, 1, 'Unity Day') IF NOT EXISTS;

INSERT INTO test_keyspace.holidays (country, month, day, name)
VALUES ('KZ', 5, 7, 'Defender of the Fatherland Day') IF NOT EXISTS;

INSERT INTO test_keyspace.holidays (country, month, day, name)
VALUES ('KZ', 5, 9, 'Victory Day') IF NOT EXISTS;

INSERT INTO test_keyspace.holidays (country, month, day, name)
VALUES ('KZ', 6, 28, 'Feast of the Sacrifice') IF NOT EXISTS;

INSERT INTO test_keyspace.holidays (country, month, day, name)
VALUES ('KZ', 7, 6, 'Day of the Capital') IF NOT EXISTS;

INSERT INTO test_keyspace.holidays (country, month, day, name)
VALUES ('KZ', 8, 30, 'Constitution Day') IF NOT EXISTS;

INSERT INTO test_keyspace.holidays (country, month, day, name)
VALUES ('KZ', 10, 25, 'Republic Day') IF NOT EXISTS;

INSERT INTO test_keyspace.holidays (country, month, day, name)
VALUES ('KZ', 12, 1, 'Day of the First President of the Republic of Kazakhstan') IF NOT EXISTS;

INSERT INTO test_keyspace.holidays (country, month, day, name)
VALUES ('KZ', 12, 16, 'Independence Day') IF NOT EXISTS;

INSERT INTO test_keyspace.holidays (country, month, day, name)
VALUES ('KZ', 12, 17, 'Day after Independence Day') IF NOT EXISTS;
