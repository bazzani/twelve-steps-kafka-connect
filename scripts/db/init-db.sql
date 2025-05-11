create table public.pizza_orders
(
    uuid uuid default gen_random_uuid() not null,
    created_at_db timestamp with time zone default now() not null,
    store_id integer not null,
    store_order_id integer not null,
    coupon_code integer not null,
    date date not null,
    status text not null,
    order_lines jsonb not null,
    constraint pizza_orders_pk primary key (store_id, store_order_id)
);