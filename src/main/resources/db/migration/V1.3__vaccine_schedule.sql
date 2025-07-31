-- Date ranges where computed with online calculator and took the next int.

-------------- 0 month

-- RSV - 1 dose (up to 6 month)
INSERT INTO vaccine_schedule (vaccine_type_id, dose_number, application_deadline_days)
VALUES(1, 1, 183);



-------------- 2 months

-- Vaccin combiné (D, T, aP, Hib, IPV, Hep B) - 1 dose
INSERT INTO vaccine_schedule (vaccine_type_id, dose_number, application_deadline_days)
VALUES(2, 1, 61);

-- Rotavirus - 1 dose
INSERT INTO vaccine_schedule (vaccine_type_id, dose_number, application_deadline_days)
VALUES(3, 1, 61);

-- Pneumocoques - 1 dose
INSERT INTO vaccine_schedule (vaccine_type_id, dose_number, application_deadline_days)
VALUES(4, 1, 61);



-------------- 3 months

-- Rotavirus - 2 dose
INSERT INTO vaccine_schedule (vaccine_type_id, dose_number, application_deadline_days)
VALUES(3, 2, 92);

-- Méningocoque B - 1 dose
INSERT INTO vaccine_schedule (vaccine_type_id, dose_number, application_deadline_days)
VALUES(5, 1, 92);



-------------- 4 months

-- Vaccin combiné (D, T, aP, Hib, IPV, Hep B) - 2 dose
INSERT INTO vaccine_schedule (vaccine_type_id, dose_number, application_deadline_days)
VALUES(2, 2, 122);

-- Pneumocoques - 2 dose
INSERT INTO vaccine_schedule (vaccine_type_id, dose_number, application_deadline_days)
VALUES(4, 2, 122);

-- Rotavirus - 3 dose
INSERT INTO vaccine_schedule (vaccine_type_id, dose_number, application_deadline_days)
VALUES(3, 3, 122);



-------------- 5 months

-- Méningocoque B - 2 dose
INSERT INTO vaccine_schedule (vaccine_type_id, dose_number, application_deadline_days)
VALUES(5, 2, 153);



-------------- 11 months

-- Vaccin combiné (D, T, aP, Hib, IPV, Hep B) - 3 dose
INSERT INTO vaccine_schedule (vaccine_type_id, dose_number, application_deadline_days)
VALUES(2, 3, 335);

-- Pneumocoques - 3 dose
INSERT INTO vaccine_schedule (vaccine_type_id, dose_number, application_deadline_days)
VALUES(4, 3, 335);



-------------- 12 months

-- vaccin combiné (RORV)  - 1 dose
INSERT INTO vaccine_schedule (vaccine_type_id, dose_number, application_deadline_days)
VALUES(6, 1, 335);

-- Méningocoque B - 3 dose
INSERT INTO vaccine_schedule (vaccine_type_id, dose_number, application_deadline_days)
VALUES(5, 3, 335);



-------------- 13 months

-- Méningocoques ACWY - 1 dose
INSERT INTO vaccine_schedule (vaccine_type_id, dose_number, application_deadline_days)
VALUES(7, 1, 396);