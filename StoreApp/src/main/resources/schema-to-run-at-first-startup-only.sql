CREATE TABLE PRODUCT
    (
    product_id bigint auto_increment primary key,
    product_name varchar(100),
    product_bar_code varchar(100),
    brand varchar(100),
    category varchar(100),
    model_number varchar(100),
    price DOUBLE,
    quantity varchar(100),
    active BOOLEAN
    );