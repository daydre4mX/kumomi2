package main.java.empmanip;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import main.java.types.Address;
import main.java.types.Demographics;
import main.java.types.Employee;
public class Search {

   public static Employee searchForEmployeeID(Connection myConn, int ID){
        String sql = """
            SELECT e.empid, e.Fname, e.Lname, e.email, e.HireDate, e.Salary, e.SSN,
            a.street, a.city_id, a.state_id, a.postalCode, 
            jt.job_title, divi.Name
            d.gender, d.race, d.DOB, d.moblie,
            FROM employee e
            Left JOIN Address a ON e.empid = a.empid
            Left JOIN Employee_jb_titles ejt ON e.empid = ejt.empid
            Left JOIN job_tiles jt ON ejt.job_title_id = jt.job_title_id
            Left JOIN employee_division ed ON e.empid = ed.empid
            Left JOIN division divi ON ed.div_ID = divi.ID
            Left JOIN Demographics d ON e.empid = d.empid
            where e.empid = ? 
        """; /*Do I grab ALL from job and division?*/

        try(PreparedStatement ps = myConn.prepareStatement(sql)){
            ps.setInt(1, ID);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Employee nemp = new Employee();
                    nemp.setEmployeeID(rs.getInt("empid"));
                    nemp.setfName(rs.getString("Fname"));
                    nemp.setlName(rs.getString("Lname"));
                    nemp.setEmail(rs.getString("email"));
                    nemp.setHireDate(rs.getDate("HireDate").toLocalDate());
                    nemp.setSalary(rs.getDouble("Salary"));
                    nemp.setSSN(rs.getString("SSN"));

                    Address nadd = new Address();
                    nadd.setStreet(rs.getString("street"));
                    nadd.setCity(rs.getString("city_id"));
                    nadd.setState(rs.getString("state_id"));
                    nadd.setPostalCode(rs.getString("postalCode"));

                    nemp.setAddress(nadd);

                    Demographics ndemo = new Demographics();
                    ndemo.setGender(rs.getString("gender").charAt(0));
                    ndemo.setRace(rs.getString("race"));
                    ndemo.setBirthDate(rs.getDate("DOB").toLocalDate());
                    ndemo.setPhoneNumber(rs.getString("mobile"));

                    nemp.setDemographics(ndemo);

                    // nemp.setJobTitle(rs.getString("job_title"));

                    // nemp.setDivision(rs.getString("Name"));

                    return nemp;

                }
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;

        
   }

   public static void searchForEmployeeDOB(Connection myConn, Date Date) {
       
   }

   public static void searchForEmployeeSSN(Connection myConn, String SSN) {
       
   }

   public static void searchForEmployeeNAME(Connection myConn, String fName, String lName) {
       
   }
}
