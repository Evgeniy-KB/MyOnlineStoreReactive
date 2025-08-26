drop table if exists accounts;

-- Таблица с балансом покупателя
create table accounts(
    id bigserial primary key,
    balance number
);

insert into accounts(balance) values (105);





