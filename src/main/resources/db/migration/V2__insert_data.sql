insert into customers (name, email, created_at) values
    ('Alice Johnson', 'alice@example.com', now()),
    ('Bob Smith',     'bob@example.com',   now()),
    ('Carol White',   'carol@example.com', now());

insert into orders (customer_id, amount, status, created_at) values
    (1, 150.00,  'NEW',       now()),
    (1, 299.99,  'PAID',      now()),
    (2, 49.50,   'CANCELLED', now()),
    (2, 999.00,  'NEW',       now()),
    (3, 75.25,   'PAID',      now());
