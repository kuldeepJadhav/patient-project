-- liquibase formatted sql

-- changeset system:insert-initial-patient-visit dbms:mysql



-- Insert visit for John Smith (BP diagnosis) - using direct patient ID lookup
INSERT INTO PATIENT_MANAGEMENT_PATIENT_VISIT (
    patient_id,
    visited_on,
    doctor_id,
    diagnosis,
    treatment,
    visit_notes,
    created_on
)
SELECT
    p.id,
    '2024-01-15 10:30:00',
    u.id,
    'Hypertension (High Blood Pressure)',
    'Prescribed Amlodipine 5mg daily, lifestyle modifications including low-sodium diet and regular exercise',
    'Patient presents with elevated BP readings over the past week. Blood pressure: 150/95 mmHg. Recommended regular monitoring and follow-up in 2 weeks.',
    '2024-01-16 14:15:00'
FROM PATIENT_MANAGEMENT_PATIENT p, PATIENT_MANAGEMENT_USER u
WHERE p.email = 'john.smith@email.com' AND u.username = 'dr_kuldeep';

-- Insert visit for Emily Johnson (Diabetes diagnosis) - using direct patient ID lookup
INSERT INTO PATIENT_MANAGEMENT_PATIENT_VISIT (
    patient_id,
    visited_on,
    doctor_id,
    diagnosis,
    treatment,
    visit_notes,
    created_on
)
SELECT
    p.id,
    '2024-01-16 14:15:00',
    u.id,
    'Type 2 Diabetes Mellitus',
    'Prescribed Metformin 500mg twice daily, blood glucose monitoring, dietary consultation scheduled',
    'Patient reports increased thirst and frequent urination. Fasting blood glucose: 180 mg/dL. HbA1c: 8.2%. Recommended diabetes education program and regular blood sugar monitoring.',
    '2024-01-16 14:15:00'
FROM PATIENT_MANAGEMENT_PATIENT p, PATIENT_MANAGEMENT_USER u
WHERE p.email = 'john.smith@email.com' AND u.username = 'dr_kuldeep';

INSERT INTO PATIENT_MANAGEMENT_PATIENT_VISIT (
    patient_id,
    visited_on,
    doctor_id,
    diagnosis,
    treatment,
    visit_notes,
    created_on
)
SELECT
    p.id,
    '2024-01-15 10:30:00',
    u.id,
    'Hypertension (High Blood Pressure)',
    'Prescribed Amlodipine 5mg daily, lifestyle modifications including low-sodium diet and regular exercise',
    'Patient presents with elevated BP readings over the past week. Blood pressure: 150/95 mmHg. Recommended regular monitoring and follow-up in 2 weeks.',
    '2024-01-16 14:15:00'
FROM PATIENT_MANAGEMENT_PATIENT p, PATIENT_MANAGEMENT_USER u
WHERE p.email = 'john.smith1@email.com' AND u.username = 'dr_kuldeep';

-- Insert visit for Emily Johnson (Diabetes diagnosis) - using direct patient ID lookup
INSERT INTO PATIENT_MANAGEMENT_PATIENT_VISIT (
    patient_id,
    visited_on,
    doctor_id,
    diagnosis,
    treatment,
    visit_notes,
    created_on
)
SELECT
    p.id,
    '2024-01-16 14:15:00',
    u.id,
    'Type 2 Diabetes Mellitus',
    'Prescribed Metformin 500mg twice daily, blood glucose monitoring, dietary consultation scheduled',
    'Patient reports increased thirst and frequent urination. Fasting blood glucose: 180 mg/dL. HbA1c: 8.2%. Recommended diabetes education program and regular blood sugar monitoring.',
    '2024-01-16 14:15:00'
FROM PATIENT_MANAGEMENT_PATIENT p, PATIENT_MANAGEMENT_USER u
WHERE p.email = 'john.smith1@email.com' AND u.username = 'dr_kuldeep';

commit;