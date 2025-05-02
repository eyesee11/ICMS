package model;

public class Faculty {
    private String id;
    private String name;
    private String email;
    private ds.CustomArrayList<String> subjects;

    public Faculty(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.subjects = new ds.CustomArrayList<>();
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

    public ds.CustomArrayList<String> getSubjects() {
        return subjects;
    }

    public void addSubject(String subject) {
        subjects.add(subject);
    }
}