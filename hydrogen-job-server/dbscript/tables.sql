create table t_user
(
    user_id    bigint primary key,
    user_name  varchar(50)         not null unique,
    password   varchar(80)         not null,
    email      varchar(50)         not null unique,
    product_id bigint              not null,
    line_id    bigint              not null,
    type       tinyint   default 1 not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp
)
;

create table t_product
(
    product_id   bigint primary key,
    product_name varchar(50) not null unique,
    created_at   timestamp default current_timestamp,
    updated_at   timestamp default current_timestamp on update current_timestamp
)
;

create table t_line
(
    line_id    bigint primary key,
    line_name  varchar(50) not null unique,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp
)
;