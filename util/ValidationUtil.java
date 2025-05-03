package util;

import ds.CustomArrayList;

public class ValidationUtil {
    // Valid streams for students
    private static final String[] VALID_STREAMS = {
        "Computer Science", 
        "Electronics", 
        "Civil Engineering", 
        "Mechanical Engineering",
        "Information Technology",
        "Electrical Engineering",
        "Chemical Engineering",
        "Biotechnology"
    };
    
    /**
     * Check if a string is null or empty
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Validate email format (must end with @gmail.com)
     */
    public static boolean isValidEmail(String email) {
        if (isNullOrEmpty(email)) {
            return false;
        }
        
        return email.toLowerCase().endsWith("@gmail.com");
    }
    
    /**
     * Fix email domain if needed (append @gmail.com if missing)
     */
    public static String fixEmailDomain(String email) {
        if (isNullOrEmpty(email)) {
            return "";
        }
        
        if (!email.contains("@")) {
            return email + "@gmail.com";
        } else if (email.endsWith("@")) {
            return email + "gmail.com";
        } else if (!email.endsWith("@gmail.com")) {
            // Extract username (part before @) and append @gmail.com
            int atIndex = email.indexOf("@");
            if (atIndex > 0) {
                return email.substring(0, atIndex) + "@gmail.com";
            }
        }
        
        return email;
    }
    
    /**
     * Generate a new unique ID using a prefix and checking against existing IDs
     */
    public static String generateNextId(String prefix, CustomArrayList<String> existingIds) {
        int maxNumber = 0;
        
        // Find the maximum numeric part of existing IDs
        for (int i = 0; i < existingIds.size(); i++) {
            String id = existingIds.get(i);
            if (id.startsWith(prefix) && id.length() > prefix.length()) {
                try {
                    String numPart = id.substring(prefix.length());
                    int num = Integer.parseInt(numPart);
                    if (num > maxNumber) {
                        maxNumber = num;
                    }
                } catch (NumberFormatException e) {
                    // Skip if not a valid number format
                    continue;
                }
            }
        }
        
        // Format with leading zeros to ensure 3 digits (001, 002, etc.)
        int nextNumber = maxNumber + 1;
        
        if (nextNumber < 10) {
            return prefix + "00" + nextNumber;
        } else if (nextNumber < 100) {
            return prefix + "0" + nextNumber;
        } else {
            return prefix + nextNumber;
        }
    }
    
    /**
     * Check if a stream is valid
     */
    public static boolean isValidStream(String stream) {
        if (isNullOrEmpty(stream)) {
            return false;
        }
        
        for (int i = 0; i < VALID_STREAMS.length; i++) {
            if (VALID_STREAMS[i].equalsIgnoreCase(stream)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Get all valid streams
     */
    public static String[] getValidStreams() {
        return VALID_STREAMS;
    }
}