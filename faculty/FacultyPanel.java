package faculty;

import model.Faculty;
import model.Student;
import ds.CustomArrayList;
import util.TableFormatter;

public class FacultyPanel {
    private Faculty faculty;

    public FacultyPanel(Faculty faculty) {
        this.faculty = faculty;
    }

    public void viewProfile() {
        System.out.println("\nFaculty Profile:");
        TableFormatter table = new TableFormatter("Field", "Value");
        table.addRow("ID", faculty.getId());
        table.addRow("Name", faculty.getName());
        table.addRow("Email", faculty.getEmail());
        System.out.println(table.toString());
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
        
        TableFormatter table = new TableFormatter("No.", "Subject");
        for (int i = 0; i < subjects.size(); i++) {
            table.addRow(String.valueOf(i + 1), subjects.get(i));
        }
        System.out.println(table.toString());
    }
    
    public void viewAssignedClasses() {
        System.out.println("\nAssigned Classes:");
        ds.CustomArrayList<String> classes = faculty.getAssignedClasses();
        if (classes.size() == 0) {
            System.out.println("No classes assigned yet.");
            return;
        }
        
        TableFormatter table = new TableFormatter("No.", "Class");
        for (int i = 0; i < classes.size(); i++) {
            table.addRow(String.valueOf(i + 1), classes.get(i));
        }
        System.out.println(table.toString());
    }
    
    public void viewAttendance() {
        System.out.println("\nYour Attendance Record:");
        ds.CustomHashMap<String, Boolean> attendance = faculty.getAttendance();
        if (attendance.size() == 0) {
            System.out.println("No attendance records available.");
            return;
        }
        
        // Display attendance in table format (simplified for demo)
        TableFormatter table = new TableFormatter("Date", "Status");
        
        // Sample entries for demonstration
        table.addRow("2025-05-01", TableFormatter.formatAttendance(true));
        table.addRow("2025-05-02", TableFormatter.formatAttendance(true));
        
        System.out.println(table.toString());
        System.out.println("For complete attendance records, please check the system database.");
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
        
        // Display hardcoded timetable in table format (for demo)
        TableFormatter table = new TableFormatter("Day", "Time", "Subject", "Location");
        table.addRow("Monday", "09:00 - 10:30", "Database Systems", "Room 101");
        table.addRow("Monday", "11:00 - 12:30", "Data Structures", "Lab 202");
        table.addRow("Tuesday", "09:00 - 10:30", "Algorithm Design", "Lab 301");
        table.addRow("Tuesday", "11:00 - 12:30", "Software Engineering", "Room 102");
        
        System.out.println(table.toString());
    }
    
    public void viewIssuedBooks() {
        System.out.println("\nIssued Books:");
        ds.CustomArrayList<String> books = faculty.getIssuedBooks();
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
    
    // Method to view class roster (students in a class) in table format
    public void viewClassRoster(String className, CustomArrayList<Student> students) {
        System.out.println("\nStudents in " + className + ":");
        
        TableFormatter table = new TableFormatter("No.", "ID", "Name", "Email", "Stream");
        int count = 0;
        
        // Find all students in this class (simplified logic - in a real system this would be more complex)
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            // For demo purposes, we'll just display all students
            count++;
            table.addRow(
                String.valueOf(count),
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getStream()
            );
        }
        
        if (count == 0) {
            System.out.println("No students found in this class.");
        } else {
            System.out.println(table.toString());
        }
        System.out.println("ID: " + faculty.getId());
        System.out.println("Name: " + faculty.getName());
        System.out.println("Email: " + faculty.getEmail());
        System.out.println("Subjects: " + faculty.getSubjects());
    }
}
