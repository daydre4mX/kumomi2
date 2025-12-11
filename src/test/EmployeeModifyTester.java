package test;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import main.java.databaseinteraction.DatabaseInteractor;
import main.java.empmanip.EmployeeModify;
import main.java.types.Address;
import main.java.types.Demographics;
import main.java.types.Division;
import main.java.types.Employee;
import main.java.types.JobTitle;

public class EmployeeModifyTester {

    Employee emp;
    Connection database;

    public EmployeeModifyTester() {
        database = DatabaseInteractor.getDatabaseConnection();

        emp = new Employee("111-111-1111", new Address(), LocalDate.now(), new Demographics(), new Division(),
                "me@you.com", 5,
                "Reimu", "Hakurei", 120000,
                new JobTitle());

        emp.getDivision().setId(1);
        emp.getDivision().setDivisionFromID(database);

        emp.getJobTitle().setJobTitleID(101);
        emp.getJobTitle().setJobTitleFromID(database);
    }

    @Test
    void testAddEmployee() {

        try (PreparedStatement stmt = database.prepareStatement("SELECT * FROM employees WHERE empid = ?")) {
            stmt.setInt(1, emp.getEmployeeID());

            ResultSet rs = stmt.executeQuery();
            if (rs.isBeforeFirst()) {
                EmployeeModify.terminateEmployee(database, emp.getEmployeeID());
            }

            EmployeeModify.addEmployee(database, emp);

            rs = stmt.executeQuery();
            while (rs.next()) {
                assertEquals(rs.getInt("empid"), emp.getEmployeeID());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error occurred while creating prepared statement");
        }

    }

    @Test
    void testRemoveEmployee() {
        EmployeeModify.addEmployee(database, emp);
        EmployeeModify.terminateEmployee(database, emp.getEmployeeID());

        try (PreparedStatement check = database.prepareStatement("SELECT 1 FROM employees WHERE empid = ?")) {
            check.setInt(1, emp.getEmployeeID());
            ResultSet rs = check.executeQuery();
            assertEquals(true, !rs.isBeforeFirst());
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }
}
