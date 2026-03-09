create table customers (
    id         bigserial primary key,
    name       varchar(255) not null,
    email      varchar(255) not null unique,
    created_at timestamp default now()
);

create table orders (
    id          bigserial primary key,
    customer_id bigint not null references customers(id),
    amount      numeric(19,2) not null,
    status      varchar(20)  not null default 'NEW',
    created_at  timestamp default now()
);
