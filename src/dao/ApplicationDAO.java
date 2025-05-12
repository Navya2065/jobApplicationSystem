package dao;

import java.sql.*;
import model.*;
import java.util.List;
import java.util.ArrayList;
import dao.NotificationManager;


public class ApplicationDAO {
    private Connection conn;

    public ApplicationDAO(Connection conn) {
        this.conn = conn;
    }

    public void applyForJob(Application application) throws SQLException {
        String sql = "INSERT INTO applications (user_id, job_id, user_name, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, application.getUserId());
            stmt.setInt(2, application.getJobId());
            stmt.setString(3, application.getUserName());
            stmt.setString(4, application.getStatus());
            stmt.executeUpdate();
        }
    }
    public List<Application> getApplicationsByUser(int userId) throws SQLException {
        List<Application> applications = new ArrayList<>();
        String sql = "SELECT a.job_id, a.status, j.title, j.company " +
                "FROM applications a JOIN jobs j ON a.job_id = j.id " +
                "WHERE a.user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Pass all required parameters to the constructor
                Application app = new Application(
                        userId,  // User ID
                        rs.getInt("job_id"),  // Job ID
                        rs.getString("title"),  // Job Title (String)
                        rs.getString("status"),  // Status (String)
                        rs.getString("company")  // Company (String)
                );
                applications.add(app);
            }
        }
        return applications;
    }
    public void updateApplicationStatus(int userId, int jobId, String status) throws SQLException {
        String sql = "UPDATE applications SET status = ? WHERE user_id = ? AND job_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, userId);
            stmt.setInt(3, jobId);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Application status updated successfully!");
                // Push notification for the user
                NotificationManager.pushNotification(userId, "Your application for Job ID " + jobId + " is " + status);
            } else {
                System.out.println("No matching application found to update.");
            }
        }
    }

    public String getCompanyName(int jobId) throws SQLException {
        String sql = "SELECT company FROM jobs WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, jobId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("company");
            } else {
                return null; // Or throw an exception if jobId is not found
            }
        }
    }
    public String getJobTitle(int jobId) throws SQLException {
        String sql = "SELECT title FROM jobs WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, jobId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("title");
            } else {
                return null; // Or throw an exception if jobId is not found
            }
        }
    }


}


