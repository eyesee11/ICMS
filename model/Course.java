package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Course implements Comparable<Course> {
    private String id;
    private String name;
    private int duration;
    private double fees;
    private String scope;
    private String stream;  // Stream/department field
    private int semester;
    private String assignedFaculty;
    private int availableSeats;
    private String lastUpdated;

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
    
    // Constructor with all fields
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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public double getFees() {
        return fees;
    }

    public String getScope() {
        return scope;
    }
    
    public String getStream() {
        return stream;
    }
    
    public int getSemester() {
        return semester;
    }
    
    public void setSemester(int semester) {
        this.semester = semester;
    }
    
    public String getAssignedFaculty() {
        return assignedFaculty;
    }
    
    public void setAssignedFaculty(String assignedFaculty) {
        this.assignedFaculty = assignedFaculty;
        this.lastUpdated = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
    
    public int getAvailableSeats() {
        return availableSeats;
    }
    
    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
    
    public String getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public int compareTo(Course other) {
        return this.name.compareTo(other.name);
    }
}