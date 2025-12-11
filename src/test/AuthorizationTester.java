package test;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import org.junit.jupiter.api.Test;

import main.java.auth.Authorization;
import main.java.databaseinteraction.DatabaseInteractor;

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
        employeeDatabase = DatabaseInteractor.getDatabaseConnection();
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

    @Test
    void authorizeWithNullValues() {
        assertEquals(false, sessionTest.authorizeSessionState(employeeDatabase, "NULL", "NULL"));
    }

    @Test
    void authorizeAndThenDeauthorize() {
        sessionTest.authorizeSessionState(employeeDatabase, "admin", "iamarealperson");
        sessionTest.deElevateState();
        assertEquals(false, sessionTest.checkElevated());
    }

    @Test
    void authorizeWithEmptyStrings() {
        assertEquals(false, sessionTest.authorizeSessionState(employeeDatabase, "", ""));
    }

    @Test
    void authorizeWithUsernameAndEmptyPassword() {
        assertEquals(true, sessionTest.authorizeSessionState(employeeDatabase, "adminWithNoPassword", ""));
    }
}