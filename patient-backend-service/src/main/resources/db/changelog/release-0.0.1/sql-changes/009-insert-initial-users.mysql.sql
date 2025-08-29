-- liquibase formatted sql

-- changeset system:insert-admin-user dbms:mysql
INSERT IGNORE INTO PATIENT_MANAGEMENT_USER (username, password, email, first_name, last_name, enabled, created_on, updated_on)
VALUES ('admin', '$2a$10$L5esMnkorSys8CfFdC6hkO0ogDrKlLSKO9giph60oKBgnvU4vduaW', 'admin@test.com', 'Admin', 'User', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT IGNORE INTO PATIENT_MANAGEMENT_USER (username, password, email, first_name, last_name, enabled, created_on, updated_on)
VALUES ('dr_kuldeep', '$2a$10$L5esMnkorSys8CfFdC6hkO0ogDrKlLSKO9giph60oKBgnvU4vduaW', 'dr_kuldeep@test.com', 'Kuldeep', 'Jadhav', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

COMMIT;