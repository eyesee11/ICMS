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
import java.util.Scanner;

public class ICMS {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthSystem auth = new AuthSystem();
        AdminPanel adminPanel = new AdminPanel();
        LibrarySystem library = new LibrarySystem();
        CourseManagement courseManagement = new CourseManagement();

        while (true) {
            System.out.println("\nIntegrated College Management System");
            System.out.println("1. Admin");
            System.out.println("2. Student");
            System.out.println("3. Faculty");
            System.out.println("4. Library");
            System.out.println("5. Courses");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 6) {
                System.out.println("Exiting...");
                break;
            }

            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            String role = auth.login(username, password);
            if (role == null) {
                System.out.println("Invalid credentials!");
                continue;
            }

            switch (choice) {
                case 1:
                    if (role.equals("ADMIN")) {
                        handleAdminPanel(scanner, adminPanel);
                    }
                    break;
                case 2:
                    if (role.equals("STUDENT")) {
                        Student student = new Student(username, "Student Name", "student@example.com", "CS");
                        StudentPanel studentPanel = new StudentPanel(student);
                        handleStudentPanel(scanner, studentPanel);
                    }
                    break;
                case 3:
                    if (role.equals("FACULTY")) {
                        Faculty faculty = new Faculty(username, "Faculty Name", "faculty@example.com");
                        FacultyPanel facultyPanel = new FacultyPanel(faculty);
                        handleFacultyPanel(scanner, facultyPanel);
                    }
                    break;
                case 4:
                    if (role.equals("LIBRARY")) {
                        handleLibraryPanel(scanner, library);
                    }
                    break;
                case 5:
                    handleCoursePanel(scanner, courseManagement);
                    break;
            }
        }
        scanner.close();
    }

    private static void handleAdminPanel(Scanner scanner, AdminPanel adminPanel) {
        System.out.println("Admin Panel");
        System.out.print("Enter student ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter student email: ");
        String email = scanner.nextLine();
        System.out.print("Enter stream: ");
        String stream = scanner.nextLine();
        adminPanel.addStudent(new Student(id, name, email, stream));
        System.out.println("Student added!");
    }

    private static void handleStudentPanel(Scanner scanner, StudentPanel studentPanel) {
        System.out.println("Student Panel");
        studentPanel.viewProfile();
    }

    private static void handleFacultyPanel(Scanner scanner, FacultyPanel facultyPanel) {
        System.out.println("Faculty Panel");
        facultyPanel.viewProfile();
    }

    private static void handleLibraryPanel(Scanner scanner, LibrarySystem library) {
        System.out.println("Library Panel");
        System.out.print("Enter book ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        library.addBook(new Book(id, title, author));
        System.out.println("Book added!");
    }

    private static void handleCoursePanel(Scanner scanner, CourseManagement courseManagement) {
        System.out.println("Course Panel");
        System.out.print("Enter course ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter course name: ");
        String name = scanner.nextLine();
        System.out.print("Enter duration: ");
        int duration = scanner.nextInt();
        System.out.print("Enter fees: ");
        double fees = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter scope: ");
        String scope = scanner.nextLine();
        courseManagement.addCourse(new Course(id, name, duration, fees, scope));
        System.out.println("Course added!");
    }
}