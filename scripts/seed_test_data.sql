/* Quick seed data to exercise EMS features. Run after creating schema. */
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

/* Reference data */
INSERT INTO division (ID, Name, city, addressLine1, addressLine2, state, country, postalCode) VALUES
(1, 'Engineering', 'Atlanta', '123 Tech Way', NULL, 'GA', 'USA', '30303'),
(2, 'HR', 'Atlanta', '500 People St', NULL, 'GA', 'USA', '30303');

INSERT INTO job_titles (job_title_id, job_title) VALUES
(101, 'Software Engineer'),
(102, 'HR Specialist'),
(103, 'Manager');

/* Employees */
INSERT INTO employees (empid, Fname, Lname, email, HireDate, Salary, SSN) VALUES
(1001, 'Jane', 'Doe', 'jane.doe@example.com', '2022-01-15', 90000.00, '111-22-3333'),
(1002, 'John', 'Smith', 'john.smith@example.com', '2021-06-01', 75000.00, '222-33-4444'),
(1003, 'Alice', 'Admin', 'alice.admin@example.com', '2020-03-20', 120000.00, '333-44-5555');

INSERT INTO address (empid, street, city_id, state_id, postalCode) VALUES
(1001, '10 Main St', 'Atlanta', 'GA', '30303'),
(1002, '20 Peachtree Rd', 'Atlanta', 'GA', '30305'),
(1003, '30 Admin Ave', 'Atlanta', 'GA', '30306');

INSERT INTO demographics (empid, gender, race, DOB, mobile) VALUES
(1001, 'F', 'Human', '1990-05-05', '5551234567'),
(1002, 'M', 'Human', '1988-08-08', '5559876543'),
(1003, 'F', 'Human', '1985-09-09', '5551112222');

INSERT INTO employee_division (empid, div_ID) VALUES
(1001, 1),
(1002, 1),
(1003, 2);

INSERT INTO employee_job_titles (empid, job_title_id) VALUES
(1001, 101),
(1002, 103),
(1003, 102);

/* Payroll*/
INSERT INTO payroll (payID, pay_date, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care, empid) VALUES
(1, '2025-04-15', 3500.00, 400.00, 50.00, 215.00, 120.00, 150.00, 75.00, 1001),
(2, '2025-04-15', 5000.00, 600.00, 75.00, 300.00, 180.00, 225.00, 90.00, 1002),
(3, '2025-04-15', 7000.00, 900.00, 100.00, 434.00, 250.00, 300.00, 120.00, 1003);

/* Login accounts */
INSERT INTO user_accounts (username, password, role, empid) VALUES
('admin', 'adminpassword', 'admin', NULL),
('jane', 'janepassword', 'employee', 1001),
('john', 'johnpassword', 'employee', 1002);
