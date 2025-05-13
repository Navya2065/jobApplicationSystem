package dao;

import java.sql.*;
import model.*;
import java.util.List;
import java.util.ArrayList;

public class ApplicationDAO {
    private Connection conn;

    public ApplicationDAO(Connection conn) {
        this.conn = conn;
    }

    // Method to update application status
    public void updateApplicationStatus(int userId, int jobId, String status) throws SQLException {
        String sql = "UPDATE applications SET status = ? WHERE user_id = ? AND job_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, userId);
            stmt.setInt(3, jobId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Application status updated successfully.");
            } else {
                System.out.println("No application found for the given user ID and job ID.");
            }
        }
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
                Application app = new Application(
                        userId,
                        rs.getInt("job_id"),
                        rs.getString("title"),
                        rs.getString("status"),
                        rs.getString("company")
                );
                applications.add(app);
            }
        }
        return applications;
    }

    public String getCompanyName(int jobId) throws SQLException {
        String sql = "SELECT company FROM jobs WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, jobId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("company");
            } else {
                return null;
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
                return null;
            }
        }
    }

}

