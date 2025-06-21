-- Customer Management System Database Setup
-- Database: test
-- User: rangga
-- Password: rangga

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS test;
USE test;

-- Create customer table
CREATE TABLE IF NOT EXISTS customer (
    idx int(11) NOT NULL AUTO_INCREMENT,
    nik varchar(6) NOT NULL,
    name varchar(50) NOT NULL,
    born date DEFAULT NULL,
    active tinyint(4) DEFAULT NULL,
    salary int(11) DEFAULT NULL,
    PRIMARY KEY (idx)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Insert sample data
INSERT INTO customer (nik, name, born, active, salary) VALUES
('845723', 'Eren Yeager', '2000-03-30', 1, 7500000),
('319604', 'Mikasa Ackerman', '2000-02-10', 1, 8000000),
('672198', 'Armin Arlert', '2000-11-03', 1, 6500000),
('503267', 'Levi Ackerman', '1985-12-25', 1, 12000000),
('927415', 'Erwin Smith', '1980-07-14', 0, 9500000),
('184629', 'Historia Reiss', '2001-01-15', 1, 7000000),
('756341', 'Jean Kirstein', '2000-04-07', 1, 6800000),
('492857', 'Connie Springer', '2000-05-02', 1, 6200000),
('638174', 'Sasha Blouse', '2000-07-26', 0, 6300000),
('815396', 'Reiner Braun', '1999-08-01', 1, 8500000),
('274658', 'Bertolt Hoover', '1999-12-30', 0, 7800000),
('593827', 'Annie Leonhart', '2000-03-22', 1, 8200000),
('467192', 'Ymir', '1998-02-17', 0, 7200000),
('738465', 'Marco Bott', '2000-06-16', 1, 6600000),
('951283', 'Christa Lenz', '2001-01-15', 1, 6900000);

-- Display inserted data
SELECT * FROM customer ORDER BY idx;
