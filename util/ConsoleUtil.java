package util;

import java.io.Console;
import java.io.IOException;
import java.util.Scanner;

public class ConsoleUtil {
    private static final Scanner scanner = new Scanner(System.in);
    
    // Method to read a password with masking
    public static String readPassword(String prompt) {
        Console console = System.console();
        
        // If console is available (not in IDE), use its readPassword
        if (console != null) {
            System.out.print(prompt);
            char[] passwordChars = console.readPassword();
            return new String(passwordChars);
        } else {
            // Fallback for IDE environment - simulate password masking
            System.out.print(prompt);
            StringBuilder password = new StringBuilder();
            
            // Try to enable raw mode for character input
            try {
                while (true) {
                    char c = (char) System.in.read();
                    
                    // Enter key - finish input
                    if (c == '\r' || c == '\n') {
                        System.out.println();
                        break;
                    }
                    
                    // Backspace - remove last character
                    if (c == '\b' && password.length() > 0) {
                        password.deleteCharAt(password.length() - 1);
                        System.out.print("\b \b"); // Remove asterisk
                    } else if (c != '\b') {
                        // Regular character - add to password and print asterisk
                        password.append(c);
                        System.out.print("*");
                    }
                }
            } catch (IOException e) {
                // If raw mode fails, fall back to regular input
                return scanner.nextLine();
            }
            
            return password.toString();
        }
    }
    
    // Method to clear the console screen
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // If clearing screen fails, just print new lines as fallback
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    
    // Method to prompt user to press Enter to continue
    public static void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    // Method to read input with custom prompt and validation
    public static String readInput(String prompt, boolean required) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (!required || !ValidationUtil.isNullOrEmpty(input)) {
                return input;
            }
            
            System.out.println("This field is required. Please enter a value.");
        }
    }
    
    // Method to read email with validation
    public static String readEmail(String prompt) {
        String email;
        boolean isValid = false;
        
        do {
            System.out.print(prompt);
            email = scanner.nextLine().trim();
            
            if (email.isEmpty()) {
                // Allow empty for update operations (user wants to keep current)
                isValid = true;
            } else if (!ValidationUtil.isValidEmail(email)) {
                String fixedEmail = ValidationUtil.fixEmailDomain(email);
                System.out.println("Email format incorrect. Suggested: " + fixedEmail);
                System.out.print("Use suggested email? (Y/n): ");
                String choice = scanner.nextLine().trim().toLowerCase();
                
                if (choice.isEmpty() || choice.equals("y") || choice.equals("yes")) {
                    email = fixedEmail;
                    isValid = true;
                }
            } else {
                isValid = true;
            }
        } while (!isValid);
        
        return email;
    }
    
    // Method to display a menu of options and get selection
    public static int showMenu(String title, String[] options) {
        while (true) {
            System.out.println("\n=========================");
            System.out.println(title);
            System.out.println("=========================");
            
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ". " + options[i]);
            }
            
            System.out.print("\nChoose an option (1-" + options.length + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 1 && choice <= options.length) {
                    return choice;
                }
                System.out.println("Invalid option. Please enter a number between 1 and " + options.length);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    // Method to select from a list of valid streams
    public static String selectStream() {
        String[] validStreams = ValidationUtil.getValidStreams();
        
        System.out.println("\nAvailable streams:");
        for (int i = 0; i < validStreams.length; i++) {
            System.out.println((i + 1) + ". " + validStreams[i]);
        }
        
        int selection = -1;
        do {
            System.out.print("\nSelect stream (1-" + validStreams.length + ") or enter 0 to input custom: ");
            try {
                String input = scanner.nextLine().trim();
                
                // Allow empty input for update operations
                if (input.isEmpty()) {
                    return "";
                }
                
                selection = Integer.parseInt(input);
                
                if (selection >= 1 && selection <= validStreams.length) {
                    return validStreams[selection - 1];
                } else if (selection == 0) {
                    System.out.print("Enter custom stream: ");
                    return scanner.nextLine().trim();
                } else {
                    System.out.println("Please enter a number between 0 and " + validStreams.length);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        } while (true);
    }
}