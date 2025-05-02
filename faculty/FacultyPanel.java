package faculty;

import model.Faculty;
import model.Student;
import ds.CustomArrayList;

public class FacultyPanel {
    private Faculty faculty;

    public FacultyPanel(Faculty faculty) {
        this.faculty = faculty;
    }

    public void viewProfile() {
        System.out.println("\nFaculty Profile:");
        System.out.println("ID: " + faculty.getId());
        System.out.println("Name: " + faculty.getName());
        System.out.println("Email: " + faculty.getEmail());
    }
    
    public void updateProfile(String name, String email) {
        faculty.setName(name);
        faculty.setEmail(email);
        System.out.println("Profile updated successfully!");
    }
    
    public void viewSubjects() {
        System.out.println("\nAssigned Subjects:");
        ds.CustomArrayList<String> subjects = faculty.getSubjects();
        if (subjects.size() == 0) {
            System.out.println("No subjects assigned yet.");
            return;
        }
        for (int i = 0; i < subjects.size(); i++) {
            System.out.println((i + 1) + ". " + subjects.get(i));
        }
    }
    
    public void viewAssignedClasses() {
        System.out.println("\nAssigned Classes:");
        ds.CustomArrayList<String> classes = faculty.getAssignedClasses();
        if (classes.size() == 0) {
            System.out.println("No classes assigned yet.");
            return;
        }
        for (int i = 0; i < classes.size(); i++) {
            System.out.println((i + 1) + ". " + classes.get(i));
        }
    }
    
    public void viewAttendance() {
        System.out.println("\nYour Attendance Record:");
        ds.CustomHashMap<String, Boolean> attendance = faculty.getAttendance();
        if (attendance.size() == 0) {
            System.out.println("No attendance records available.");
            return;
        }
        
        // Display attendance (simplified for demo)
        System.out.println("Attendance records are available in the system.");
    }
    
    public void markStudentAttendance(Student student, String date, boolean present) {
        student.markAttendance(date, present);
        System.out.println("Attendance marked for " + student.getName() + " on " + date + ": " + (present ? "Present" : "Absent"));
    }
    
    public void viewTimetable() {
        System.out.println("\nYour Teaching Timetable:");
        ds.CustomHashMap<String, String> timetable = faculty.getTimetable();
        if (timetable.size() == 0) {
            System.out.println("No timetable entries available.");
            return;
        }
        
        // Display hardcoded timetable (for demo)
        System.out.println("Monday:");
        System.out.println("  09:00 - 10:30: Database Systems (Room 101)");
        System.out.println("  11:00 - 12:30: Data Structures (Lab 202)");
        System.out.println("Tuesday:");
        System.out.println("  09:00 - 10:30: Algorithm Design (Lab 301)");
        System.out.println("  11:00 - 12:30: Software Engineering (Room 102)");
    }
    
    public void viewIssuedBooks() {
        System.out.println("\nIssued Books:");
        ds.CustomArrayList<String> books = faculty.getIssuedBooks();
        if (books.size() == 0) {
            System.out.println("No books issued currently.");
            return;
        }
        for (int i = 0; i < books.size(); i++) {
            System.out.println((i + 1) + ". Book ID: " + books.get(i));
        }
    }
}
