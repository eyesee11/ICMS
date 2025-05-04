package util;

import java.io.*;
import ds.CustomArrayList;
import model.Student;
import model.Faculty;
import model.Course;
import model.Book;

public class FileHandler {
    private static final String DATA_DIRECTORY = "data";

    static {
        File dataDir = new File(DATA_DIRECTORY);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
    }

    /**
     * Save data to a file
     * 
     * @param filename The filename to save to (will be placed in the data
     *                 directory)
     * @param lines    The lines to write to the file
     */
    public static void saveToFile(String filename, CustomArrayList<String> lines) {
        String filePath = DATA_DIRECTORY + File.separator + filename;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < lines.size(); i++) {
                writer.write(lines.get(i));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }

    /**
     * Read data from a file
     * 
     * @param filename The filename to read from (will look in the data directory)
     * @return CustomArrayList containing the lines from the file
     */
    public static CustomArrayList<String> readFromFile(String filename) {
        String filePath = DATA_DIRECTORY + File.separator + filename;
        CustomArrayList<String> lines = new CustomArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return lines;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading from file " + filePath + ": " + e.getMessage());
        }

        return lines;
    }

    /**
     * Delete a file
     * 
     * @param filename The filename to delete (will look in the data directory)
     * @return true if deletion was successful, false otherwise
     */
    public static boolean deleteFile(String filename) {
        String filePath = DATA_DIRECTORY + File.separator + filename;
        File file = new File(filePath);

        if (file.exists()) {
            return file.delete();
        }

        return false;
    }

    /**
     * Check if a file exists
     * 
     * @param filename The filename to check (will look in the data directory)
     * @return true if file exists, false otherwise
     */
    public static boolean fileExists(String filename) {
        String filePath = DATA_DIRECTORY + File.separator + filename;
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * Save students to CSV file
     * 
     * @param students The list of students to save
     */
    public static void saveStudents(CustomArrayList<Student> students) {
        CustomArrayList<String> lines = new CustomArrayList<>();
        lines.add("id,name,email,stream,admissionYear");

        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            lines.add(student.getId() + "," + student.getName() + "," +
                    student.getEmail() + "," + student.getStream() + "," +
                    student.getAdmissionYear());
        }

        saveToFile("students.csv", lines);
    }

    /**
     * Load students from CSV file
     * 
     * @return List of students loaded from CSV
     */
    public static CustomArrayList<Student> loadStudents() {
        CustomArrayList<Student> students = new CustomArrayList<>();
        CustomArrayList<String> lines = readFromFile("students.csv");

        // Skip header line if it exists
        int startIndex = lines.size() > 0 && lines.get(0).startsWith("id,") ? 1 : 0;

        for (int i = startIndex; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            if (parts.length >= 5) {
                String id = parts[0];
                String name = parts[1];
                String email = parts[2];
                String stream = parts[3];
                int admissionYear = Integer.parseInt(parts[4]);

                students.add(new Student(id, name, email, stream, admissionYear));
            } else if (parts.length >= 4) {
                // Handle old format files without admission year
                String id = parts[0];
                String name = parts[1];
                String email = parts[2];
                String stream = parts[3];
                // Use current year as default for old records
                int currentYear = java.time.Year.now().getValue();

                students.add(new Student(id, name, email, stream, currentYear));
            }
        }

        return students;
    }

    /**
     * Save faculty to CSV file
     * 
     * @param faculty The list of faculty to save
     */
    public static void saveFaculty(CustomArrayList<Faculty> faculty) {
        CustomArrayList<String> lines = new CustomArrayList<>();
        lines.add("id,name,email");

        for (int i = 0; i < faculty.size(); i++) {
            Faculty f = faculty.get(i);
            lines.add(f.getId() + "," + f.getName() + "," + f.getEmail());
        }

        saveToFile("faculty.csv", lines);
    }

    /**
     * Load faculty from CSV file
     * 
     * @return List of faculty loaded from CSV
     */
    public static CustomArrayList<Faculty> loadFaculty() {
        CustomArrayList<Faculty> faculty = new CustomArrayList<>();
        CustomArrayList<String> lines = readFromFile("faculty.csv");

        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            if (parts.length >= 3) {
                String id = parts[0];
                String name = parts[1];
                String email = parts[2];

                faculty.add(new Faculty(id, name, email));
            }
        }

        return faculty;
    }

    /**
     * Save courses to CSV file
     * 
     * @param courses The list of courses to save
     */
    public static void saveCourses(CustomArrayList<Course> courses) {
        CustomArrayList<String> lines = new CustomArrayList<>();
        lines.add("id,name,duration,fees,scope,stream");

        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            lines.add(course.getId() + "," + course.getName() + "," +
                    course.getDuration() + "," + course.getFees() + "," +
                    course.getScope() + "," + course.getStream());
        }

        saveToFile("courses.csv", lines);
    }

    /**
     * Load courses from CSV file
     * 
     * @return List of courses loaded from CSV
     */
    public static CustomArrayList<Course> loadCourses() {
        CustomArrayList<Course> courses = new CustomArrayList<>();
        CustomArrayList<String> lines = readFromFile("courses.csv");

        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            if (parts.length >= 6) {
                String id = parts[0];
                String name = parts[1];
                int duration = Integer.parseInt(parts[2]);
                double fees = Double.parseDouble(parts[3]);
                String scope = parts[4];
                String stream = parts[5];

                courses.add(new Course(id, name, duration, fees, scope, stream));
            }
        }

        return courses;
    }

    /**
     * Save books to CSV file
     * 
     * @param books The list of books to save
     */
    public static void saveBooks(CustomArrayList<Book> books) {
        CustomArrayList<String> lines = new CustomArrayList<>();
        lines.add("id,title,author,isIssued");

        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            lines.add(book.getId() + "," + book.getTitle() + "," +
                    book.getAuthor() + "," + book.isIssued());
        }

        saveToFile("books.csv", lines);
    }

    /**
     * Load books from CSV file
     * 
     * @return List of books loaded from CSV
     */
    public static CustomArrayList<Book> loadBooks() {
        CustomArrayList<Book> books = new CustomArrayList<>();
        CustomArrayList<String> lines = readFromFile("books.csv");

        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            if (parts.length >= 4) {
                String id = parts[0];
                String title = parts[1];
                String author = parts[2];
                boolean isIssued = Boolean.parseBoolean(parts[3]);

                Book book = new Book(id, title, author);
                book.setIssued(isIssued);
                books.add(book);
            }
        }

        return books;
    }

    /**
     * Save faculty subjects
     * 
     * @param facultyId The faculty ID
     * @param subjects  List of subjects
     */
    public static void saveFacultySubjects(String facultyId, CustomArrayList<String> subjects) {
        saveToFile("faculty_" + facultyId + "_subjects.txt", subjects);
    }

    /**
     * Load faculty subjects
     * 
     * @param facultyId The faculty ID
     * @return List of subjects
     */
    public static CustomArrayList<String> loadFacultySubjects(String facultyId) {
        return readFromFile("faculty_" + facultyId + "_subjects.txt");
    }

    /**
     * Save faculty classes
     * 
     * @param facultyId The faculty ID
     * @param classes   List of classes
     */
    public static void saveFacultyClasses(String facultyId, CustomArrayList<String> classes) {
        saveToFile("faculty_" + facultyId + "_classes.txt", classes);
    }

    /**
     * Load faculty classes
     * 
     * @param facultyId The faculty ID
     * @return List of classes
     */
    public static CustomArrayList<String> loadFacultyClasses(String facultyId) {
        return readFromFile("faculty_" + facultyId + "_classes.txt");
    }

    /**
     * Save student courses
     * 
     * @param studentId The student ID
     * @param courses   List of courses
     */
    public static void saveStudentCourses(String studentId, CustomArrayList<String> courses) {
        saveToFile("student_" + studentId + "_courses.txt", courses);
    }

    /**
     * Load student courses
     * 
     * @param studentId The student ID
     * @return List of courses
     */
    public static CustomArrayList<String> loadStudentCourses(String studentId) {
        return readFromFile("student_" + studentId + "_courses.txt");
    }
}