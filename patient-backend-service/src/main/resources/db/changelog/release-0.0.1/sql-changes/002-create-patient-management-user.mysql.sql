-- liquibase formatted sql
-- changeset system:create-patient-management-user dbms:mysql

CREATE TABLE IF NOT EXISTS PATIENT_MANAGEMENT_USER (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(100) NOT NULL,
  first_name VARCHAR(100),
  last_name VARCHAR(100),
  enabled TINYINT(1) NOT NULL DEFAULT 1,
  created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_patient_mgmt_user_username (username),
  UNIQUE KEY uk_patient_mgmt_user_email (email)
); 