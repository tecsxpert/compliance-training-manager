-- ============================================
-- V4: Additional performance indexes
-- ============================================

-- training_records composite indexes
CREATE INDEX IF NOT EXISTS idx_tr_status_due
    ON training_records(status, due_date);

CREATE INDEX IF NOT EXISTS idx_tr_assigned_to
    ON training_records(assigned_to);

CREATE INDEX IF NOT EXISTS idx_tr_category_status
    ON training_records(category, status);

-- audit_log quick lookup
CREATE INDEX IF NOT EXISTS idx_audit_performed_by
    ON audit_log(performed_by);

-- users lookup by email
CREATE INDEX IF NOT EXISTS idx_users_email
    ON users(email);