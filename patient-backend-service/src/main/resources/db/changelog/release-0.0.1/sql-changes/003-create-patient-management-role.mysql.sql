-- liquibase formatted sql
-- changeset system:create-patient-management-role dbms:mysql

CREATE TABLE IF NOT EXISTS PATIENT_MANAGEMENT_ROLE (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(20) NOT NULL,
  description VARCHAR(255) NOT NULL,
  UNIQUE KEY uk_patient_mgmt_role_name (name)
); 