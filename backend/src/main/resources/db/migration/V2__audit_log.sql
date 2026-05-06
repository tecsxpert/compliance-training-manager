-- ============================================
-- V2: Create audit_log table
-- ============================================
CREATE TABLE IF NOT EXISTS audit_log (
    id           BIGSERIAL PRIMARY KEY,
    entity_type  VARCHAR(100)  NOT NULL,
    entity_id    BIGINT        NOT NULL,
    action       VARCHAR(50)   NOT NULL,
    old_value    TEXT,
    new_value    TEXT,
    performed_by VARCHAR(255),
    performed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_audit_entity   ON audit_log(entity_type, entity_id);
CREATE INDEX IF NOT EXISTS idx_audit_action   ON audit_log(action);
CREATE INDEX IF NOT EXISTS idx_audit_performed_at ON audit_log(performed_at);