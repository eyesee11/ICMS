package main;

import auth.AuthSystem;
import admin.AdminPanel;
import student.StudentPanel;
import faculty.FacultyPanel;
import library.LibrarySystem;
import course.CourseManagement;
import model.Student;
import model.Faculty;
import model.Book;
import model.Course;
import data.CourseData;
import ds.CustomArrayList;
import util.TableFormatter;
import util.ConsoleUtil;
import util.ValidationUtil;
import util.FileHandler;
import java.util.Scanner;

public class ICMS {
    private static Scanner scanner = new Scanner(System.in);
    private static AuthSystem auth = new AuthSystem();
    private static AdminPanel adminPanel = new AdminPanel(auth);
    private static LibrarySystem library = new LibrarySystem();
    private static CourseManagement courseManagement = new CourseManagement();
    private static CustomArrayList<Book> allBooks = new CustomArrayList<>();

    public static void main(String[] args) {
        adminPanel.setCourseManagement(courseManagement);
        CourseData.loadSampleCourses(courseManagement);
        
        boolean exit = false;
        while (!exit) {
            ConsoleUtil.clearScreen();
            displayMainMenu();
            int choice = getIntInput("Choose an option: ");
            
            if (choice == 6) {
                System.out.println("Exiting...");
                exit = true;
                continue;
            }
            
            ConsoleUtil.clearScreen();
            System.out.println("=== LOGIN REQUIRED ===");
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            
            String password = ConsoleUtil.readPassword("Enter password: ");
            
            String role = auth.login(username, password);
            if (role == null) {
                System.out.println("Invalid credentials!");
                ConsoleUtil.pressEnterToContinue();
                continue;
            }
            
            switch (choice) {
                case 1: 
                    if (role.equals("ADMIN")) {
                        handleAdminPanel();
                    } else {
                        System.out.println("Access denied: Admin privileges required!");
                        ConsoleUtil.pressEnterToContinue();
                    }
                    break;
                case 2: 
                    if (role.equals("STUDENT")) {
                        Student student = adminPanel.searchStudent(username);
                        if (student != null) {
                            handleStudentPanel(student);
                        } else {
                            System.out.println("Student account not found. Please contact admin.");
                            ConsoleUtil.pressEnterToContinue();
                        }
                    } else {
                        System.out.println("Access denied: Student privileges required!");
                        ConsoleUtil.pressEnterToContinue();
                    }
                    break;
                case 3: 
                    if (role.equals("FACULTY")) {
                        Faculty faculty = adminPanel.searchFaculty(username);
                        if (faculty != null) {
                            handleFacultyPanel(faculty);
                        } else {
                            System.out.println("Faculty account not found. Please contact admin.");
                            ConsoleUtil.pressEnterToContinue();
                        }
                    } else {
                        System.out.println("Access denied: Faculty privileges required!");
                        ConsoleUtil.pressEnterToContinue();
                    }
                    break;
                case 4: 
                    if (role.equals("LIBRARY")) {
                        handleLibraryPanel();
                    } else {
                        System.out.println("Access denied: Library privileges required!");
                        ConsoleUtil.pressEnterToContinue();
                    }
                    break;
                case 5:
                    handleCoursePanel();
                    break;
                default:
                    System.out.println("Invalid option!");
                    ConsoleUtil.pressEnterToContinue();
                    break;
            }
        }
        scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("=========================");
        System.out.println("Integrated College Management System");
        System.out.println("=========================");
        System.out.println("1. Admin");
        System.out.println("2. Student");
        System.out.println("3. Faculty");
        System.out.println("4. Library");
        System.out.println("5. Courses");
        System.out.println("6. Exit");
    }

    private static void handleAdminPanel() {
        boolean back = false;
        while (!back) {
            ConsoleUtil.clearScreen();
            System.out.println("=========================");
            System.out.println("Admin Panel");
            System.out.println("=========================");
            System.out.println("1. Student Administration");
            System.out.println("2. Faculty Administration");
            System.out.println("3. Course Administration");
            System.out.println("4. Back to Main Menu");
            
            int choice = getIntInput("Choose an option: ");
            
            switch (choice) {
                case 1:
                    handleStudentAdmin();
                    break;
                case 2:
                    handleFacultyAdmin();
                    break;
                case 3:
                    adminPanel.handleCourseManagement(scanner);
                    break;
                case 4:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option!");
                    ConsoleUtil.pressEnterToContinue();
            }
        }
    }
    
    private static void handleStudentAdmin() {
        boolean back = false;
        while (!back) {
            ConsoleUtil.clearScreen();
            System.out.println("=========================");
            System.out.println("Student Administration");
            System.out.println("=========================");
            System.out.println("1. Add Student");
            System.out.println("2. Update Student");
            System.out.println("3. Delete Student");
            System.out.println("4. View All Students");
            System.out.println("5. Assign Course to Student");
            System.out.println("6. Back");
            
            int choice = getIntInput("Choose an option: ");
            
            switch (choice) {
                case 1:
                    String id = adminPanel.generateNextStudentId();
                    System.out.println("Auto-generated student ID: " + id);
                    
                    System.out.print("Enter student name: ");
                    String name = scanner.nextLine();
                    
                    String email = ConsoleUtil.readEmail("Enter student email: ");
                    
                    String stream = ConsoleUtil.selectStream();
                    
                    adminPanel.addStudent(new Student(id, name, email, stream));
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 2:
                    adminPanel.viewAllStudents();
                    System.out.print("\nEnter student ID to update: ");
                    String updateId = scanner.nextLine();
                    Student student = adminPanel.searchStudent(updateId);
                    if (student != null) {
                        System.out.print("Enter new name (current: " + student.getName() + "): ");
                        String newName = scanner.nextLine();
                        if (ValidationUtil.isNullOrEmpty(newName)) {
                            newName = student.getName();
                        }
                        
                        System.out.print("Enter new email (current: " + student.getEmail() + "): ");
                        String newEmail = scanner.nextLine();
                        if (!ValidationUtil.isNullOrEmpty(newEmail)) {
                            if (!ValidationUtil.isValidEmail(newEmail)) {
                                newEmail = ValidationUtil.fixEmailDomain(newEmail);
                                System.out.println("Email fixed to: " + newEmail);
                            }
                        } else {
                            newEmail = student.getEmail();
                        }
                        
                        System.out.println("Current stream: " + student.getStream());
                        System.out.println("Enter new stream or leave blank to keep current: ");
                        String newStream = ConsoleUtil.selectStream();
                        
                        adminPanel.updateStudent(updateId, newName, newEmail, newStream);
                    } else {
                        System.out.println("Student not found with ID: " + updateId);
                    }
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 3:
                    adminPanel.viewAllStudents();
                    System.out.print("\nEnter student ID to delete: ");
                    String deleteId = scanner.nextLine();
                    
                    Student studentToDelete = adminPanel.searchStudent(deleteId);
                    if (studentToDelete != null) {
                        System.out.print("Are you sure you want to delete " + studentToDelete.getName() + " (y/n)? ");
                        String confirm = scanner.nextLine().trim().toLowerCase();
                        if (confirm.equals("y") || confirm.equals("yes")) {
                            adminPanel.deleteStudent(deleteId);
                        } else {
                            System.out.println("Deletion cancelled.");
                        }
                    } else {
                        System.out.println("Student not found with ID: " + deleteId);
                    }
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 4:
                    adminPanel.viewAllStudents();
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 5:
                    adminPanel.viewAllStudents();
                    System.out.print("\nEnter student ID: ");
                    String studentId = scanner.nextLine();
                    Student studentToAssign = adminPanel.searchStudent(studentId);
                    if (studentToAssign != null) {
                        courseManagement.viewCourses();
                        System.out.print("\nEnter course name to assign: ");
                        String courseName = scanner.nextLine();
                        Course course = courseManagement.searchCourse(courseName);
                        if (course != null) {
                            adminPanel.assignCourseToStudent(studentId, courseName);
                        } else {
                            System.out.println("Course not found: " + courseName);
                        }
                    } else {
                        System.out.println("Student not found with ID: " + studentId);
                    }
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 6:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option!");
                    ConsoleUtil.pressEnterToContinue();
            }
        }
    }
    
    private static void handleFacultyAdmin() {
        boolean back = false;
        while (!back) {
            ConsoleUtil.clearScreen();
            System.out.println("=========================");
            System.out.println("Faculty Administration");
            System.out.println("=========================");
            System.out.println("1. Add Faculty");
            System.out.println("2. Update Faculty");
            System.out.println("3. Delete Faculty");
            System.out.println("4. View All Faculty");
            System.out.println("5. Assign Subject to Faculty");
            System.out.println("6. Assign Class to Faculty");
            System.out.println("7. Back");
            
            int choice = getIntInput("Choose an option: ");
            
            switch (choice) {
                case 1:
                    String id = adminPanel.generateNextFacultyId();
                    System.out.println("Auto-generated faculty ID: " + id);
                    
                    System.out.print("Enter faculty name: ");
                    String name = scanner.nextLine();
                    
                    String email = ConsoleUtil.readEmail("Enter faculty email: ");
                    
                    adminPanel.addFaculty(new Faculty(id, name, email));
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 2:
                    adminPanel.viewAllFaculty();
                    System.out.print("\nEnter faculty ID to update: ");
                    String updateId = scanner.nextLine();
                    Faculty faculty = adminPanel.searchFaculty(updateId);
                    if (faculty != null) {
                        System.out.print("Enter new name (current: " + faculty.getName() + "): ");
                        String newName = scanner.nextLine();
                        if (ValidationUtil.isNullOrEmpty(newName)) {
                            newName = faculty.getName();
                        }
                        
                        System.out.print("Enter new email (current: " + faculty.getEmail() + "): ");
                        String newEmail = scanner.nextLine();
                        if (!ValidationUtil.isNullOrEmpty(newEmail)) {
                            if (!ValidationUtil.isValidEmail(newEmail)) {
                                newEmail = ValidationUtil.fixEmailDomain(newEmail);
                                System.out.println("Email fixed to: " + newEmail);
                            }
                        } else {
                            newEmail = faculty.getEmail();
                        }
                        
                        adminPanel.updateFaculty(updateId, newName, newEmail);
                    } else {
                        System.out.println("Faculty not found with ID: " + updateId);
                    }
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 3:
                    adminPanel.viewAllFaculty();
                    System.out.print("\nEnter faculty ID to delete: ");
                    String deleteId = scanner.nextLine();
                    
                    Faculty facultyToDelete = adminPanel.searchFaculty(deleteId);
                    if (facultyToDelete != null) {
                        System.out.print("Are you sure you want to delete " + facultyToDelete.getName() + " (y/n)? ");
                        String confirm = scanner.nextLine().trim().toLowerCase();
                        if (confirm.equals("y") || confirm.equals("yes")) {
                            adminPanel.deleteFaculty(deleteId);
                        } else {
                            System.out.println("Deletion cancelled.");
                        }
                    } else {
                        System.out.println("Faculty not found with ID: " + deleteId);
                    }
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 4:
                    adminPanel.viewAllFaculty();
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 5:
                    adminPanel.viewAllFaculty();
                    System.out.print("\nEnter faculty ID: ");
                    String facultyId = scanner.nextLine();
                    Faculty facultyMember = adminPanel.searchFaculty(facultyId);
                    if (facultyMember != null) {
                        System.out.print("Enter subject to assign: ");
                        String subject = scanner.nextLine();
                        adminPanel.assignSubjectToFaculty(facultyId, subject);
                    } else {
                        System.out.println("Faculty not found with ID: " + facultyId);
                    }
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 6:
                    adminPanel.viewAllFaculty();
                    System.out.print("\nEnter faculty ID: ");
                    String facId = scanner.nextLine();
                    Faculty facMember = adminPanel.searchFaculty(facId);
                    if (facMember != null) {
                        System.out.print("Enter class name to assign: ");
                        String className = scanner.nextLine();
                        adminPanel.assignClassToFaculty(facId, className);
                    } else {
                        System.out.println("Faculty not found with ID: " + facId);
                    }
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 7:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option!");
                    ConsoleUtil.pressEnterToContinue();
            }
        }
    }

    private static void handleStudentPanel(Student student) {
        StudentPanel studentPanel = new StudentPanel(student);
        boolean back = false;
        
        while (!back) {
            ConsoleUtil.clearScreen();
            System.out.println("=========================");
            System.out.println("Student Panel - Welcome " + student.getName());
            System.out.println("=========================");
            System.out.println("1. View Profile");
            System.out.println("2. Update Profile");
            System.out.println("3. View Courses");
            System.out.println("4. View Attendance");
            System.out.println("5. View Timetable");
            System.out.println("6. View Issued Books");
            System.out.println("7. Change Password");
            System.out.println("8. Back to Main Menu");
            
            int choice = getIntInput("Choose an option: ");
            
            switch (choice) {
                case 1:
                    studentPanel.viewProfile();
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 2:
                    System.out.print("Enter new name (current: " + student.getName() + "): ");
                    String name = scanner.nextLine();
                    if (ValidationUtil.isNullOrEmpty(name)) {
                        name = student.getName();
                    }
                    
                    String email = ConsoleUtil.readEmail("Enter new email (current: " + student.getEmail() + "): ");
                    if (ValidationUtil.isNullOrEmpty(email)) {
                        email = student.getEmail();
                    }
                    
                    studentPanel.updateProfile(name, email);
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 3:
                    studentPanel.viewCourses();
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 4:
                    studentPanel.viewAttendance();
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 5:
                    studentPanel.viewTimetable();
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 6:
                    studentPanel.viewIssuedBooks();
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 7:
                    String oldPassword = ConsoleUtil.readPassword("Enter current password: ");
                    String newPassword = ConsoleUtil.readPassword("Enter new password: ");
                    String confirmPassword = ConsoleUtil.readPassword("Confirm new password: ");
                    
                    if (!newPassword.equals(confirmPassword)) {
                        System.out.println("Error: New passwords don't match.");
                    } else if (auth.changePassword(student.getId(), oldPassword, newPassword)) {
                        System.out.println("Password changed successfully.");
                    } else {
                        System.out.println("Error: Current password is incorrect.");
                    }
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 8:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option!");
                    ConsoleUtil.pressEnterToContinue();
            }
        }
    }

    private static void handleFacultyPanel(Faculty faculty) {
        FacultyPanel facultyPanel = new FacultyPanel(faculty);
        boolean back = false;
        
        while (!back) {
            ConsoleUtil.clearScreen();
            System.out.println("=========================");
            System.out.println("Faculty Panel - Welcome " + faculty.getName());
            System.out.println("=========================");
            System.out.println("1. View Profile");
            System.out.println("2. Update Profile");
            System.out.println("3. View Assigned Subjects");
            System.out.println("4. View Assigned Classes");
            System.out.println("5. View My Attendance");
            System.out.println("6. Mark Student Attendance");
            System.out.println("7. View Timetable");
            System.out.println("8. View Issued Books");
            System.out.println("9. Change Password");
            System.out.println("10. Back to Main Menu");
            
            int choice = getIntInput("Choose an option: ");
            
            switch (choice) {
                case 1:
                    facultyPanel.viewProfile();
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 2:
                    System.out.print("Enter new name (current: " + faculty.getName() + "): ");
                    String name = scanner.nextLine();
                    if (ValidationUtil.isNullOrEmpty(name)) {
                        name = faculty.getName();
                    }
                    
                    String email = ConsoleUtil.readEmail("Enter new email (current: " + faculty.getEmail() + "): ");
                    if (ValidationUtil.isNullOrEmpty(email)) {
                        email = faculty.getEmail();
                    }
                    
                    facultyPanel.updateProfile(name, email);
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 3:
                    facultyPanel.viewSubjects();
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 4:
                    facultyPanel.viewAssignedClasses();
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 5:
                    facultyPanel.viewAttendance();
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 6:
                    adminPanel.viewAllStudents();
                    System.out.print("\nEnter student ID: ");
                    String studentId = scanner.nextLine();
                    Student student = adminPanel.searchStudent(studentId);
                    if (student != null) {
                        System.out.print("Enter date (YYYY-MM-DD): ");
                        String date = scanner.nextLine();
                        System.out.print("Is student present? (y/n): ");
                        String input = scanner.nextLine().trim().toLowerCase();
                        boolean present = input.equals("y") || input.equals("yes");
                        facultyPanel.markStudentAttendance(student, date, present);
                    } else {
                        System.out.println("Student not found with ID: " + studentId);
                    }
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 7:
                    facultyPanel.viewTimetable();
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 8:
                    facultyPanel.viewIssuedBooks();
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 9:
                    String oldPassword = ConsoleUtil.readPassword("Enter current password: ");
                    String newPassword = ConsoleUtil.readPassword("Enter new password: ");
                    String confirmPassword = ConsoleUtil.readPassword("Confirm new password: ");
                    
                    if (!newPassword.equals(confirmPassword)) {
                        System.out.println("Error: New passwords don't match.");
                    } else if (auth.changePassword(faculty.getId(), oldPassword, newPassword)) {
                        System.out.println("Password changed successfully.");
                    } else {
                        System.out.println("Error: Current password is incorrect.");
                    }
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 10:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option!");
                    ConsoleUtil.pressEnterToContinue();
            }
        }
    }

    private static void handleLibraryPanel() {
        boolean back = false;
        
        while (!back) {
            ConsoleUtil.clearScreen();
            System.out.println("=========================");
            System.out.println("Library System");
            System.out.println("=========================");
            System.out.println("1. Add Book");
            System.out.println("2. Issue Book");
            System.out.println("3. Return Book");
            System.out.println("4. View Available Books");
            System.out.println("5. View Issued Books");
            System.out.println("6. Search Book by Title");
            System.out.println("7. Display Issue Queue");
            System.out.println("8. Back to Main Menu");
            
            int choice = getIntInput("Choose an option: ");
            
            switch (choice) {
                case 1:
                    System.out.print("Enter book ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter book title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();
                    Book newBook = new Book(id, title, author);
                    library.addBook(newBook);
                    allBooks.add(newBook);
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 2:
                    library.viewAvailableBooks(allBooks);
                    
                    System.out.println("\nAvailable Students:");
                    adminPanel.viewAllStudents();
                    
                    System.out.print("\nEnter student ID: ");
                    String studentId = scanner.nextLine();
                    Student student = adminPanel.searchStudent(studentId);
                    if (student != null) {
                        System.out.print("Enter book title to issue: ");
                        String bookTitle = scanner.nextLine();
                        Book book = library.linearSearchByTitle(allBooks, bookTitle);
                        if (book != null && !book.isIssued()) {
                            library.issueBook(student, book);
                        } else if (book == null) {
                            System.out.println("Book not found: " + bookTitle);
                        } else {
                            System.out.println("Book is already issued!");
                        }
                    } else {
                        System.out.println("Student not found with ID: " + studentId);
                    }
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 3:
                    library.viewIssuedBooks(allBooks);
                    
                    System.out.print("\nEnter student ID: ");
                    String studId = scanner.nextLine();
                    Student stud = adminPanel.searchStudent(studId);
                    if (stud != null) {
                        System.out.println("\nBooks issued to " + stud.getName() + ":");
                        CustomArrayList<String> issuedBooks = stud.getIssuedBooks();
                        if (issuedBooks.size() == 0) {
                            System.out.println("No books issued to this student.");
                        } else {
                            TableFormatter booksTable = new TableFormatter("No.", "Book Title", "Author");
                            int count = 0;
                            for (int i = 0; i < issuedBooks.size(); i++) {
                                String bookId = issuedBooks.get(i);
                                for (int j = 0; j < allBooks.size(); j++) {
                                    Book b = allBooks.get(j);
                                    if (b.getId().equals(bookId)) {
                                        count++;
                                        booksTable.addRow(String.valueOf(count), b.getTitle(), b.getAuthor());
                                    }
                                }
                            }
                            System.out.println(booksTable.toString());
                            
                            System.out.print("\nEnter book title to return: ");
                            String bookTitle = scanner.nextLine();
                            Book book = library.linearSearchByTitle(allBooks, bookTitle);
                            if (book != null && book.isIssued()) {
                                library.returnBook(stud, book);
                            } else if (book == null) {
                                System.out.println("Book not found: " + bookTitle);
                            } else {
                                System.out.println("Book is not issued!");
                            }
                        }
                    } else {
                        System.out.println("Student not found with ID: " + studId);
                    }
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 4:
                    library.viewAvailableBooks(allBooks);
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 5:
                    library.viewIssuedBooks(allBooks);
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 6:
                    System.out.print("Enter book title to search: ");
                    String searchTitle = scanner.nextLine();
                    System.out.println("\nUsing Linear Search:");
                    Book foundBook = library.linearSearchByTitle(allBooks, searchTitle);
                    if (foundBook != null) {
                        System.out.println("Book found:");
                        TableFormatter bookTable = new TableFormatter("Field", "Value");
                        bookTable.addRow("ID", foundBook.getId());
                        bookTable.addRow("Title", foundBook.getTitle());
                        bookTable.addRow("Author", foundBook.getAuthor());
                        bookTable.addRow("Status", (foundBook.isIssued() ? "Issued" : "Available"));
                        System.out.println(bookTable.toString());
                    } else {
                        System.out.println("Book not found: " + searchTitle);
                    }
                    
                    if (allBooks.size() > 1) {
                        System.out.println("\nUsing Binary Search:");
                        Book binarySearchBook = library.binarySearchByTitle(allBooks, searchTitle);
                        if (binarySearchBook != null) {
                            System.out.println("Book found:");
                            TableFormatter binaryBookTable = new TableFormatter("Field", "Value");
                            binaryBookTable.addRow("ID", binarySearchBook.getId());
                            binaryBookTable.addRow("Title", binarySearchBook.getTitle());
                            binaryBookTable.addRow("Author", binarySearchBook.getAuthor());
                            binaryBookTable.addRow("Status", (binarySearchBook.isIssued() ? "Issued" : "Available"));
                            System.out.println(binaryBookTable.toString());
                        } else {
                            System.out.println("Book not found using binary search: " + searchTitle);
                        }
                    }
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 7:
                    library.displayIssueQueue();
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 8:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option!");
                    ConsoleUtil.pressEnterToContinue();
            }
        }
    }

    private static void handleCoursePanel() {
        boolean back = false;
        
        while (!back) {
            ConsoleUtil.clearScreen();
            System.out.println("=========================");
            System.out.println("Course View Panel");
            System.out.println("=========================");
            System.out.println("1. View All Courses");
            System.out.println("2. Search Course by Name");
            System.out.println("3. View Courses by Stream");
            System.out.println("4. Sort Courses by Name");
            System.out.println("5. Sort Courses by Fees");
            System.out.println("6. Sort Courses by Duration");
            System.out.println("7. Search Courses by Fees Range");
            System.out.println("8. Back to Main Menu");
            
            int choice = getIntInput("Choose an option: ");
            
            switch (choice) {
                case 1:
                    courseManagement.viewCourses();
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 2:
                    System.out.print("Enter course name to search: ");
                    String courseName = scanner.nextLine();
                    Course course = courseManagement.searchCourse(courseName);
                    if (course != null) {
                        courseManagement.displayCourseDetails(course);
                    } else {
                        System.out.println("Course not found: " + courseName);
                    }
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 3:
                    System.out.print("Select stream to view courses for: ");
                    String streamName = ConsoleUtil.selectStream();
                    courseManagement.viewCoursesByStream(streamName);
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 4:
                    courseManagement.sortCoursesByName();
                    courseManagement.viewCourses();
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 5:
                    courseManagement.sortCoursesByFees();
                    courseManagement.viewCourses();
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 6:
                    courseManagement.sortCoursesByDuration();
                    courseManagement.viewCourses();
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 7:
                    System.out.print("Enter minimum fees: ₹");
                    double minFees = getDoubleInput("");
                    System.out.print("Enter maximum fees: ₹");
                    double maxFees = getDoubleInput("");
                    CustomArrayList<Course> feesFilteredCourses = courseManagement.searchCoursesByFeesRange(minFees, maxFees);
                    courseManagement.displayCourseSearchResults(feesFilteredCourses, "Courses in fee range ₹" + minFees + " - ₹" + maxFees);
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 8:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option!");
                    ConsoleUtil.pressEnterToContinue();
            }
        }
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
    
    private static double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                double value = Double.parseDouble(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}