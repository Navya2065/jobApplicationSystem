package model;
public class Application {
    private int id;
    private int userId;
    private int jobId;
    private String userName;  // New field for user's name
    private String status;
    private String jobTitle;
    private String company;

    // Constructor
    public Application(int userId, int jobId, String status, String jobTitle, String company) {
        this.userId = userId;
        this.jobId = jobId;
        this.status = status;
        this.jobTitle = jobTitle;
        this.company = company;
    }


    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getJobTitle() {
        return jobTitle;
    }
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
}
