package main.java.types;

import java.util.Date;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author aweso
 */
public class Employee implements IBaseEmployee {

    private String fName;
    private String lName;
    private String email;
    private String SSN;
    private Date date;
    private double salary;
    private Division division;
    private Demographics demographics;
    private Address address;
    private JobTitle title;
    private int employeeID;

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
    public Date getHireDate() {
        return date;
    }

    @Override
    public void setHireDate(Date date) {
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

}
