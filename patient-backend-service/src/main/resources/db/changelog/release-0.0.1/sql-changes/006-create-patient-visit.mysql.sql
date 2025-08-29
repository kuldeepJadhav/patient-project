-- liquibase formatted sql
-- changeset system:create-patient-visit dbms:mysql

CREATE TABLE IF NOT EXISTS PATIENT_MANAGEMENT_PATIENT_VISIT (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  patient_id BIGINT NOT NULL,
  visited_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  doctor_id BIGINT NOT NULL,
  diagnosis TEXT,
  treatment TEXT,
  visit_notes TEXT,
  created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- changeset system:add-fk-visit-patient dbms:mysql
ALTER TABLE PATIENT_MANAGEMENT_PATIENT_VISIT
  ADD CONSTRAINT fk_visit_patient FOREIGN KEY (patient_id)
  REFERENCES PATIENT_MANAGEMENT_PATIENT (id);

-- changeset system:add-fk-visit-doctor dbms:mysql
ALTER TABLE PATIENT_MANAGEMENT_PATIENT_VISIT
  ADD CONSTRAINT fk_visit_doctor FOREIGN KEY (doctor_id)
  REFERENCES PATIENT_MANAGEMENT_USER (id); 