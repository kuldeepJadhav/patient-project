-- liquibase formatted sql

-- changeset system:insert-initial-patient dbms:mysql
INSERT INTO PATIENT_MANAGEMENT_PATIENT (first_name,last_name,dob,gender,age,address,contact_number,email,city,state,blood_group,emergency_contact_name,emergency_contact_phone,created_by,status
) VALUES ('John','Smith','1985-03-15','MALE',38,'123 Oak Street, Apt 4B','+1-555-0123','john.smith@email.com','New York','NY',
          'A_PLUS','Sarah Smith','+1-555-0124',1,'ACTIVE');

INSERT INTO PATIENT_MANAGEMENT_PATIENT (first_name,last_name,dob,gender,age,address,contact_number,email,city,state,blood_group,emergency_contact_name,emergency_contact_phone,created_by,status
) VALUES ('John1','Smith1','1986-03-15','MALE',38,'124 Oak Street, Apt 4B','+1-555-0123','john.smith1@email.com','New York','NY',
          'B_PLUS','Sarah Smith1','+1-555-0124',1,'ACTIVE');

COMMIT;
