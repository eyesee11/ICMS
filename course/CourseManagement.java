package course;

import model.Course;
import ds.CustomArrayList;
import util.TableFormatter;

public class CourseManagement {
    private CustomArrayList<Course> courses;

    public CourseManagement() {
        courses = new CustomArrayList<>();
    }

    public void addCourse(Course course) {
        courses.add(course);
        System.out.println("Course added successfully: " + course.getName());
    }

    public void viewCourses() {
        System.out.println("\nAll Courses:");
        if (courses.size() == 0) {
            System.out.println("No courses available.");
            return;
        }
        
        TableFormatter table = new TableFormatter("ID", "Name", "Duration", "Fees", "Stream", "Semester", "Faculty", "Available Seats");
        
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            table.addRow(
                course.getId(),
                course.getName(),
                course.getDuration() + " months",
                "₹" + course.getFees(),
                course.getStream(),
                String.valueOf(course.getSemester()),
                course.getAssignedFaculty(),
                String.valueOf(course.getAvailableSeats())
            );
        }
        
        System.out.println(table.toString());
    }

    public Course searchCourse(String name) {
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (course.getName().equalsIgnoreCase(name)) {
                return course;
            }
        }
        return null;
    }
    
    /**
     * Search for a course by ID
     * @param id Course ID to search for
     * @return Course if found, null otherwise
     */
    public Course searchCourseById(String id) {
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (course.getId().equalsIgnoreCase(id)) {
                return course;
            }
        }
        return null;
    }
    
    /**
     * Update an existing course
     * @param id Course ID to update
     * @param name New name
     * @param duration New duration
     * @param fees New fees
     * @param scope New scope
     * @param stream New stream
     * @param semester New semester
     * @param assignedFaculty New faculty
     * @param availableSeats New available seats
     */
    public void updateCourse(String id, String name, int duration, double fees, 
                          String scope, String stream, int semester, 
                          String assignedFaculty, int availableSeats) {
        Course course = searchCourseById(id);
        if (course == null) {
            System.out.println("Course not found with ID: " + id);
            return;
        }
        
        // Create a new updated course
        Course updatedCourse = new Course(id, name, duration, fees, scope, stream, semester, assignedFaculty, availableSeats);
        
        // Find and replace in the arraylist
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getId().equals(id)) {
                courses.remove(i);
                courses.add(i, updatedCourse);
                System.out.println("Course updated successfully: " + name);
                return;
            }
        }
    }
    
    /**
     * Delete a course from the system
     * @param id Course ID to delete
     * @return true if deleted, false if not found
     */
    public boolean deleteCourse(String id) {
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getId().equals(id)) {
                courses.remove(i);
                return true;
            }
        }
        return false;
    }
    
    public void viewCoursesByStream(String stream) {
        System.out.println("\nCourses in stream: " + stream);
        
        TableFormatter table = new TableFormatter("ID", "Name", "Duration", "Fees", "Scope", "Semester", "Faculty", "Available Seats");
        int count = 0;
        
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (course.getStream().equalsIgnoreCase(stream)) {
                count++;
                table.addRow(
                    course.getId(),
                    course.getName(),
                    course.getDuration() + " months",
                    "₹" + course.getFees(),
                    course.getScope(),
                    String.valueOf(course.getSemester()),
                    course.getAssignedFaculty(),
                    String.valueOf(course.getAvailableSeats())
                );
            }
        }
        
        if (count == 0) {
            System.out.println("No courses found for stream: " + stream);
        } else {
            System.out.println(table.toString());
        }
    }
    
    // Sorting methods
    public void sortCoursesByName() {
        // Simple bubble sort implementation
        for (int i = 0; i < courses.size() - 1; i++) {
            for (int j = 0; j < courses.size() - i - 1; j++) {
                if (courses.get(j).getName().compareTo(courses.get(j + 1).getName()) > 0) {
                    // Swap courses
                    Course temp = courses.get(j);
                    Course next = courses.get(j + 1);
                    
                    // Implementation that works with CustomArrayList
                    courses.remove(j);
                    courses.remove(j); // After first remove, j+1 becomes j
                    courses.add(j, next);
                    courses.add(j + 1, temp);
                }
            }
        }
        System.out.println("Courses sorted by name using bubble sort.");
    }
    
    public void sortCoursesByFees() {
        // Selection sort implementation for fees
        for (int i = 0; i < courses.size() - 1; i++) {
            int minIndex = i;
            
            // Find the minimum element
            for (int j = i + 1; j < courses.size(); j++) {
                if (courses.get(j).getFees() < courses.get(minIndex).getFees()) {
                    minIndex = j;
                }
            }
            
            // Swap the found minimum element with the current element
            if (minIndex != i) {
                Course temp = courses.get(i);
                Course min = courses.get(minIndex);
                
                // Implementation that works with CustomArrayList
                if (minIndex > i) {
                    courses.remove(i);
                    courses.remove(minIndex - 1); // Adjust index after first remove
                    courses.add(i, min);
                    courses.add(minIndex, temp);
                } else {
                    courses.remove(minIndex);
                    courses.remove(i - 1); // Adjust index after first remove
                    courses.add(minIndex, temp);
                    courses.add(i, min);
                }
            }
        }
        System.out.println("Courses sorted by fees using selection sort.");
    }
    
    public void sortCoursesByDuration() {
        // Create a new list to store sorted courses
        CustomArrayList<Course> sorted = new CustomArrayList<>();
        
        // Add all courses to sorted list
        for (int i = 0; i < courses.size(); i++) {
            sorted.add(courses.get(i));
        }
        
        // Insertion sort implementation for duration
        for (int i = 1; i < sorted.size(); i++) {
            Course key = sorted.get(i);
            int j = i - 1;
            
            while (j >= 0 && sorted.get(j).getDuration() > key.getDuration()) {
                j--;
            }
            
            // Insert key at correct position
            sorted.remove(i);
            sorted.add(j + 1, key);
        }
        
        // Replace original list with sorted list
        courses = sorted;
        System.out.println("Courses sorted by duration using insertion sort.");
    }
    
    public CustomArrayList<Course> searchCoursesByFeesRange(double minFees, double maxFees) {
        CustomArrayList<Course> result = new CustomArrayList<>();
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (course.getFees() >= minFees && course.getFees() <= maxFees) {
                result.add(course);
            }
        }
        return result;
    }
    
    // Display search results in table format
    public void displayCourseSearchResults(CustomArrayList<Course> courseList, String title) {
        System.out.println("\n" + title + ":");
        
        if (courseList.size() == 0) {
            System.out.println("No courses found matching the criteria.");
            return;
        }
        
        TableFormatter table = new TableFormatter("ID", "Name", "Duration", "Fees", "Stream", "Semester", "Faculty", "Available Seats");
        
        for (int i = 0; i < courseList.size(); i++) {
            Course course = courseList.get(i);
            table.addRow(
                course.getId(),
                course.getName(),
                course.getDuration() + " months",
                "₹" + course.getFees(),
                course.getStream(),
                String.valueOf(course.getSemester()),
                course.getAssignedFaculty(),
                String.valueOf(course.getAvailableSeats())
            );
        }
        
        System.out.println(table.toString());
    }
    
    // Merge sort implementation for sorting courses by name
    public void mergeSortCoursesByName() {
        if (courses.size() <= 1) {
            return; // Already sorted
        }
        
        CustomArrayList<Course> result = new CustomArrayList<>();
        CustomArrayList<Course> left = new CustomArrayList<>();
        CustomArrayList<Course> right = new CustomArrayList<>();
        
        int middle = courses.size() / 2;
        
        // Fill left half
        for (int i = 0; i < middle; i++) {
            left.add(courses.get(i));
        }
        
        // Fill right half
        for (int i = middle; i < courses.size(); i++) {
            right.add(courses.get(i));
        }
        
        // Recursively sort both halves
        if (left.size() > 1) {
            left = mergeSort(left);
        }
        
        if (right.size() > 1) {
            right = mergeSort(right);
        }
        
        // Merge the sorted halves
        result = merge(left, right);
        
        // Copy result back to courses
        courses = result;
        System.out.println("Courses sorted by name using merge sort.");
    }
    
    private CustomArrayList<Course> mergeSort(CustomArrayList<Course> list) {
        if (list.size() <= 1) {
            return list;
        }
        
        CustomArrayList<Course> result = new CustomArrayList<>();
        CustomArrayList<Course> left = new CustomArrayList<>();
        CustomArrayList<Course> right = new CustomArrayList<>();
        
        int middle = list.size() / 2;
        
        // Fill left half
        for (int i = 0; i < middle; i++) {
            left.add(list.get(i));
        }
        
        // Fill right half
        for (int i = middle; i < list.size(); i++) {
            right.add(list.get(i));
        }
        
        // Recursively sort both halves
        if (left.size() > 1) {
            left = mergeSort(left);
        }
        
        if (right.size() > 1) {
            right = mergeSort(right);
        }
        
        // Merge the sorted halves
        result = merge(left, right);
        
        return result;
    }
    
    private CustomArrayList<Course> merge(CustomArrayList<Course> left, CustomArrayList<Course> right) {
        CustomArrayList<Course> result = new CustomArrayList<>();
        int leftIndex = 0;
        int rightIndex = 0;
        
        // Compare elements from both lists and add the smaller one to the result
        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (left.get(leftIndex).getName().compareTo(right.get(rightIndex).getName()) <= 0) {
                result.add(left.get(leftIndex));
                leftIndex++;
            } else {
                result.add(right.get(rightIndex));
                rightIndex++;
            }
        }
        
        // Add remaining elements from left list
        while (leftIndex < left.size()) {
            result.add(left.get(leftIndex));
            leftIndex++;
        }
        
        // Add remaining elements from right list
        while (rightIndex < right.size()) {
            result.add(right.get(rightIndex));
            rightIndex++;
        }
        
        return result;
    }
    
    // Utility method for displaying course details
    public void displayCourseDetails(Course course) {
        if (course == null) {
            System.out.println("No course to display.");
            return;
        }
        
        System.out.println("\nCourse Details:");
        TableFormatter table = new TableFormatter("Field", "Value");
        table.addRow("ID", course.getId());
        table.addRow("Name", course.getName());
        table.addRow("Duration", course.getDuration() + " months");
        table.addRow("Fees", "₹" + course.getFees());
        table.addRow("Scope", course.getScope());
        table.addRow("Stream", course.getStream());
        table.addRow("Semester", String.valueOf(course.getSemester()));
        table.addRow("Faculty", course.getAssignedFaculty());
        table.addRow("Available Seats", String.valueOf(course.getAvailableSeats()));
        table.addRow("Last Updated", course.getLastUpdated());
        
        System.out.println(table.toString());
    }
    
    public CustomArrayList<Course> getCourses() {
        return courses;
    }
}