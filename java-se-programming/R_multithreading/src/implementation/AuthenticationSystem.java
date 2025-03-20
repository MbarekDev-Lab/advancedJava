package implementation;

import java.sql.*;
import java.util.concurrent.Semaphore;
/*
    CREATE DATABASE authDB;
    USE authDB;
    CREATE TABLE users (
            id INT AUTO_INCREMENT PRIMARY KEY,
            username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL
);
    INSERT INTO users (username, password) VALUES ('admin', 'admin123'), ('user1', 'password1');
*/

public class AuthenticationSystem {
    private static String userName = null;
    private static String password = null;
    private static final Semaphore semaphore = new Semaphore(0); // No permit initially

    public static void main(String[] args) {
        Thread authThread1 = new Thread(() -> authenticateUser("admin", "admin123"));
        Thread authThread2 = new Thread(() -> authenticateUser("user1", "password1"));
        Thread userThread = new Thread(() -> setCredentials("admin", "admin123"));

        authThread1.start();
        authThread2.start();
        userThread.start();
    }

    private static void authenticateUser(String user, String pass) {
        try {
            System.out.println(Thread.currentThread().getName() + " is waiting for credentials...");
            semaphore.acquire(); // Wait for credentials

            if (checkDatabase(user, pass)) {
                System.out.println(Thread.currentThread().getName() + " Authentication successful for user: " + user);
            } else {
                System.out.println(Thread.currentThread().getName() + " Authentication failed for user: " + user);
                resetCredentials();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void setCredentials(String user, String pass) {
        try {
            Thread.sleep(2000); // Simulating credential input delay
            userName = user;
            password = pass;
            System.out.println("Credentials set for: " + user);
            semaphore.release(); // Allow authentication to proceed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static boolean checkDatabase(String user, String pass) {
        String url = "jdbc:mysql://localhost:3306/authDB";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?")) {
            stmt.setString(1, user);
            stmt.setString(2, pass);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // If there is a match, return true
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void resetCredentials() {
        userName = null;
        password = null;
        semaphore.release(); // Allow new authentication attempts
    }
}
