-- Creates the DB, in a PROD-like setup should not be modified (enforced by Flyway).


-------------------- VACCINE
CREATE TABLE user (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email TEXT,
    name TEXT,
    surname TEXT,
    birth_date text not null,

    UNIQUE (email)
);

CREATE TABLE user_password (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    password_hash TEXT,
    salt TEXT,
    user_id INTEGER not null,

    foreign key (user_id) REFERENCES user(id)
);

CREATE INDEX user_password_user_id_index
ON user_password(user_id);

CREATE TABLE vaccine_schedule (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    dose_number INTEGER not null,
    application_deadline_days INTEGER not null,
    vaccine_type_id INTEGER,

    foreign key (vaccine_type_id) REFERENCES vaccine_type(id),

    UNIQUE (vaccine_type_id, dose_number)
);

CREATE INDEX vaccine_schedule_vaccine_type_id_index
ON vaccine_schedule(vaccine_type_id);

CREATE TABLE vaccine_type (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT not null,
    number_of_doses INTEGER not null,
    treatment_description TEXT not null,

    UNIQUE (name)
);

CREATE TABLE vaccine_administered (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    administration_date INTEGER not null,
    comment TEXT,
    dose_number INTEGER not null,
    user_id INTEGER not null,
    vaccine_type_id INTEGER not null,

    foreign key (user_id) REFERENCES user(id)
    foreign key (vaccine_type_id) REFERENCES vaccine_type(id),

    UNIQUE (user_id, vaccine_type_id, dose_number)
);

CREATE INDEX vaccine_administered_vaccine_type_id_index
ON vaccine_administered(vaccine_type_id);
CREATE INDEX vaccine_administered_user_id_index
ON vaccine_administered(user_id);
-- Used as default SORT column
CREATE INDEX vaccine_administered_administration_date_index
ON vaccine_administered(administration_date);

-------------------- NOTIFICATION
CREATE TABLE notification_vaccine (
    id INTEGER PRIMARY KEY AUTOINCREMENT,

    user_id INTEGER not null,
    vaccine_schedule_id INTEGER not null,

    deadline INTEGER not null,

    foreign key (user_id) REFERENCES user(id),
    foreign key (vaccine_schedule_id) REFERENCES vaccine_schedule(id)

    UNIQUE (user_id, vaccine_schedule_id)
);

CREATE INDEX notification_vaccine_vaccine_schedule_id_index
ON notification_vaccine(vaccine_schedule_id);
CREATE INDEX notification_vaccine_user_id_index
ON notification_vaccine(user_id);

-- used within batch for deletion
CREATE INDEX notification_vaccine_deadline_index
ON notification_vaccine(deadline);