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
    
    public ds.CustomArrayList<String> getAssignedClasses() {
        return assignedClasses;
    }
    
    public void assignClass(String className) {
        assignedClasses.add(className);
    }
    
    public ds.CustomHashMap<String, Boolean> getAttendance() {
        return attendance;
    }
    
    public void markAttendance(String date, boolean present) {
        attendance.put(date, present);
    }
    
    public ds.CustomHashMap<String, String> getTimetable() {
        return timetable;
    }
    
    public void setTimetableEntry(String timeSlot, String classDetails) {
        timetable.put(timeSlot, classDetails);
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
}