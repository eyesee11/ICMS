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
import data.BookData;
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

        CustomArrayList<Book> booksFromCSV = FileHandler.loadBooks();
        if (booksFromCSV.size() > 0) {
            for (int i = 0; i < booksFromCSV.size(); i++) {
                allBooks.add(booksFromCSV.get(i));
                library.addBook(booksFromCSV.get(i));
            }
            System.out.println("Loaded " + booksFromCSV.size() + " books from CSV file.");
        } else {
            BookData.loadSampleBooks(allBooks);
            for (int i = 0; i < allBooks.size(); i++) {
                library.addBook(allBooks.get(i));
            }
        }

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
                    if (role.equals("LIBRARY") || role.equals("STUDENT") || role.equals("FACULTY")) {
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
            System.out.println("4. Library Management");
            System.out.println("5. Reset User Password");
            System.out.println("6. Back to Main Menu");

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
                    handleAdminLibraryManagement();
                    break;
                case 5:
                    System.out.println("\nReset User Password");
                    System.out.println("===================");
                    adminPanel.viewAllStudents();
                    System.out.println("\nFaculty Members:");
                    adminPanel.viewAllFaculty();
                    System.out.print("\nEnter user ID to reset password: ");
                    String userId = scanner.nextLine();
                    adminPanel.resetUserPassword(userId);
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

                    String stream = ConsoleUtil.selectStream();

                    int admissionYear = 0;
                    while (admissionYear < 2000 || admissionYear > 2100) {
                        System.out.print("Enter admission year (YYYY): ");
                        try {
                            admissionYear = Integer.parseInt(scanner.nextLine());
                            if (admissionYear < 2000 || admissionYear > 2100) {
                                System.out.println("Please enter a valid year between 2000 and 2100!");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid year!");
                        }
                    }

                    adminPanel.addStudent(new Student(id, name, "", stream, admissionYear));
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

                        System.out.println("Current stream: " + student.getStream());
                        System.out.println("Enter new stream or leave blank to keep current: ");
                        String newStream = ConsoleUtil.selectStream();

                        adminPanel.updateStudent(updateId, newName, "", newStream);
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
                        System.out.print("\nEnter course ID to assign: ");
                        String courseId = scanner.nextLine();
                        Course course = courseManagement.searchCourseById(courseId);
                        if (course != null) {
                            adminPanel.assignCourseToStudent(studentId, courseId);
                        } else {
                            System.out.println("Course not found: " + courseId);
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

                    adminPanel.addFaculty(new Faculty(id, name, ""));
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

                        adminPanel.updateFaculty(updateId, newName, "");
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
                        courseManagement.viewCourses();
                        System.out.print("\nEnter course ID to assign as subject: ");
                        String courseId = scanner.nextLine();
                        adminPanel.assignSubjectToFaculty(facultyId, courseId);
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

                    studentPanel.updateProfile(name, "");
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

                    // Display password requirements
                    System.out.println("\nPassword Requirements:");
                    System.out.println("- Minimum 6 characters long");
                    System.out.println("- Must contain at least one uppercase letter");
                    System.out.println("- Must contain at least one number");
                    System.out.println("- Must contain at least one special character");
                    System.out.println();

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

                    facultyPanel.updateProfile(name, "");
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

                    // Display password requirements
                    System.out.println("\nPassword Requirements:");
                    System.out.println("- Minimum 6 characters long");
                    System.out.println("- Must contain at least one uppercase letter");
                    System.out.println("- Must contain at least one number");
                    System.out.println("- Must contain at least one special character");
                    System.out.println();

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
        String currentUserRole = auth.getUserRole(auth.getCurrentUsername());
        Student currentStudent = null;
        Faculty currentFaculty = null;

        if ("STUDENT".equals(currentUserRole)) {
            currentStudent = adminPanel.searchStudent(auth.getCurrentUsername());
        } else if ("FACULTY".equals(currentUserRole)) {
            currentFaculty = adminPanel.searchFaculty(auth.getCurrentUsername());
        }

        while (!back) {
            ConsoleUtil.clearScreen();
            System.out.println("=========================");
            System.out.println("Library System");
            System.out.println("=========================");

            // Different menu based on user role
            if ("LIBRARY".equals(currentUserRole)) {
                System.out.println("1. Issue Book");
                System.out.println("2. Return Book");
                System.out.println("3. View Available Books");
                System.out.println("4. View Issued Books");
                System.out.println("5. Search Book by Title");
                System.out.println("6. Display Issue Queue");
                System.out.println("7. Back to Main Menu");
            } else {
                // Enhanced menu for students and faculty
                System.out.println("1. View Available Books");
                System.out.println("2. View My Issued Books");
                System.out.println("3. Issue/Borrow a Book");
                System.out.println("4. Return Book");
                System.out.println("5. Search Book by Title");
                System.out.println("6. Back to Main Menu");
            }

            int choice = getIntInput("Choose an option: ");

            if ("LIBRARY".equals(currentUserRole)) {
                switch (choice) {
                    case 1:
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
                    case 2:
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
                    case 3:
                        library.viewAvailableBooks(allBooks);
                        ConsoleUtil.pressEnterToContinue();
                        break;
                    case 4:
                        library.viewIssuedBooks(allBooks);
                        ConsoleUtil.pressEnterToContinue();
                        break;
                    case 5:
                        searchBookByTitle();
                        ConsoleUtil.pressEnterToContinue();
                        break;
                    case 6:
                        library.displayIssueQueue();
                        ConsoleUtil.pressEnterToContinue();
                        break;
                    case 7:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid option!");
                        ConsoleUtil.pressEnterToContinue();
                }
            } else {
                // Enhanced student/faculty menu with book request functionality
                switch (choice) {
                    case 1:
                        library.viewAvailableBooks(allBooks);
                        ConsoleUtil.pressEnterToContinue();
                        break;
                    case 2:
                        if (currentStudent != null) {
                            library.viewUserIssuedBooks(allBooks, currentStudent);
                        } else if (currentFaculty != null) {
                            library.viewUserIssuedBooks(allBooks, currentFaculty);
                        }
                        ConsoleUtil.pressEnterToContinue();
                        break;
                    case 3:
                        // New functionality: Request book issue for students and faculty
                        library.viewAvailableBooks(allBooks);
                        System.out.print("\nEnter book title to request: ");
                        String bookTitle = scanner.nextLine();
                        Book book = library.linearSearchByTitle(allBooks, bookTitle);

                        if (book != null && !book.isIssued()) {
                            if (currentStudent != null) {
                                // Check if student already has 3 or more books
                                if (currentStudent.getIssuedBooks().size() >= 3) {
                                    System.out.println(
                                            "You have reached the maximum limit of 3 books. Please return a book first.");
                                } else {
                                    System.out.println("Book '" + book.getTitle() + "' requested successfully.");
                                    System.out.println("Please visit the library desk to get your book issued.");
                                    library.issueBook(currentStudent, book);
                                }
                            } else if (currentFaculty != null) {
                                // Faculty can issue up to 5 books
                                if (currentFaculty.getIssuedBooks().size() >= 5) {
                                    System.out.println(
                                            "You have reached the maximum limit of 5 books. Please return a book first.");
                                } else {
                                    System.out.println("Book '" + book.getTitle() + "' requested successfully.");
                                    System.out.println("Please visit the library desk to get your book issued.");
                                    library.issueBook(currentFaculty, book);
                                }
                            }
                        } else if (book == null) {
                            System.out.println("Book not found: " + bookTitle);
                        } else {
                            System.out.println("This book is already issued.");
                        }
                        ConsoleUtil.pressEnterToContinue();
                        break;
                    case 4:
                        // Return book functionality for students and faculty
                        if (currentStudent != null) {
                            library.viewUserIssuedBooks(allBooks, currentStudent);
                            if (currentStudent.getIssuedBooks().size() > 0) {
                                System.out.print("\nEnter book title to return: ");
                                String returnBookTitle = scanner.nextLine();
                                Book returnBook = library.linearSearchByTitle(allBooks, returnBookTitle);
                                if (returnBook != null && returnBook.isIssued()) {
                                    // Check if this student has issued this book
                                    boolean hasIssuedBook = false;
                                    CustomArrayList<String> issuedBooks = currentStudent.getIssuedBooks();
                                    for (int i = 0; i < issuedBooks.size(); i++) {
                                        if (issuedBooks.get(i).equals(returnBook.getId())) {
                                            hasIssuedBook = true;
                                            break;
                                        }
                                    }

                                    if (hasIssuedBook) {
                                        library.returnBook(currentStudent, returnBook);
                                    } else {
                                        System.out.println("This book was not issued to you.");
                                    }
                                } else if (returnBook == null) {
                                    System.out.println("Book not found: " + returnBookTitle);
                                } else {
                                    System.out.println("This book is not currently issued.");
                                }
                            }
                        } else if (currentFaculty != null) {
                            library.viewUserIssuedBooks(allBooks, currentFaculty);
                            if (currentFaculty.getIssuedBooks().size() > 0) {
                                System.out.print("\nEnter book title to return: ");
                                String returnBookTitle = scanner.nextLine();
                                Book returnBook = library.linearSearchByTitle(allBooks, returnBookTitle);
                                if (returnBook != null && returnBook.isIssued()) {
                                    // Check if this faculty has issued this book
                                    boolean hasIssuedBook = false;
                                    CustomArrayList<String> issuedBooks = currentFaculty.getIssuedBooks();
                                    for (int i = 0; i < issuedBooks.size(); i++) {
                                        if (issuedBooks.get(i).equals(returnBook.getId())) {
                                            hasIssuedBook = true;
                                            break;
                                        }
                                    }

                                    if (hasIssuedBook) {
                                        library.returnBook(currentFaculty, returnBook);
                                    } else {
                                        System.out.println("This book was not issued to you.");
                                    }
                                } else if (returnBook == null) {
                                    System.out.println("Book not found: " + returnBookTitle);
                                } else {
                                    System.out.println("This book is not currently issued.");
                                }
                            }
                        }
                        ConsoleUtil.pressEnterToContinue();
                        break;
                    case 5:
                        searchBookByTitle();
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
    }

    private static void searchBookByTitle() {
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
    }

    private static void handleAdminLibraryManagement() {
        boolean back = false;
        while (!back) {
            ConsoleUtil.clearScreen();
            System.out.println("=========================");
            System.out.println("Library Management (Admin)");
            System.out.println("=========================");
            System.out.println("1. Add New Book");
            System.out.println("2. Update Book Details");
            System.out.println("3. Remove Book");
            System.out.println("4. View All Books");
            System.out.println("5. View All Issued Books");
            System.out.println("6. View Issue History");
            System.out.println("7. Search Books");
            System.out.println("8. Back");

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
                    FileHandler.saveBooks(allBooks);
                    System.out.println("Book added successfully: " + title + " by " + author);
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 2:
                    library.viewAllBooks(allBooks);
                    System.out.print("\nEnter book ID to update: ");
                    String bookId = scanner.nextLine();
                    Book bookToUpdate = null;

                    for (int i = 0; i < allBooks.size(); i++) {
                        Book b = allBooks.get(i);
                        if (b.getId().equals(bookId)) {
                            bookToUpdate = b;
                            break;
                        }
                    }

                    if (bookToUpdate != null) {
                        System.out.print("Enter new title (current: " + bookToUpdate.getTitle() + "): ");
                        String newTitle = scanner.nextLine();
                        if (!ValidationUtil.isNullOrEmpty(newTitle)) {
                            bookToUpdate.setTitle(newTitle);
                        }

                        System.out.print("Enter new author (current: " + bookToUpdate.getAuthor() + "): ");
                        String newAuthor = scanner.nextLine();
                        if (!ValidationUtil.isNullOrEmpty(newAuthor)) {
                            bookToUpdate.setAuthor(newAuthor);
                        }

                        // Save books to CSV after updating
                        FileHandler.saveBooks(allBooks);
                        System.out.println("Book updated successfully.");
                    } else {
                        System.out.println("Book not found with ID: " + bookId);
                    }
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 3:
                    library.viewAllBooks(allBooks);
                    System.out.print("\nEnter book ID to remove: ");
                    String removeId = scanner.nextLine();
                    Book bookToRemove = null;
                    int indexToRemove = -1;

                    for (int i = 0; i < allBooks.size(); i++) {
                        Book b = allBooks.get(i);
                        if (b.getId().equals(removeId)) {
                            bookToRemove = b;
                            indexToRemove = i;
                            break;
                        }
                    }

                    if (bookToRemove != null) {
                        if (bookToRemove.isIssued()) {
                            System.out.println("Cannot remove book as it is currently issued.");
                        } else {
                            System.out
                                    .print("Are you sure you want to remove '" + bookToRemove.getTitle() + "' (y/n)? ");
                            String confirm = scanner.nextLine().trim().toLowerCase();
                            if (confirm.equals("y") || confirm.equals("yes")) {
                                allBooks.remove(indexToRemove);
                                // Save books to CSV after removing
                                FileHandler.saveBooks(allBooks);
                                System.out.println("Book removed successfully.");
                            } else {
                                System.out.println("Removal cancelled.");
                            }
                        }
                    } else {
                        System.out.println("Book not found with ID: " + removeId);
                    }
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 4:
                    library.viewAllBooks(allBooks);
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 5:
                    // View all issued books with detailed info on who has them
                    library.viewDetailedIssuedBooks(allBooks, adminPanel);
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 6:
                    library.displayIssueQueue();
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 7:
                    System.out.print("Enter search term (title/author): ");
                    String searchTerm = scanner.nextLine();
                    library.searchBooks(allBooks, searchTerm);
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
                    CustomArrayList<Course> feesFilteredCourses = courseManagement.searchCoursesByFeesRange(minFees,
                            maxFees);
                    courseManagement.displayCourseSearchResults(feesFilteredCourses,
                            "Courses in fee range ₹" + minFees + " - ₹" + maxFees);
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