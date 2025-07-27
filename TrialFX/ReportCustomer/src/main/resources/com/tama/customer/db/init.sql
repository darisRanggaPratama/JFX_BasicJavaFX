-- Drop table if exists
DROP TABLE IF EXISTS customer;

-- Create customers table
CREATE TABLE customer (
    idx INT AUTO_INCREMENT PRIMARY KEY,
    nik VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    born DATE NOT NULL,
    active BOOLEAN DEFAULT true,
    salary INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_salary CHECK (salary >= 0),
    INDEX idx_customer_nik (nik),
    INDEX idx_customer_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert sample data
INSERT INTO customer (nik, name, born, active, salary) VALUES
('1234567890', 'John Doe', '1990-01-15', true, 5000000),
('9876543210', 'Jane Smith', '1992-05-20', true, 6000000),
('4567890123', 'Bob Johnson', '1988-11-30', false, 4500000);
