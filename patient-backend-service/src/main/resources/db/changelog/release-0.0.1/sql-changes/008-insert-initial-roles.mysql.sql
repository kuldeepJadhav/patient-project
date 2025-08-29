-- liquibase formatted sql

-- changeset system:insert-initial-roles dbms:mysql
INSERT IGNORE INTO PATIENT_MANAGEMENT_ROLE (name, description)
VALUES ('ROLE_MANAGER', 'Manager role with full permissions');

-- changeset system:insert-initial-roles-doctor dbms:mysql
INSERT IGNORE INTO PATIENT_MANAGEMENT_ROLE (name, description)
VALUES ('ROLE_DOCTOR', 'Doctor role with clinical permissions');

COMMIT;