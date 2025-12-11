package main.java;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import main.java.auth.Authorization;
import main.java.auth.Authorization.Role;
import main.java.databaseinteraction.DatabaseInteractor;
import main.java.empmanip.EmployeeModify;
import main.java.empmanip.UserSearch;
import main.java.types.Address;
import main.java.types.Demographics;
import main.java.types.Division;
import main.java.types.Employee;
import main.java.types.JobTitle;
import main.java.types.Payroll;

/**
 * Entry point for the Employee Management System.
 * 
 */
/**
 * Entry point and CLI flow controller for the Employee Management System.
 * Handles role-aware menus and dispatches to CRUD/report helpers.
 * 
 * @author Zerubbabel Ashenafi
 * @author Jonathan Bell
 */
public class EMS {

    public static void main(String[] args) throws Exception {

        Authorization sessionState = new Authorization();

        // Connect to the database given in config.properties
        try (Connection employeeDatabase = DatabaseInteractor.getDatabaseConnection()) {

            if (employeeDatabase == null) {
                System.out.println("Error in connecting to database, is your configuration setup properly?");
                System.exit(1);
            }

            System.out.println("Connected to database successfully.");
            Scanner sc = new Scanner(System.in);

            while (true) {
                if (sessionState.getRole() == Role.NONE) {
                    System.out.println("1. Login");
                    System.out.println("Type exit to quit.");
                    String input = sc.nextLine();
                    if (input.equals("1")) {
                        loginFlow(employeeDatabase, sessionState, sc);
                    } else if (input.equalsIgnoreCase("exit")) {
                        System.out.println("Exiting Employee Management System!");
                        employeeDatabase.close();
                        System.exit(0);
                    }
                } else if (sessionState.getRole() == Role.ADMIN) {
                    System.out.println("Admin Menu");
                    System.out.println("1. Logout");
                    System.out.println("2. Search for employee(s)");
                    System.out.println("3. Add Employee");
                    System.out.println("4. Update Employee");
                    System.out.println("5. Remove/Terminate Employee");
                    System.out.println("6. Update salaries within a range");
                    System.out.println("7. View pay statements for an employee");
                    System.out.println("8. Total monthly pay by job title");
                    System.out.println("9. Total monthly pay by division");
                    System.out.println("10. Employees hired within date range");
                    System.out.println("Type exit to quit.");
                    String input = sc.nextLine();
                    switch (input) {
                        case "1":
                            System.out.println("Logging out!");
                            sessionState.deElevateState();
                            break;
                        case "2":
                            handleSearch(employeeDatabase, sessionState, sc, true);
                            break;
                        case "3":
                            handleAddEmployee(employeeDatabase, sc);
                            break;
                        case "4":
                            handleUpdateEmployee(employeeDatabase, sc);
                            break;
                        case "5":
                            handleRemoveEmployee(employeeDatabase, sc);
                            break;
                        case "6":
                            handleSalaryRangeUpdate(employeeDatabase, sc);
                            break;
                        case "7":
                            handlePayHistory(employeeDatabase, sessionState, sc, true);
                            break;
                        case "8":
                            handleMonthlyPayByJobTitle(employeeDatabase, sc);
                            break;
                        case "9":
                            handleMonthlyPayByDivision(employeeDatabase, sc);
                            break;
                        case "10":
                            handleHireDateRangeReport(employeeDatabase, sc);
                            break;
                        case "exit":
                            System.out.println("Exiting Employee management system!");
                            employeeDatabase.close();
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Invalid option.");
                    }
                } else {
                    System.out.println("Employee Menu");
                    System.out.println("1. Search/view my record");
                    System.out.println("2. View my pay statements");
                    System.out.println("3. Logout");
                    System.out.println("Type exit to quit.");

                    String input = sc.nextLine();
                    switch (input) {
                        case "1":
                            handleSearch(employeeDatabase, sessionState, sc, false);
                            break;
                        case "2":
                            handlePayHistory(employeeDatabase, sessionState, sc, false);
                            break;
                        case "3":
                            System.out.println("Logging out!");
                            sessionState.deElevateState();
                            break;
                        case "exit":
                            System.out.println("Exiting Employee management system!");
                            employeeDatabase.close();
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Invalid option.");
                    }
                }

            }

        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e.getLocalizedMessage());
        }

    }

    private static void loginFlow(Connection employeeDatabase, Authorization sessionState, Scanner sc) {
        while (sessionState.getRole() == Role.NONE) {
            System.out.println("Enter a username:");
            String user = sc.nextLine();
            System.out.println("Enter a password:");
            String password = sc.nextLine();

            if (!sessionState.authorizeSessionState(employeeDatabase, user, password)) {
                System.out.println("Login failed, try again? (y/n)");
                String retry = sc.nextLine();
                if (!retry.equalsIgnoreCase("y")) {
                    break;
                }
            }
        }
    }

    private static void handleSearch(Connection db, Authorization sessionState, Scanner sc, boolean admin) {
        if (!admin) {
            Integer empId = sessionState.getEmployeeId();
            if (empId == null) {
                System.out.println("Unable to determine your employee record.");
                return;
            }
            List<Employee> employees = UserSearch.searchForEmployeeID(db, empId);
            if (employees.isEmpty()) {
                System.out.println("No matching employee found.");
            } else {
                employees.forEach(EMS::printEmployee);
            }
            return;
        }

        System.out.println("Search by: 1) Employee ID 2) SSN 3) DOB (yyyy-MM-dd)");
        String choice = sc.nextLine().trim();
        List<Employee> employees = List.of();
        switch (choice) {
            case "1":
                System.out.println("Enter employee id:");
                try {
                    int id = Integer.parseInt(sc.nextLine());
                    employees = UserSearch.searchForEmployeeID(db, id);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid employee id.");
                    return;
                }
                break;
            case "2":
                System.out.println("Enter SSN:");
                employees = UserSearch.searchForEmployeeSSN(db, sc.nextLine());
                break;
            case "3":
                System.out.println("Enter DOB (yyyy-MM-dd):");
                try {
                    LocalDate dob = LocalDate.parse(sc.nextLine());
                    employees = UserSearch.searchForEmployeeDOB(db, dob);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format.");
                    return;
                }
                break;
            default:
                // Allow user to type an ID directly without selecting option
                try {
                    int id = Integer.parseInt(choice);
                    employees = UserSearch.searchForEmployeeID(db, id);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid choice.");
                    return;
                }
        }

        if (employees.isEmpty()) {
            System.out.println("Invalid/Not Found");
            return;
        }

        employees.forEach(EMS::printEmployee);

        System.out.println("Enter an employee id to edit/update, or press Enter to return:");
        String editId = sc.nextLine();
        if (!editId.isBlank()) {
            try {
                int id = Integer.parseInt(editId);
                handleUpdateEmployee(db, sc, id);
            } catch (NumberFormatException e) {
                System.out.println("Invalid employee id.");
            }
        }
    }

    private static void handleAddEmployee(Connection db, Scanner sc) {
        try {
            System.out.println("Enter employee id:");
            int empId = Integer.parseInt(sc.nextLine());
            System.out.println("Enter first name:");
            String fName = sc.nextLine();
            System.out.println("Enter last name:");
            String lName = sc.nextLine();
            System.out.println("Enter email:");
            String email = sc.nextLine();
            System.out.println("Enter SSN:");
            String ssn = sc.nextLine();
            System.out.println("Enter salary:");
            double salary = Double.parseDouble(sc.nextLine());
            System.out.println("Enter hire date (yyyy-MM-dd):");
            LocalDate hireDate = LocalDate.parse(sc.nextLine());

            System.out.println("Enter job title id:");
            int jobId = Integer.parseInt(sc.nextLine());
            System.out.println("Enter job title name:");
            String jobTitleName = sc.nextLine();

            System.out.println("Enter division id:");
            int divisionId = Integer.parseInt(sc.nextLine());
            System.out.println("Enter division name:");
            String divisionName = sc.nextLine();

            System.out.println("Enter gender (single character):");
            char gender = sc.nextLine().charAt(0);
            System.out.println("Enter race:");
            String race = sc.nextLine();
            System.out.println("Enter birth date (yyyy-MM-dd):");
            LocalDate dob = LocalDate.parse(sc.nextLine());
            System.out.println("Enter phone number:");
            String phone = sc.nextLine();

            System.out.println("Enter street:");
            String street = sc.nextLine();
            System.out.println("Enter city:");
            String city = sc.nextLine();
            System.out.println("Enter state:");
            String state = sc.nextLine();
            System.out.println("Enter postal code:");
            String postalCode = sc.nextLine();

            Address address = new Address(city, postalCode, state, street);
            Demographics demographics = new Demographics(gender, race, dob, phone);
            Division division = new Division();
            division.setId(divisionId);
            division.setName(divisionName);
            JobTitle jobTitle = new JobTitle(jobTitleName, jobId);
            Employee emp = new Employee(ssn, address, hireDate, demographics, division, email, empId, fName, lName,
                    salary, jobTitle);

            if (EmployeeModify.addEmployee(db, emp)) {
                System.out.println("Add Successful.");
            } else {
                System.out.println("Error adding employee.");
            }
        } catch (NumberFormatException | DateTimeParseException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    private static void handleUpdateEmployee(Connection db, Scanner sc) {
        System.out.println("Enter employee id to update:");
        try {
            int id = Integer.parseInt(sc.nextLine());
            handleUpdateEmployee(db, sc, id);
        } catch (NumberFormatException e) {
            System.out.println("Invalid employee id.");
        }
    }

    private static void handleUpdateEmployee(Connection db, Scanner sc, int empId) {
        List<Employee> employees = UserSearch.searchForEmployeeID(db, empId);
        if (employees.isEmpty()) {
            System.out.println("Employee not found.");
            return;
        }

        Employee existing = employees.get(0);
        printEmployee(existing);
        System.out.println("Press Enter to keep existing value.");

        System.out.println("First name [" + existing.getfName() + "]:");
        String fName = readOrDefault(sc, existing.getfName());
        System.out.println("Last name [" + existing.getlName() + "]:");
        String lName = readOrDefault(sc, existing.getlName());
        System.out.println("Email [" + existing.getEmail() + "]:");
        String email = readOrDefault(sc, existing.getEmail());
        System.out.println("SSN [" + existing.getSSN() + "]:");
        String ssn = readOrDefault(sc, existing.getSSN());
        System.out.println("Salary [" + existing.getSalary() + "]:");
        double salary = existing.getSalary();
        String salaryInput = sc.nextLine();
        if (!salaryInput.isBlank()) {
            salary = Double.parseDouble(salaryInput);
        }
        System.out.println("Hire date [" + existing.getHireDate() + "] (yyyy-MM-dd):");
        LocalDate hireDate = existing.getHireDate();
        String hireInput = sc.nextLine();
        if (!hireInput.isBlank()) {
            hireDate = LocalDate.parse(hireInput);
        }

        System.out.println("Job title id [" + existing.getJobTitle().getJobTitleID() + "]:");
        String jobIdInput = sc.nextLine();
        int jobId = jobIdInput.isBlank() ? existing.getJobTitle().getJobTitleID() : Integer.parseInt(jobIdInput);
        System.out.println("Job title name [" + existing.getJobTitle().getJobTitle() + "]:");
        String jobTitleName = readOrDefault(sc, existing.getJobTitle().getJobTitle());

        System.out.println("Division id [" + existing.getDivision().getId() + "]:");
        String divIdInput = sc.nextLine();
        int divisionId = divIdInput.isBlank() ? existing.getDivision().getId() : Integer.parseInt(divIdInput);
        System.out.println("Division name [" + existing.getDivision().getName() + "]:");
        String divisionName = readOrDefault(sc, existing.getDivision().getName());

        System.out.println("Gender [" + existing.getDemographics().getGender() + "]:");
        String genderInput = sc.nextLine();
        char gender = genderInput.isBlank() ? existing.getDemographics().getGender() : genderInput.charAt(0);
        System.out.println("Race [" + existing.getDemographics().getRace() + "]:");
        String race = readOrDefault(sc, existing.getDemographics().getRace());
        System.out.println("Birth date [" + existing.getDemographics().getBirthDate() + "] (yyyy-MM-dd):");
        LocalDate dob = existing.getDemographics().getBirthDate();
        String dobInput = sc.nextLine();
        if (!dobInput.isBlank()) {
            dob = LocalDate.parse(dobInput);
        }
        System.out.println("Phone number [" + existing.getDemographics().getPhoneNumber() + "]:");
        String phone = readOrDefault(sc, existing.getDemographics().getPhoneNumber());

        System.out.println("Street [" + existing.getAddress().getStreet() + "]:");
        String street = readOrDefault(sc, existing.getAddress().getStreet());
        System.out.println("City [" + existing.getAddress().getCity() + "]:");
        String city = readOrDefault(sc, existing.getAddress().getCity());
        System.out.println("State [" + existing.getAddress().getState() + "]:");
        String state = readOrDefault(sc, existing.getAddress().getState());
        System.out.println("Postal code [" + existing.getAddress().getPostalCode() + "]:");
        String postal = readOrDefault(sc, existing.getAddress().getPostalCode());

        Address address = new Address(city, postal, state, street);
        Demographics demographics = new Demographics(gender, race, dob, phone);
        Division division = new Division();
        division.setId(divisionId);
        division.setName(divisionName);
        JobTitle jobTitle = new JobTitle(jobTitleName, jobId);

        Employee updated = new Employee(ssn, address, hireDate, demographics, division, email, empId, fName, lName,
                salary, jobTitle);

        if (EmployeeModify.updateEmployee(db, updated)) {
            System.out.println("Update Successful.");
        } else {
            System.out.println("Update failed.");
        }
    }

    private static void handleRemoveEmployee(Connection db, Scanner sc) {
        System.out.println("Remove employee by: 1) Employee ID 2) SSN");
        String choice = sc.nextLine().trim();
        int empId = -1;
        if (choice.equals("1")) {
            System.out.println("Enter employee id:");
            try {
                empId = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid id.");
                return;
            }
        } else if (choice.equals("2")) {
            System.out.println("Enter SSN:");
            String ssn = sc.nextLine();
            List<Employee> employees = UserSearch.searchForEmployeeSSN(db, ssn);
            if (employees.isEmpty()) {
                System.out.println("Employee not found.");
                return;
            }
            empId = employees.get(0).getEmployeeID();
        } else {
            // Allow direct numeric entry
            try {
                empId = Integer.parseInt(choice);
            } catch (NumberFormatException e) {
                System.out.println("Invalid option.");
                return;
            }
        }

        if (EmployeeModify.terminateEmployee(db, empId)) {
            System.out.println("Deletion Successful.");
        } else {
            System.out.println("Employee Not Found or deletion failed.");
        }
    }

    private static void handleSalaryRangeUpdate(Connection db, Scanner sc) {
        try {
            System.out.println("Enter min salary:");
            double min = Double.parseDouble(sc.nextLine());
            System.out.println("Enter max salary:");
            double max = Double.parseDouble(sc.nextLine());
            System.out.println("Enter percentage increase (e.g., 5 for 5%):");
            double pct = Double.parseDouble(sc.nextLine());

            int updated = EmployeeModify.updateSalaryRange(db, min, max, pct);
            System.out.println("Updated " + updated + " employees.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input.");
        }
    }

    private static void handlePayHistory(Connection db, Authorization session, Scanner sc, boolean admin) {
        Integer empId = session.getEmployeeId();
        if (admin) {
            System.out.println("Enter employee id to view pay history:");
            try {
                empId = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid employee id.");
                return;
            }
        }

        if (empId == null) {
            System.out.println("Unable to determine employee id for pay history.");
            return;
        }

        List<Employee> employees = UserSearch.searchForEmployeeID(db, empId);
        if (employees.isEmpty()) {
            System.out.println("Employee not found.");
            return;
        }

        Employee emp = employees.get(0);
        List<Payroll> statements = emp.getAllPayStatements(db);
        if (statements == null || statements.isEmpty()) {
            System.out.println("No pay history found");
            return;
        }

        statements.sort((a, b) -> b.getDateOfPayment().compareTo(a.getDateOfPayment()));
        for (Payroll p : statements) {
            System.out.println("PayID: " + p.getPaymentID() + " Date: " + p.getDateOfPayment() + " Earnings: "
                    + p.getEarnings());
        }
    }

    private static void handleMonthlyPayByJobTitle(Connection db, Scanner sc) {
        try {
            System.out.println("Enter month (1-12):");
            int month = Integer.parseInt(sc.nextLine());
            System.out.println("Enter year (e.g., 2024):");
            int year = Integer.parseInt(sc.nextLine());

            String sql = """
                    SELECT jt.job_title, SUM(p.earnings) AS total_pay
                    FROM payroll p
                    JOIN employees e ON p.empid = e.empid
                    JOIN employee_job_titles ejt ON e.empid = ejt.empid
                    JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id
                    WHERE MONTH(p.pay_date) = ? AND YEAR(p.pay_date) = ?
                    GROUP BY jt.job_title
                    """;

            try (PreparedStatement stmt = db.prepareStatement(sql)) {
                stmt.setInt(1, month);
                stmt.setInt(2, year);
                try (ResultSet rs = stmt.executeQuery()) {
                    boolean found = false;
                    while (rs.next()) {
                        found = true;
                        System.out.println(rs.getString("job_title") + " - " + rs.getDouble("total_pay"));
                    }
                    if (!found) {
                        System.out.println("No records for that month.");
                    }
                }
            }
        } catch (NumberFormatException | SQLException e) {
            System.out.println("Error running report: " + e.getMessage());
        }
    }

    private static void handleMonthlyPayByDivision(Connection db, Scanner sc) {
        try {
            System.out.println("Enter month (1-12):");
            int month = Integer.parseInt(sc.nextLine());
            System.out.println("Enter year (e.g., 2024):");
            int year = Integer.parseInt(sc.nextLine());

            String sql = "SELECT d.Name AS division_name, SUM(p.earnings) AS total_pay "
                    + "FROM payroll p "
                    + "JOIN employees e ON p.empid = e.empid "
                    + "JOIN employee_division ed ON e.empid = ed.empid "
                    + "JOIN division d ON ed.div_ID = d.ID "
                    + "WHERE MONTH(p.pay_date) = ? AND YEAR(p.pay_date) = ? "
                    + "GROUP BY d.Name";

            try (PreparedStatement stmt = db.prepareStatement(sql)) {
                stmt.setInt(1, month);
                stmt.setInt(2, year);
                try (ResultSet rs = stmt.executeQuery()) {
                    boolean found = false;
                    while (rs.next()) {
                        found = true;
                        System.out.println(rs.getString("division_name") + " - " + rs.getDouble("total_pay"));
                    }
                    if (!found) {
                        System.out.println("No records for that month.");
                    }
                }
            }
        } catch (NumberFormatException | SQLException e) {
            System.out.println("Error running report: " + e.getMessage());
        }
    }

    private static void handleHireDateRangeReport(Connection db, Scanner sc) {
        try {
            System.out.println("Enter start date (yyyy-MM-dd):");
            String startInput = sc.nextLine().trim();
            if (startInput.isBlank()) {
                System.out.println("Start date cannot be empty.");
                return;
            }
            LocalDate start = LocalDate.parse(startInput);
            System.out.println("Enter end date (yyyy-MM-dd):");
            String endInput = sc.nextLine().trim();
            if (endInput.isBlank()) {
                System.out.println("End date cannot be empty.");
                return;
            }
            LocalDate end = LocalDate.parse(endInput);

            String sql = "SELECT e.empid, e.Fname, e.Lname, e.HireDate, jt.job_title, d.Name AS division_name "
                    + "FROM employees e "
                    + "LEFT JOIN employee_job_titles ejt ON e.empid = ejt.empid "
                    + "LEFT JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id "
                    + "LEFT JOIN employee_division ed ON e.empid = ed.empid "
                    + "LEFT JOIN division d ON ed.div_ID = d.ID "
                    + "WHERE e.HireDate BETWEEN ? AND ? "
                    + "ORDER BY e.HireDate ASC";

            try (PreparedStatement stmt = db.prepareStatement(sql)) {
                stmt.setObject(1, start);
                stmt.setObject(2, end);
                try (ResultSet rs = stmt.executeQuery()) {
                    boolean found = false;
                    while (rs.next()) {
                        found = true;
                        System.out.println("EmpID: " + rs.getInt("empid") + " Name: " + rs.getString("Fname") + " "
                                + rs.getString("Lname") + " Title: " + rs.getString("job_title") + " Division: "
                                + rs.getString("division_name") + " Hire Date: " + rs.getDate("HireDate"));
                    }
                    if (!found) {
                        System.out.println("No employees hired within this date range.");
                    }
                }
            }
        } catch (DateTimeParseException | SQLException e) {
            System.out.println("Error running report: " + e.getMessage());
        }
    }

    private static String readOrDefault(Scanner sc, String defaultValue) {
        String input = sc.nextLine();
        return input.isBlank() ? defaultValue : input;
    }

    private static void printEmployee(Employee emp) {
        System.out.println("Employee ID: " + emp.getEmployeeID());
        System.out.println("Name: " + emp.getfName() + " " + emp.getlName());
        System.out.println("Email: " + emp.getEmail());
        System.out.println("Hire Date: " + emp.getHireDate());
        System.out.println("Salary: " + emp.getSalary());
        System.out.println("SSN: " + emp.getSSN());
        System.out.println("Job Title: " + emp.getJobTitle().getJobTitle());
        System.out.println("Division: " + emp.getDivision().getName());
        System.out.println(
                "Address: " + emp.getAddress().getStreet() + ", " + emp.getAddress().getCity() + ", "
                        + emp.getAddress().getState() + " " + emp.getAddress().getPostalCode());
        System.out.println(
                "Demographics: " + emp.getDemographics().getGender() + ", " + emp.getDemographics().getRace()
                        + ", DOB: " + emp.getDemographics().getBirthDate() + ", Phone: "
                        + emp.getDemographics().getPhoneNumber());
        System.out.println("--------------------");
    }
}
