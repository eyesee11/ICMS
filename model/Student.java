package model;

public class Student {
    private String id;
    private String name;
    private String email;
    private String stream;
    private ds.CustomArrayList<String> courses;
    private ds.CustomArrayList<String> issuedBooks;
    private ds.CustomHashMap<String, Boolean> attendance;

    /**
     * Constructor for Student
     * @param id Student unique identifier
     * @param name Student name
     * @param email Student email
     * @param stream Student's department/stream
     */
    public Student(String id, String name, String email, String stream) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.stream = stream;
        this.courses = new ds.CustomArrayList<>();
        this.issuedBooks = new ds.CustomArrayList<>();
        this.attendance = new ds.CustomHashMap<>();
    }

    /**
     * Get student ID
     * @return Student ID
     */
    public String getId() {
        return id;
    }

    /**
     * Get student name
     * @return Student name
     */
    public String getName() {
        return name;
    }

    /**
     * Set student name
     * @param name New student name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get student email
     * @return Student email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set student email
     * @param email New student email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get student stream/department
     * @return Stream or department
     */
    public String getStream() {
        return stream;
    }

    /**
     * Set student stream/department
     * @param stream New stream or department
     */
    public void setStream(String stream) {
        this.stream = stream;
    }

    /**
     * Get courses enrolled by student
     * @return List of courses
     */
    public ds.CustomArrayList<String> getCourses() {
        return courses;
    }

    /**
     * Add course to student's enrollment
     * @param course Course to add
     */
    public void addCourse(String course) {
        courses.add(course);
    }
    
    /**
     * Get books issued to student
     * @return List of issued book IDs
     */
    public ds.CustomArrayList<String> getIssuedBooks() {
        return issuedBooks;
    }
    
    /**
     * Issue a book to student
     * @param bookId ID of book to issue
     */
    public void issueBook(String bookId) {
        issuedBooks.add(bookId);
    }
    
    /**
     * Return a book from student
     * @param bookId ID of book to return
     */
    public void returnBook(String bookId) {
        for (int i = 0; i < issuedBooks.size(); i++) {
            if (issuedBooks.get(i).equals(bookId)) {
                issuedBooks.remove(i);
                break;
            }
        }
    }
    
    /**
     * Get student's attendance record
     * @return Map of date to attendance status
     */
    public ds.CustomHashMap<String, Boolean> getAttendance() {
        return attendance;
    }
    
    /**
     * Mark student's attendance for a date
     * @param date Date in string format
     * @param present Whether student was present
     */
    public void markAttendance(String date, boolean present) {
        attendance.put(date, present);
    }
}