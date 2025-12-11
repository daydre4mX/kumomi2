package test;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Date;
import java.util.ArrayList;
import main.java.databaseinteraction.DatabaseInteractor;
import main.java.empmanip.UserSearch;
import main.java.types.Employee;

public class Search_Tester {

        Connection database;

        public Search_Tester() {
                database = DatabaseInteractor.getDatabaseConnection();
        }

        /* Test for Search by Employee ID, Returns information if in DB */
        @Test
        void testSearchforID() {
                ArrayList<Employee> result = UserSearch.searchForEmployeeID(database, 1001);

                assertNotNull(result);
                assertEquals(1, result.size(), "If correrect, return a single employee.");

                Employee emp = result.get(0);
                assertEquals(101, emp.getEmployeeID());
                assertNotNull(emp.getfName(), "First name must be populated");
                assertNotNull(emp.getlName(), "Last name must be populated");
                assertNotNull(emp.getJobTitle(), "Job Title name must be populated");
                assertNotNull(emp.getAddress(), "Address name must be populated");
                assertNotNull(emp.getDivision(), "Division name must be populated");
                assertNotNull(emp.getDemographics(), "Demo name must be populated");
        }

        /*
         * Test for Search by Employee SSN, If SSN correct, returns matching SSN from
         * employee that it found.
         */
        @Test
        void testSearchforSSN() {
                ArrayList<Employee> result = UserSearch.searchForEmployeeSSN(database, "111-22-3333");

                assertNotNull(result);
                assertEquals(1, result.size(), "If correrect, return a single employee.");

                Employee emp = result.get(0);
                assertEquals("111-22-3333", emp.getSSN());
        }

        /*
         * Test for Search by Employee DOB, If DOB correct, returns matching DOB from
         * employee that it found.
         */
        @Test
        void testSearchforDOB() {
                LocalDate birthday = LocalDate.of(1990, 05, 05);

                ArrayList<Employee> result = UserSearch.searchForEmployeeDOB(database, birthday);

                assertNotNull(result);
                assertEquals(result.isEmpty(), "If correct, return a single employee.");

                assertEquals(birthday, result.get(0).getDemographics().getBirthDate());

        }
}
