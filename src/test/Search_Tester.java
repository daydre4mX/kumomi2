package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import main.java.ems.handleSearch;
import main.java.databaseinteraction.DatabaseInteractor;
import main.java.empmanip.UserSearch;
import main.java.types.Employee;

public class Search_Tester {

        Connection database;

        public Search_Tester() {
                database = DatabaseInteractor.getDatabaseConnection();
        }

        /*
         * Test for Search by Employee ID, should have a Employee whose ID matches the
         * one below
         */
        @Test
        void testSearchforID() {
                int ID = 1001;
                List<Employee> result = UserSearch.searchForEmployeeID(database, ID);

                assertNotNull(result);
                assertEquals(1, result.size(), "If correrect, return a single employee.");

                Employee emp = result.get(0);
                assertEquals(ID, emp.getEmployeeID());
                assertNotNull(emp.getfName(), "First name must be populated");
                assertNotNull(emp.getlName(), "Last name must be populated");
                assertNotNull(emp.getJobTitle(), "Job Title name must be populated");
                assertNotNull(emp.getAddress(), "Address name must be populated");
                assertNotNull(emp.getDivision(), "Division name must be populated");
                assertNotNull(emp.getDemographics(), "Demo name must be populated");
        }

        @Test
        void testSearchforSSN() {
                List<Employee> result = UserSearch.searchForEmployeeSSN(database, "111-22-3333");

                assertNotNull(result);
                assertEquals(1, result.size(), "If correrect, return a single employee.");

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
                                        false);

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
                                        "General employees must not be allowed to view other employees.");
                }
        }
}
