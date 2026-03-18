-- liquibase formatted sql

-- changeset antony:004-add-resources

ALTER TABLE resources ADD COLUMN content TEXT;

INSERT INTO resources (
    id, skill_id, title, description, content, type, audience, url, is_published, created_by
)
SELECT
    gen_random_uuid(),
    s.id,
    'Ігри з текстурами',
    'Розвиток тактильної чутливості через гру',
    'Опис:
Ці вправи допомагають дитині поступово звикнути до різних відчуттів.

Вправи:
- Насипте рис або пісок і сховайте іграшки
- Дайте дитині торкатись різних матеріалів
- Гра "вгадай на дотик"

Рекомендації:
- Починайте з приємних текстур
- Не змушуйте дитину
- 5-10 хвилин щодня',
    'TEXT',
    'HOME',
    NULL,
    true,
    NULL
FROM skills s
WHERE s.code = 'TACTILE';

INSERT INTO resources (
    id, skill_id, title, description, type, audience, url, is_published, created_by
)
SELECT
    gen_random_uuid(),
    s.id,
    'Тактильні сенсорні активності',
    'Відео з прикладами сенсорних вправ',
    'VIDEO',
    'HOME',
    'https://example.com/tactile-video',
    true,
    NULL
FROM skills s
WHERE s.code = 'TACTILE';


INSERT INTO resources (
    id, skill_id, title, description, content, type, audience, url, is_published, created_by
)
SELECT
    gen_random_uuid(),
    s.id,
    'Адаптація до звуків',
    'Зменшення чутливості до шумів',
    'Опис:
Допомагає дитині звикнути до різних звуків.

Вправи:
- Вмикайте тиху музику
- Поступово збільшуйте гучність
- Гра "знайди звук"

Рекомендації:
- Уникайте різких шумів
- Починайте з тихих звуків
- Використовуйте улюблені звуки',
    'TEXT',
    'HOME',
    NULL,
    true,
    NULL
FROM skills s
WHERE s.code = 'AUDITORY';

INSERT INTO resources (
    id, skill_id, title, description, type, audience, url, is_published, created_by
)
SELECT
    gen_random_uuid(),
    s.id,
    'Як зменшити шумове перевантаження',
    'Поради для батьків',
    'VIDEO',
    'HOME',
    'https://example.com/auditory-video',
    true,
    NULL
FROM skills s
WHERE s.code = 'AUDITORY';


INSERT INTO resources (
    id, skill_id, title, description, content, type, audience, url, is_published, created_by
)
SELECT
    gen_random_uuid(),
    s.id,
    'Візуальний порядок',
    'Зменшення візуального перевантаження',
    'Опис:
Допомагає дитині краще концентруватись.

Вправи:
- Приберіть зайві предмети
- Гра "знайди предмет"
- Сортування за кольорами

Рекомендації:
- Використовуйте спокійні кольори
- Уникайте яскравого світла
- Мінімалізм у просторі',
    'TEXT',
    'HOME',
    NULL,
    true,
    NULL
FROM skills s
WHERE s.code = 'VISUAL';

INSERT INTO resources (
    id, skill_id, title, description, type, audience, url, is_published, created_by
)
SELECT
    gen_random_uuid(),
    s.id,
    'Розвиток візуальної уваги',
    'Вправи для фокусу',
    'VIDEO',
    'HOME',
    'https://example.com/visual-video',
    true,
    NULL
FROM skills s
WHERE s.code = 'VISUAL';


INSERT INTO resources (
    id, skill_id, title, description, content, type, audience, url, is_published, created_by
)
SELECT
    gen_random_uuid(),
    s.id,
    'Вправи для тіла',
    'Розвиток координації',
    'Опис:
Допомагає розвинути баланс і контроль рухів.

Вправи:
- Стрибки
- Ходьба по лінії
- Перенесення предметів

Рекомендації:
- Робіть регулярно
- Додавайте гру
- Безпечне середовище',
    'TEXT',
    'HOME',
    NULL,
    true,
    NULL
FROM skills s
WHERE s.code = 'PROPRIOCEPTION';

INSERT INTO resources (
    id, skill_id, title, description, type, audience, url, is_published, created_by
)
SELECT
    gen_random_uuid(),
    s.id,
    'Баланс і координація',
    'Відео з вправами',
    'VIDEO',
    'HOME',
    'https://example.com/proprio-video',
    true,
    NULL
FROM skills s
WHERE s.code = 'PROPRIOCEPTION';


INSERT INTO resources (
    id, skill_id, title, description, content, type, audience, url, is_published, created_by
)
SELECT
    gen_random_uuid(),
    s.id,
    'Текстури їжі',
    'Звикання до нової їжі',
    'Опис:
Допомагає дитині приймати нові текстури.

Вправи:
- Маленькі порції нової їжі
- Гра "опиши смак"
- Різні текстури

Рекомендації:
- Не змушуйте
- Давайте вибір
- Хваліть дитину',
    'TEXT',
    'HOME',
    NULL,
    true,
    NULL
FROM skills s
WHERE s.code = 'ORAL';

INSERT INTO resources (
    id, skill_id, title, description, type, audience, url, is_published, created_by
)
SELECT
    gen_random_uuid(),
    s.id,
    'Оральні вправи',
    'Розвиток моторики',
    'VIDEO',
    'HOME',
    'https://example.com/oral-video',
    true,
    NULL
FROM skills s
WHERE s.code = 'ORAL';