package test;

import java.sql.Connection;
import org.junit.jupiter.api.Test;

import main.java.databaseinteraction.DatabaseInteractor;
import main.java.types.JobTitle;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Zerubbabel Ashenafi
 */
public class JobTitleTester {

    Connection database;
    JobTitle jobTitle;

    public JobTitleTester() {
        database = DatabaseInteractor.getDatabaseConnection();
    }

    @Test
    void testGetJobTitleFromID() {
        jobTitle = new JobTitle();
        jobTitle.setJobTitleID(101);
        jobTitle.setJobTitleFromID(database);

        assertEquals("Software Engineer", jobTitle.getJobTitle());

        jobTitle.setJobTitleID(102);
        jobTitle.setJobTitleFromID(database);

        assertEquals("HR Specialist", jobTitle.getJobTitle());

        jobTitle.setJobTitleID(0);
        jobTitle.setJobTitleFromID(database);

        assertEquals("", jobTitle.getJobTitle());
    }

}