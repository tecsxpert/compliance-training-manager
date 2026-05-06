CREATE TABLE IF NOT EXISTS audit_log (
                                         id SERIAL PRIMARY KEY,
                                         entity_type VARCHAR(100) NOT NULL,
    entity_id BIGINT NOT NULL,
    action VARCHAR(50) NOT NULL,
    changed_by VARCHAR(255),
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    old_value TEXT,
    new_value TEXT
    );

-- Composite index (important)
CREATE INDEX IF NOT EXISTS idx_entity_type_id
    ON audit_log(entity_type, entity_id);

-- Additional indexes
CREATE INDEX IF NOT EXISTS idx_action
    ON audit_log(action);

CREATE INDEX IF NOT EXISTS idx_changed_at
    ON audit_log(changed_at);