
import java.util.Date;

/**
 *
 * @author Zerubbabel Ashenafi
 */
public interface IBaseEmployee {
    public String getfName();

    public String setfName(String fName);

    public String getlName();

    public String setlName(String lName);

    public String getEmail();

    public String setEmail(String email);

    public String getSSN();

    public String setSSN(String SSN);

    public Date getHireDate();

    public Date setHireDate(Date date);

    public double getSalary();

    public double setSalary(double Salary);

    public Address getAddress();

    public Address setAddress(Address address);

    public Demographics getDemographics();

    public Demographics setDemographics(Demographics demo);

    public JobTitle getJobTitle();

    public JobTitle setJobTitle(JobTitle jobTitle);

    public Division getDivision();

    public Division setDivision(Division div);

    public int getEmployeeID();

    public int setEmployeeID(int id);

}
