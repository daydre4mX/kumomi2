package main.java;

import java.sql.Connection;
import java.util.Scanner;
import main.java.auth.Authorization;
import main.java.databaseinteraction.DatabaseInteractor;
import main.java.empmanip.userSearch;

public class EMS {

    public static void main(String[] args) throws Exception {

        Authorization sessionState = new Authorization();

        // Connect to the database given in config.properties
        try (Connection employeeDatabase = DatabaseInteractor.getDatabaseConnection()) {

            if (employeeDatabase == null) {
                System.out.println("Error in connecting to database, is your configuration setup properly?");
                System.exit(1);
            }

            System.out.println("Connected to database successfully.");
            Scanner sc = new Scanner(System.in);

            while (true) {
                // Show two different menus depending on whether or not the user is currently
                // authorized to do admin work.
                if (!sessionState.checkElevated()) {
                    System.out.println("1. Authorize administrator.");
                    System.out.println("2. Search for employee(s)");
                    System.out.println("3. Show pay statements.");
                    System.out.println("Enter an option from 1-3, or type exit: ");

                    String input = sc.nextLine();

                    if (input.equals("1")) {
                        System.out.println("Enter a username:");
                        String user = sc.nextLine();
                        System.out.println("Enter a password:");
                        String password = sc.nextLine();

                        sessionState.authorizeSessionState(employeeDatabase, user, password);

                    } else if (input.equals("2")) {
                        // implement search menu
                    } else if (input.equals("3")) {
                        // implement pay statements for an employee id
                    } else if (input.equals("exit")) {
                        System.out.println("Exiting Employee Management System!");
                        employeeDatabase.close();
                        System.exit(0);
                    }
                } else {
                    System.out.println("1. Logout of administrator.");
                    System.out.println("2. Search for employee(s)");
                    System.out.println("3. Add Employee");
                    System.out.println("4. Remove Employee");
                    System.out.println("5. Reports menu");
                    System.out.println("Enter an option from 1-5, or type exit: ");

                    String input = sc.nextLine();

                    if (input.equals("1")) {
                        System.out.println("Logging out of admin!");
                        sessionState.deElevateState();
                    } else if (input.equals("2")) {
                        // implement search menu
                    } else if (input.equals("3")) {
                        // implement adding employees
                    } else if (input.equals("4")) {
                        // implement removing employees
                    } else if (input.equals("5")) {
                        // implement reports menu
                    } else if (input.equals("exit")) {
                        System.out.println("Exiting Employee management system!");
                        employeeDatabase.close();
                        System.exit(0);
                    }
                }

            }

        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e.getLocalizedMessage());
        }

    }
}
