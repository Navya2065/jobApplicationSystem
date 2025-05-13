package dao;
import model.AdminHistoryEntry;
public class AdminHistory {
    private AdminHistoryEntry head;
    public  AdminHistoryEntry next;
    public AdminHistory() {
        this.head = null;

    }


    public void addEntry(String userName, String jobTitle, String companyName, String status) {
        AdminHistoryEntry newEntry = new AdminHistoryEntry(userName, jobTitle, companyName, status);
        if (head == null) {
            head = newEntry;
        } else {
            AdminHistoryEntry current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newEntry;
        }
    }


    public void showHistory() {
        if (head == null) {
            System.out.println("No job application history.");
            return;
        }
        System.out.println("\n=== Job Application History ===");
        AdminHistoryEntry current = head;
        while (current != null) {
            System.out.println(current);
            current = current.next;
        }
    }
}
