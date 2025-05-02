package model;

public class Course implements Comparable<Course> {
    private String id;
    private String name;
    private int duration;
    private double fees;
    private String scope;
    private String stream;  // Stream/department field

    public Course(String id, String name, int duration, double fees, String scope, String stream) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.fees = fees;
        this.scope = scope;
        this.stream = stream;
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

    @Override
    public int compareTo(Course other) {
        return this.name.compareTo(other.name);
    }
}