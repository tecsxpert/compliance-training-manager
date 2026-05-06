# Compliance Training Manager — V5: seed sample training records
INSERT INTO training_records (title, description, category, status, priority, assigned_to, score, compliance_score, due_date, created_by)
VALUES
    ('GDPR Awareness Training', 'Annual GDPR compliance training for all employees', 'GDPR', 'COMPLETED', 'HIGH', 'all-staff', 92, 92.00, CURRENT_DATE - INTERVAL '10 days', 'system'),
    ('Cybersecurity Awareness', 'Phishing and social engineering awareness', 'SECURITY', 'PENDING', 'HIGH', 'all-staff', NULL, NULL, CURRENT_DATE + INTERVAL '14 days', 'system'),
    ('Anti-Bribery Training', 'ABC compliance policy training', 'COMPLIANCE', 'IN_PROGRESS', 'MEDIUM', 'managers', NULL, NULL, CURRENT_DATE + INTERVAL '7 days', 'system'),
    ('Health & Safety Induction', 'Workplace health and safety mandatory training', 'SAFETY', 'COMPLETED', 'LOW', 'all-staff', 88, 88.00, CURRENT_DATE - INTERVAL '30 days', 'system'),
    ('Data Handling Procedures', 'Proper data classification and handling', 'DATA', 'PENDING', 'HIGH', 'dev-team', NULL, NULL, CURRENT_DATE + INTERVAL '3 days', 'system'),
    ('Code of Conduct', 'Annual ethics and code of conduct refresher', 'ETHICS', 'OVERDUE', 'HIGH', 'all-staff', NULL, NULL, CURRENT_DATE - INTERVAL '5 days', 'system')
ON CONFLICT DO NOTHING;
