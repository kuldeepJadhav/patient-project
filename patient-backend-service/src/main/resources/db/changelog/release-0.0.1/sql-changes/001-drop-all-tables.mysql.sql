-- liquibase formatted sql

-- changeset system:drop-all-tables dbms:mysql
-- Drop tables in reverse dependency order (FKs first, then parent tables)

-- Drop join tables first (they reference other tables)
DROP TABLE IF EXISTS PATIENT_MANAGEMENT_COHORT_PATIENT;
DROP TABLE IF EXISTS PATIENT_MANAGEMENT_USER_ROLE;

-- Drop tables with foreign keys
DROP TABLE IF EXISTS PATIENT_MANAGEMENT_PATIENT_VISIT;
DROP TABLE IF EXISTS PATIENT_MANAGEMENT_PATIENT;

-- Drop base tables last
DROP TABLE IF EXISTS PATIENT_MANAGEMENT_ROLE;
DROP TABLE IF EXISTS PATIENT_MANAGEMENT_USER;