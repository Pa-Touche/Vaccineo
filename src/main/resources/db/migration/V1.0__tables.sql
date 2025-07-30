-- Creates the DB, in a PROD-like setup should not be modified (enforced by Flyway).

CREATE TABLE user (
    id integer PRIMARY KEY AUTOINCREMENT,
    email TEXT,
    name TEXT,
    surname TEXT
);

CREATE TABLE user_password (
    id integer PRIMARY KEY AUTOINCREMENT,
    surname TEXT,
    salt TEXT,
    user_id bigint not null,
    foreign key (user_id) REFERENCES user(id)
);

CREATE TABLE vaccine_schedule (
    id integer PRIMARY KEY AUTOINCREMENT,
    dose_number integer not null,
    lower_applicability_days integer not null,
    upper_applicability_days integer not null,
    vaccine_type_id bigint
);

CREATE TABLE vaccine_administered (
    id integer PRIMARY KEY AUTOINCREMENT,
    administration_date_time timestamp not null,
    comment TEXT not null,
    dose_number integer not null,
    user_id bigint not null,
    vaccine_type_id integer not null,
    foreign key (user_id) REFERENCES user(id)
);

CREATE TABLE vaccine_type (
    id integer PRIMARY KEY AUTOINCREMENT,
    name TEXT not null,
    number_of_doses integer not null,
    treatment_description TEXT not null
);
