CREATE TABLE training_record (
                                 id SERIAL PRIMARY KEY,
                                 title VARCHAR(255) NOT NULL,
                                 description TEXT,
                                 status VARCHAR(50) NOT NULL,
                                 priority VARCHAR(50),
                                 assigned_to VARCHAR(255) NOT NULL,
                                 due_date DATE,
                                 score INT,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_status ON training_record(status);
CREATE INDEX idx_due_date ON training_record(due_date);