package main.java.types;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Zerubbabel Ashenafi
 */
public class Employee implements IBaseEmployee {

    private String fName;
    private String lName;
    private String email;
    private String SSN;
    private LocalDate date;
    private double salary;
    private Division division;
    private Demographics demographics;
    private Address address;
    private JobTitle title;
    private int employeeID;

    public Employee() {
        this.fName = "";
        this.lName = "";
        this.email = "";
        this.SSN = "";
        this.date = LocalDate.of(1, 1, 1);
        ;
        this.salary = 0;
        this.division = new Division();
        this.demographics = new Demographics();
        this.address = new Address();
        this.title = new JobTitle();
        this.employeeID = 0;
    }

    public Employee(String SSN, Address address, LocalDate date, Demographics demographics, Division division,
            String email,
            int employeeID, String fName, String lName, double salary, JobTitle title) {
        this.SSN = SSN;
        this.address = address;
        this.date = date;
        this.demographics = demographics;
        this.division = division;
        this.email = email;
        this.employeeID = employeeID;
        this.fName = fName;
        this.lName = lName;
        this.salary = salary;
        this.title = title;
    }

    @Override
    public String getfName() {
        return fName;
    }

    @Override
    public void setfName(String fName) {
        this.fName = fName;
    }

    @Override
    public String getlName() {
        return lName;
    }

    @Override
    public void setlName(String lName) {
        this.lName = lName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getSSN() {
        return SSN;
    }

    @Override
    public void setSSN(String SSN) {
        this.SSN = SSN;
    }

    @Override
    public LocalDate getHireDate() {
        return date;
    }

    @Override
    public void setHireDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public double getSalary() {
        return salary;
    }

    @Override
    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public Demographics getDemographics() {
        return demographics;
    }

    @Override
    public void setDemographics(Demographics demo) {
        this.demographics = demo;
    }

    @Override
    public JobTitle getJobTitle() {
        return title;
    }

    @Override
    public void setJobTitle(JobTitle jobTitle) {
        this.title = jobTitle;
    }

    @Override
    public Division getDivision() {
        return division;
    }

    @Override
    public void setDivision(Division div) {
        this.division = div;
    }

    @Override
    public int getEmployeeID() {
        return employeeID;
    }

    @Override
    public void setEmployeeID(int id) {
        this.employeeID = id;
    }

    public List<Payroll> getAllPayStatements(Connection database) {
        try (PreparedStatement getPayroll = database.prepareStatement("SELECT * FROM payroll WHERE empid = ?")) {
            getPayroll.setInt(1, this.employeeID);

            ResultSet payStatements = getPayroll.executeQuery();

            ArrayList<Payroll> payStatementList = new ArrayList<>();

            while (payStatements.next()) {
                Payroll temp = new Payroll();
                temp.setPaymentID(payStatements.getInt("payID"));
                temp.setDateOfPayment(payStatements.getDate("pay_date").toLocalDate());
                temp.setEarnings(payStatements.getDouble("earnings"));
                temp.setFederalTax(payStatements.getDouble("fed_tax"));
                temp.setFederalMedi(payStatements.getDouble("fed_med"));
                temp.setFederalSocialSec(payStatements.getDouble("fed_ss"));
                temp.setRetirement401k(payStatements.getDouble("retire_401k"));
                temp.setHealthcareWithheld(payStatements.getDouble("health_care"));
                temp.setStateTax(payStatements.getDouble("state_tax"));

                payStatementList.add(temp);
            }

            return payStatementList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
