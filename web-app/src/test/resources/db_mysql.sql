drop table if exists product_order;
drop table if exists cart_product;
drop table if exists products;
drop table if exists orders;
drop table if exists carts;

-- Таблица с товарами
create table products(
  id INT UNSIGNED AUTO_INCREMENT primary key,
  title varchar(255) not null,
  picture blob,
  description varchar(255) not null,
  price int UNSIGNED
);

-- Таблица с заказами
create table orders(
    id INT UNSIGNED AUTO_INCREMENT primary key
);

-- Таблица связей товара и заказа
create table product_order(
    id INT UNSIGNED AUTO_INCREMENT primary key,
    product_id INT UNSIGNED not null,
    order_id INT UNSIGNED not null,
    quantity numeric default 0,
    foreign key (product_id) references products(id),
    foreign key (order_id) references orders(id)
);

-- Таблица с корзинами
create table carts(
    id INT UNSIGNED AUTO_INCREMENT primary key
);

-- Таблица связей корзины и товара
create table cart_product(
    id INT UNSIGNED AUTO_INCREMENT primary key,
    cart_id INT UNSIGNED not null,
    product_id INT UNSIGNED not null,
    quantity numeric default 0,
    foreign key (cart_id) references carts(id)
    on update cascade on delete cascade,
    foreign key (product_id) references products(id)
    on update cascade on delete cascade
);





