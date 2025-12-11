package main.java.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Holds the session state for the management system.
 * Tracks authenticated role and associated employee id for downstream checks.
 * 
 * @author Zerubbabel Ashenafi
 * @author Jonathan Bell
 */

public class Authorization {

    public enum Role {
        NONE,
        ADMIN,
        EMPLOYEE
    }

    /**
     * Signifies whether the session can perform edits or not.
     */
    // Tracks current session role (admin/employee/unauthenticated).
    private Role role = Role.NONE;

    /**
     * The employee id tied to the logged in user. This is only populated for
     * general employees.
     */
    // Cached employee id for a logged-in general employee; null for admins or anonymous.
    private Integer employeeId = null;

    /**
     * Method that authorizes the session to allow an admin to edit the database.
     * Will connect to the database, check the passwords table, and see whether the
     * row exists for the specified credentials.
     * 
     * @param database The connection pointing to a database with a passwords table.
     * @param username The username for an admin.
     * @param password The password corresponding to the username.
     * @return A boolean signifying whether the request to authorize was successful.
     */
    public boolean authorizeSessionState(Connection database, String username, String password) {

        if (role != Role.NONE) {
            System.out.println("Already signed in as " + role + "!");
            return true;
        }

        String sql = """
                SELECT username, password, role, empid
                FROM user_accounts
                WHERE username = ?
                """;

        try (PreparedStatement getUsers = database.prepareStatement(sql)) {
            getUsers.setString(1, username);
            ResultSet user = getUsers.executeQuery();

            if (!user.isBeforeFirst()) {
                System.out.println("Username not in database!");
                return false;
            }

            while (user.next()) {
                String queryPassword = user.getString("password");
                if (!password.equals(queryPassword)) {
                    System.out.println("Password is wrong!");
                    return false;
                }

                String userRole = user.getString("role");
                if (userRole != null && userRole.equalsIgnoreCase("admin")) {
                    role = Role.ADMIN;
                } else {
                    role = Role.EMPLOYEE;
                }

                int empId = user.getInt("empid");
                if (!user.wasNull()) {
                    employeeId = empId;
                }

                System.out.println("Authorized as " + role + "!");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Method to deescalate the session, use this method after all editing is
     * complete.
     */
    public void deElevateState() {
        role = Role.NONE;
        employeeId = null;
    }

    /**
     * 
     * @return A boolean signifying whether the session is elevated or not. Reduces
     *         the number of times we might need to reauthorize the state.
     */
    public boolean checkElevated() {
        return role == Role.ADMIN;
    }

    /**
     * @return The current user role.
     */
    public Role getRole() {
        return role;
    }

    /**
     * @return The employee id tied to the logged in user, null for admins or when
     *         unauthenticated.
     */
    public Integer getEmployeeId() {
        return employeeId;
    }

}
