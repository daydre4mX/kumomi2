package main.java.types;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Ahmed Cisse
 * @author Zerubbabel Ashenafi
 */
public class JobTitle {
    private String jobTitle;
    private int jobTitleID;

    public JobTitle() {
        this.jobTitle = "";
        this.jobTitleID = 0;
    }

    public JobTitle(String jobTitle, int jobTitleID) {
        this.jobTitle = jobTitle;
        this.jobTitleID = jobTitleID;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * Sets jobTitle to a matching title found in the job_titles table using
     * jobTitleID.
     * 
     * @param database Database with a job_titles database
     */
    public void setJobTitleFromID(Connection database) {
        try (PreparedStatement getJobTitle = database
                .prepareStatement("SELECT * FROM job_titles WHERE job_title_id = ?")) {

            getJobTitle.setInt(1, jobTitleID);

            ResultSet jobTitle = getJobTitle.executeQuery();

            // If no job title matches the id given.
            if (!jobTitle.isBeforeFirst()) {
                this.jobTitle = "";
                return;
            }

            while (jobTitle.next()) {
                this.jobTitle = jobTitle.getString("job_title");
            }
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: " + e.getLocalizedMessage());
        }
    }

    public int getJobTitleID() {
        return jobTitleID;
    }

    public void setJobTitleID(int jobTitleID) {
        this.jobTitleID = jobTitleID;
    }

}
