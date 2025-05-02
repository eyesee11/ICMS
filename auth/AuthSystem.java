package auth;

import ds.CustomHashMap;

public class AuthSystem {
    private CustomHashMap<String, String> credentials;
    private CustomHashMap<String, String> roles;

    public AuthSystem() {
        credentials = new CustomHashMap<>();
        roles = new CustomHashMap<>();
        // Dummy credentials
        credentials.put("admin1", "admin123");
        roles.put("admin1", "ADMIN");
        credentials.put("student1", "student123");
        roles.put("student1", "STUDENT");
        credentials.put("faculty1", "faculty123");
        roles.put("faculty1", "FACULTY");
        credentials.put("library1", "library123");
        roles.put("library1", "LIBRARY");
    }

    public String login(String username, String password) {
        String storedPassword = credentials.get(username);
        if (storedPassword != null && storedPassword.equals(password)) {
            return roles.get(username);
        }
        return null;
    }
}