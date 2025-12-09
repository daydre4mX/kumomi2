import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EMS {
    public static void main(String[] args) throws Exception {

        // attempt to load configuration file from root of classpath
        try (InputStream props = EMS.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties databaseSettings = new Properties();

            if (props == null) {
                System.out.println("Could not find configuration file!");
                return;
            }

            databaseSettings.load(props);

            System.out.println();
            System.out.println(databaseSettings.getProperty("db.url"));
            System.out.println(databaseSettings.getProperty("db.user"));
            System.out.println(databaseSettings.getProperty("db.password"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
