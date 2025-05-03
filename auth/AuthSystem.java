package auth;

import ds.CustomHashMap;
import ds.CustomArrayList;
import util.FileHandler;
import model.Student;
import model.Faculty;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class AuthSystem {
    private CustomHashMap<String, String> credentials;
    private CustomHashMap<String, String> roles;
    private CustomArrayList<String> allUserIds;
    private static final String CREDENTIALS_FILE = "credentials.csv";
    private static final String ROLES_FILE = "roles.csv";
    
    private String currentUsername; 

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final boolean REQUIRE_UPPERCASE = true;
    private static final boolean REQUIRE_NUMBER = true;
    private static final boolean REQUIRE_SPECIAL = true;

    public AuthSystem() {
        credentials = new CustomHashMap<>();
        roles = new CustomHashMap<>();
        allUserIds = new CustomArrayList<>();
        loadCredentials();

        if (credentials.get("admin1") == null) {
            credentials.put("admin1", hashPassword("admin123"));
            roles.put("admin1", "ADMIN");
            allUserIds.add("admin1");

            credentials.put("library1", hashPassword("library123"));
            roles.put("library1", "LIBRARY");
            allUserIds.add("library1");

            saveCredentials();
        }
    }

    public String login(String username, String password) {
        String storedHash = credentials.get(username);
        if (storedHash != null && verifyPassword(password, storedHash)) {
            String role = roles.get(username);
            currentUsername = username;
            return role; 
        }
        return null;
    }
    
    public String getCurrentUsername() {
        return currentUsername;
    }

    public void createStudentCredentials(Student student) {
        String id = student.getId();
        String defaultPassword = generateDefaultPassword(student);

        if (credentials.get(id) == null) {
            credentials.put(id, hashPassword(defaultPassword));
            roles.put(id, "STUDENT");
            allUserIds.add(id);
            saveCredentials();

            System.out.println("\nCredentials created for " + student.getName() + ":");
            System.out.println("Username: " + id);
            System.out.println("Password: " + defaultPassword);
        }
    }

    public void createFacultyCredentials(Faculty faculty) {
        String id = faculty.getId();
        String defaultPassword = generateDefaultPassword(faculty);

        if (credentials.get(id) == null) {
            credentials.put(id, hashPassword(defaultPassword));
            roles.put(id, "FACULTY");
            allUserIds.add(id);
            saveCredentials();

            System.out.println("\nCredentials created for " + faculty.getName() + ":");
            System.out.println("Username: " + id);
            System.out.println("Password: " + defaultPassword);
        }
    }

    private String generateDefaultPassword(Student student) {
        String name = student.getName();
        String firstLetter = name.length() > 0 ? name.substring(0, 1).toLowerCase() : "s";
        return firstLetter + student.getId() + "@123";
    }

    private String generateDefaultPassword(Faculty faculty) {
        String name = faculty.getName();
        String firstLetter = name.length() > 0 ? name.substring(0, 1).toLowerCase() : "f";
        return firstLetter + faculty.getId() + "@456";
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        String storedHash = credentials.get(username);
        if (storedHash != null && verifyPassword(oldPassword, storedHash)) {
            if (!isPasswordValid(newPassword)) {
                System.out.println("New password does not meet security requirements.");
                System.out.println("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long.");
                if (REQUIRE_UPPERCASE)
                    System.out.println("Password must contain an uppercase letter.");
                if (REQUIRE_NUMBER)
                    System.out.println("Password must contain a number.");
                if (REQUIRE_SPECIAL)
                    System.out.println("Password must contain a special character.");
                return false;
            }

            credentials.put(username, hashPassword(newPassword));
            saveCredentials();
            return true;
        }
        return false;
    }

    public String resetPassword(String username) {
        String role = roles.get(username);
        if (role == null) {
            return null;
        }

        String newPassword;
        if (role.equals("STUDENT")) {
            newPassword = username + "@" + System.currentTimeMillis() % 1000;
        } else {
            newPassword = username + "#" + System.currentTimeMillis() % 1000;
        }

        credentials.put(username, hashPassword(newPassword));
        saveCredentials();
        return newPassword;
    }

    public void removeCredentials(String userId) {
        credentials.remove(userId);
        roles.remove(userId);

        for (int i = 0; i < allUserIds.size(); i++) {
            if (allUserIds.get(i).equals(userId)) {
                allUserIds.remove(i);
                break;
            }
        }

        saveCredentials();
    }

    private boolean isPasswordValid(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }

        boolean hasUpper = !REQUIRE_UPPERCASE;
        boolean hasNumber = !REQUIRE_NUMBER;
        boolean hasSpecial = !REQUIRE_SPECIAL;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c))
                hasUpper = true;
            if (Character.isDigit(c))
                hasNumber = true;
            if (!Character.isLetterOrDigit(c))
                hasSpecial = true;
        }

        return hasUpper && hasNumber && hasSpecial;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Warning: Password hashing unavailable. Using plain text instead.");
            return password;
        }
    }

    private boolean verifyPassword(String password, String storedHash) {
        String hashedAttempt = hashPassword(password);
        return hashedAttempt.equals(storedHash);
    }

    public CustomArrayList<String> getAllUserIds() {
        return allUserIds;
    }

    public String getUserRole(String username) {
        return roles.get(username);
    }

    private void saveCredentials() {
        try {
            CustomArrayList<String> credentialLines = new CustomArrayList<>();
            credentialLines.add("username,password,role");
            for (int i = 0; i < allUserIds.size(); i++) {
                String id = allUserIds.get(i);
                String password = credentials.get(id);
                String role = roles.get(id);
                credentialLines.add(id + "," + password + "," + role);
            }
            FileHandler.saveToFile(CREDENTIALS_FILE, credentialLines);
        } catch (Exception e) {
            System.err.println("Error saving credentials: " + e.getMessage());
        }
    }

    /**
     * Load credentials from CSV file
     */
    private void loadCredentials() {
        try {
            CustomArrayList<String> credentialLines = FileHandler.readFromFile(CREDENTIALS_FILE);
            // Skip header row if it exists
            int startIndex = 0;
            if (credentialLines.size() > 0 && credentialLines.get(0).startsWith("username,password")) {
                startIndex = 1;
            }
            
            for (int i = startIndex; i < credentialLines.size(); i++) {
                String line = credentialLines.get(i);
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String id = parts[0];
                    String password = parts[1];
                    String role = parts[2];
                    credentials.put(id, password);
                    roles.put(id, role);
                    allUserIds.add(id);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading credentials: " + e.getMessage());
        }
    }
}