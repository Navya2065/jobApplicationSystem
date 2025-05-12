package model;

public class AdminHistoryEntry {
    String userName;
    String jobTitle;
    String companyName;
    String status;
   public  AdminHistoryEntry next;

    public AdminHistoryEntry(String userName, String jobTitle, String companyName, String status) {
        this.userName = userName;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.status = status;
        this.next = null;
    }

    @Override
    public String toString() {
        return "User: " + userName + ", Job: " + jobTitle + ", Company: " + companyName + ", Status: " + status;
    }
}