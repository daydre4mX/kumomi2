package main.java.empmanip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.types.Employee;

/**
 * Class holding methods to modify employees in the database.
 * 
 * @author Zerubbabel Ashenafi
 */
public class EmployeeModify {

    /**
     * Removes the given employee from the database given.
     * 
     * @param database The connection to a database with an employees table
     * @param employee The employee to remove.
     */
    public static void removeEmployee(Connection database, Employee employee) {
        try {
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

            removeFromEmployeeTable.setInt(1, employee.getEmployeeID());
            removeFromAddressTable.setInt(1, employee.getEmployeeID());
            removeFromDemographicsTable.setInt(1, employee.getEmployeeID());
            removeFromEmployeeDivisionTable.setInt(1, employee.getEmployeeID());
            removeFromPayrollTable.setInt(1, employee.getEmployeeID());
            removeFromJobTitleTable.setInt(1, employee.getEmployeeID());

            removeFromAddressTable.execute();
            removeFromDemographicsTable.execute();
            removeFromEmployeeDivisionTable.execute();
            removeFromPayrollTable.execute();
            removeFromJobTitleTable.execute();
            removeFromEmployeeTable.execute();

            removeFromEmployeeTable.close();
            removeFromAddressTable.close();
            removeFromDemographicsTable.close();
            removeFromEmployeeDivisionTable.close();
            removeFromPayrollTable.close();
            removeFromJobTitleTable.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds the given employee to the database given.
     * 
     * @param database The connection to a database with an employees table
     * @param employee The employee to add.
     */
    public static void addEmployee(Connection database, Employee employee) {
        // Check if employee already in database.

        try (PreparedStatement checkDuplicate = database.prepareStatement("SELECT 1 FROM employees WHERE empid = ?")) {
            checkDuplicate.setInt(1, employee.getEmployeeID());

            ResultSet doesEmployeeExist = checkDuplicate.executeQuery();

            // If any employee shows up in the result set, return early.
            if (doesEmployeeExist.isBeforeFirst()) {
                System.out.println("Employee ID already in database!");
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
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

            System.out.println("Employee added to database!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
