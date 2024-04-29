create table users
(
    user_id    int primary key,
    user_name  varchar(50) not null unique,
    password   varchar(50) not null,
    email      varchar(50) not null unique,
    product_id int         not null,
    line_id    int         not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp
)
;