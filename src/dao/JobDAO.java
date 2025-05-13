package dao;
import java.sql.*;
import java.util.*;

import model.Application;
import model.Job;

public class JobDAO {
    private Connection conn;

    public JobDAO(Connection conn) {
        this.conn = conn;
    }

    public int createJob(Job job) throws SQLException {
        String sql = "INSERT INTO jobs (title, description, company, role_required, deadline) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, job.getTitle());
            stmt.setString(2, job.getDescription());
            stmt.setString(3, job.getCompany());
            stmt.setString(4, job.getRoleRequired());
            stmt.setDate(5, job.getDeadline());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
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



    public boolean deleteJobById(int jobId) throws SQLException {
        // Step 1: Delete applications related to the job
        String deleteApplicationsSql = "DELETE FROM applications WHERE job_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteApplicationsSql)) {
            stmt.setInt(1, jobId);
            stmt.executeUpdate(); // even if no rows, it's okay
        }


        String deleteJobSql = "DELETE FROM jobs WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteJobSql)) {
            stmt.setInt(1, jobId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public List<Application> getAppliedJobs(int jobId) throws SQLException {
        String sql = "SELECT a.user_id, a.job_id, u.name AS user_name, j.title AS job_title, a.status, j.company " +
                "FROM applications a " +
                "JOIN users u ON a.user_id = u.id " +
                "JOIN jobs j ON a.job_id = j.id " +
                "WHERE a.job_id = ?";

        List<Application> applications = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, jobId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Application application = new Application(
                        rs.getInt("user_id"),
                        rs.getInt("job_id"),
                        rs.getString("job_title"),
                        rs.getString("status"),
                        rs.getString("company")
                );
                applications.add(application);
            }
        }

        return applications;
    }

}
