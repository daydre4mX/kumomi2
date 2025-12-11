package main.java.types;

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

    public int getJobTitleID() {
        return jobTitleID;
    }

    public void setJobTitleID(int jobTitleID) {
        this.jobTitleID = jobTitleID;
    }

}
