package model;

public class Student {
    private String id;
    private String name;
    private String email;
    private String stream;
    private ds.CustomArrayList<String> courses;

    public Student(String id, String name, String email, String stream) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.stream = stream;
        this.courses = new ds.CustomArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public ds.CustomArrayList<String> getCourses() {
        return courses;
    }

    public void addCourse(String course) {
        courses.add(course);
    }
}