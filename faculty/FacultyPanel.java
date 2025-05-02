package faculty;

import model.Faculty;

public class FacultyPanel {
    private Faculty faculty;

    public FacultyPanel(Faculty faculty) {
        this.faculty = faculty;
    }

    public void viewProfile() {
        System.out.println("ID: " + faculty.getId());
        System.out.println("Name: " + faculty.getName());
        System.out.println("Email: " + faculty.getEmail());
        System.out.println("Subjects: " + faculty.getSubjects());
    }
}
