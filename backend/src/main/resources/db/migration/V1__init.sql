-- ============================================
-- V1: Create training_records table (complete schema)
-- ============================================
CREATE TABLE IF NOT EXISTS training_records (
    id               BIGSERIAL PRIMARY KEY,
    title            VARCHAR(255)   NOT NULL,
    description      TEXT,
    category         VARCHAR(100),
    status           VARCHAR(50)    NOT NULL DEFAULT 'PENDING',
    priority         VARCHAR(50),
    compliance_score NUMERIC(5, 2),
    score            INT,
    assigned_to      VARCHAR(255),
    due_date         DATE,
    completed_date   DATE,
    ai_description   TEXT,
    ai_recommendations TEXT,
    file_path        VARCHAR(500),
    file_name        VARCHAR(255),
    created_by       VARCHAR(255),
    updated_by       VARCHAR(255),
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_training_status   ON training_records(status);
CREATE INDEX IF NOT EXISTS idx_training_due_date ON training_records(due_date);
CREATE INDEX IF NOT EXISTS idx_training_title    ON training_records(title);
CREATE INDEX IF NOT EXISTS idx_training_category ON training_records(category);