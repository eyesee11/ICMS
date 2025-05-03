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
    private static final String CREDENTIALS_FILE = "credentials.dat";
    private static final String ROLES_FILE = "roles.dat";
    
    // Constants for password requirements
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final boolean REQUIRE_UPPERCASE = true;
    private static final boolean REQUIRE_NUMBER = true;
    private static final boolean REQUIRE_SPECIAL = true;

    public AuthSystem() {
        credentials = new CustomHashMap<>();
        roles = new CustomHashMap<>();
        allUserIds = new CustomArrayList<>();
        loadCredentials();
        
        // Add admin credentials if not present
        if (credentials.get("admin1") == null) {
            credentials.put("admin1", hashPassword("admin123"));
            roles.put("admin1", "ADMIN");
            allUserIds.add("admin1");
            
            // Add library admin
            credentials.put("library1", hashPassword("library123"));
            roles.put("library1", "LIBRARY");
            allUserIds.add("library1");

            saveCredentials();
        }
    }

    /**
     * Authenticate a user login
     * @param username The username
     * @param password The plain text password
     * @return The user's role if authenticated, null otherwise
     */
    public String login(String username, String password) {
        String storedHash = credentials.get(username);
        if (storedHash != null && verifyPassword(password, storedHash)) {
            return roles.get(username);
        }
        return null;
    }
    
    /**
     * Create credentials for a new student
     * @param student The student object
     */
    public void createStudentCredentials(Student student) {
        String id = student.getId();
        String defaultPassword = generateDefaultPassword(student);
        
        // Check if credentials already exist for this ID
        if (credentials.get(id) == null) {
            credentials.put(id, hashPassword(defaultPassword));
            roles.put(id, "STUDENT");
            allUserIds.add(id);
            saveCredentials();
            
            System.out.println("\nCredentials created for " + student.getName() + ":");
            System.out.println("Username: " + id);
            System.out.println("Password: " + defaultPassword);
            System.out.println("(Please change this default password on first login)");
        }
    }
    
    /**
     * Create credentials for a new faculty member
     * @param faculty The faculty object
     */
    public void createFacultyCredentials(Faculty faculty) {
        String id = faculty.getId();
        String defaultPassword = generateDefaultPassword(faculty);
        
        // Check if credentials already exist for this ID
        if (credentials.get(id) == null) {
            credentials.put(id, hashPassword(defaultPassword));
            roles.put(id, "FACULTY");
            allUserIds.add(id);
            saveCredentials();
            
            System.out.println("\nCredentials created for " + faculty.getName() + ":");
            System.out.println("Username: " + id);
            System.out.println("Password: " + defaultPassword);
            System.out.println("(Please change this default password on first login)");
        }
    }
    
    /**
     * Generate a default password based on user details
     * @param student The student object
     * @return A generated password
     */
    private String generateDefaultPassword(Student student) {
        // Format: first letter of name + id + @123
        String name = student.getName();
        String firstLetter = name.length() > 0 ? name.substring(0, 1).toLowerCase() : "s";
        return firstLetter + student.getId() + "@123";
    }
    
    /**
     * Generate a default password based on user details
     * @param faculty The faculty object
     * @return A generated password
     */
    private String generateDefaultPassword(Faculty faculty) {
        // Format: first letter of name + id + @456
        String name = faculty.getName();
        String firstLetter = name.length() > 0 ? name.substring(0, 1).toLowerCase() : "f";
        return firstLetter + faculty.getId() + "@456";
    }
    
    /**
     * Change a user's password
     * @param username The username
     * @param oldPassword The current password
     * @param newPassword The new password
     * @return true if password changed successfully, false otherwise
     */
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        // First validate the old password is correct
        String storedHash = credentials.get(username);
        if (storedHash != null && verifyPassword(oldPassword, storedHash)) {
            // Validate new password meets requirements
            if (!isPasswordValid(newPassword)) {
                System.out.println("New password does not meet security requirements.");
                System.out.println("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long.");
                if (REQUIRE_UPPERCASE) System.out.println("Password must contain an uppercase letter.");
                if (REQUIRE_NUMBER) System.out.println("Password must contain a number.");
                if (REQUIRE_SPECIAL) System.out.println("Password must contain a special character.");
                return false;
            }
            
            // Store the hashed password
            credentials.put(username, hashPassword(newPassword));
            saveCredentials();
            return true;
        }
        return false;
    }
    
    /**
     * Force reset a user's password to a default
     * @param username The username
     * @return The new default password
     */
    public String resetPassword(String username) {
        // Check if user exists
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
    
    /**
     * Remove credentials for a deleted user
     * @param userId The user ID to remove
     */
    public void removeCredentials(String userId) {
        credentials.remove(userId);
        roles.remove(userId);
        
        // Remove from allUserIds
        for (int i = 0; i < allUserIds.size(); i++) {
            if (allUserIds.get(i).equals(userId)) {
                allUserIds.remove(i);
                break;
            }
        }
        
        saveCredentials();
    }
    
    /**
     * Validate that a password meets security requirements
     * @param password The password to check
     * @return true if valid, false otherwise
     */
    private boolean isPasswordValid(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }
        
        boolean hasUpper = !REQUIRE_UPPERCASE; // true if not required
        boolean hasNumber = !REQUIRE_NUMBER;   // true if not required
        boolean hasSpecial = !REQUIRE_SPECIAL; // true if not required
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isDigit(c)) hasNumber = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        
        return hasUpper && hasNumber && hasSpecial;
    }
    
    /**
     * Hash a password using SHA-256
     * @param password The plain text password
     * @return A hashed version of the password
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            
            // Convert to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Fall back to plain text if hashing is unavailable
            System.err.println("Warning: Password hashing unavailable. Using plain text instead.");
            return password;
        }
    }
    
    /**
     * Verify a password against a stored hash
     * @param password The plain text password attempt
     * @param storedHash The stored hash to compare against
     * @return true if password matches hash, false otherwise
     */
    private boolean verifyPassword(String password, String storedHash) {
        String hashedAttempt = hashPassword(password);
        return hashedAttempt.equals(storedHash);
    }
    
    /**
     * Get all user IDs
     * @return List of all user IDs
     */
    public CustomArrayList<String> getAllUserIds() {
        return allUserIds;
    }
    
    /**
     * Get a user's role
     * @param username The username
     * @return The user's role or null if user doesn't exist
     */
    public String getUserRole(String username) {
        return roles.get(username);
    }
    
    // Save credentials to file
    private void saveCredentials() {
        try {
            // Save credentials
            CustomArrayList<String> credentialLines = new CustomArrayList<>();
            for (int i = 0; i < allUserIds.size(); i++) {
                String id = allUserIds.get(i);
                String password = credentials.get(id);
                credentialLines.add(id + ":" + password);
            }
            FileHandler.saveToFile(CREDENTIALS_FILE, credentialLines);
            
            // Save roles
            CustomArrayList<String> roleLines = new CustomArrayList<>();
            for (int i = 0; i < allUserIds.size(); i++) {
                String id = allUserIds.get(i);
                String role = roles.get(id);
                roleLines.add(id + ":" + role);
            }
            FileHandler.saveToFile(ROLES_FILE, roleLines);
        } catch (Exception e) {
            System.err.println("Error saving credentials: " + e.getMessage());
        }
    }
    
    // Load credentials from file
    private void loadCredentials() {
        try {
            CustomArrayList<String> credentialLines = FileHandler.readFromFile(CREDENTIALS_FILE);
            for (int i = 0; i < credentialLines.size(); i++) {
                String line = credentialLines.get(i);
                int separatorIndex = line.indexOf(':');
                if (separatorIndex > 0 && separatorIndex < line.length() - 1) {
                    String id = line.substring(0, separatorIndex);
                    String password = line.substring(separatorIndex + 1);
                    credentials.put(id, password);
                    allUserIds.add(id);
                }
            }
            
            CustomArrayList<String> roleLines = FileHandler.readFromFile(ROLES_FILE);
            for (int i = 0; i < roleLines.size(); i++) {
                String line = roleLines.get(i);
                int separatorIndex = line.indexOf(':');
                if (separatorIndex > 0 && separatorIndex < line.length() - 1) {
                    String id = line.substring(0, separatorIndex);
                    String role = line.substring(separatorIndex + 1);
                    roles.put(id, role);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading credentials: " + e.getMessage());
        }
    }
}