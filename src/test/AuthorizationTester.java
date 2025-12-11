package test;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import org.junit.jupiter.api.Test;

import main.java.auth.Authorization;
import main.java.databaseinteraction.DatabaseInteractor;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

/**
 *
 * @author aweso
 */
public class AuthorizationTester {

    Authorization sessionTest;

    Connection employeeDatabase;

    public AuthorizationTester() {
        sessionTest = new Authorization();
        employeeDatabase = DatabaseInteractor.getDatabaseConnection();
    }

    @Test
    void authorizeWithProperCredentials() {
        assertEquals(true, sessionTest.authorizeSessionState(employeeDatabase, "admin", "adminpassword"));
        assertEquals(Authorization.Role.ADMIN, sessionTest.getRole());
        assertEquals(null, sessionTest.getEmployeeId());
    }

    @Test
    void authorizeWithIncorrectPassword() {
        assertEquals(false, sessionTest.authorizeSessionState(employeeDatabase, "admin", "iamanobviouslyfakemoron"));
        assertEquals(Authorization.Role.NONE, sessionTest.getRole());
        assertEquals(null, sessionTest.getEmployeeId());
    }

    @Test
    void authorizeWithIncorrectUsername() {
        assertEquals(false, sessionTest.authorizeSessionState(employeeDatabase, "bleah", "iamarealperson"));
        assertEquals(Authorization.Role.NONE, sessionTest.getRole());
        assertEquals(null, sessionTest.getEmployeeId());
    }

    @Test
    void authorizeWithNullValues() {
        assertEquals(false, sessionTest.authorizeSessionState(employeeDatabase, "NULL", "NULL"));
        assertEquals(Authorization.Role.NONE, sessionTest.getRole());
        assertEquals(null, sessionTest.getEmployeeId());
    }

    @Test
    void authorizeAndThenDeauthorize() {
        sessionTest.authorizeSessionState(employeeDatabase, "admin", "iamarealperson");
        assertEquals(true, sessionTest.authorizeSessionState(employeeDatabase, "admin", "adminpassword"));
        assertEquals(Authorization.Role.ADMIN, sessionTest.getRole());
        assertEquals(null, sessionTest.getEmployeeId());

        sessionTest.deElevateState();
        assertEquals(false, sessionTest.checkElevated());
        assertEquals(Authorization.Role.NONE, sessionTest.getRole());
        assertEquals(null, sessionTest.getEmployeeId());
    }

    @Test
    void authorizeWithEmptyStrings() {
        assertEquals(false, sessionTest.authorizeSessionState(employeeDatabase, "", ""));
        assertEquals(Authorization.Role.NONE, sessionTest.getRole());
        assertEquals(null, sessionTest.getEmployeeId());
    }

    @Test
    void authorizeWithUsernameAndEmptyPassword() {
        assertEquals(true, sessionTest.authorizeSessionState(employeeDatabase, "adminWithNoPassword", ""));
        assertEquals(Authorization.Role.ADMIN, sessionTest.getRole());
        assertEquals(null, sessionTest.getEmployeeId());
    }

    @Test
    void authorizeWithEmployeeCredentials() {
        assertEquals(true, sessionTest.authorizeSessionState(employeeDatabase, "bobf", "bobpassword"));
        assertEquals(Authorization.Role.EMPLOYEE, sessionTest.getRole());
        assertEquals(1004, sessionTest.getEmployeeId());
    }

    @Test
    void doubleAuthorize() {
        assertEquals(true, sessionTest.authorizeSessionState(employeeDatabase, "bobf", "bobpassword"));
        assertEquals(true, sessionTest.authorizeSessionState(employeeDatabase, "bobf", "bobpassword"));

    }
}