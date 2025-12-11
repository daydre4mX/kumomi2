package main.java.empmanip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import main.java.types.Address;
import main.java.types.Demographics;
import main.java.types.Division;
import main.java.types.Employee;
import main.java.types.JobTitle;

/**
 * Search utilities for employees.
 * Builds employee objects with joined address/division/job/demographics data.
 * 
 * @author Jonathan Bell
 * @author Ahmed Cisse
 * @author Zerubbabel Ashenafi
 */
public class UserSearch {

    private static final String BASE_QUERY = "SELECT e.empid, e.Fname, e.Lname, e.email, e.HireDate, e.Salary, e.SSN, "
            + "a.street, a.city_id AS city_id, a.state_id AS state_id, a.postalCode, "
            + "d.gender, d.race, d.DOB, d.mobile, "
            + "ediv.div_ID, divs.Name AS division_name, "
            + "jt.job_title_id, jt.job_title "
            + "FROM employees e "
            + "LEFT JOIN address a ON e.empid = a.empid "
            + "LEFT JOIN demographics d ON e.empid = d.empid "
            + "LEFT JOIN employee_division ediv ON e.empid = ediv.empid "
            + "LEFT JOIN division divs ON ediv.div_ID = divs.ID "
            + "LEFT JOIN employee_job_titles ejt ON e.empid = ejt.empid "
            + "LEFT JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id";

    // Search by employee id.
    public static List<Employee> searchForEmployeeID(Connection myConn, int ID) {
        String sql = BASE_QUERY + " WHERE e.empid = ?";
        return executeQuery(myConn, sql, stmt -> stmt.setInt(1, ID));
    }

    // Search by employee first name.
    public static List<Employee> searchForEmployeeFirstName(Connection myConn, String fName) {
        String sql = BASE_QUERY + " WHERE e.Fname = ?";
        return executeQuery(myConn, sql, stmt -> stmt.setString(1, fName));
    }

    // Search by employee last name.
    public static List<Employee> searchForEmployeeLastName(Connection myConn, String lName) {
        String sql = BASE_QUERY + " WHERE e.Lname = ?";
        return executeQuery(myConn, sql, stmt -> stmt.setString(1, lName));
    }

    // Search by employee first and last name.
    public static List<Employee> searchForEmployeeFirstLastName(Connection myConn, String fName, String lName) {
        String sql = BASE_QUERY + " WHERE e.Fname = ? AND e.Lname = ?";
        return executeQuery(myConn, sql, stmt -> {
            stmt.setString(1, fName);
            stmt.setString(2, lName);
        });
    }

    // Search by SSN.
    public static List<Employee> searchForEmployeeSSN(Connection myConn, String SSN) {
        String sql = BASE_QUERY + " WHERE e.SSN = ?";
        return executeQuery(myConn, sql, stmt -> stmt.setString(1, SSN));
    }

    // Search by date of birth.
    public static List<Employee> searchForEmployeeDOB(Connection myConn, LocalDate dob) {
        String sql = BASE_QUERY + " WHERE d.DOB = ?";
        return executeQuery(myConn, sql, stmt -> stmt.setObject(1, dob));
    }

    private interface StatementPopulator {
        void populate(PreparedStatement stmt) throws SQLException;
    }

    // Executes a prepared query and maps resulting rows to Employee objects.
    private static List<Employee> executeQuery(Connection myConn, String sql, StatementPopulator populator) {
        ArrayList<Employee> results = new ArrayList<>();
        try (PreparedStatement ps = myConn.prepareStatement(sql)) {
            populator.populate(ps);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapEmployee(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return results;
    }

    private static Employee mapEmployee(ResultSet rs) throws SQLException {
        Employee nemp = new Employee();
        nemp.setEmployeeID(rs.getInt("empid"));
        nemp.setfName(rs.getString("Fname"));
        nemp.setlName(rs.getString("Lname"));
        nemp.setEmail(rs.getString("email"));
        if (rs.getDate("HireDate") != null) {
            nemp.setHireDate(rs.getDate("HireDate").toLocalDate());
        }
        nemp.setSalary(rs.getDouble("Salary"));
        nemp.setSSN(rs.getString("SSN"));

        Address address = new Address();
        address.setStreet(rs.getString("street"));
        address.setCity(rs.getString("city_id"));
        address.setState(rs.getString("state_id"));
        address.setPostalCode(rs.getString("postalCode"));
        nemp.setAddress(address);

        Division division = new Division();
        division.setId(rs.getInt("div_ID"));
        division.setName(rs.getString("division_name"));
        nemp.setDivision(division);

        JobTitle jobTitle = new JobTitle();
        jobTitle.setJobTitleID(rs.getInt("job_title_id"));
        jobTitle.setJobTitle(rs.getString("job_title"));
        nemp.setJobTitle(jobTitle);

        Demographics demo = new Demographics();
        String gender = rs.getString("gender");
        if (gender != null && !gender.isBlank()) {
            demo.setGender(gender.charAt(0));
        }
        demo.setRace(rs.getString("race"));
        if (rs.getDate("DOB") != null) {
            demo.setBirthDate(rs.getDate("DOB").toLocalDate());
        }
        demo.setPhoneNumber(rs.getString("mobile"));
        nemp.setDemographics(demo);

        return nemp;
    }
}
