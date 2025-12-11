package main.java.empmanip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.types.Employee;

/**
 * Class holding methods to modify employees in the database.
 * Provides transactional add/update/delete helpers plus salary updates.
 * 
 * @author Zerubbabel Ashenafi
 * @author Jonathan Bell
 */
public class EmployeeModify {

    // Deletes an employee and related records in a single transaction, including login mappings.
    public static boolean removeEmployee(Connection database, Employee employee) {
        try {
            database.setAutoCommit(false);

            PreparedStatement removeFromUserAccounts = database
                    .prepareStatement("DELETE FROM user_accounts WHERE empid = ?");
            PreparedStatement removeFromEmployeeTable = database
                    .prepareStatement("DELETE FROM employees WHERE empid = ?");
            PreparedStatement removeFromAddressTable = database
                    .prepareStatement("DELETE FROM address WHERE empid = ?");
            PreparedStatement removeFromDemographicsTable = database
                    .prepareStatement("DELETE FROM demographics WHERE empid = ?");
            PreparedStatement removeFromEmployeeDivisionTable = database
                    .prepareStatement("DELETE FROM employee_division WHERE empid = ?");
            PreparedStatement removeFromPayrollTable = database
                    .prepareStatement("DELETE FROM payroll WHERE empid = ?");
            PreparedStatement removeFromJobTitleTable = database
                    .prepareStatement("DELETE FROM employee_job_titles WHERE empid = ?");

            removeFromUserAccounts.setInt(1, employee.getEmployeeID());
            removeFromEmployeeTable.setInt(1, employee.getEmployeeID());
            removeFromAddressTable.setInt(1, employee.getEmployeeID());
            removeFromDemographicsTable.setInt(1, employee.getEmployeeID());
            removeFromEmployeeDivisionTable.setInt(1, employee.getEmployeeID());
            removeFromPayrollTable.setInt(1, employee.getEmployeeID());
            removeFromJobTitleTable.setInt(1, employee.getEmployeeID());

            removeFromUserAccounts.execute();
            removeFromAddressTable.execute();
            removeFromDemographicsTable.execute();
            removeFromEmployeeDivisionTable.execute();
            removeFromPayrollTable.execute();
            removeFromJobTitleTable.execute();
            removeFromEmployeeTable.execute();

            removeFromUserAccounts.close();
            removeFromEmployeeTable.close();
            removeFromAddressTable.close();
            removeFromDemographicsTable.close();
            removeFromEmployeeDivisionTable.close();
            removeFromPayrollTable.close();
            removeFromJobTitleTable.close();

            database.commit();
            database.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            try {
                database.rollback();
                database.setAutoCommit(true);
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }

    // Inserts an employee and dependent records; enforces unique id and SSN before writing.
    public static boolean addEmployee(Connection database, Employee employee) {
        try (PreparedStatement checkDuplicate = database.prepareStatement("SELECT 1 FROM employees WHERE empid = ?")) {
            checkDuplicate.setInt(1, employee.getEmployeeID());
            ResultSet doesEmployeeExist = checkDuplicate.executeQuery();
            if (doesEmployeeExist.isBeforeFirst()) {
                System.out.println("Employee ID already in database!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        try {
            database.setAutoCommit(false);

            try (PreparedStatement checkSsn = database
                    .prepareStatement("SELECT 1 FROM employees WHERE SSN = ? AND empid <> ?")) {
                checkSsn.setString(1, employee.getSSN());
                checkSsn.setInt(2, employee.getEmployeeID());
                ResultSet duplicateSsn = checkSsn.executeQuery();
                if (duplicateSsn.isBeforeFirst()) {
                    System.out.println("SSN already in database!");
                    database.setAutoCommit(true);
                    return false;
                }
            }

            PreparedStatement addEmployee = database.prepareStatement(
                    "INSERT INTO employees (empid, Fname, Lname, email, HireDate, Salary, SSN) VALUES (?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement addDivision = database
                    .prepareStatement("INSERT INTO employee_division (empid, div_ID) VALUES (?, ?)");
            PreparedStatement addAddress = database.prepareStatement(
                    "INSERT INTO address (empid, street, city_id, state_id, postalCode) VALUES (?, ?, ?, ?, ?)");
            PreparedStatement addJobTitle = database
                    .prepareStatement("INSERT INTO employee_job_titles (empid, job_title_id) VALUES (?, ?)");
            PreparedStatement addDemographics = database.prepareStatement(
                    "INSERT INTO demographics (empid, gender, race, DOB, mobile) VALUE (?, ?, ?, ?, ?)");

            addEmployee.setInt(1, employee.getEmployeeID());
            addEmployee.setString(2, employee.getfName());
            addEmployee.setString(3, employee.getlName());
            addEmployee.setString(4, employee.getEmail());
            addEmployee.setObject(5, employee.getHireDate());
            addEmployee.setDouble(6, employee.getSalary());
            addEmployee.setString(7, employee.getSSN());

            addDivision.setInt(1, employee.getEmployeeID());
            addDivision.setInt(2, employee.getDivision().getId());

            addAddress.setInt(1, employee.getEmployeeID());
            addAddress.setString(2, employee.getAddress().getStreet());
            addAddress.setString(3, employee.getAddress().getCity());
            addAddress.setString(4, employee.getAddress().getState());
            addAddress.setString(5, employee.getAddress().getPostalCode());

            addJobTitle.setInt(1, employee.getEmployeeID());
            addJobTitle.setInt(2, employee.getJobTitle().getJobTitleID());

            addDemographics.setInt(1, employee.getEmployeeID());
            addDemographics.setString(2, Character.toString(employee.getDemographics().getGender()));
            addDemographics.setString(3, employee.getDemographics().getRace());
            addDemographics.setObject(4, employee.getDemographics().getBirthDate());
            addDemographics.setString(5, employee.getDemographics().getPhoneNumber());

            addEmployee.execute();
            addDivision.execute();
            addAddress.execute();
            addJobTitle.execute();
            addDemographics.execute();

            addEmployee.close();
            addDivision.close();
            addAddress.close();
            addJobTitle.close();
            addDemographics.close();

            database.commit();
            database.setAutoCommit(true);
            System.out.println("Employee added to database!");
            return true;
        } catch (SQLException e) {
            try {
                database.rollback();
                database.setAutoCommit(true);
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }

    // Updates all related tables with data from the provided employee object.
    public static boolean updateEmployee(Connection database, Employee employee) {
        try {
            database.setAutoCommit(false);

            PreparedStatement updateEmployee = database.prepareStatement(
                    "UPDATE employees SET Fname = ?, Lname = ?, email = ?, HireDate = ?, Salary = ?, SSN = ? WHERE empid = ?");
            PreparedStatement updateDivision = database
                    .prepareStatement("UPDATE employee_division SET div_ID = ? WHERE empid = ?");
            PreparedStatement updateAddress = database.prepareStatement(
                    "UPDATE address SET street = ?, city_id = ?, state_id = ?, postalCode = ? WHERE empid = ?");
            PreparedStatement updateJobTitle = database
                    .prepareStatement("UPDATE employee_job_titles SET job_title_id = ? WHERE empid = ?");
            PreparedStatement updateDemographics = database.prepareStatement(
                    "UPDATE demographics SET gender = ?, race = ?, DOB = ?, mobile = ? WHERE empid = ?");

            updateEmployee.setString(1, employee.getfName());
            updateEmployee.setString(2, employee.getlName());
            updateEmployee.setString(3, employee.getEmail());
            updateEmployee.setObject(4, employee.getHireDate());
            updateEmployee.setDouble(5, employee.getSalary());
            updateEmployee.setString(6, employee.getSSN());
            updateEmployee.setInt(7, employee.getEmployeeID());

            updateDivision.setInt(1, employee.getDivision().getId());
            updateDivision.setInt(2, employee.getEmployeeID());

            updateAddress.setString(1, employee.getAddress().getStreet());
            updateAddress.setString(2, employee.getAddress().getCity());
            updateAddress.setString(3, employee.getAddress().getState());
            updateAddress.setString(4, employee.getAddress().getPostalCode());
            updateAddress.setInt(5, employee.getEmployeeID());

            updateJobTitle.setInt(1, employee.getJobTitle().getJobTitleID());
            updateJobTitle.setInt(2, employee.getEmployeeID());

            updateDemographics.setString(1, Character.toString(employee.getDemographics().getGender()));
            updateDemographics.setString(2, employee.getDemographics().getRace());
            updateDemographics.setObject(3, employee.getDemographics().getBirthDate());
            updateDemographics.setString(4, employee.getDemographics().getPhoneNumber());
            updateDemographics.setInt(5, employee.getEmployeeID());

            updateEmployee.executeUpdate();
            updateDivision.executeUpdate();
            updateAddress.executeUpdate();
            updateJobTitle.executeUpdate();
            updateDemographics.executeUpdate();

            updateEmployee.close();
            updateDivision.close();
            updateAddress.close();
            updateJobTitle.close();
            updateDemographics.close();

            database.commit();
            database.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            try {
                database.rollback();
                database.setAutoCommit(true);
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }

    // Removes an employee after verifying existence.
    public static boolean terminateEmployee(Connection database, int empId) {
        try (PreparedStatement check = database.prepareStatement("SELECT 1 FROM employees WHERE empid = ?")) {
            check.setInt(1, empId);
            ResultSet rs = check.executeQuery();
            if (!rs.isBeforeFirst()) {
                System.out.println("Employee not found.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        Employee temp = new Employee();
        temp.setEmployeeID(empId);
        return removeEmployee(database, temp);
    }

    // Applies a percentage raise to employees within the salary bounds.
    public static int updateSalaryRange(Connection database, double minSalary, double maxSalary, double percentage) {
        String sql = "UPDATE employees SET Salary = Salary * (1 + (? / 100)) WHERE Salary >= ? AND Salary < ?";
        try (PreparedStatement stmt = database.prepareStatement(sql)) {
            stmt.setDouble(1, percentage);
            stmt.setDouble(2, minSalary);
            stmt.setDouble(3, maxSalary);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
