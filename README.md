# Integrated College Management System (ICMS)

## Overview
The Integrated College Management System (ICMS) is a robust console-based Java application designed to streamline college operations through a modular architecture. It's built with a focus on custom Data Structures and Algorithms (DSA) implementation, completely avoiding Java's built-in collections framework. The system provides comprehensive management features for administrators, students, faculty, and library staff through an intuitive command-line interface.

## Core Features & Implementation Logic

### Custom Data Structures
All data management in ICMS relies on custom implementations of standard data structures:

- **CustomArrayList**: Dynamic array implementation with O(1) access time and automatic resizing
- **CustomLinkedList**: Doubly linked list with efficient insertions and deletions at O(1) for ends
- **CustomStack**: LIFO structure using the LinkedList backbone for push/pop operations
- **CustomQueue**: FIFO implementation for ordered processing (used in library book issuance)
- **CustomBST**: Binary Search Tree for efficient O(log n) search operations (used for book searches)
- **CustomHashMap**: Hash-based key-value storage with O(1) average access time, using chaining for collision resolution (powers authentication and attendance tracking)

### Authentication System
- Uses the CustomHashMap for credential storage and verification
- Implements role-based access control for Admin, Student, Faculty, and Library roles
- Secure login flow with username/password validation

### Admin Functionality
- Student and Faculty CRUD operations using CustomArrayList for data storage
- Course Management for adding, updating, and deleting courses
- Efficient search capabilities for finding users by ID or name
- Data persistence through file I/O operations for student and faculty records

### Student Management
- Profile viewing and updating through object-oriented encapsulation
- Course enrollment tracking using CustomArrayList references
- Book issuance history maintained through student-book associations

### Faculty Operations
- Attendance tracking system using CustomHashMap where:
  - Keys: Date strings
  - Values: CustomArrayList of present student IDs
- Subject assignment and management
- Class assignment logic for organizing teaching workload

### Library System
- Book checkout implemented with CustomQueue for fair processing order
- Book search functionality powered by CustomBST for O(log n) search efficiency
- Issue/return logic with timestamp tracking and availability status

### Course Management
- Enhanced Course model with detailed information including:
  - Course basics: name, ID, duration, fees
  - Academic details: stream/department, semester, scope
  - Administrative details: assigned faculty, available seats, last updated date
- Separation of concerns:
  - Admin-only interface for course creation, updating, and deletion
  - View-only access for students, faculty, and other users
- Multiple sorting algorithms implementation:
  - Bubble Sort for sorting courses by name
  - Selection Sort for sorting courses by fees
  - Insertion Sort for sorting courses by duration
  - Merge Sort as an efficient alternative for sorting by name
- Advanced search capabilities:
  - Search by course name
  - Filter by academic stream
  - Filter by fee range
- Sample course data stored separately in CourseData class
- Efficient course display with formatted tabular output

## Technical Architecture

The system follows a layered architecture:

1. **Data Structure Layer**: Custom implementations in the `ds` package
2. **Model Layer**: Entity classes in the `model` package defining core objects
3. **Service Layer**: Business logic implemented in respective functional packages
4. **UI Layer**: Console-based interface with input validation and formatted output

## Data Flow

1. User authentication through the AuthSystem class
2. Role-based redirection to appropriate panel
3. Panel classes handle user interaction and delegate to service classes
4. Service classes use custom data structures to manage, process and persist data

## File Structure & Package Organization

- **ds**: Houses all custom data structure implementations
- **model**: Contains entity classes (Student, Faculty, Course, Book)
- **auth**: Authentication and authorization logic
- **admin/student/faculty/library**: Role-specific functionality
- **course**: Course management operations
- **data**: Sample data for testing and demonstration
- **util**: Helper utilities for file operations, console I/O, and validation
- **main**: Application entry point and initialization

## Project Structure
```
ICMS/
├── src/
│   ├── ds/
│   │   ├── CustomArrayList.java
│   │   ├── CustomLinkedList.java
│   │   ├── CustomStack.java
│   │   ├── CustomQueue.java
│   │   ├── CustomBST.java
│   │   ├── CustomHashMap.java
│   ├── model/
│   │   ├── Student.java
│   │   ├── Faculty.java
│   │   ├── Course.java
│   │   ├── Book.java
│   ├── auth/
│   │   ├── AuthSystem.java
│   ├── admin/
│   │   ├── AdminPanel.java
│   ├── student/
│   │   ├── StudentPanel.java
│   ├── faculty/
│   │   ├── FacultyPanel.java
│   ├── library/
│   │   ├── LibrarySystem.java
│   ├── course/
│   │   ├── CourseManagement.java
│   ├── main/
│   │   ├── ICMS.java
├── README.md
```

## Performance Considerations

- Custom data structures are optimized for their specific use cases
- Time complexity analysis guided implementation choices:
  - Student/Faculty lookup: O(n) through CustomArrayList traversal
  - Book search: O(log n) via CustomBST
  - Authentication: O(1) average case with CustomHashMap
  - Course scheduling: O(n) for conflict detection
  - Course sorting: Various algorithms with different complexities:
    - Bubble Sort: O(n²) - Simple but inefficient for large datasets
    - Selection Sort: O(n²) - Memory-efficient with minimal swaps
    - Insertion Sort: O(n²) - Efficient for small or nearly sorted datasets
    - Merge Sort: O(n log n) - More efficient for large datasets

## Setup & Execution Instructions

### Prerequisites

Java Development Kit (JDK): Version 8 or higher.
Terminal/Command Prompt: For compiling and running the application.
Text Editor/IDE: Optional (e.g., VS Code, IntelliJ IDEA, Eclipse) for easier code management.

### Setup Instructions

Clone or Download the Project:

If using a version control system, clone the repository:
```
git clone <repository-url>
```

Alternatively, download the project files and extract them to a directory (e.g., ICMS).

Verify Directory Structure:

Ensure all .java files are organized in the src directory under their respective packages (ds, model, auth, admin, student, faculty, library, course, main).

Install JDK:

Download and install JDK from Oracle or use an open-source alternative like OpenJDK.
Verify installation:
```
java -version
javac -version
```

Ensure both commands return version information.

### How to Run the Application

Navigate to the Project Directory:

Open a terminal and change to the project directory:
```
cd path/to/ICMS
```

Compile the Java Files:

Compile all .java files in the src directory:
```
javac src/*/*.java -d bin
```

This creates a bin directory with compiled .class files.
Note: Ensure the bin directory exists, or create it using `mkdir bin`.

Run the Application:

Execute the main class (ICMS) from the bin directory:
```
java -cp bin main.ICMS
```

The -cp bin flag sets the classpath to the bin directory.

Interact with the Application:

The console displays a menu:
```
Integrated College Management System
1. Admin
2. Student
3. Faculty
4. Library
5. Courses
6. Exit
```

Use the following credentials for testing:
- Admin: Username: admin1, Password: admin123
- Student: Username: student1, Password: student123
- Faculty: Username: faculty1, Password: faculty123
- Library: Username: library1, Password: library123

Follow the prompts to perform operations based on the selected role.

### Troubleshooting

Compilation Errors:
- Ensure all .java files are in the correct package directories.
- Verify JDK is properly installed and configured.

ClassNotFoundException:
- Confirm the bin directory contains compiled .class files.
- Check the classpath (-cp bin) in the run command.

No Main Method Error:
- Ensure you are running main.ICMS from the bin directory.

### Notes

The application is console-based and does not persist data (data is reset on restart).
Extend functionality by adding features like attendance tracking, timetable management, or file-based persistence.
For development, consider using an IDE to simplify compilation and debugging.

### Contributing
Feel free to fork the project, make improvements, and submit pull requests. Suggestions for optimizing DSA implementations or adding new features are welcome!

### License
This project is licensed under the MIT License.
