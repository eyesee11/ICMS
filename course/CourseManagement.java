package course;

import model.Course;
import ds.CustomArrayList;

public class CourseManagement {
    private CustomArrayList<Course> courses;

    public CourseManagement() {
        courses = new CustomArrayList<>();
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void viewCourses() {
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            System.out.println("ID: " + course.getId());
            System.out.println("Name: " + course.getName());
            System.out.println("Duration: " + course.getDuration());
            System.out.println("Fees: " + course.getFees());
            System.out.println("Scope: " + course.getScope());
        }
    }

    public Course searchCourse(String name) {
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (course.getName().equalsIgnoreCase(name)) {
                return course;
            }
        }
        return null;
    }
}