package data;

import course.CourseManagement;
import model.Course;

/**
 * Utility class to store and load sample course data
 */
public class CourseData {
    
    /**
     * Load sample courses into the course management system
     * @param courseManagement The CourseManagement instance to load data into
     */
    public static void loadSampleCourses(CourseManagement courseManagement) {
        // Computer Science courses
        courseManagement.addCourse(new Course("C001", "Database Management Systems", 6, 12000.0, 
            "Databases, SQL, DBMS design, database administration", "Computer Science", 3, 
            "Dr. Robert Brown", 40));
            
        courseManagement.addCourse(new Course("C002", "Data Structures & Algorithms", 4, 10000.0, 
            "Programming, algorithms, complexity analysis, problem-solving", "Computer Science", 2, 
            "Dr. Emily Davis", 45));
            
        courseManagement.addCourse(new Course("C003", "Computer Networks", 5, 11500.0, 
            "Networking protocols, security, distributed systems", "Computer Science", 4, 
            "Prof. James Wilson", 35));
            
        courseManagement.addCourse(new Course("C004", "Operating Systems", 3, 9000.0, 
            "OS design, process management, memory management", "Computer Science", 3, 
            "Dr. Lisa Moore", 50));
            
        courseManagement.addCourse(new Course("C005", "Web Development", 4, 8500.0, 
            "HTML, CSS, JavaScript, frontend & backend technologies", "Computer Science", 2, 
            "Prof. Michael Lee", 60));
            
        // Electronics courses
        courseManagement.addCourse(new Course("C006", "Digital Electronics", 5, 11000.0, 
            "Digital circuits, logic design, microprocessors", "Electronics", 2, 
            "Dr. Emily Davis", 40));
            
        courseManagement.addCourse(new Course("C007", "VLSI Design", 6, 13000.0, 
            "IC design, semiconductor physics, chip fabrication", "Electronics", 5, 
            "Prof. Alan Thompson", 30));
            
        courseManagement.addCourse(new Course("C008", "Microelectronics", 5, 12500.0, 
            "Electronic devices, circuit theory, analog design", "Electronics", 4, 
            "Dr. Sarah Johnson", 35));
            
        // Civil Engineering courses
        courseManagement.addCourse(new Course("C009", "Structural Engineering", 6, 11000.0, 
            "Building design, structural analysis, construction", "Civil Engineering", 3, 
            "Dr. Robert Peterson", 40));
            
        courseManagement.addCourse(new Course("C010", "Environmental Engineering", 5, 10500.0, 
            "Water treatment, waste management, sustainability", "Civil Engineering", 4, 
            "Dr. Patricia Green", 45));
            
        // Mechanical Engineering courses
        courseManagement.addCourse(new Course("C011", "Thermodynamics", 4, 9500.0, 
            "Heat transfer, energy conversion, power systems", "Mechanical Engineering", 3, 
            "Prof. Thomas Anderson", 50));
            
        courseManagement.addCourse(new Course("C012", "Machine Design", 5, 10200.0, 
            "CAD, manufacturing processes, machine elements", "Mechanical Engineering", 4, 
            "Dr. Richard Wright", 40));
    }
}