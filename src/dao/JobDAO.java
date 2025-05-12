package dao;
import java.sql.*;
import java.util.*;
import model.Job;

public class JobDAO {
    private Connection conn;

    public JobDAO(Connection conn) {
        this.conn = conn;
    }

    // Create a new job
    public int createJob(Job job) throws SQLException {
        String sql = "INSERT INTO jobs (title, description, company, role_required, deadline) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, job.getTitle());
            stmt.setString(2, job.getDescription());
            stmt.setString(3, job.getCompany());
            stmt.setString(4, job.getRoleRequired());
            stmt.setDate(5, job.getDeadline());  // Ensure it's a java.sql.Date
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);  // Return the generated ID
            }
        }
        return -1;
    }

    public List<Job> getAllJobs() throws SQLException {
        String sql = "SELECT * FROM jobs";
        List<Job> jobs = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Job job = new Job(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("company"),
                        rs.getString("role_required"),
                        rs.getDate("deadline")
                );
                jobs.add(job);
            }
        }
        return jobs;
    }

    // Get available jobs (jobs that are not expired, used by users)
    public List<Job> getAvailableJobs() throws SQLException {
        String sql = "SELECT * FROM jobs WHERE deadline >= CURDATE()";  // Fetch jobs with a future deadline
        List<Job> jobs = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Job job = new Job(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("company"),
                        rs.getString("role_required"),
                        rs.getDate("deadline")
                );
                jobs.add(job);
            }
        }
        return jobs;
    }
}
