package test;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import org.junit.jupiter.api.Test;

import main.java.EMS;
import main.java.auth.Authorization;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 *
 * @author aweso
 */
public class AuthorizationTester {

    Authorization sessionTest = new Authorization();

    Connection employeeDatabase;

    public AuthorizationTester() {
        try (InputStream props = AuthorizationTester.class.getClassLoader().getResourceAsStream("config.properties")) {

            if (props == null) {
                System.out.println("Could not find configuration file!");
                return;
            }

            // Load configuration file into a properties object.
            Properties databaseSettings = new Properties();
            databaseSettings.load(props);

            String databaseUrl = "jdbc:mysql://" + databaseSettings.getProperty("db.url") + ":3306/employeeData";
            String databaseUsername = databaseSettings.getProperty("db.user");
            String databasePassword = databaseSettings.getProperty("db.password");

            try {
                employeeDatabase = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void authorizeWithProperCredentials() {
        assertEquals(true, sessionTest.authorizeSessionState(employeeDatabase, "admin", "iamarealperson"));
    }

    @Test
    void authorizeWithIncorrectPassword() {
        assertEquals(false, sessionTest.authorizeSessionState(employeeDatabase, "admin", "iamanobviouslyfakemoron"));
    }

    @Test
    void authorizeWithIncorrectUsername() {
        assertEquals(false, sessionTest.authorizeSessionState(employeeDatabase, "bleah", "iamarealperson"));
    }

    void authorizeWithNullValues() {
        assertEquals(false, sessionTest.authorizeSessionState(employeeDatabase, "NULL", "NULL"));
    }

}