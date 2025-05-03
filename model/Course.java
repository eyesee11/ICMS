package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Course implements Comparable<Course> {
    private String id;
    private String name;
    private int duration;
    private double fees;
    private String scope;
    private String stream;
    private int semester;
    private String assignedFaculty;
    private int availableSeats;
    private String lastUpdated;

    /**
     * Basic constructor for Course
     * @param id Course unique identifier
     * @param name Course name
     * @param duration Duration in months
     * @param fees Course fees
     * @param scope Course scope
     * @param stream Department/stream
     */
    public Course(String id, String name, int duration, double fees, String scope, String stream) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.fees = fees;
        this.scope = scope;
        this.stream = stream;
        this.semester = 1; // Default semester
        this.assignedFaculty = "Not Assigned";
        this.availableSeats = 60; // Default seats
        this.lastUpdated = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
    
    /**
     * Complete constructor for Course
     * @param id Course unique identifier
     * @param name Course name
     * @param duration Duration in months
     * @param fees Course fees
     * @param scope Course scope
     * @param stream Department/stream
     * @param semester Semester number
     * @param assignedFaculty Faculty assigned to teach
     * @param availableSeats Available seats
     */
    public Course(String id, String name, int duration, double fees, String scope, String stream,
                 int semester, String assignedFaculty, int availableSeats) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.fees = fees;
        this.scope = scope;
        this.stream = stream;
        this.semester = semester;
        this.assignedFaculty = assignedFaculty;
        this.availableSeats = availableSeats;
        this.lastUpdated = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Get course ID
     * @return Course ID
     */
    public String getId() {
        return id;
    }

    /**
     * Get course name
     * @return Course name
     */
    public String getName() {
        return name;
    }

    /**
     * Get course duration
     * @return Duration in months
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Get course fees
     * @return Fees amount
     */
    public double getFees() {
        return fees;
    }

    /**
     * Get course scope
     * @return Course scope
     */
    public String getScope() {
        return scope;
    }
    
    /**
     * Get course stream/department
     * @return Stream or department
     */
    public String getStream() {
        return stream;
    }
    
    /**
     * Get course semester
     * @return Semester number
     */
    public int getSemester() {
        return semester;
    }
    
    /**
     * Set course semester
     * @param semester New semester number
     */
    public void setSemester(int semester) {
        this.semester = semester;
    }
    
    /**
     * Get assigned faculty
     * @return Name of assigned faculty
     */
    public String getAssignedFaculty() {
        return assignedFaculty;
    }
    
    /**
     * Assign faculty to course
     * @param assignedFaculty Faculty name
     */
    public void setAssignedFaculty(String assignedFaculty) {
        this.assignedFaculty = assignedFaculty;
        this.lastUpdated = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
    
    /**
     * Get available seats
     * @return Number of available seats
     */
    public int getAvailableSeats() {
        return availableSeats;
    }
    
    /**
     * Set available seats
     * @param availableSeats New number of available seats
     */
    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
    
    /**
     * Get last updated date
     * @return Last updated date string
     */
    public String getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public int compareTo(Course other) {
        return this.name.compareTo(other.name);
    }
}