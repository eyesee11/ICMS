package admin;

import model.Student;
import model.Faculty;
import model.Course;
import ds.CustomArrayList;

public class AdminPanel {
    private CustomArrayList<Student> students;
    private CustomArrayList<Faculty> faculty;

    public AdminPanel() {
        students = new CustomArrayList<>();
        faculty = new CustomArrayList<>();
    }

    // Student Management Methods
    public void addStudent(Student student) {
        students.add(student);
        System.out.println("Student added successfully: " + student.getName());
    }

    public void updateStudent(String id, String name, String email, String stream) {
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            if (student.getId().equals(id)) {
                student.setName(name);
                student.setEmail(email);
                student.setStream(stream);
                System.out.println("Student updated successfully: " + student.getName());
                return;
            }
        }
        System.out.println("Student with ID " + id + " not found.");
    }

    public void deleteStudent(String id) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(id)) {
                System.out.println("Student deleted: " + students.get(i).getName());
                students.remove(i);
                return;
            }
        }
        System.out.println("Student with ID " + id + " not found.");
    }

    public void viewAllStudents() {
        System.out.println("\nAll Students:");
        if (students.size() == 0) {
            System.out.println("No students registered.");
            return;
        }
        
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            System.out.println("\nStudent #" + (i+1) + ":");
            System.out.println("ID: " + student.getId());
            System.out.println("Name: " + student.getName());
            System.out.println("Email: " + student.getEmail());
            System.out.println("Stream: " + student.getStream());
        }
    }

    public Student searchStudent(String id) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(id)) {
                return students.get(i);
            }
        }
        return null;
    }

    public void assignCourseToStudent(String studentId, String course) {
        Student student = searchStudent(studentId);
        if (student != null) {
            student.addCourse(course);
            System.out.println("Course '" + course + "' assigned to " + student.getName());
        } else {
            System.out.println("Student with ID " + studentId + " not found.");
        }
    }

    // Faculty Management Methods
    public void addFaculty(Faculty facultyMember) {
        faculty.add(facultyMember);
        System.out.println("Faculty added successfully: " + facultyMember.getName());
    }

    public void updateFaculty(String id, String name, String email) {
        for (int i = 0; i < faculty.size(); i++) {
            Faculty facultyMember = faculty.get(i);
            if (facultyMember.getId().equals(id)) {
                facultyMember.setName(name);
                facultyMember.setEmail(email);
                System.out.println("Faculty updated successfully: " + facultyMember.getName());
                return;
            }
        }
        System.out.println("Faculty with ID " + id + " not found.");
    }

    public void deleteFaculty(String id) {
        for (int i = 0; i < faculty.size(); i++) {
            if (faculty.get(i).getId().equals(id)) {
                System.out.println("Faculty deleted: " + faculty.get(i).getName());
                faculty.remove(i);
                return;
            }
        }
        System.out.println("Faculty with ID " + id + " not found.");
    }

    public void viewAllFaculty() {
        System.out.println("\nAll Faculty Members:");
        if (faculty.size() == 0) {
            System.out.println("No faculty members registered.");
            return;
        }
        
        for (int i = 0; i < faculty.size(); i++) {
            Faculty facultyMember = faculty.get(i);
            System.out.println("\nFaculty #" + (i+1) + ":");
            System.out.println("ID: " + facultyMember.getId());
            System.out.println("Name: " + facultyMember.getName());
            System.out.println("Email: " + facultyMember.getEmail());
        }
    }

    public Faculty searchFaculty(String id) {
        for (int i = 0; i < faculty.size(); i++) {
            if (faculty.get(i).getId().equals(id)) {
                return faculty.get(i);
            }
        }
        return null;
    }

    public void assignSubjectToFaculty(String facultyId, String subject) {
        Faculty facultyMember = searchFaculty(facultyId);
        if (facultyMember != null) {
            facultyMember.addSubject(subject);
            System.out.println("Subject '" + subject + "' assigned to " + facultyMember.getName());
        } else {
            System.out.println("Faculty with ID " + facultyId + " not found.");
        }
    }

    public void assignClassToFaculty(String facultyId, String className) {
        Faculty facultyMember = searchFaculty(facultyId);
        if (facultyMember != null) {
            facultyMember.assignClass(className);
            System.out.println("Class '" + className + "' assigned to " + facultyMember.getName());
        } else {
            System.out.println("Faculty with ID " + facultyId + " not found.");
        }
    }

    public CustomArrayList<Student> getStudents() {
        return students;
    }

    public CustomArrayList<Faculty> getFaculty() {
        return faculty;
    }
}