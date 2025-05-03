package admin;

import model.Student;
import model.Faculty;
import model.Course;
import ds.CustomArrayList;
import util.TableFormatter;
import util.FileHandler;
import util.ValidationUtil;
import util.ConsoleUtil;
import auth.AuthSystem;
import course.CourseManagement;

public class AdminPanel {
    private CustomArrayList<Student> students;
    private CustomArrayList<Faculty> faculty;
    private AuthSystem authSystem;
    private CourseManagement courseManagement;
    
    private static final String STUDENTS_FILE = "students.dat";
    private static final String FACULTY_FILE = "faculty.dat";
    private static final String COURSES_FILE = "courses.dat";

    public AdminPanel(AuthSystem authSystem) {
        this.authSystem = authSystem;
        students = new CustomArrayList<>();
        faculty = new CustomArrayList<>();
        loadData();
    }
    
    /**
     * Set the CourseManagement instance for the admin panel
     * @param courseManagement CourseManagement instance to use
     */
    public void setCourseManagement(CourseManagement courseManagement) {
        this.courseManagement = courseManagement;
    }

    // Student Management Methods
    public void addStudent(Student student) {
        // Check for duplicate ID
        if (searchStudent(student.getId()) != null) {
            System.out.println("Error: Student with ID " + student.getId() + " already exists!");
            return;
        }
        
        students.add(student);
        saveStudents();
        
        // Create login credentials for the student
        authSystem.createStudentCredentials(student);
        
        System.out.println("Student added successfully: " + student.getName());
    }

    public void updateStudent(String id, String name, String email, String stream) {
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            if (student.getId().equals(id)) {
                student.setName(name);
                student.setEmail(email);
                student.setStream(stream);
                saveStudents();
                System.out.println("Student updated successfully: " + student.getName());
                return;
            }
        }
        System.out.println("Student with ID " + id + " not found.");
    }

    public void deleteStudent(String id) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(id)) {
                System.out.println("Student deleted: " + students.get(i).getName());
                
                // Remove credentials
                authSystem.removeCredentials(id);
                
                students.remove(i);
                saveStudents();
                return;
            }
        }
        System.out.println("Student with ID " + id + " not found.");
    }

    public void viewAllStudents() {
        System.out.println("\nAll Students:");
        if (students.size() == 0) {
            System.out.println("No students registered.");
            return;
        }
        
        TableFormatter table = new TableFormatter("No.", "ID", "Name", "Email", "Stream");
        
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            table.addRow(
                String.valueOf(i+1),
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getStream()
            );
        }
        
        System.out.println(table.toString());
    }

    public Student searchStudent(String id) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(id)) {
                return students.get(i);
            }
        }
        return null;
    }

    public void assignCourseToStudent(String studentId, String course) {
        Student student = searchStudent(studentId);
        if (student != null) {
            student.addCourse(course);
            saveStudents();
            System.out.println("Course '" + course + "' assigned to " + student.getName());
        } else {
            System.out.println("Student with ID " + studentId + " not found.");
        }
    }

    // Faculty Management Methods
    public void addFaculty(Faculty facultyMember) {
        // Check for duplicate ID
        if (searchFaculty(facultyMember.getId()) != null) {
            System.out.println("Error: Faculty with ID " + facultyMember.getId() + " already exists!");
            return;
        }
        
        faculty.add(facultyMember);
        saveFaculty();
        
        // Create login credentials for the faculty
        authSystem.createFacultyCredentials(facultyMember);
        
        System.out.println("Faculty added successfully: " + facultyMember.getName());
    }

    public void updateFaculty(String id, String name, String email) {
        for (int i = 0; i < faculty.size(); i++) {
            Faculty facultyMember = faculty.get(i);
            if (facultyMember.getId().equals(id)) {
                facultyMember.setName(name);
                facultyMember.setEmail(email);
                saveFaculty();
                System.out.println("Faculty updated successfully: " + facultyMember.getName());
                return;
            }
        }
        System.out.println("Faculty with ID " + id + " not found.");
    }

    public void deleteFaculty(String id) {
        for (int i = 0; i < faculty.size(); i++) {
            if (faculty.get(i).getId().equals(id)) {
                System.out.println("Faculty deleted: " + faculty.get(i).getName());
                
                // Remove credentials
                authSystem.removeCredentials(id);
                
                faculty.remove(i);
                saveFaculty();
                return;
            }
        }
        System.out.println("Faculty with ID " + id + " not found.");
    }

    public void viewAllFaculty() {
        System.out.println("\nAll Faculty Members:");
        if (faculty.size() == 0) {
            System.out.println("No faculty members registered.");
            return;
        }
        
        TableFormatter table = new TableFormatter("No.", "ID", "Name", "Email", "Subjects", "Classes");
        
        for (int i = 0; i < faculty.size(); i++) {
            Faculty facultyMember = faculty.get(i);
            
            // Convert subjects to string
            StringBuilder subjects = new StringBuilder();
            CustomArrayList<String> subjectList = facultyMember.getSubjects();
            for (int j = 0; j < subjectList.size(); j++) {
                subjects.append(subjectList.get(j));
                if (j < subjectList.size() - 1) {
                    subjects.append(", ");
                }
            }
            
            // Convert classes to string
            StringBuilder classes = new StringBuilder();
            CustomArrayList<String> classList = facultyMember.getAssignedClasses();
            for (int j = 0; j < classList.size(); j++) {
                classes.append(classList.get(j));
                if (j < classList.size() - 1) {
                    classes.append(", ");
                }
            }
            
            table.addRow(
                String.valueOf(i+1),
                facultyMember.getId(),
                facultyMember.getName(),
                facultyMember.getEmail(),
                subjects.toString(),
                classes.toString()
            );
        }
        
        System.out.println(table.toString());
    }

    public Faculty searchFaculty(String id) {
        for (int i = 0; i < faculty.size(); i++) {
            if (faculty.get(i).getId().equals(id)) {
                return faculty.get(i);
            }
        }
        return null;
    }

    public void assignSubjectToFaculty(String facultyId, String subject) {
        Faculty facultyMember = searchFaculty(facultyId);
        if (facultyMember != null) {
            facultyMember.addSubject(subject);
            saveFaculty();
            System.out.println("Subject '" + subject + "' assigned to " + facultyMember.getName());
        } else {
            System.out.println("Faculty with ID " + facultyId + " not found.");
        }
    }

    public void assignClassToFaculty(String facultyId, String className) {
        Faculty facultyMember = searchFaculty(facultyId);
        if (facultyMember != null) {
            facultyMember.assignClass(className);
            saveFaculty();
            System.out.println("Class '" + className + "' assigned to " + facultyMember.getName());
        } else {
            System.out.println("Faculty with ID " + facultyId + " not found.");
        }
    }
    
    // Course Management Methods
    /**
     * Add a new course to the system
     * @param course Course to add
     */
    public void addCourse(Course course) {
        if (courseManagement == null) {
            System.out.println("Error: Course management system not initialized!");
            return;
        }
        courseManagement.addCourse(course);
    }
    
    /**
     * Update an existing course
     * @param id Course ID to update
     * @param name New course name
     * @param duration New course duration
     * @param fees New course fees
     * @param scope New course scope
     * @param stream New course stream
     * @param semester New course semester
     * @param assignedFaculty New assigned faculty
     * @param availableSeats New available seats
     */
    public void updateCourse(String id, String name, int duration, double fees, 
                          String scope, String stream, int semester, 
                          String assignedFaculty, int availableSeats) {
        if (courseManagement == null) {
            System.out.println("Error: Course management system not initialized!");
            return;
        }
        
        Course course = courseManagement.searchCourseById(id);
        if (course == null) {
            System.out.println("Course with ID " + id + " not found.");
            return;
        }
        
        courseManagement.updateCourse(id, name, duration, fees, scope, stream, semester, assignedFaculty, availableSeats);
        System.out.println("Course updated successfully: " + name);
    }
    
    /**
     * Delete a course by ID
     * @param id Course ID to delete
     */
    public void deleteCourse(String id) {
        if (courseManagement == null) {
            System.out.println("Error: Course management system not initialized!");
            return;
        }
        
        Course course = courseManagement.searchCourseById(id);
        if (course == null) {
            System.out.println("Course with ID " + id + " not found.");
            return;
        }
        
        courseManagement.deleteCourse(id);
        System.out.println("Course deleted successfully: " + course.getName());
    }
    
    /**
     * View all courses in the system
     */
    public void viewAllCourses() {
        if (courseManagement == null) {
            System.out.println("Error: Course management system not initialized!");
            return;
        }
        
        courseManagement.viewCourses();
    }
    
    /**
     * Generate next course ID
     * @return Next available course ID
     */
    public String generateNextCourseId() {
        if (courseManagement == null) {
            System.out.println("Error: Course management system not initialized!");
            return "C001"; // Default if management system not available
        }
        
        CustomArrayList<String> existingIds = new CustomArrayList<>();
        CustomArrayList<Course> courses = courseManagement.getCourses();
        
        for (int i = 0; i < courses.size(); i++) {
            existingIds.add(courses.get(i).getId());
        }
        
        return ValidationUtil.generateNextId("C", existingIds);
    }
    
    /**
     * Handle course management panel
     */
    public void handleCourseManagement(java.util.Scanner scanner) {
        if (courseManagement == null) {
            System.out.println("Error: Course management system not initialized!");
            return;
        }
        
        boolean back = false;
        while (!back) {
            ConsoleUtil.clearScreen();
            System.out.println("============================");
            System.out.println("Course Management (Admin)");
            System.out.println("============================");
            System.out.println("1. Add Course");
            System.out.println("2. Update Course");
            System.out.println("3. Delete Course");
            System.out.println("4. View All Courses");
            System.out.println("5. Back");
            
            System.out.print("\nChoose an option: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid option! Please enter a number.");
                ConsoleUtil.pressEnterToContinue();
                continue;
            }
            
            switch (choice) {
                case 1:
                    addNewCourse(scanner);
                    break;
                case 2:
                    updateExistingCourse(scanner);
                    break;
                case 3:
                    deleteExistingCourse(scanner);
                    break;
                case 4:
                    viewAllCourses();
                    ConsoleUtil.pressEnterToContinue();
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option!");
                    ConsoleUtil.pressEnterToContinue();
            }
        }
    }
    
    /**
     * Helper method to add a new course with user input
     */
    private void addNewCourse(java.util.Scanner scanner) {
        String id = generateNextCourseId();
        System.out.println("Auto-generated course ID: " + id);
        
        System.out.print("Enter course name: ");
        String name = scanner.nextLine();
        
        int duration = 0;
        while (duration <= 0) {
            System.out.print("Enter duration (months): ");
            try {
                duration = Integer.parseInt(scanner.nextLine());
                if (duration <= 0) {
                    System.out.println("Duration must be a positive number!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
        
        double fees = 0;
        while (fees <= 0) {
            System.out.print("Enter fees: ₹");
            try {
                fees = Double.parseDouble(scanner.nextLine());
                if (fees <= 0) {
                    System.out.println("Fees must be a positive number!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
        
        System.out.print("Enter scope: ");
        String scope = scanner.nextLine();
        
        // Select stream
        String stream = ConsoleUtil.selectStream();
        
        int semester = 0;
        while (semester < 1 || semester > 8) {
            System.out.print("Enter semester (1-8): ");
            try {
                semester = Integer.parseInt(scanner.nextLine());
                if (semester < 1 || semester > 8) {
                    System.out.println("Semester must be between 1 and 8!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
        
        // Show available faculty
        viewAllFaculty();
        System.out.print("Enter faculty ID to assign (leave blank for none): ");
        String facultyId = scanner.nextLine();
        String assignedFaculty = "Not Assigned";
        if (!facultyId.isEmpty()) {
            Faculty faculty = searchFaculty(facultyId);
            if (faculty != null) {
                assignedFaculty = faculty.getName();
            }
        }
        
        int availableSeats = 0;
        while (availableSeats <= 0) {
            System.out.print("Enter available seats: ");
            try {
                availableSeats = Integer.parseInt(scanner.nextLine());
                if (availableSeats <= 0) {
                    System.out.println("Available seats must be a positive number!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
        
        Course newCourse = new Course(id, name, duration, fees, scope, stream, semester, assignedFaculty, availableSeats);
        addCourse(newCourse);
        ConsoleUtil.pressEnterToContinue();
    }
    
    /**
     * Helper method to update an existing course with user input
     */
    private void updateExistingCourse(java.util.Scanner scanner) {
        viewAllCourses();
        
        System.out.print("\nEnter course ID to update: ");
        String id = scanner.nextLine();
        
        Course course = courseManagement.searchCourseById(id);
        if (course == null) {
            System.out.println("Course not found with ID: " + id);
            ConsoleUtil.pressEnterToContinue();
            return;
        }
        
        System.out.print("Enter new name (current: " + course.getName() + "): ");
        String name = scanner.nextLine();
        if (name.isEmpty()) {
            name = course.getName();
        }
        
        System.out.print("Enter new duration (current: " + course.getDuration() + " months): ");
        String durationStr = scanner.nextLine();
        int duration = durationStr.isEmpty() ? course.getDuration() : Integer.parseInt(durationStr);
        
        System.out.print("Enter new fees (current: ₹" + course.getFees() + "): ");
        String feesStr = scanner.nextLine();
        double fees = feesStr.isEmpty() ? course.getFees() : Double.parseDouble(feesStr);
        
        System.out.print("Enter new scope (current: " + course.getScope() + "): ");
        String scope = scanner.nextLine();
        if (scope.isEmpty()) {
            scope = course.getScope();
        }
        
        System.out.println("Current stream: " + course.getStream());
        System.out.print("Enter new stream (leave blank to keep current): ");
        String stream = ConsoleUtil.selectStream();
        if (stream.isEmpty()) {
            stream = course.getStream();
        }
        
        System.out.print("Enter new semester (current: " + course.getSemester() + "): ");
        String semesterStr = scanner.nextLine();
        int semester = semesterStr.isEmpty() ? course.getSemester() : Integer.parseInt(semesterStr);
        if (semester < 1 || semester > 8) {
            semester = course.getSemester();
        }
        
        System.out.print("Enter new faculty ID (current: " + course.getAssignedFaculty() + "): ");
        String facultyId = scanner.nextLine();
        String assignedFaculty = course.getAssignedFaculty();
        
        if (!facultyId.isEmpty()) {
            Faculty faculty = searchFaculty(facultyId);
            if (faculty != null) {
                assignedFaculty = faculty.getName();
            }
        }
        
        System.out.print("Enter new available seats (current: " + course.getAvailableSeats() + "): ");
        String seatsStr = scanner.nextLine();
        int availableSeats = seatsStr.isEmpty() ? course.getAvailableSeats() : Integer.parseInt(seatsStr);
        
        updateCourse(id, name, duration, fees, scope, stream, semester, assignedFaculty, availableSeats);
        ConsoleUtil.pressEnterToContinue();
    }
    
    /**
     * Helper method to delete an existing course with user input
     */
    private void deleteExistingCourse(java.util.Scanner scanner) {
        viewAllCourses();
        
        System.out.print("\nEnter course ID to delete: ");
        String id = scanner.nextLine();
        
        Course course = courseManagement.searchCourseById(id);
        if (course == null) {
            System.out.println("Course not found with ID: " + id);
            ConsoleUtil.pressEnterToContinue();
            return;
        }
        
        System.out.print("Are you sure you want to delete " + course.getName() + " (y/n)? ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("y") || confirm.equals("yes")) {
            deleteCourse(id);
        } else {
            System.out.println("Deletion cancelled.");
        }
        
        ConsoleUtil.pressEnterToContinue();
    }
    
    // Generate next student ID
    public String generateNextStudentId() {
        CustomArrayList<String> existingIds = new CustomArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            existingIds.add(students.get(i).getId());
        }
        
        return ValidationUtil.generateNextId("S", existingIds);
    }
    
    // Generate next faculty ID
    public String generateNextFacultyId() {
        CustomArrayList<String> existingIds = new CustomArrayList<>();
        for (int i = 0; i < faculty.size(); i++) {
            existingIds.add(faculty.get(i).getId());
        }
        
        return ValidationUtil.generateNextId("F", existingIds);
    }

    // Method to show student attendance in tabular format
    public void viewStudentAttendance(String studentId) {
        Student student = searchStudent(studentId);
        if (student == null) {
            System.out.println("Student with ID " + studentId + " not found.");
            return;
        }
        
        System.out.println("\nAttendance for " + student.getName() + " (" + student.getId() + "):");
        ds.CustomHashMap<String, Boolean> attendance = student.getAttendance();
        
        if (attendance.size() == 0) {
            System.out.println("No attendance records available.");
            return;
        }
        
        TableFormatter table = new TableFormatter("Date", "Status");
        
        // Iterate through the CustomHashMap using available methods
        CustomArrayList<String> dates = attendance.keySet();
        for (int i = 0; i < dates.size(); i++) {
            String date = dates.get(i);
            Boolean present = attendance.get(date);
            table.addRow(date, TableFormatter.formatAttendance(present));
        }
        
        System.out.println(table.toString());
    }

    // Method to view student's courses in tabular format
    public void viewStudentCourses(String studentId) {
        Student student = searchStudent(studentId);
        if (student == null) {
            System.out.println("Student with ID " + studentId + " not found.");
            return;
        }
        
        System.out.println("\nCourses for " + student.getName() + " (" + student.getId() + "):");
        CustomArrayList<String> courses = student.getCourses();
        
        if (courses.size() == 0) {
            System.out.println("No courses assigned yet.");
            return;
        }
        
        TableFormatter table = new TableFormatter("No.", "Course");
        for (int i = 0; i < courses.size(); i++) {
            table.addRow(String.valueOf(i+1), courses.get(i));
        }
        
        System.out.println(table.toString());
    }

    // Method to view faculty's subjects in tabular format
    public void viewFacultySubjects(String facultyId) {
        Faculty facultyMember = searchFaculty(facultyId);
        if (facultyMember == null) {
            System.out.println("Faculty with ID " + facultyId + " not found.");
            return;
        }
        
        System.out.println("\nSubjects taught by " + facultyMember.getName() + " (" + facultyMember.getId() + "):");
        CustomArrayList<String> subjects = facultyMember.getSubjects();
        
        if (subjects.size() == 0) {
            System.out.println("No subjects assigned yet.");
            return;
        }
        
        TableFormatter table = new TableFormatter("No.", "Subject");
        for (int i = 0; i < subjects.size(); i++) {
            table.addRow(String.valueOf(i+1), subjects.get(i));
        }
        
        System.out.println(table.toString());
    }
    
    // Method to view faculty's classes in tabular format
    public void viewFacultyClasses(String facultyId) {
        Faculty facultyMember = searchFaculty(facultyId);
        if (facultyMember == null) {
            System.out.println("Faculty with ID " + facultyId + " not found.");
            return;
        }
        
        System.out.println("\nClasses taught by " + facultyMember.getName() + " (" + facultyMember.getId() + "):");
        CustomArrayList<String> classes = facultyMember.getAssignedClasses();
        
        if (classes.size() == 0) {
            System.out.println("No classes assigned yet.");
            return;
        }
        
        TableFormatter table = new TableFormatter("No.", "Class");
        for (int i = 0; i < classes.size(); i++) {
            table.addRow(String.valueOf(i+1), classes.get(i));
        }
        
        System.out.println(table.toString());
    }

    public CustomArrayList<Student> getStudents() {
        return students;
    }

    public CustomArrayList<Faculty> getFaculty() {
        return faculty;
    }
    
    // File handling methods
    private void saveStudents() {
        try {
            CustomArrayList<String> lines = new CustomArrayList<>();
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                if (student != null) {
                    StringBuilder line = new StringBuilder();
                    line.append(student.getId()).append("|");
                    line.append(student.getName()).append("|");
                    line.append(student.getEmail()).append("|");
                    line.append(student.getStream()).append("|");
                    
                    // Add courses
                    CustomArrayList<String> courses = student.getCourses();
                    for (int j = 0; j < courses.size(); j++) {
                        line.append(courses.get(j));
                        if (j < courses.size() - 1) {
                            line.append(",");
                        }
                    }
                    
                    lines.add(line.toString());
                }
            }
            FileHandler.saveToFile(STUDENTS_FILE, lines);
            System.out.println("Student data saved successfully.");
        } catch (Exception e) {
            System.out.println("Error saving student data: " + e.getMessage());
        }
    }
    
    private void saveFaculty() {
        try {
            CustomArrayList<String> lines = new CustomArrayList<>();
            for (int i = 0; i < faculty.size(); i++) {
                Faculty facultyMember = faculty.get(i);
                if (facultyMember != null) {
                    StringBuilder line = new StringBuilder();
                    line.append(facultyMember.getId()).append("|");
                    line.append(facultyMember.getName()).append("|");
                    line.append(facultyMember.getEmail()).append("|");
                    
                    // Add subjects
                    CustomArrayList<String> subjects = facultyMember.getSubjects();
                    for (int j = 0; j < subjects.size(); j++) {
                        line.append(subjects.get(j));
                        if (j < subjects.size() - 1) {
                            line.append(",");
                        }
                    }
                    
                    // Separator between subjects and classes
                    line.append("|");
                    
                    // Add classes
                    CustomArrayList<String> classes = facultyMember.getAssignedClasses();
                    for (int j = 0; j < classes.size(); j++) {
                        line.append(classes.get(j));
                        if (j < classes.size() - 1) {
                            line.append(",");
                        }
                    }
                    
                    lines.add(line.toString());
                }
            }
            FileHandler.saveToFile(FACULTY_FILE, lines);
            System.out.println("Faculty data saved successfully.");
        } catch (Exception e) {
            System.out.println("Error saving faculty data: " + e.getMessage());
        }
    }
    
    private void loadData() {
        loadStudents();
        loadFaculty();
    }
    
    private void loadStudents() {
        try {
            CustomArrayList<String> lines = FileHandler.readFromFile(STUDENTS_FILE);
            for (int i = 0; i < lines.size(); i++) {
                String[] data = lines.get(i).split("\\|");
                if (data.length >= 4) {
                    String id = data[0];
                    String name = data[1];
                    String email = data[2];
                    String stream = data[3];
                    
                    Student student = new Student(id, name, email, stream);
                    
                    // Add courses
                    if (data.length > 4) {
                        String[] courses = data[4].split(",");
                        for (String course : courses) {
                            student.addCourse(course);
                        }
                    }
                    
                    students.add(student);
                }
            }
            System.out.println("Student data loaded successfully.");
        } catch (Exception e) {
            System.out.println("Error loading student data: " + e.getMessage());
        }
    }
    
    private void loadFaculty() {
        try {
            CustomArrayList<String> lines = FileHandler.readFromFile(FACULTY_FILE);
            for (int i = 0; i < lines.size(); i++) {
                String[] data = lines.get(i).split("\\|");
                if (data.length >= 3) {
                    String id = data[0];
                    String name = data[1];
                    String email = data[2];
                    
                    Faculty facultyMember = new Faculty(id, name, email);
                    
                    // Add subjects
                    if (data.length > 3 && !data[3].isEmpty()) {
                        String[] subjects = data[3].split(",");
                        for (String subject : subjects) {
                            if (!subject.trim().isEmpty()) {
                                facultyMember.addSubject(subject);
                            }
                        }
                    }
                    
                    // Add classes
                    if (data.length > 4 && !data[4].isEmpty()) {
                        String[] classes = data[4].split(",");
                        for (String className : classes) {
                            if (!className.trim().isEmpty()) {
                                facultyMember.assignClass(className);
                            }
                        }
                    }
                    
                    faculty.add(facultyMember);
                }
            }
            System.out.println("Faculty data loaded successfully.");
        } catch (Exception e) {
            System.out.println("Error loading faculty data: " + e.getMessage());
        }
    }
}