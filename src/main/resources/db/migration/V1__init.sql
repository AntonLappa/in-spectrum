DO $$ BEGIN
    CREATE TYPE user_type AS ENUM ('PARENT', 'EDUCATOR');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

DO $$ BEGIN
    CREATE TYPE user_role AS ENUM ('USER', 'ADMIN');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

DO $$ BEGIN
    CREATE TYPE resource_type AS ENUM ('VIDEO', 'TEXT', 'IMAGE');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

DO $$ BEGIN
    CREATE TYPE audience_type AS ENUM ('HOME', 'CLASS', 'BOTH');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

DO $$ BEGIN
    CREATE TYPE plan_item_status AS ENUM ('TODO', 'DONE', 'SKIPPED');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

CREATE TABLE IF NOT EXISTS users (
                                     id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                     email         TEXT NOT NULL UNIQUE,
                                     password_hash TEXT NOT NULL,
                                     user_type     user_type NOT NULL,
                                     role          user_role NOT NULL DEFAULT 'USER',
                                     is_active     BOOLEAN NOT NULL DEFAULT TRUE,
                                     created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
                                     updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS assessments (
                                           id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                           user_id       UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                           submitted_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
                                           answers_json  JSONB NOT NULL,
                                           result_json   JSONB,
                                           created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_assessments_user_id ON assessments(user_id);
CREATE INDEX IF NOT EXISTS idx_assessments_answers_gin ON assessments USING GIN (answers_json);

CREATE TABLE IF NOT EXISTS skills (
                                      id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                      code        TEXT NOT NULL UNIQUE,
                                      name        TEXT NOT NULL,
                                      created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
                                      updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS resources (
                                         id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                         skill_id      UUID NOT NULL REFERENCES skills(id),
                                         title         TEXT NOT NULL,
                                         description   TEXT,
                                         type          resource_type NOT NULL,
                                         audience      audience_type NOT NULL DEFAULT 'BOTH',
                                         url           TEXT NOT NULL,
                                         is_published  BOOLEAN NOT NULL DEFAULT TRUE,
                                         created_by    UUID REFERENCES users(id) ON DELETE SET NULL,
                                         created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
                                         updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_resources_skill_id ON resources(skill_id);
CREATE INDEX IF NOT EXISTS idx_resources_audience ON resources(audience);

CREATE TABLE IF NOT EXISTS plans (
                                     id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                     user_id        UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                     assessment_id  UUID REFERENCES assessments(id) ON DELETE SET NULL,
                                     title          TEXT NOT NULL DEFAULT 'Персональний план',
                                     created_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_plans_user_id ON plans(user_id);

CREATE TABLE IF NOT EXISTS plan_items (
                                          id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                          plan_id     UUID NOT NULL REFERENCES plans(id) ON DELETE CASCADE,
                                          resource_id UUID NOT NULL REFERENCES resources(id),
                                          status      plan_item_status NOT NULL DEFAULT 'TODO',
                                          sort_order  INT NOT NULL DEFAULT 0,
                                          created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
                                          UNIQUE (plan_id, resource_id)
);

CREATE INDEX IF NOT EXISTS idx_plan_items_plan_id ON plan_items(plan_id);
CREATE INDEX IF NOT EXISTS idx_plan_items_status ON plan_items(status);

CREATE TABLE IF NOT EXISTS progress_entries (
                                                id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                                plan_item_id UUID NOT NULL REFERENCES plan_items(id) ON DELETE CASCADE,
                                                entry_date   DATE NOT NULL DEFAULT CURRENT_DATE,
                                                note         TEXT,
                                                created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
                                                UNIQUE (plan_item_id, entry_date)
);

CREATE INDEX IF NOT EXISTS idx_progress_plan_item ON progress_entries(plan_item_id);
CREATE INDEX IF NOT EXISTS idx_progress_date ON progress_entries(entry_date);