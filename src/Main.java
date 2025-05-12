import java.sql.*;  // Make sure to import java.sql.Date
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

            // Initialize JobBST
            JobBST jobBST = new JobBST();
            // Initialize AdminHistory to store application history
            AdminHistory adminHistory = new AdminHistory();

            // Step 1: Login (email and password)
            System.out.println("Enter your email: ");
            String email = scanner.nextLine();
            System.out.println("Enter your password: ");
            String password = scanner.nextLine();

            // Check for hardcoded admin credentials
            if (email.equals("admin@example.com") && password.equals("admin123")) {
                // Admin login
                User currentUser = new User(0, "Admin", email, password, "admin");
                System.out.println("Login successful as Admin!");
                adminMenu(scanner, jobDAO, appDAO, currentUser, jobBST, adminHistory, conn);  // Pass conn here
                return;
            }

            // Check if the user exists in the database for user login
            User currentUser = userDAO.getUserByEmail(email);
            if (currentUser == null) {
                // If the user does not exist, give the option to create a new user
                System.out.println("No account found with this email. Would you like to create a new account? (yes/no)");
                String createAccount = scanner.nextLine();

                if (createAccount.equalsIgnoreCase("yes")) {
                    System.out.println("Enter your name: ");
                    String name = scanner.nextLine();
                    System.out.println("Enter your password: ");
                    String newPassword = scanner.nextLine();

                    // Create a new user and add to the database
                    User newUser = new User(0, name, email, newPassword, "user");
                    userDAO.createUser(newUser);  // Assuming createUser() method in UserDAO
                    System.out.println("Account created successfully! Please login now.");

                    // Call login again after successful user creation
                    return; // Restart the login process
                } else {
                    System.out.println("Exiting...");
                    return; // Exit the program if user doesn't want to create an account
                }
            }

            if (!currentUser.getPassword().equals(password)) {
                System.out.println("Invalid credentials. Exiting...");
                return;
            }

            System.out.println("Login successful! Welcome, " + currentUser.getName());

            // Fetch all jobs and insert them into the BST
            List<Job> jobs = jobDAO.getAllJobs();
            for (Job job : jobs) {
                jobBST.insert(job);  // Insert jobs into BST
            }

            // Display menu for user
            userMenu(scanner, jobDAO, appDAO, currentUser, jobBST);  // Pass jobBST
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Admin menu handling
    private static void adminMenu(Scanner scanner, JobDAO jobDAO, ApplicationDAO appDAO, User currentUser, JobBST jobBST, AdminHistory adminHistory, Connection conn) {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add a Job");
            System.out.println("2. View All Jobs");
            System.out.println("3. Manage Job Application (Accept/Reject)");
            System.out.println("4. View Application History");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    // Add Job
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

                    // Create new job and add to database
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
                    // View All Jobs
                    List<Job> jobs;
                    try {
                        jobs = jobDAO.getAllJobs();
                        System.out.println("\nAll Jobs:");
                        for (Job job : jobs) {
                            System.out.println("ID: " + job.getId() + ", Title: " + job.getTitle());
                        }
                    } catch (SQLException e) {
                        System.out.println("Error fetching jobs: " + e.getMessage());
                    }
                    break;

                case 3:
                    // Manage Job Application (Accept/Reject)
                    System.out.println("Enter User ID of the applicant: ");
                    int userId = scanner.nextInt();
                    System.out.println("Enter Job ID for the application: ");
                    int jobId = scanner.nextInt();
                    System.out.println("Enter new status (Accepted/Rejected): ");
                    String status = scanner.next();

                    try {
                        // Update application status in the database
                        appDAO.updateApplicationStatus(userId, jobId, status);

                        String jobTitle = appDAO.getJobTitle(jobId);
                        String companyName = appDAO.getCompanyName(jobId);

                        // Add history entry for application status change
                        adminHistory.addEntry("User" + userId, jobTitle, companyName, status);
                        System.out.println("History updated successfully.");

                        // Push a notification to the user
                        NotificationManager.pushNotification(userId, "Your job application for job ID " + jobId + " has been " + status + ".");
                        System.out.println("Notification sent to user.");

                    } catch (SQLException e) {
                        System.out.println("Error updating status: " + e.getMessage());
                    }
                    break;

                case 4:
                    // View Application History
                    adminHistory.showHistory();
                    break;

                case 5:
                    // Exit
                    System.out.println("Exiting admin menu...");
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // User menu handling
    private static void userMenu(Scanner scanner, JobDAO jobDAO, ApplicationDAO appDAO, User currentUser, JobBST jobBST) {
        while (true) {
            System.out.println("\nUser Menu:");
            System.out.println("1. View Available Jobs");
            System.out.println("2. Apply for a Job");
            System.out.println("3. Your Applications");
            System.out.println("4. View Notifications");
            System.out.println("5. Search Jobs by Title");
            System.out.println("6. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    // View Available Jobs
                    List<Job> availableJobs;
                    try {
                        availableJobs = jobDAO.getAvailableJobs();
                        System.out.println("\nAvailable Jobs:");
                        for (Job job : availableJobs) {
                            System.out.println("ID: " + job.getId() + ", Title: " + job.getTitle());
                        }
                    } catch (SQLException e) {
                        System.out.println("Error fetching available jobs: " + e.getMessage());
                    }
                    break;

                case 2:
                    // Apply for a Job
                    System.out.println("Enter Job ID to apply for: ");
                    int jobIdToApply = scanner.nextInt();
                    Application newApplication = new Application(currentUser.getId(), jobIdToApply, "Job Title Here", "Applied", "Company Name");

                    try {
                        appDAO.applyForJob(newApplication);
                        System.out.println("Job application submitted successfully!");
                    } catch (SQLException e) {
                        System.out.println("Error applying for job: " + e.getMessage());
                    }
                    break;

                case 3:
                    // View your applications
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
                    // View notifications
                    System.out.println("=== Your Notifications ===");
                    NotificationManager.peekNotifications(currentUser.getId());
                    break;

                case 5:
                    // Search Jobs using BST
                    System.out.println("Enter the job title you are looking for: ");
                    String searchTitle = scanner.nextLine();

                    Job job = jobBST.search(searchTitle.toLowerCase());
                    if (job != null) {
                        System.out.println("Job Found: " + job);
                    } else {
                        System.out.println("Job not found.");
                    }
                    break;

                case 6:
                    // Exit
                    System.out.println("Exiting user menu...");
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
