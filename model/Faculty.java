package model;

public class Faculty {
    private String id;
    private String name;
    private String email;
    private ds.CustomArrayList<String> subjects;
    private ds.CustomArrayList<String> assignedClasses;
    private ds.CustomHashMap<String, Boolean> attendance;
    private ds.CustomHashMap<String, String> timetable;
    private ds.CustomArrayList<String> issuedBooks;

    /**
     * Constructor for Faculty
     * @param id Faculty unique identifier
     * @param name Faculty name
     * @param email Faculty email
     */
    public Faculty(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.subjects = new ds.CustomArrayList<>();
        this.assignedClasses = new ds.CustomArrayList<>();
        this.attendance = new ds.CustomHashMap<>();
        this.timetable = new ds.CustomHashMap<>();
        this.issuedBooks = new ds.CustomArrayList<>();
    }

    /**
     * Get faculty ID
     * @return Faculty ID
     */
    public String getId() {
        return id;
    }

    /**
     * Get faculty name
     * @return Faculty name
     */
    public String getName() {
        return name;
    }

    /**
     * Set faculty name
     * @param name New faculty name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get faculty email
     * @return Faculty email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set faculty email
     * @param email New faculty email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get subjects taught by faculty
     * @return List of subjects
     */
    public ds.CustomArrayList<String> getSubjects() {
        return subjects;
    }

    /**
     * Add subject to faculty's teaching assignments
     * @param subject Subject to add
     */
    public void addSubject(String subject) {
        subjects.add(subject);
    }

    /**
     * Get classes assigned to faculty
     * @return List of assigned classes
     */
    public ds.CustomArrayList<String> getAssignedClasses() {
        return assignedClasses;
    }
    
    /**
     * Assign class to faculty
     * @param className Class to assign
     */
    public void assignClass(String className) {
        assignedClasses.add(className);
    }
    
    /**
     * Get faculty's attendance record
     * @return Map of date to attendance status
     */
    public ds.CustomHashMap<String, Boolean> getAttendance() {
        return attendance;
    }
    
    /**
     * Mark faculty's attendance for a date
     * @param date Date in string format
     * @param present Whether faculty was present
     */
    public void markAttendance(String date, boolean present) {
        attendance.put(date, present);
    }
    
    /**
     * Get faculty's timetable
     * @return Map of time slots to class details
     */
    public ds.CustomHashMap<String, String> getTimetable() {
        return timetable;
    }
    
    /**
     * Set entry in faculty's timetable
     * @param timeSlot Time slot (e.g., "Monday 9-10")
     * @param classDetails Details of the class
     */
    public void setTimetableEntry(String timeSlot, String classDetails) {
        timetable.put(timeSlot, classDetails);
    }
    
    /**
     * Get books issued to faculty
     * @return List of issued book IDs
     */
    public ds.CustomArrayList<String> getIssuedBooks() {
        return issuedBooks;
    }
    
    /**
     * Issue a book to faculty
     * @param bookId ID of book to issue
     */
    public void issueBook(String bookId) {
        issuedBooks.add(bookId);
    }
    
    /**
     * Return a book from faculty
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
}