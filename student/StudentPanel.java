package student;

import model.Student;

public class StudentPanel {
    private Student student;

    public StudentPanel(Student student) {
        this.student = student;
    }

    public void viewProfile() {
        System.out.println("\nStudent Profile:");
        System.out.println("ID: " + student.getId());
        System.out.println("Name: " + student.getName());
        System.out.println("Email: " + student.getEmail());
        System.out.println("Stream: " + student.getStream());
    }

    public void updateProfile(String name, String email) {
        student.setName(name);
        student.setEmail(email);
        System.out.println("Profile updated successfully!");
    }

    public void viewCourses() {
        System.out.println("\nAssigned Courses:");
        ds.CustomArrayList<String> courses = student.getCourses();
        if (courses.size() == 0) {
            System.out.println("No courses assigned yet.");
            return;
        }
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i));
        }
    }
    
    public void viewAttendance() {
        System.out.println("\nAttendance Record:");
        ds.CustomHashMap<String, Boolean> attendance = student.getAttendance();
        if (attendance.size() == 0) {
            System.out.println("No attendance records available.");
            return;
        }
        
        // Display attendance (Note: In a real implementation, we would iterate through the HashMap)
        // This is a simplified version since we can't iterate through our custom HashMap easily
        System.out.println("Attendance records are available in the system.");
    }
    
    public void viewTimetable() {
        System.out.println("\nClass Timetable:");
        System.out.println("Monday:");
        System.out.println("  09:00 - 10:30: Mathematics (Room 101)");
        System.out.println("  11:00 - 12:30: Physics (Room 202)");
        System.out.println("Tuesday:");
        System.out.println("  09:00 - 10:30: Computer Science (Lab 301)");
        System.out.println("  11:00 - 12:30: English (Room 102)");
        System.out.println("Wednesday:");
        System.out.println("  09:00 - 10:30: Chemistry (Lab 305)");
        System.out.println("  11:00 - 12:30: Mathematics (Room 101)");
    }
    
    public void viewIssuedBooks() {
        System.out.println("\nIssued Books:");
        ds.CustomArrayList<String> books = student.getIssuedBooks();
        if (books.size() == 0) {
            System.out.println("No books issued currently.");
            return;
        }
        for (int i = 0; i < books.size(); i++) {
            System.out.println((i + 1) + ". Book ID: " + books.get(i));
        }
    }
}