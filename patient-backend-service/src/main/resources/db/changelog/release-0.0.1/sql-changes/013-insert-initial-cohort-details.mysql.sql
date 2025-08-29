-- liquibase formatted sql

-- changeset system:insert-initial-cohort-details dbms:mysql

INSERT INTO PATIENT_MANAGEMENT_PATIENT_COHORT (
    cohort_name,
    cohort_description,
    cohort_criteria
) VALUES (
             'Hypertension Patients',
             'Cohort of patients diagnosed with high blood pressure requiring regular monitoring',
             '{"diagnosis": "Hypertension (High Blood Pressure)"}'
         );

-- Insert second cohort for patients aged 30-40 with pagination
INSERT INTO PATIENT_MANAGEMENT_PATIENT_COHORT (
    cohort_name,
    cohort_description,
    cohort_criteria
) VALUES (
             'Middle-aged Adults (30-40)',
             'Cohort of patients between 30-40 years old for age-specific health monitoring',
             '{"fromAge": "30", "toAge": "40", "limit": "1", "offset": "0"}'
         );

COMMIT;