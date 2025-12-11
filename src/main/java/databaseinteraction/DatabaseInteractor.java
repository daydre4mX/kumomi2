/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package main.java.databaseinteraction;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import test.AuthorizationTester;

/**
 *
 * @author Zerubbabel Ashenafi
 */
public class DatabaseInteractor {

    /**
     * @return A connection to the database given in config.properties, or null if connection fails to be established.
     */
    public static Connection getDatabaseConnection() {
        try (InputStream props = AuthorizationTester.class.getClassLoader().getResourceAsStream("config.properties")) {

            if (props == null) {
                System.out.println("Could not find configuration file!");
                return null;
            }

            // Load configuration file into a properties object.
            Properties databaseSettings = new Properties();
            databaseSettings.load(props);

            String databaseUrl = "jdbc:mysql://" + databaseSettings.getProperty("db.url") + ":3306/employeeData";
            String databaseUsername = databaseSettings.getProperty("db.user");
            String databasePassword = databaseSettings.getProperty("db.password");

            try {
                return DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
