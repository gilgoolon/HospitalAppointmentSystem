DROP TABLE Users;
DROP TABLE Appointments;
DROP TABLE Doctors;
DROP TABLE Working_Hours;

CREATE TABLE Users (
                       id varchar(9) NOT NULL UNIQUE,
                       first_name varchar(20) NOT NULL,
                       last_name varchar(20) NOT NULL,
                       birth_date date NOT NULL,
                       password varchar(16) NOT NULL,
                       phone_number varchar(10),
                       email varchar(320),
                       is_admin bit NOT NULL DEFAULT 0,
                       PRIMARY KEY (id)
);

CREATE TABLE Appointments (
                              start_time timestamp NOT NULL,
                              end_time timestamp NOT NULL,
                              doctor_id varchar(9) NOT NULL,
                              patient_id varchar(9) NOT NULL,
                              description varchar(1000),
                              is_cancelled bit NOT NULL DEFAULT 0,-- by default an appointment is not cancelled
                              PRIMARY KEY (start_time, doctor_id)
);

CREATE TABLE Doctors (
                         id varchar(9) NOT NULL UNIQUE,
                         type varchar(20) NOT NULL,
                         about varchar(100),
                         city varchar(20) NOT NULL,
                         PRIMARY KEY (id)
);

CREATE TABLE Working_Hours (
                               doctor_id varchar(9) NOT NULL,
                               week_day int NOT NULL,
                               start_time time NOT NULL,
                               end_time time NOT NULL,
                               apt_length int NOT NULL DEFAULT 10,
                               PRIMARY KEY (doctor_id, week_day)
);