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
import ds.CustomArrayList;
import java.util.Scanner;

public class ICMS {
    private static Scanner scanner = new Scanner(System.in);
    private static AuthSystem auth = new AuthSystem();
    private static AdminPanel adminPanel = new AdminPanel();
    private static LibrarySystem library = new LibrarySystem();
    private static CourseManagement courseManagement = new CourseManagement();
    private static CustomArrayList<Book> allBooks = new CustomArrayList<>();

    public static void main(String[] args) {
        // Add some initial data
        initializeData();
        
        boolean exit = false;
        while (!exit) {
            clearScreen();
            displayMainMenu();
            int choice = getIntInput("Choose an option: ");
            
            if (choice == 6) {
                System.out.println("Exiting...");
                exit = true;
                continue;
            }
            
            clearScreen();
            System.out.println("=== LOGIN REQUIRED ===");
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            
            String role = auth.login(username, password);
            if (role == null) {
                System.out.println("Invalid credentials!");
                pressEnterToContinue();
                continue;
            }
            
            switch (choice) {
                case 1: // Admin
                    if (role.equals("ADMIN")) {
                        handleAdminPanel();
                    } else {
                        System.out.println("Access denied: Admin privileges required!");
                        pressEnterToContinue();
                    }
                    break;
                case 2: // Student
                    if (role.equals("STUDENT")) {
                        // Find existing student or create new one
                        Student student = adminPanel.searchStudent(username);
                        if (student == null) {
                            student = new Student(username, "Student " + username, username + "@college.edu", "CS");
                            adminPanel.addStudent(student);
                        }
                        handleStudentPanel(student);
                    } else {
                        System.out.println("Access denied: Student privileges required!");
                        pressEnterToContinue();
                    }
                    break;
                case 3: // Faculty
                    if (role.equals("FACULTY")) {
                        Faculty faculty = adminPanel.searchFaculty(username);
                        if (faculty == null) {
                            faculty = new Faculty(username, "Faculty " + username, username + "@college.edu");
                            adminPanel.addFaculty(faculty);
                        }
                        handleFacultyPanel(faculty);
                    } else {
                        System.out.println("Access denied: Faculty privileges required!");
                        pressEnterToContinue();
                    }
                    break;
                case 4: // Library
                    if (role.equals("LIBRARY")) {
                        handleLibraryPanel();
                    } else {
                        System.out.println("Access denied: Library privileges required!");
                        pressEnterToContinue();
                    }
                    break;
                case 5: // Courses
                    handleCoursePanel();
                    break;
                default:
                    System.out.println("Invalid option!");
                    pressEnterToContinue();
                    break;
            }
        }
        scanner.close();
    }

    // Helper method to clear the screen
    private static void clearScreen() {
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

    // Helper method to wait for user input before continuing
    private static void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    // Helper method to display main menu
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

    // Admin Panel Handling
    private static void handleAdminPanel() {
        boolean back = false;
        while (!back) {
            clearScreen();
            System.out.println("=========================");
            System.out.println("Admin Panel");
            System.out.println("=========================");
            System.out.println("1. Student Administration");
            System.out.println("2. Faculty Administration");
            System.out.println("3. Back to Main Menu");
            
            int choice = getIntInput("Choose an option: ");
            
            switch (choice) {
                case 1:
                    handleStudentAdmin();
                    break;
                case 2:
                    handleFacultyAdmin();
                    break;
                case 3:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option!");
                    pressEnterToContinue();
            }
        }
    }
    
    private static void handleStudentAdmin() {
        boolean back = false;
        while (!back) {
            clearScreen();
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
                    // Add student
                    System.out.print("Enter student ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter student name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter student email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter stream: ");
                    String stream = scanner.nextLine();
                    adminPanel.addStudent(new Student(id, name, email, stream));
                    pressEnterToContinue();
                    break;
                case 2:
                    // Update student
                    System.out.print("Enter student ID to update: ");
                    String updateId = scanner.nextLine();
                    Student student = adminPanel.searchStudent(updateId);
                    if (student != null) {
                        System.out.print("Enter new name (current: " + student.getName() + "): ");
                        String newName = scanner.nextLine();
                        System.out.print("Enter new email (current: " + student.getEmail() + "): ");
                        String newEmail = scanner.nextLine();
                        System.out.print("Enter new stream (current: " + student.getStream() + "): ");
                        String newStream = scanner.nextLine();
                        adminPanel.updateStudent(updateId, newName, newEmail, newStream);
                    } else {
                        System.out.println("Student not found with ID: " + updateId);
                    }
                    pressEnterToContinue();
                    break;
                case 3:
                    // Delete student
                    System.out.print("Enter student ID to delete: ");
                    String deleteId = scanner.nextLine();
                    adminPanel.deleteStudent(deleteId);
                    pressEnterToContinue();
                    break;
                case 4:
                    // View all students
                    adminPanel.viewAllStudents();
                    pressEnterToContinue();
                    break;
                case 5:
                    // Assign course
                    System.out.print("Enter student ID: ");
                    String studentId = scanner.nextLine();
                    Student studentToAssign = adminPanel.searchStudent(studentId);
                    if (studentToAssign != null) {
                        // Show available courses
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
                    pressEnterToContinue();
                    break;
                case 6:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option!");
                    pressEnterToContinue();
            }
        }
    }
    
    private static void handleFacultyAdmin() {
        boolean back = false;
        while (!back) {
            clearScreen();
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
                    // Add faculty
                    System.out.print("Enter faculty ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter faculty name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter faculty email: ");
                    String email = scanner.nextLine();
                    adminPanel.addFaculty(new Faculty(id, name, email));
                    pressEnterToContinue();
                    break;
                case 2:
                    // Update faculty
                    System.out.print("Enter faculty ID to update: ");
                    String updateId = scanner.nextLine();
                    Faculty faculty = adminPanel.searchFaculty(updateId);
                    if (faculty != null) {
                        System.out.print("Enter new name (current: " + faculty.getName() + "): ");
                        String newName = scanner.nextLine();
                        System.out.print("Enter new email (current: " + faculty.getEmail() + "): ");
                        String newEmail = scanner.nextLine();
                        adminPanel.updateFaculty(updateId, newName, newEmail);
                    } else {
                        System.out.println("Faculty not found with ID: " + updateId);
                    }
                    pressEnterToContinue();
                    break;
                case 3:
                    // Delete faculty
                    System.out.print("Enter faculty ID to delete: ");
                    String deleteId = scanner.nextLine();
                    adminPanel.deleteFaculty(deleteId);
                    pressEnterToContinue();
                    break;
                case 4:
                    // View all faculty
                    adminPanel.viewAllFaculty();
                    pressEnterToContinue();
                    break;
                case 5:
                    // Assign subject
                    System.out.print("Enter faculty ID: ");
                    String facultyId = scanner.nextLine();
                    Faculty facultyMember = adminPanel.searchFaculty(facultyId);
                    if (facultyMember != null) {
                        System.out.print("Enter subject to assign: ");
                        String subject = scanner.nextLine();
                        adminPanel.assignSubjectToFaculty(facultyId, subject);
                    } else {
                        System.out.println("Faculty not found with ID: " + facultyId);
                    }
                    pressEnterToContinue();
                    break;
                case 6:
                    // Assign class
                    System.out.print("Enter faculty ID: ");
                    String facId = scanner.nextLine();
                    Faculty facMember = adminPanel.searchFaculty(facId);
                    if (facMember != null) {
                        System.out.print("Enter class name to assign: ");
                        String className = scanner.nextLine();
                        adminPanel.assignClassToFaculty(facId, className);
                    } else {
                        System.out.println("Faculty not found with ID: " + facId);
                    }
                    pressEnterToContinue();
                    break;
                case 7:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option!");
                    pressEnterToContinue();
            }
        }
    }

    // Student Panel Handling
    private static void handleStudentPanel(Student student) {
        StudentPanel studentPanel = new StudentPanel(student);
        boolean back = false;
        
        while (!back) {
            clearScreen();
            System.out.println("=========================");
            System.out.println("Student Panel - Welcome " + student.getName());
            System.out.println("=========================");
            System.out.println("1. View Profile");
            System.out.println("2. Update Profile");
            System.out.println("3. View Courses");
            System.out.println("4. View Attendance");
            System.out.println("5. View Timetable");
            System.out.println("6. View Issued Books");
            System.out.println("7. Back to Main Menu");
            
            int choice = getIntInput("Choose an option: ");
            
            switch (choice) {
                case 1:
                    studentPanel.viewProfile();
                    pressEnterToContinue();
                    break;
                case 2:
                    System.out.print("Enter new name (current: " + student.getName() + "): ");
                    String name = scanner.nextLine();
                    System.out.print("Enter new email (current: " + student.getEmail() + "): ");
                    String email = scanner.nextLine();
                    studentPanel.updateProfile(name, email);
                    pressEnterToContinue();
                    break;
                case 3:
                    studentPanel.viewCourses();
                    pressEnterToContinue();
                    break;
                case 4:
                    studentPanel.viewAttendance();
                    pressEnterToContinue();
                    break;
                case 5:
                    studentPanel.viewTimetable();
                    pressEnterToContinue();
                    break;
                case 6:
                    studentPanel.viewIssuedBooks();
                    pressEnterToContinue();
                    break;
                case 7:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option!");
                    pressEnterToContinue();
            }
        }
    }

    // Faculty Panel Handling
    private static void handleFacultyPanel(Faculty faculty) {
        FacultyPanel facultyPanel = new FacultyPanel(faculty);
        boolean back = false;
        
        while (!back) {
            clearScreen();
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
            System.out.println("9. Back to Main Menu");
            
            int choice = getIntInput("Choose an option: ");
            
            switch (choice) {
                case 1:
                    facultyPanel.viewProfile();
                    pressEnterToContinue();
                    break;
                case 2:
                    System.out.print("Enter new name (current: " + faculty.getName() + "): ");
                    String name = scanner.nextLine();
                    System.out.print("Enter new email (current: " + faculty.getEmail() + "): ");
                    String email = scanner.nextLine();
                    facultyPanel.updateProfile(name, email);
                    pressEnterToContinue();
                    break;
                case 3:
                    facultyPanel.viewSubjects();
                    pressEnterToContinue();
                    break;
                case 4:
                    facultyPanel.viewAssignedClasses();
                    pressEnterToContinue();
                    break;
                case 5:
                    facultyPanel.viewAttendance();
                    pressEnterToContinue();
                    break;
                case 6:
                    // List available students first
                    adminPanel.viewAllStudents();
                    System.out.print("\nEnter student ID: ");
                    String studentId = scanner.nextLine();
                    Student student = adminPanel.searchStudent(studentId);
                    if (student != null) {
                        System.out.print("Enter date (YYYY-MM-DD): ");
                        String date = scanner.nextLine();
                        System.out.print("Is student present? (true/false): ");
                        boolean present = Boolean.parseBoolean(scanner.nextLine());
                        facultyPanel.markStudentAttendance(student, date, present);
                    } else {
                        System.out.println("Student not found with ID: " + studentId);
                    }
                    pressEnterToContinue();
                    break;
                case 7:
                    facultyPanel.viewTimetable();
                    pressEnterToContinue();
                    break;
                case 8:
                    facultyPanel.viewIssuedBooks();
                    pressEnterToContinue();
                    break;
                case 9:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option!");
                    pressEnterToContinue();
            }
        }
    }

    // Library Panel Handling
    private static void handleLibraryPanel() {
        boolean back = false;
        
        while (!back) {
            clearScreen();
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
                    pressEnterToContinue();
                    break;
                case 2:
                    // Display available books first
                    library.viewAvailableBooks(allBooks);
                    
                    // Display available students
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
                    pressEnterToContinue();
                    break;
                case 3:
                    // Display issued books first
                    library.viewIssuedBooks(allBooks);
                    
                    System.out.print("\nEnter student ID: ");
                    String studId = scanner.nextLine();
                    Student stud = adminPanel.searchStudent(studId);
                    if (stud != null) {
                        // Display books issued to this student
                        System.out.println("\nBooks issued to " + stud.getName() + ":");
                        CustomArrayList<String> issuedBooks = stud.getIssuedBooks();
                        if (issuedBooks.size() == 0) {
                            System.out.println("No books issued to this student.");
                        } else {
                            for (int i = 0; i < issuedBooks.size(); i++) {
                                String bookId = issuedBooks.get(i);
                                for (int j = 0; j < allBooks.size(); j++) {
                                    Book b = allBooks.get(j);
                                    if (b.getId().equals(bookId)) {
                                        System.out.println((i + 1) + ". " + b.getTitle() + " by " + b.getAuthor());
                                    }
                                }
                            }
                            
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
                    pressEnterToContinue();
                    break;
                case 4:
                    library.viewAvailableBooks(allBooks);
                    pressEnterToContinue();
                    break;
                case 5:
                    library.viewIssuedBooks(allBooks);
                    pressEnterToContinue();
                    break;
                case 6:
                    System.out.print("Enter book title to search: ");
                    String searchTitle = scanner.nextLine();
                    System.out.println("\nUsing Linear Search:");
                    Book foundBook = library.linearSearchByTitle(allBooks, searchTitle);
                    if (foundBook != null) {
                        System.out.println("Book found:");
                        System.out.println("ID: " + foundBook.getId());
                        System.out.println("Title: " + foundBook.getTitle());
                        System.out.println("Author: " + foundBook.getAuthor());
                        System.out.println("Status: " + (foundBook.isIssued() ? "Issued" : "Available"));
                    } else {
                        System.out.println("Book not found: " + searchTitle);
                    }
                    
                    // Try binary search as well
                    if (allBooks.size() > 1) {
                        System.out.println("\nUsing Binary Search:");
                        Book binarySearchBook = library.binarySearchByTitle(allBooks, searchTitle);
                        if (binarySearchBook != null) {
                            System.out.println("Book found:");
                            System.out.println("ID: " + binarySearchBook.getId());
                            System.out.println("Title: " + binarySearchBook.getTitle());
                            System.out.println("Author: " + binarySearchBook.getAuthor());
                            System.out.println("Status: " + (binarySearchBook.isIssued() ? "Issued" : "Available"));
                        } else {
                            System.out.println("Book not found using binary search: " + searchTitle);
                        }
                    }
                    pressEnterToContinue();
                    break;
                case 7:
                    library.displayIssueQueue();
                    pressEnterToContinue();
                    break;
                case 8:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option!");
                    pressEnterToContinue();
            }
        }
    }

    // Course Panel Handling
    private static void handleCoursePanel() {
        boolean back = false;
        
        while (!back) {
            clearScreen();
            System.out.println("=========================");
            System.out.println("Course Management System");
            System.out.println("=========================");
            System.out.println("1. Add Course");
            System.out.println("2. View All Courses");
            System.out.println("3. Search Course by Name");
            System.out.println("4. View Courses by Stream");
            System.out.println("5. Sort Courses by Name (Bubble Sort)");
            System.out.println("6. Sort Courses by Fees (Selection Sort)");
            System.out.println("7. Sort Courses by Duration (Insertion Sort)");
            System.out.println("8. Sort Courses by Name (Merge Sort)");
            System.out.println("9. Search Courses by Fees Range");
            System.out.println("10. Back to Main Menu");
            
            int choice = getIntInput("Choose an option: ");
            
            switch (choice) {
                case 1:
                    System.out.print("Enter course ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter course name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter duration (months): ");
                    int duration = getIntInput("");
                    System.out.print("Enter fees: ₹");
                    double fees = getDoubleInput("");
                    System.out.print("Enter scope: ");
                    String scope = scanner.nextLine();
                    System.out.print("Enter stream/department: ");
                    String stream = scanner.nextLine();
                    courseManagement.addCourse(new Course(id, name, duration, fees, scope, stream));
                    pressEnterToContinue();
                    break;
                case 2:
                    courseManagement.viewCourses();
                    pressEnterToContinue();
                    break;
                case 3:
                    System.out.print("Enter course name to search: ");
                    String courseName = scanner.nextLine();
                    Course course = courseManagement.searchCourse(courseName);
                    if (course != null) {
                        System.out.println("\nCourse found:");
                        System.out.println("ID: " + course.getId());
                        System.out.println("Name: " + course.getName());
                        System.out.println("Duration: " + course.getDuration() + " months");
                        System.out.println("Fees: ₹" + course.getFees());
                        System.out.println("Scope: " + course.getScope());
                        System.out.println("Stream: " + course.getStream());
                    } else {
                        System.out.println("Course not found: " + courseName);
                    }
                    pressEnterToContinue();
                    break;
                case 4:
                    System.out.print("Enter stream/department: ");
                    String streamName = scanner.nextLine();
                    courseManagement.viewCoursesByStream(streamName);
                    pressEnterToContinue();
                    break;
                case 5:
                    courseManagement.sortCoursesByName();
                    courseManagement.viewCourses();
                    pressEnterToContinue();
                    break;
                case 6:
                    courseManagement.sortCoursesByFees();
                    courseManagement.viewCourses();
                    pressEnterToContinue();
                    break;
                case 7:
                    courseManagement.sortCoursesByDuration();
                    courseManagement.viewCourses();
                    pressEnterToContinue();
                    break;
                case 8:
                    courseManagement.mergeSortCoursesByName();
                    courseManagement.viewCourses();
                    pressEnterToContinue();
                    break;
                case 9:
                    System.out.print("Enter minimum fees: ₹");
                    double minFees = getDoubleInput("");
                    System.out.print("Enter maximum fees: ₹");
                    double maxFees = getDoubleInput("");
                    CustomArrayList<Course> feesFilteredCourses = courseManagement.searchCoursesByFeesRange(minFees, maxFees);
                    System.out.println("\nCourses in fee range ₹" + minFees + " - ₹" + maxFees + ":");
                    if (feesFilteredCourses.size() == 0) {
                        System.out.println("No courses found in this fee range.");
                    } else {
                        for (int i = 0; i < feesFilteredCourses.size(); i++) {
                            Course c = feesFilteredCourses.get(i);
                            System.out.println("\nCourse #" + (i + 1) + ":");
                            System.out.println("Name: " + c.getName());
                            System.out.println("Fees: ₹" + c.getFees());
                            System.out.println("Duration: " + c.getDuration() + " months");
                            System.out.println("Stream: " + c.getStream());
                        }
                    }
                    pressEnterToContinue();
                    break;
                case 10:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option!");
                    pressEnterToContinue();
            }
        }
    }

    // Helper method to initialize system with some data
    private static void initializeData() {
        // Add some students
        adminPanel.addStudent(new Student("S001", "John Smith", "john@example.com", "Computer Science"));
        adminPanel.addStudent(new Student("S002", "Sarah Johnson", "sarah@example.com", "Electronics"));
        
        // Add some faculty
        Faculty prof1 = new Faculty("F001", "Dr. Robert Brown", "robert@example.com");
        Faculty prof2 = new Faculty("F002", "Dr. Emily Davis", "emily@example.com");
        adminPanel.addFaculty(prof1);
        adminPanel.addFaculty(prof2);
        
        // Assign subjects
        prof1.addSubject("Database Systems");
        prof1.addSubject("Data Structures");
        prof2.addSubject("Digital Electronics");
        prof2.addSubject("Circuit Theory");
        
        // Assign classes
        prof1.assignClass("CS101");
        prof1.assignClass("CS205");
        prof2.assignClass("EL103");
        prof2.assignClass("EL204");
        
        // Add some books
        Book book1 = new Book("B001", "Introduction to Algorithms", "Cormen et al.");
        Book book2 = new Book("B002", "Database System Concepts", "Silberschatz et al.");
        Book book3 = new Book("B003", "Digital Design", "Morris Mano");
        Book book4 = new Book("B004", "Computer Networks", "Tanenbaum");
        Book book5 = new Book("B005", "Operating System Concepts", "Silberschatz et al.");
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
        library.addBook(book4);
        library.addBook(book5);
        allBooks.add(book1);
        allBooks.add(book2);
        allBooks.add(book3);
        allBooks.add(book4);
        allBooks.add(book5);
        
        // Add some courses
        courseManagement.addCourse(new Course("C001", "Database Management", 6, 12000.0, "Databases, SQL, DBMS", "Computer Science"));
        courseManagement.addCourse(new Course("C002", "Data Structures & Algorithms", 4, 10000.0, "Programming, Algorithms", "Computer Science"));
        courseManagement.addCourse(new Course("C003", "Digital Electronics", 5, 11000.0, "Digital Circuits, Logic Design", "Electronics"));
        courseManagement.addCourse(new Course("C004", "Computer Networks", 5, 11500.0, "Networking, Security", "Computer Science"));
        courseManagement.addCourse(new Course("C005", "Operating Systems", 3, 9000.0, "OS Design, Process Management", "Computer Science"));
        courseManagement.addCourse(new Course("C006", "VLSI Design", 6, 13000.0, "IC Design, Semiconductor", "Electronics"));
        
        // Assign courses to students
        Student student1 = adminPanel.searchStudent("S001");
        student1.addCourse("Database Management");
        student1.addCourse("Data Structures & Algorithms");
        
        Student student2 = adminPanel.searchStudent("S002");
        student2.addCourse("Digital Electronics");
        student2.addCourse("VLSI Design");
        
        // Mark some attendance
        student1.markAttendance("2025-01-15", true);
        student1.markAttendance("2025-01-16", true);
        student1.markAttendance("2025-01-17", false);
        
        student2.markAttendance("2025-01-15", true);
        student2.markAttendance("2025-01-16", true);
        student2.markAttendance("2025-01-17", true);
        
        prof1.markAttendance("2025-01-15", true);
        prof1.markAttendance("2025-01-16", true);
        prof2.markAttendance("2025-01-15", true);
        
        // Add some timetable entries (these don't actually get displayed but helps demonstrate the concept)
        prof1.setTimetableEntry("Monday 9:00", "CS101 - Room 101");
        prof1.setTimetableEntry("Monday 11:00", "CS205 - Lab 202");
        prof2.setTimetableEntry("Tuesday 9:00", "EL103 - Room 301");
        
        System.out.println("System initialized with sample data.");
    }
    
    // Helper method to safely get integer input
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
    
    // Helper method to safely get double input
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