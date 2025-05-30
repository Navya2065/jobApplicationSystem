import java.sql.*;
import java.sql.Date;
import java.util.*;

import util.DBConnection;
import model.*;
import dao.*;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            Scanner scanner = new Scanner(System.in);
            UserDAO userDAO = new UserDAO(conn);
            JobDAO jobDAO = new JobDAO(conn);
            ApplicationDAO appDAO = new ApplicationDAO(conn);

            JobBST jobBST = new JobBST();
            AdminHistory adminHistory = new AdminHistory();



  System.out.println("Do you want to login as 'user' or 'admin'?");
            String role = scanner.nextLine().trim().toLowerCase();

            if (role.equals("admin")) {

                System.out.println("Enter admin email: ");
                String email = scanner.nextLine();
                System.out.println("Enter admin password: ");
                String password = scanner.nextLine();

                if (email.equals("admin@example.com") && password.equals("admin123")) {
                    User admin = new User(0, "Admin", email, password, "admin");
                    System.out.println("Login successful as Admin!");
                    adminMenu(scanner, jobDAO, appDAO, admin, jobBST, adminHistory, conn);
                } else {
                    System.out.println("Invalid admin credentials. Exiting...");
                }

            } else if (role.equals("user")) {

                System.out.println("Enter your email: ");
                String email = scanner.nextLine();
                System.out.println("Enter your password: ");
                String password = scanner.nextLine();

                User currentUser = userDAO.getUserByEmail(email);

                if (currentUser == null) {
                    System.out.println("No account found. Would you like to register? (yes/no)");
                    String registerChoice = scanner.nextLine();

                    if (registerChoice.equalsIgnoreCase("yes")) {
                        System.out.println("Enter your name: ");
                        String name = scanner.nextLine();
                        System.out.println("Create a password: ");
                        String newPassword = scanner.nextLine();

                        User newUser = new User(0, name, email, newPassword, "user");
                        userDAO.createUser(newUser);
                        System.out.println("Account created successfully! Please restart the application to login.");

                    } else {
                        System.out.println("Exiting...");

                    }

                } else {

                    if (!currentUser.getPassword().equals(password)) {
                        System.out.println("Incorrect password. Exiting...");
                        return;
                    }

                    System.out.println("Login successful! Welcome, " + currentUser.getName());


                    List<Job> jobs = jobDAO.getAllJobs();
                    for (Job job : jobs) {
                        jobBST.insert(job);
                    }

                    userMenu(scanner, jobDAO, appDAO, currentUser, jobBST);
                }

            } else {
                System.out.println("Invalid role selection. Please restart and enter 'user' or 'admin'.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void adminMenu(Scanner scanner, JobDAO jobDAO, ApplicationDAO appDAO, User currentUser, JobBST jobBST, AdminHistory adminHistory, Connection conn) {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add a Job");
            System.out.println("2. View Applied Jobs by the Users");
            System.out.println("3. View All Jobs");
            System.out.println("4. Manage Job Application (Accept/Reject)");
            System.out.println("5. View Application History");
            System.out.println("6. Delete a Job");
            System.out.println("7. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:

                    System.out.println("Enter Job Title: ");
                    String title = scanner.nextLine();
                    System.out.println("Enter Job Description: ");
                    String description = scanner.nextLine();
                    System.out.println("Enter Company Name: ");
                    String company = scanner.nextLine();
                    System.out.println("Enter Required Role: ");
                    String roleRequired = scanner.nextLine();
                    System.out.println("Enter Job Deadline (yyyy-mm-dd): ");
                    String deadline = scanner.nextLine();


                    Job newJob = new Job(0, title, description, company, roleRequired, Date.valueOf(deadline));
                    try {
                        jobDAO.createJob(newJob);
                        // Insert new job into BST
                        jobBST.insert(newJob);  // Update BST after adding new job
                        System.out.println("Job added successfully!");
                    } catch (SQLException e) {
                        System.out.println("Error adding job: " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.println("Enter Job ID to view applied users: ");
                    int jobId = scanner.nextInt();
                    scanner.nextLine();

                    try {
                        List<Application> applications = jobDAO.getAppliedJobs(jobId);
                        if (applications.isEmpty()) {
                            System.out.println("No applications for this job.");
                        } else {
                            for (Application app : applications) {
                                System.out.println("User ID: " + app.getUserId() +
                                        ", Job Title: " + app.getJobTitle() +
                                        ", Job ID: "+app.getJobId()+
                                        ", Company: " + app.getCompany() +
                                        ", Status: " + app.getStatus());
                            }

                        }
                    } catch (SQLException e) {
                        System.out.println("Error fetching applications: " + e.getMessage());
                    }
                    break;


                case 3:

                    List<Job> jobs;
                    try {
                        jobs = jobDAO.getAllJobs();
                        System.out.println("\nAll Jobs:");
                        for (Job job : jobs) {
                            System.out.println(job);
                        }
                    } catch (SQLException e) {
                        System.out.println("Error fetching jobs: " + e.getMessage());
                    }
                    break;


                case 4:

                    System.out.println("Enter User ID of the applicant: ");
                    int userId = scanner.nextInt();
                    System.out.println("Enter Job ID for the application: ");
                     jobId = scanner.nextInt();
                    System.out.println("Enter new status (Accepted/Rejected): ");
                    String status = scanner.next();

                    try {

                        appDAO.updateApplicationStatus(userId, jobId, status);

                        String jobTitle = appDAO.getJobTitle(jobId);
                        String companyName = appDAO.getCompanyName(jobId);

                        // Add history entry for application status change
                        adminHistory.addEntry("User" + userId, jobTitle, companyName, status);
                        System.out.println("History updated successfully.");



                    } catch (SQLException e) {
                        System.out.println("Error updating status: " + e.getMessage());
                    }
                    break;


                case 5:

                    adminHistory.showHistory();
                    break;
                case 6:

                    System.out.println("Enter Job ID to delete: ");
                    int deleteJobId = scanner.nextInt();
                    scanner.nextLine();

                    try {
                        boolean deleted = jobDAO.deleteJobById(deleteJobId);
                        if (deleted) {
                            System.out.println("Job deleted successfully!");
                        } else {
                            System.out.println("Job ID not found.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Error deleting job: " + e.getMessage());
                    }
                    break;


                case 7:

                    System.out.println("Exiting admin menu...");
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    private static void userMenu(Scanner scanner, JobDAO jobDAO, ApplicationDAO appDAO, User currentUser, JobBST jobBST) {
        while (true) {
            System.out.println("\nUser Menu:");
            System.out.println("1. View Available Jobs");
            System.out.println("2. Apply for a Job");
            System.out.println("3. Your Applications");
            System.out.println("4. Search Jobs by Title");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    List<Job> jobs;
                    try {
                        jobs = jobDAO.getAllJobs();
                        System.out.println("\nAll Jobs:");
                        for (Job job : jobs) {
                            System.out.println(job);
                        }
                    } catch (SQLException e) {
                        System.out.println("Error fetching jobs: " + e.getMessage());
                    }
                    break;


                case 2:

                    System.out.println("Enter Job ID to apply for: ");
                    int jobIdToApply = scanner.nextInt();
                    scanner.nextLine();  // Consume newline

                    Application newApplication = new Application(
                            currentUser.getId(),
                            jobIdToApply,
                            currentUser.getName(),
                            "Applied"
                    );

                    try {
                        appDAO.applyForJob(newApplication);
                        System.out.println("Job application submitted successfully!");
                    } catch (SQLException e) {
                        System.out.println("Error applying for job: " + e.getMessage());
                    }
                    break;

                case 3:

                    System.out.println("=== Your Applications ===");
                    try {
                        List<Application> userApps = appDAO.getApplicationsByUser(currentUser.getId());
                        if (userApps.isEmpty()) {
                            System.out.println("You have not applied for any jobs yet.");
                        } else {
                            System.out.printf("%-10s %-30s %-20s %-15s\n", "Job ID", "Job Title", "Company", "Status");
                            for (Application app : userApps) {
                                System.out.printf("%-10d %-30s %-20s %-15s\n", app.getJobId(), app.getJobTitle(), app.getCompany(), app.getStatus());
                            }
                        }
                    } catch (SQLException e) {
                        System.out.println("Error fetching applications: " + e.getMessage());
                    }
                    break;

                case 4:
                    System.out.println("Enter the job title you are looking for: ");
                    String searchTitle = scanner.nextLine();

                    Job job = jobBST.search(searchTitle.toLowerCase());
                    if (job != null) {
                        System.out.println("Job Found: " + job);
                    } else {
                        System.out.println("Job not found.");
                    }
                    break;

                case 5:

                    System.out.println("Exiting user menu...");
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
