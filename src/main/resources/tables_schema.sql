create table if not exists person_tbl
(
    id            bigint auto_increment
        primary key,
    date_of_birth date         not null,
    first_name    varchar(255) not null,
    gender        varchar(255) not null,
    last_name     varchar(255) not null,
    national_code bigint       not null,
    constraint UK_isymm4oeoy5cow6tnc9xg4hjs
        unique (national_code)
);

create table if not exists province_tbl
(
    id            bigint auto_increment
        primary key,
    province_name varchar(255) not null,
    enable        bit          not null
);

create table if not exists city_tbl
(
    id          bigint auto_increment
        primary key,
    city_name   varchar(255) not null,
    province_id bigint       null,
    enable      bit          not null,
    constraint FK3hbfvy9k1rav64gnvnsehr2og
        foreign key (province_id) references province_tbl (id)
);

create table if not exists address_tbl
(
    id           bigint auto_increment
        primary key,
    address_info varchar(255) not null,
    city_id      bigint       null,
    province_id  bigint       null,
    constraint FK16wf37iol5httmo4p39flr4ef
        foreign key (province_id) references province_tbl (id),
    constraint FKr1ppuyoej26mmo0wn9fdgu038
        foreign key (city_id) references city_tbl (id)
);

create table if not exists account_tbl
(
    id           bigint auto_increment
        primary key,
    created_date date         null,
    password     varchar(255) not null,
    user_name    varchar(255) not null,
    address_id   bigint       null,
    person_id    bigint       null,
    enable       bit          not null,
    constraint UK_mhtyowgtcfdaakkc1d7tu7coo
        unique (user_name),
    constraint FK69sea3jtbax9xejyld6lv3sq1
        foreign key (person_id) references person_tbl (id),
    constraint FKs0i25ynnhcelr99e6o8moln40
        foreign key (address_id) references address_tbl (id)
);

create table if not exists role_tbl
(
    id   bigint auto_increment
        primary key,
    name varchar(255) not null,
    constraint UK_j7n3i44gxwbsrqdofqech6thj
        unique (name)
);

create table if not exists account_roles
(
    account_id bigint not null,
    role_id    bigint not null,
    primary key (account_id, role_id),
    constraint FKf96u38oc1rvjrlrm3nyr9dfv
        foreign key (account_id) references account_tbl (id),
    constraint FKgikdx9nhl9ydh6g7gayvyevtj
        foreign key (role_id) references role_tbl (id)
);