-- liquibase formatted sql
-- changeset system:create-patient dbms:mysql

CREATE TABLE IF NOT EXISTS PATIENT_MANAGEMENT_PATIENT (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  dob DATE NOT NULL,
  gender ENUM('MALE','FEMALE','OTHER') NOT NULL,
  age INTEGER,
  address TEXT,
  contact_number VARCHAR(20),
  email VARCHAR(100),
  city VARCHAR(100),
  state VARCHAR(100),
  blood_group  ENUM('A_PLUS','A_MINUS','B_PLUS','B_MINUS','AB_PLUS','AB_MINUS','O_PLUS','O_MINUS','UNKNOWN') DEFAULT 'UNKNOWN',
  emergency_contact_name VARCHAR(200),
  emergency_contact_phone VARCHAR(20),
  created_by BIGINT NOT NULL,
  created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  status VARCHAR(20) DEFAULT 'ACTIVE',
  UNIQUE KEY uk_patient_email (email)
);

-- changeset system:add-fk-patient-created-by dbms:mysql
ALTER TABLE PATIENT_MANAGEMENT_PATIENT
  ADD CONSTRAINT fk_patient_created_by FOREIGN KEY (created_by)
  REFERENCES PATIENT_MANAGEMENT_USER (id);


CREATE INDEX idx_patient_dob ON PATIENT_MANAGEMENT_PATIENT (dob);

CREATE INDEX idx_patient_status ON PATIENT_MANAGEMENT_PATIENT (status);

CREATE INDEX idx_patient_email ON PATIENT_MANAGEMENT_PATIENT (email);

CREATE INDEX idx_patient_name ON PATIENT_MANAGEMENT_PATIENT (first_name);