package model;

public class Student {
    private String id;
    private String name;
    private String email;
    private String stream;
    private ds.CustomArrayList<String> courses;
    private ds.CustomArrayList<String> issuedBooks;
    private ds.CustomHashMap<String, Boolean> attendance;

    public Student(String id, String name, String email, String stream) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.stream = stream;
        this.courses = new ds.CustomArrayList<>();
        this.issuedBooks = new ds.CustomArrayList<>();
        this.attendance = new ds.CustomHashMap<>();
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
    
    public ds.CustomArrayList<String> getIssuedBooks() {
        return issuedBooks;
    }
    
    public void issueBook(String bookId) {
        issuedBooks.add(bookId);
    }
    
    public void returnBook(String bookId) {
        for (int i = 0; i < issuedBooks.size(); i++) {
            if (issuedBooks.get(i).equals(bookId)) {
                issuedBooks.remove(i);
                break;
            }
        }
    }
    
    public ds.CustomHashMap<String, Boolean> getAttendance() {
        return attendance;
    }
    
    public void markAttendance(String date, boolean present) {
        attendance.put(date, present);
    }
}