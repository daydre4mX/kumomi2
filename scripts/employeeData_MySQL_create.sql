/* 
    THESE ARE FOR dBeaver usage
    VS Code usage may give you unpredictable results. 	
*/

DROP DATABASE IF EXISTS employeeData;

CREATE DATABASE employeeData;

USE employeeData;

/***********************************************************************/

CREATE TABLE employees (
  empid INT NOT NULL,
  Fname VARCHAR(65) NOT NULL,
  Lname VARCHAR(65) NOT NULL,
  email VARCHAR(65) NOT NULL,
  HireDate DATE,
  Salary DECIMAL(10,2) NOT NULL,
  SSN VARCHAR(12),
  PRIMARY KEY (empid)
);

/***********************************************************************/

CREATE TABLE payroll (
  payID INT,
  pay_date DATE,
  earnings DECIMAL(8,2),
  fed_tax DECIMAL(7,2),
  fed_med DECIMAL(7,2),
  fed_SS DECIMAL(7,2),
  state_tax DECIMAL(7,2),
  retire_401k DECIMAL(7,2),
  health_care DECIMAL(7,2),
  empid INT,
  PRIMARY KEY (empid),
  foreign key (empid) references employees(empid)
);

/***********************************************************************/ 

CREATE TABLE job_titles (
  job_title_id INT,
  job_title VARCHAR(125) NOT NULL,
  PRIMARY KEY(job_title_id)
);

/***********************************************************************/ 

CREATE TABLE employee_job_titles (
  empid INT NOT NULL,
  job_title_id INT NOT null,
  foreign key (empid) references employees(empid),
  foreign key (job_title_id) references job_titles(job_title_id)
);

/***********************************************************************/ 

CREATE TABLE division (
  ID int NOT NULL,
  Name varchar(100) DEFAULT NULL,
  city varchar(50) NOT NULL,
  addressLine1 varchar(50) NOT NULL,
  addressLine2 varchar(50) DEFAULT NULL,
  state varchar(50) DEFAULT NULL,
  country varchar(50) NOT NULL,
  postalCode varchar(15) NOT null,
  primary key(ID)
);

/***********************************************************************/ 

CREATE TABLE employee_division (
  empid int NOT NULL,
  div_ID int NOT NULL,
  PRIMARY KEY (empid),
  foreign key (empid) references employees(empid),
  foreign key (div_ID) references division(ID)
);

/***********************************************************************/ 

create table address(
	street varchar(50) not null,
	city_id varchar(20) DEFAULT NULL,
	state_id varchar(50) default null,
	postalCode varchar(15) not null,
	empid int not null,
	primary key (empid),
	foreign key (empid) references employees(empid)
	);

/***********************************************************************/ 
create table demographics(
	gender char(1) default null,
	race varchar(50) default null,
	DOB date default NULL,
	mobile varchar(10) default null,
	empid int not null,
	primary key (empid),
	foreign key (empid) references employees(empid)
);

/***********************************************************************/

CREATE TABLE IF NOT EXISTS user_accounts (
  username VARCHAR(100) PRIMARY KEY,
  password VARCHAR(255) NOT NULL,
  role ENUM('admin','employee') NOT NULL,
  empid INT NULL,
  FOREIGN KEY (empid) REFERENCES employees(empid)
);

/***********************************************************************/

create table passwords(
	user varchar(100) not null,
	password varchar(255) default null,
	primary key(user)
);
