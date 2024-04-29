create table if not exists accounts
(
    id varchar(10),
    primary key (id)
);

create table if not exists expense_limits
(
    id                       bigserial,
    limit_sum                DECIMAL     not null,
    limit_datetime           timestamp   not null,
    limit_expiration         timestamp   not null,
    limit_currency_shortname varchar     not null,
    expense_category         varchar     not null,
    account_id               varchar(10) not null,
    created                  timestamp(6),
    updated                  timestamp(6),
    primary key (id),
    constraint account_id_fk
        foreign key (account_id) references accounts (id)
);

create table if not exists transactions
(
    id                 bigserial,
    account_from       varchar(10) not null,
    account_to         varchar(10) not null,
    currency_shortname varchar     not null,
    amount             DECIMAL     not null,
    expense_category   varchar     not null,
    datetime           timestamp   not null,
    limit_exceeded     boolean,
    created            timestamp(6),
    updated            timestamp(6),
    primary key (id),
    constraint account_from_fk
        foreign key (account_from) references accounts (id),
    constraint account_to_fk
        foreign key (account_to) references accounts (id)
);

create table if not exists currency_rates
(
    id         bigserial,
    symbol     varchar not null,
    rate       decimal not null,
    time_stamp timestamp,
    close      boolean,
    created    timestamp(6),
    updated    timestamp(6),
    primary key (id)
);

-- SET datestyle = dmy;

insert into accounts (id)
values ('0000000001'),
       ('0000000002'),
       ('0000000003');

insert into expense_limits (limit_sum, limit_datetime, limit_expiration, limit_currency_shortname, expense_category,
                            account_id)
values (1000, '2022-01-01 00:00:00+06', '2022-02-01 00:00:00+06', 'USD', 'SERVICES', '0000000001'),
       (2000, '2022-01-10 00:00:00+06', '2022-02-01 00:00:00+06', 'USD', 'SERVICES', '0000000001'),
       (1000, '2022-02-01 00:00:00+06', '2022-03-01 00:00:00+06', 'USD', 'GOODS', '0000000002'),
       (400, '2022-02-10 00:00:00+06', '2022-03-01 00:00:00+06', 'USD', 'GOODS', '0000000002');

insert into transactions (account_from, account_to, currency_shortname, amount, expense_category, datetime,
                          limit_exceeded)
values ('0000000001', '0000000002', 'USD', 500, 'SERVICES', '2022-01-02 00:00:00+06', false),
       ('0000000001', '0000000002', 'USD', 600, 'SERVICES', '2022-01-03 00:00:00+06', true),
       ('0000000001', '0000000002', 'USD', 100, 'SERVICES', '2022-01-11 00:00:00+06', false),
       ('0000000001', '0000000002', 'USD', 700, 'SERVICES', '2022-01-12 00:00:00+06', false),
       ('0000000001', '0000000002', 'USD', 100, 'SERVICES', '2022-01-13 00:00:00+06', false),
       ('0000000001', '0000000002', 'USD', 100, 'SERVICES', '2022-01-13 00:01:00+06', true),

       ('0000000002', '0000000003', 'USD', 500, 'GOODS', '2022-02-02 00:00:00+06', false),
       ('0000000002', '0000000003', 'USD', 100, 'GOODS', '2022-02-03 00:00:00+06', false),
       ('0000000002', '0000000003', 'USD', 100, 'GOODS', '2022-02-11 00:00:00+06', true),
       ('0000000002', '0000000003', 'USD', 100, 'GOODS', '2022-02-12 00:00:00+06', true);