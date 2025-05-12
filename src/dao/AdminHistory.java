package dao;


import model.AdminHistoryEntry;
public class AdminHistory {
    private AdminHistoryEntry head;

    public AdminHistory() {
        this.head = null;
    }

    // Insert a new entry at the end (tail) of the linked list
    public void addEntry(String userName, String jobTitle, String companyName, String status) {
        AdminHistoryEntry newEntry = new AdminHistoryEntry(userName, jobTitle, companyName, status);
        if (head == null) {
            head = newEntry;  // If the list is empty, make the new entry the head
        } else {
            AdminHistoryEntry current = head;
            while (current.next != null) {
                current = current.next;  // Traverse to the end of the list
            }
            current.next = newEntry;  // Insert the new entry at the end
        }
    }

    // Display the job application history
    public void showHistory() {
        if (head == null) {
            System.out.println("No job application history.");
            return;
        }
        System.out.println("\n=== Job Application History ===");
        AdminHistoryEntry current = head;
        while (current != null) {
            System.out.println(current);  // The toString method of AdminHistoryEntry will be called here
            current = current.next;  // Move to the next entry
        }
    }
}
