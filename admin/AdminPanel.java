package admin;

import model.Student;
import model.Faculty;
import ds.CustomArrayList;

public class AdminPanel {
    private CustomArrayList<Student> students;
    private CustomArrayList<Faculty> faculty;

    public AdminPanel() {
        students = new CustomArrayList<>();
        faculty = new CustomArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void updateStudent(String id, String name, String email, String stream) {
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            if (student.getId().equals(id)) {
                student.setName(name);
                student.setEmail(email);
                student.setStream(stream);
                break;
            }
        }
    }

    public void deleteStudent(String id) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(id)) {
                students.remove(i);
                break;
            }
        }
    }

    public void addFaculty(Faculty facultyMember) {
        faculty.add(facultyMember);
    }

    public CustomArrayList<Student> getStudents() {
        return students;
    }

    public CustomArrayList<Faculty> getFaculty() {
        return faculty;
    }
}