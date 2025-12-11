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
    Connection database;

    public EmployeeModifyTester() {
        database = DatabaseInteractor.getDatabaseConnection();
    }

    @Test
    void testAddEmployee() {
        Employee emp = new Employee("111-111-1111", new Address(), LocalDate.now(), new Demographics(), new Division(),
                "me@you.com", 5,
                "Reimu", "Hakurei", 120000,
                new JobTitle());

        EmployeeModify.addEmployee(database, emp);

        try (PreparedStatement stmt = database.prepareStatement("SELECT * FROM employees WHERE empid = ?")) {
            stmt.setInt(1, emp.getEmployeeID());

            ResultSet rs = stmt.executeQuery();
            if (!rs.isBeforeFirst()) {
                fail();
            }

            while (rs.next()) {
                assertEquals(rs.getInt("empid"), emp.getEmployeeID());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    void testRemoveEmployee() {

    }
}
