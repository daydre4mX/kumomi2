package test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import main.java.ems.handleSearch;
import main.java.auth.Authorization;
import main.java.databaseinteraction.DatabaseInteractor;
import main.java.types.Employee;

public class Search_Tester {

    Connection database;
    Authorization sessionTest;

    public Search_Tester() {
        database = DatabaseInteractor.getDatabaseConnection();
        sessionTest = new Authorization();
    }

    @Test
    void testGeneralEmployeeSearchRestriction() {

        boolean authorized = sessionTest.authorizeSessionState(database, "user101", "pass101");

        assertTrue(authorized, "Login must succeed.");
        assertEquals(Authorization.Role.EMPLOYEE, sessionTest.getRole());
        assertEquals(101, sessionTest.getEmployeeId(),
                "Session must be tied to employee ID 101.");

        Scanner sc = new Scanner("102\n");

        try {
            List<Employee> results = handleSearch.search(
                database,
                sessionTest,
                sc,
                false
            );

            assertNotNull(results,
                    "Search should return a restricted result when no exception is thrown.");

            assertEquals(1, results.size(),
                    "Restricted search should only return one record.");

            assertEquals(101, results.get(0).getEmployeeID(),
                    "Search must automatically restrict to the logged-in employee (101).");

        } catch (SecurityException ex) {

            assertEquals(
                "Employees may only view their own record.",
                ex.getMessage(),
                "General employees must not be allowed to view other employees."
            );
        }
    }
}
