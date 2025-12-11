-- Reset and seed everything
USE employeeData;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE payroll;
TRUNCATE TABLE employee_job_titles;
TRUNCATE TABLE employee_division;
TRUNCATE TABLE address;
TRUNCATE TABLE demographics;
TRUNCATE TABLE user_accounts;
TRUNCATE TABLE employees;
TRUNCATE TABLE division;
TRUNCATE TABLE job_titles;
SET FOREIGN_KEY_CHECKS = 1;

-- Divisions
INSERT INTO division (ID, Name, city, addressLine1, addressLine2, state, country, postalCode) VALUES
(1, 'Engineering', 'Atlanta', '123 Tech Way', NULL, 'GA', 'USA', '30303'),
(2, 'HR',          'Atlanta', '500 People St', NULL, 'GA', 'USA', '30303'),
(3, 'Finance',     'Atlanta', '900 Money Ave', NULL, 'GA', 'USA', '30303');

-- Job titles
INSERT INTO job_titles (job_title_id, job_title) VALUES
(101, 'Software Engineer'),
(102, 'HR Specialist'),
(103, 'Finance Analyst'),
(104, 'Manager');

-- Employees (spread hire dates for date-range testing)
INSERT INTO employees (empid, Fname, Lname, email, HireDate, Salary, SSN) VALUES
(1001, 'Jane',  'Doe',   'jane.doe@example.com',  '2022-01-15', 90000.00, '111-22-3333'),
(1002, 'John',  'Smith', 'john.smith@example.com', '2021-06-01', 75000.00, '222-33-4444'),
(1003, 'Alice', 'Admin', 'alice.admin@example.com', '2020-03-20', 120000.00,'333-44-5555'),
(1004, 'Bob',   'Finance','bob.finance@example.com','2023-02-10', 68000.00, '444-55-6666');

-- Addresses
INSERT INTO address (empid, street, city_id, state_id, postalCode) VALUES
(1001, '10 Main St',    'Atlanta', 'GA', '30303'),
(1002, '20 Peachtree',  'Atlanta', 'GA', '30305'),
(1003, '30 Admin Ave',  'Atlanta', 'GA', '30306'),
(1004, '40 Ledger Ln',  'Atlanta', 'GA', '30307');

-- Demographics
INSERT INTO demographics (empid, gender, race, DOB, mobile) VALUES
(1001, 'F', 'Human', '1990-05-05', '5551234567'),
(1002, 'M', 'Human', '1988-08-08', '5559876543'),
(1003, 'F', 'Human', '1985-09-09', '5551112222'),
(1004, 'M', 'Human', '1992-03-03', '5553334444');

-- Division links
INSERT INTO employee_division (empid, div_ID) VALUES
(1001, 1),
(1002, 1),
(1003, 2),
(1004, 3);

-- Job title links
INSERT INTO employee_job_titles (empid, job_title_id) VALUES
(1001, 101),
(1002, 104),
(1003, 102),
(1004, 103);

-- Payroll (one row per employee due to PK on payroll.empid)
INSERT INTO payroll (payID, pay_date, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care, empid) VALUES
(1, '2025-04-15', 3500.00, 400.00, 50.00, 215.00, 120.00, 150.00, 75.00, 1001),
(2, '2025-04-15', 5000.00, 600.00, 75.00, 300.00, 180.00, 225.00, 90.00, 1002),
(3, '2025-04-15', 7000.00, 900.00, 100.00, 434.00, 250.00, 300.00, 120.00, 1003),
(4, '2025-04-15', 4200.00, 500.00, 60.00, 250.00, 150.00, 180.00, 85.00, 1004);

-- Login accounts (admin + employees)
INSERT INTO user_accounts (username, password, role, empid) VALUES
('admin', 'adminpassword', 'admin', NULL),
('jane',  'janepassword',  'employee', 1001),
('john',  'johnpassword',  'employee', 1002),
('bobf',  'bobpassword',   'employee', 1004);
