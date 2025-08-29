-- liquibase formatted sql
-- changeset system:create-patient-cohort dbms:mysql

CREATE TABLE IF NOT EXISTS PATIENT_MANAGEMENT_PATIENT_COHORT (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  cohort_name VARCHAR(100) NOT NULL,
  cohort_description TEXT,
  cohort_criteria MEDIUMTEXT,
  created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

