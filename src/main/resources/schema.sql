DROP TABLE IF EXISTS USERS;
CREATE TABLE USERS
(
    sn bigint not null auto_increment,
    token char(20) not null,
    name varchar(20) not null,
    age int not null,
    gender enum('MALE', 'FEMALE') not null,
    disease char(1) not null,
    img_path varchar(500),
    primary key(sn)
);

CREATE UNIQUE INDEX UNIQUE_TOKEN_USERS ON USERS (token);