-- Integration Test Database Setup Script
-- This script creates all necessary tables and test data for integration tests

-- Drop tables in reverse order of dependency
DROP TABLE IF EXISTS PATIENT_MANAGEMENT_USER_ROLE;
DROP TABLE IF EXISTS PATIENT_MANAGEMENT_PATIENT_VISIT;
DROP TABLE IF EXISTS PATIENT_MANAGEMENT_PATIENT_COHORT;
DROP TABLE IF EXISTS PATIENT_MANAGEMENT_PATIENT;
DROP TABLE IF EXISTS PATIENT_MANAGEMENT_ROLE;
DROP TABLE IF EXISTS PATIENT_MANAGEMENT_USER;

-- Create User table
CREATE TABLE PATIENT_MANAGEMENT_USER (
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

-- Create Role table
CREATE TABLE IF NOT EXISTS PATIENT_MANAGEMENT_ROLE (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    description VARCHAR(255) NOT NULL,
    UNIQUE KEY uk_patient_mgmt_role_name (name)
);

-- Create User-Role mapping table
CREATE TABLE IF NOT EXISTS PATIENT_MANAGEMENT_USER_ROLE (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES PATIENT_MANAGEMENT_USER (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES PATIENT_MANAGEMENT_ROLE (id) ON DELETE CASCADE
);

-- Create Patient table
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
    blood_group ENUM('A_PLUS','A_MINUS','B_PLUS','B_MINUS','AB_PLUS','AB_MINUS','O_PLUS','O_MINUS','UNKNOWN') DEFAULT 'UNKNOWN',
    emergency_contact_name VARCHAR(200),
    emergency_contact_phone VARCHAR(20),
    created_by BIGINT NOT NULL,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    UNIQUE KEY uk_patient_email (email),
    FOREIGN KEY (created_by) REFERENCES PATIENT_MANAGEMENT_USER (id) ON DELETE CASCADE
);

-- Create Patient Visit table
CREATE TABLE IF NOT EXISTS PATIENT_MANAGEMENT_PATIENT_VISIT (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    visited_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    doctor_id BIGINT NOT NULL,
    diagnosis TEXT,
    treatment TEXT,
    visit_notes TEXT,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES PATIENT_MANAGEMENT_PATIENT (id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES PATIENT_MANAGEMENT_USER (id) ON DELETE CASCADE
);

-- Create Patient Cohort table
CREATE TABLE IF NOT EXISTS PATIENT_MANAGEMENT_PATIENT_COHORT (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    cohort_name VARCHAR(100) NOT NULL,
    cohort_description TEXT,
    cohort_criteria MEDIUMTEXT,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX idx_patient_dob ON PATIENT_MANAGEMENT_PATIENT (dob);
CREATE INDEX idx_patient_status ON PATIENT_MANAGEMENT_PATIENT (status);
CREATE INDEX idx_patient_email ON PATIENT_MANAGEMENT_PATIENT (email);
CREATE INDEX idx_patient_name ON PATIENT_MANAGEMENT_PATIENT (first_name);
CREATE INDEX idx_visit_patient ON PATIENT_MANAGEMENT_PATIENT_VISIT (patient_id);
CREATE INDEX idx_visit_date ON PATIENT_MANAGEMENT_PATIENT_VISIT (visited_on);
CREATE INDEX idx_visit_doctor ON PATIENT_MANAGEMENT_PATIENT_VISIT (doctor_id);

-- Insert initial roles
INSERT INTO PATIENT_MANAGEMENT_ROLE (name, description) VALUES
('ROLE_MANAGER', 'Manager role with full permissions'),
('ROLE_DOCTOR', 'Doctor role with clinical permissions'),
('ROLE_NURSE', 'Nurse role with patient care permissions'),
('ROLE_ADMIN', 'Administrator role with system permissions');

-- Insert admin user for integration tests
INSERT INTO PATIENT_MANAGEMENT_USER (username, password, email, first_name, last_name, enabled, created_on, updated_on) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'admin@test.com', 'Admin', 'User', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert user-role mapping for admin (ROLE_MANAGER)
INSERT INTO PATIENT_MANAGEMENT_USER_ROLE (user_id, role_id) VALUES
((SELECT id FROM PATIENT_MANAGEMENT_USER WHERE username = 'admin'), 
 (SELECT id FROM PATIENT_MANAGEMENT_ROLE WHERE name = 'ROLE_MANAGER'));

-- Commit all changes
COMMIT; 