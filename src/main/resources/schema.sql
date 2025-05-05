drop table if exists product_order;
drop table if exists cart_product;
drop table if exists products;
drop table if exists orders;
drop table if exists carts;

-- Таблица с товарами
create table products(
  id bigserial primary key,
  title varchar(255) not null,
  picture blob,
  description varchar(255) not null,
  price number
);

insert into products(title, description, price) values ('Товар1', 'Описание товара 1', 17);
insert into products(title, description, price) values ('Товар2', 'Описание товара 2', 35);
insert into products(title, description, price) values ('Редкий Товар3', 'Довольно редкий товар 3', 26);
insert into products(title, description, price) values ('Товар4', 'Описание товара 4', 4);
insert into products(title, description, price) values ('Товар5', 'Описание товара 5', 100);
insert into products(title, description, price) values ('Товар22', 'Описание товара 22', 59);

-- Таблица с заказами
create table orders(
    id bigserial primary key
);

-- Таблица связей товара и заказа
create table product_order(
    id bigserial primary key,
    product_id bigserial not null,
    order_id bigserial not null,
    quantity numeric default 0,
    foreign key (product_id) references products(id),
    foreign key (order_id) references orders(id)
);

-- Таблица с корзинами
create table carts(
    id bigserial primary key
);

-- Таблица связей корзины и товара
create table cart_product(
    id bigserial primary key,
    cart_id bigserial not null,
    product_id bigserial not null,
    quantity numeric default 0,
    foreign key (cart_id) references carts(id),
    foreign key (product_id) references products(id)
);




