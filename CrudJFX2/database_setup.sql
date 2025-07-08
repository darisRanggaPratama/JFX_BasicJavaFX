-- Database setup script for Customer Management System
-- Run this script in your MySQL database

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS test;
USE test;

-- Drop table if exists (for clean setup)
DROP TABLE IF EXISTS customer;

-- Create customer table
CREATE TABLE customer (
    idx INT AUTO_INCREMENT PRIMARY KEY,
    nik VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    born DATE,
    active BOOLEAN DEFAULT TRUE,
    salary INT DEFAULT 0
);

-- Insert sample data
INSERT INTO customer (nik, name, born, active, salary) VALUES
('1234567890123456', 'John Doe', '1990-01-15', TRUE, 5000000),
('2345678901234567', 'Jane Smith', '1985-03-22', TRUE, 7500000),
('3456789012345678', 'Bob Johnson', '1992-07-10', FALSE, 4500000),
('4567890123456789', 'Alice Brown', '1988-11-05', TRUE, 6000000),
('5678901234567890', 'Charlie Wilson', '1995-09-18', TRUE, 5500000),
('6789012345678901', 'Diana Davis', '1987-12-30', FALSE, 4000000),
('7890123456789012', 'Edward Miller', '1993-04-12', TRUE, 6500000),
('8901234567890123', 'Fiona Garcia', '1991-08-25', TRUE, 5800000),
('9012345678901234', 'George Martinez', '1989-06-14', FALSE, 4200000),
('0123456789012345', 'Helen Rodriguez', '1994-02-28', TRUE, 7000000);

-- Verify data insertion
SELECT COUNT(*) as total_records FROM customer;
SELECT * FROM customer LIMIT 5;
