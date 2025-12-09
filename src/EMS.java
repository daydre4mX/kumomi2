import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.Scanner;

public class EMS {

    public static void main(String[] args) throws Exception {

        // Attempt to load configuration file from root of classpath
        try (InputStream props = EMS.class.getClassLoader().getResourceAsStream("config.properties")) {

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

            Authorization sessionState = new Authorization();

            // Connect to the database given in config.properties
            try (Connection employeeDatabase = DriverManager.getConnection(databaseUrl, databaseUsername,
                    databasePassword)) {
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

                        String input = sc.next();

                        if (input.equals("1")) {
                            System.out.println("Enter a username:");
                            String user = sc.next();
                            System.out.println("Enter a password:");
                            String password = sc.next();

                            sessionState.authorizeSessionState(employeeDatabase, user, password);

                        } else if (input.equals("2")) {
                            // implement search menu
                        } else if (input.equals("3")) {
                            // implement pay statements for an employee id
                        } else if (input.equals("exit")) {
                            System.out.println("Exiting Employee management system!");
                            System.exit(0);
                        }
                    } else {
                        System.out.println("1. Logout of administrator.");
                        System.out.println("2. Search for employee(s)");
                        System.out.println("3. Add Employee");
                        System.out.println("4. Remove Employee");
                        System.out.println("5. Reports menu");
                        System.out.println("Enter an option from 1-5, or type exit: ");

                        String input = sc.next();

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
                            System.exit(0);
                        }
                    }

                }

            } catch (Exception e) {
                System.out.println("Error connecting to database: " + e.getLocalizedMessage());
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
