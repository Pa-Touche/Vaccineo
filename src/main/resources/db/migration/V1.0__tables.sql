-- Creates the DB, in a PROD-like setup should not be modified (enforced by Flyway).


-------------------- VACCINE
CREATE TABLE user (
    id integer PRIMARY KEY AUTOINCREMENT,
    email TEXT,
    name TEXT,
    surname TEXT,
    birth_date REAL not null,

    UNIQUE (email)
);

CREATE TABLE user_password (
    id integer PRIMARY KEY AUTOINCREMENT,
    surname TEXT,
    salt TEXT,
    user_id bigint not null,

    foreign key (user_id) REFERENCES user(id)
);

CREATE INDEX user_password_user_id_index
ON user_password(user_id);

CREATE TABLE vaccine_schedule (
    id integer PRIMARY KEY AUTOINCREMENT,
    dose_number integer not null,
    application_deadline_days integer not null,
    vaccine_type_id bigint,

    foreign key (vaccine_type_id) REFERENCES vaccine_type(id),

    UNIQUE (vaccine_type_id, dose_number)
);

CREATE INDEX vaccine_schedule_vaccine_type_id_index
ON vaccine_schedule(vaccine_type_id);

CREATE TABLE vaccine_type (
    id integer PRIMARY KEY AUTOINCREMENT,
    name TEXT not null,
    number_of_doses integer not null,
    treatment_description TEXT not null,

    UNIQUE (name)
);

CREATE TABLE vaccine_administered (
    id integer PRIMARY KEY AUTOINCREMENT,
    administration_date_time timestamp not null,
    comment TEXT not null,
    dose_number integer not null,
    user_id bigint not null,
    vaccine_type_id integer not null,

    foreign key (user_id) REFERENCES user(id)
    foreign key (vaccine_type_id) REFERENCES vaccine_type(id),

    UNIQUE (user_id, vaccine_type_id, dose_number)
);

CREATE INDEX vaccine_administered_vaccine_type_id_index
ON vaccine_administered(vaccine_type_id);
CREATE INDEX vaccine_administered_user_id_index
ON vaccine_administered(user_id);

-------------------- NOTIFICATION
CREATE TABLE notification_vaccine (
    id integer PRIMARY KEY AUTOINCREMENT,

    user_id integer not null,
    vaccine_schedule_id integer not null,

    expiration_date REAL not null,

    foreign key (user_id) REFERENCES user(id),
    foreign key (vaccine_schedule_id) REFERENCES vaccine_schedule(id)

    UNIQUE (user_id, vaccine_schedule_id)
);

CREATE INDEX notification_vaccine_vaccine_schedule_id_index
ON notification_vaccine(vaccine_schedule_id);
CREATE INDEX notification_vaccine_user_id_index
ON notification_vaccine(user_id);