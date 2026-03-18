-- liquibase formatted sql

-- changeset antony:003-add-skills

INSERT INTO skills (id, code, name) VALUES
                                        (gen_random_uuid(), 'TACTILE', 'Тактильна чутливість'),
                                        (gen_random_uuid(), 'AUDITORY', 'Слухова чутливість'),
                                        (gen_random_uuid(), 'VISUAL', 'Візуальна чутливість'),
                                        (gen_random_uuid(), 'PROPRIOCEPTION', 'Пропріоцепція'),
                                        (gen_random_uuid(), 'ORAL', 'Оральна сфера');

