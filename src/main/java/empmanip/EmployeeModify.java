package main.java.empmanip;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

            removeFromEmployeeTable.executeQuery();
            removeFromAddressTable.executeQuery();
            removeFromDemographicsTable.executeQuery();
            removeFromEmployeeDivisionTable.executeQuery();
            removeFromPayrollTable.executeQuery();
            removeFromJobTitleTable.executeQuery();

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
        try (PreparedStatement addEmployee = database.prepareStatement(
                "INSERT INTO employees (empid, Fname, Lname, email, HireDate, Salary, SSN) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            addEmployee.setInt(1, employee.getEmployeeID());
            addEmployee.setString(2, employee.getfName());
            addEmployee.setString(3, employee.getlName());
            addEmployee.setString(4, employee.getEmail());
            addEmployee.setObject(5, employee.getHireDate());
            addEmployee.setDouble(6, employee.getSalary());
            addEmployee.setString(7, employee.getSSN());

            addEmployee.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
