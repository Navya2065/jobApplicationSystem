package dao;

import java.util.*;
public class NotificationManager {
    private static final Map<Integer, Stack<String>> userNotifications = new HashMap<>();

    public static void pushNotification(int userId, String message) {
        userNotifications.computeIfAbsent(userId, k -> new Stack<>()).push(message);
    }

    public static void peekNotifications(int userId) {
        Stack<String> notifications = userNotifications.get(userId);

        if (notifications == null || notifications.isEmpty()) {
            System.out.println("No new notifications.");
            return;
        }

        System.out.println("Your Notifications (not cleared):");
        for (String note : notifications) {
            System.out.println("- " + note);
        }
    }


}
