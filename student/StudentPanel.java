package student;

import model.Student;
import util.TableFormatter;

public class StudentPanel {
    private Student student;

    public StudentPanel(Student student) {
        this.student = student;
    }

    public void viewProfile() {
        System.out.println("\nStudent Profile:");
        TableFormatter table = new TableFormatter("Field", "Value");
        table.addRow("ID", student.getId());
        table.addRow("Name", student.getName());
        table.addRow("Email", student.getEmail());
        table.addRow("Stream", student.getStream());
        System.out.println(table.toString());
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
        
        TableFormatter table = new TableFormatter("No.", "Course");
        for (int i = 0; i < courses.size(); i++) {
            table.addRow(String.valueOf(i + 1), courses.get(i));
        }
        System.out.println(table.toString());
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
        TableFormatter table = new TableFormatter("Day", "Time", "Subject", "Location");
        table.addRow("Monday", "09:00 - 10:30", "Mathematics", "Room 101");
        table.addRow("Monday", "11:00 - 12:30", "Physics", "Room 202");
        table.addRow("Tuesday", "09:00 - 10:30", "Computer Science", "Lab 301");
        table.addRow("Tuesday", "11:00 - 12:30", "English", "Room 102");
        table.addRow("Wednesday", "09:00 - 10:30", "Chemistry", "Lab 305");
        table.addRow("Wednesday", "11:00 - 12:30", "Mathematics", "Room 101");
        System.out.println(table.toString());
    }
    
    public void viewIssuedBooks() {
        System.out.println("\nIssued Books:");
        ds.CustomArrayList<String> books = student.getIssuedBooks();
        if (books.size() == 0) {
            System.out.println("No books issued currently.");
            return;
        }
        
        TableFormatter table = new TableFormatter("No.", "Book ID");
        for (int i = 0; i < books.size(); i++) {
            table.addRow(String.valueOf(i + 1), books.get(i));
        }
        System.out.println(table.toString());
    }
}