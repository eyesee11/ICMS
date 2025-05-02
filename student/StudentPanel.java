package student;

import model.Student;

public class StudentPanel {
    private Student student;

    public StudentPanel(Student student) {
        this.student = student;
    }

    public void viewProfile() {
        System.out.println("ID: " + student.getId());
        System.out.println("Name: " + student.getName());
        System.out.println("Email: " + student.getEmail());
        System.out.println("Stream: " + student.getStream());
    }

    public void updateProfile(String name, String email) {
        student.setName(name);
        student.setEmail(email);
    }

    public void viewCourses() {
        ds.CustomArrayList<String> courses = student.getCourses();
        for (int i = 0; i < courses.size(); i++) {
            System.out.println(courses.get(i));
        }
    }
}