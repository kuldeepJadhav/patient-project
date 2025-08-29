-- liquibase formatted sql
-- changeset system:create-patient-management-user-role dbms:mysql

CREATE TABLE IF NOT EXISTS PATIENT_MANAGEMENT_USER_ROLE (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, role_id)
);

-- changeset system:add-fk-user-role-user dbms:mysql
ALTER TABLE PATIENT_MANAGEMENT_USER_ROLE
  ADD CONSTRAINT fk_pm_user_role_user FOREIGN KEY (user_id)
  REFERENCES PATIENT_MANAGEMENT_USER (id);

-- changeset system:add-fk-user-role-role dbms:mysql
ALTER TABLE PATIENT_MANAGEMENT_USER_ROLE
  ADD CONSTRAINT fk_pm_user_role_role FOREIGN KEY (role_id)
  REFERENCES PATIENT_MANAGEMENT_ROLE (id); 