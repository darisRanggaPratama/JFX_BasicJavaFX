-- Create the customers database
CREATE DATABASE IF NOT EXISTS testing;
USE testing;

-- Create the customers table
CREATE TABLE IF NOT EXISTS customer (
    nik VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    born DATE NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    salary DECIMAL(15,2) NOT NULL DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert sample data
INSERT INTO customers (nik, name, born, active, salary) VALUES
('1234567890', 'John Doe', '1990-01-15', true, 5000000.00),
('1234567891', 'Jane Smith', '1992-03-20', true, 6000000.00),
('1234567892', 'Bob Johnson', '1988-07-10', false, 4500000.00);
