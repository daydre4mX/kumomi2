package main.java.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Holds the session state for the management system
 */

public class Authorization {

    /**
     * Signifies whether the session can perform edits or not.
     */
    boolean elevated = false;

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

        if (elevated) {
            System.out.println("Already authorized!");
            return true;
        }

        // Use a prepared statement to allow a variable to be easily inserted into the
        // command without concatenation.
        try (PreparedStatement getUsers = database.prepareStatement("SELECT * FROM passwords where user=?")) {
            getUsers.setString(1, username);
            ResultSet user = getUsers.executeQuery();

            if (!user.isBeforeFirst()) {
                System.out.println("Username not in database!");
                return false;
            }

            while (user.next()) {
                String queryPassword = user.getString("password");

                if (password.equals(queryPassword)) {
                    System.out.println("Authorized!");
                    elevated = true;
                    return true;
                } else {
                    System.out.println("Password is wrong!");
                    return false;
                }

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
        elevated = false;
    }

    /**
     * 
     * @return A boolean signifying whether the session is elevated or not. Reduces
     *         the number of times we might need to reauthorize the state.
     */
    public boolean checkElevated() {
        return elevated;
    }

}
