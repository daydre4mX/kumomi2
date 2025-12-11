package main.java.empmanip;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import main.java.types.Employee;
public class UserSearch {

   public static Employee searchForEmployeeID(Connection myConn, int ID){
        String sql = """
            SELECT e.empid, e.Fname, e.Lname, e.email, e.HireDate, e.Salary, e.SSN, a.street, a.city_id, a.state_id, a.postalCode
            FROM employee e
            Left JOIN Address a ON e.empid = a.empid
            where e.empid = ? 
        """; //Need to fix query

        try(PreparedStatement ps = myConn.prepareStatement(sql)){
            ps.setInt(1, ID);

            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Employee nemp = new Employee();
                    nemp.setEmployeeID(rs.getInt("empid"));
                    nemp.setfName(rs.getString("Fname"));
                    nemp.setlName(rs.getString("Lname"));
                    nemp.setEmail(rs.getString("email"));
                    nemp.setHireDate(rs.getDate("HireDate"));
                    nemp.setSalary(rs.getDouble("Salary")); 
                    nemp.setSSN(rs.getString("SSN"));
                    // nemp.setJobTitle(rs.getString("Title"));
                    // nemp.setDivision(rs.getString("Division"));
                    // nemp.setDemographics(rs.getArray("Demo"));
                    // nemp.setAddress(rs.getString("Address"))

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
