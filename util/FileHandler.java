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
        // Create data directory if it doesn't exist
        File dataDir = new File(DATA_DIRECTORY);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
    }

    /**
     * Save data to a file
     * @param filename The filename to save to (will be placed in the data directory)
     * @param lines The lines to write to the file
     */
    public static void saveToFile(String filename, CustomArrayList<String> lines) {
        String filePath = DATA_DIRECTORY + File.separator + filename;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < lines.size(); i++) {
                writer.write(lines.get(i));
                writer.newLine();
            }
            System.out.println("Data saved to: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Read data from a file
     * @param filename The filename to read from (will look in the data directory)
     * @return CustomArrayList containing the lines from the file
     */
    public static CustomArrayList<String> readFromFile(String filename) {
        String filePath = DATA_DIRECTORY + File.separator + filename;
        CustomArrayList<String> lines = new CustomArrayList<>();
        File file = new File(filePath);

        // If file doesn't exist, return empty list
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
     * @param filename The filename to check (will look in the data directory)
     * @return true if file exists, false otherwise
     */
    public static boolean fileExists(String filename) {
        String filePath = DATA_DIRECTORY + File.separator + filename;
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * Backup a file (create a copy with .bak extension)
     * @param filename The filename to backup (will look in the data directory)
     * @return true if backup was successful, false otherwise
     */
    public static boolean backupFile(String filename) {
        String filePath = DATA_DIRECTORY + File.separator + filename;
        String backupPath = filePath + ".bak";

        try (FileInputStream in = new FileInputStream(filePath);
             FileOutputStream out = new FileOutputStream(backupPath)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            return true;
        } catch (IOException e) {
            System.err.println("Error backing up file " + filePath + ": " + e.getMessage());
            return false;
        }
    }

    // Save students to file
    public static void saveStudents(CustomArrayList<Student> students) {
        CustomArrayList<String> lines = new CustomArrayList<>();
        lines.add("id,name,email,stream");

        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            lines.add(student.getId() + "," + student.getName() + "," +
                    student.getEmail() + "," + student.getStream());
        }

        saveToFile("students.csv", lines);
    }

    // Load students from file
    public static CustomArrayList<Student> loadStudents() {
        CustomArrayList<Student> students = new CustomArrayList<>();
        CustomArrayList<String> lines = readFromFile("students.csv");

        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            if (parts.length >= 4) {
                String id = parts[0];
                String name = parts[1];
                String email = parts[2];
                String stream = parts[3];

                students.add(new Student(id, name, email, stream));
            }
        }

        return students;
    }

    // Save faculty to file
    public static void saveFaculty(CustomArrayList<Faculty> faculty) {
        CustomArrayList<String> lines = new CustomArrayList<>();
        lines.add("id,name,email");

        for (int i = 0; i < faculty.size(); i++) {
            Faculty f = faculty.get(i);
            lines.add(f.getId() + "," + f.getName() + "," + f.getEmail());
        }

        saveToFile("faculty.csv", lines);
    }

    // Load faculty from file
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

    // Save courses to file
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

    // Load courses from file
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

    // Save books to file
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

    // Load books from file
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

    // Save credentials to file
    public static void saveCredentials(CustomArrayList<String[]> credentials) {
        CustomArrayList<String> lines = new CustomArrayList<>();
        lines.add("username,password,role");

        for (int i = 0; i < credentials.size(); i++) {
            String[] cred = credentials.get(i);
            lines.add(cred[0] + "," + cred[1] + "," + cred[2]);
        }

        saveToFile("credentials.csv", lines);
    }

    // Load credentials from file
    public static CustomArrayList<String[]> loadCredentials() {
        CustomArrayList<String[]> credentials = new CustomArrayList<>();
        CustomArrayList<String> lines = readFromFile("credentials.csv");

        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            if (parts.length >= 3) {
                credentials.add(new String[]{parts[0], parts[1], parts[2]});
            }
        }

        return credentials;
    }

    // Save faculty subjects
    public static void saveFacultySubjects(String facultyId, CustomArrayList<String> subjects) {
        saveToFile("faculty_" + facultyId + "_subjects.txt", subjects);
    }

    // Load faculty subjects
    public static CustomArrayList<String> loadFacultySubjects(String facultyId) {
        return readFromFile("faculty_" + facultyId + "_subjects.txt");
    }

    // Save faculty classes
    public static void saveFacultyClasses(String facultyId, CustomArrayList<String> classes) {
        saveToFile("faculty_" + facultyId + "_classes.txt", classes);
    }

    // Load faculty classes
    public static CustomArrayList<String> loadFacultyClasses(String facultyId) {
        return readFromFile("faculty_" + facultyId + "_classes.txt");
    }

    // Save student courses
    public static void saveStudentCourses(String studentId, CustomArrayList<String> courses) {
        saveToFile("student_" + studentId + "_courses.txt", courses);
    }

    // Load student courses
    public static CustomArrayList<String> loadStudentCourses(String studentId) {
        return readFromFile("student_" + studentId + "_courses.txt");
    }
}