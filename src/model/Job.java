package model;



import java.sql.Date; // Only import java.sql.Date


public class Job {
    private int id;
    private String title;
    private String description;
    private String company;
    private String roleRequired;
    private Date deadline;

   

    // Constructor
    public Job(int id, String title, String description, String company, String roleRequired, Date deadline) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.company = company;
        this.roleRequired = roleRequired;
        this.deadline = deadline;
    }

    // Getters and Setters (if needed)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRoleRequired() {
        return roleRequired;
    }

    public void setRoleRequired(String roleRequired) {
        this.roleRequired = roleRequired;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "Job ID: " + id + ", Job Title: " + title + ", Company: " + company + ", Role Required: " + roleRequired + ", Deadline: " + deadline;
    }




}