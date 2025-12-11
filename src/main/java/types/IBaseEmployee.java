package main.java.types;

import java.time.LocalDate;

/**
 *
 * @author Zerubbabel Ashenafi
 */
public interface IBaseEmployee {
    public String getfName();

    public void setfName(String fName);

    public String getlName();

    public void setlName(String lName);

    public String getEmail();

    public void setEmail(String email);

    public String getSSN();

    public void setSSN(String SSN);

    public LocalDate getHireDate();

    public void setHireDate(LocalDate date);

    public double getSalary();

    public void setSalary(double Salary);

    public Address getAddress();

    public void setAddress(Address address);

    public Demographics getDemographics();

    public void setDemographics(Demographics demo);

    public JobTitle getJobTitle();

    public void setJobTitle(JobTitle jobTitle);

    public Division getDivision();

    public void setDivision(Division div);

    public int getEmployeeID();

    public void setEmployeeID(int id);

}
