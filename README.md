## Integrated College Management System (ICMS)

# Overview
The Integrated College Management System (ICMS) is a console-based Java application designed to manage college operations, including student, faculty, course, library, and admin functionalities. It emphasizes Data Structures and Algorithms (DSA) by implementing custom data structures like ArrayList, LinkedList, Stack, Queue, Binary Search Tree (BST), and HashMap without using Java's built-in collections. The system features role-based access, full CRUD operations, and modular code design.

# Features

Role-Based Login: Supports Admin, Student, Faculty, and Library roles with custom HashMap-based authentication.
Admin Panel: Manage students and faculty (add, update, delete, view).
Student Panel: View/update profile, view courses, and issued books.
Faculty Panel: View/update profile, view subjects, and mark attendance.
Library System: Issue/return books using a Queue, search books via BST.
Course Management: Add/view courses, search by name.
Custom Data Structures: Manual implementations of ArrayList, LinkedList, Stack, Queue, BST, and HashMap.

# Project Structure
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

# Prerequisites

Java Development Kit (JDK): Version 8 or higher.
Terminal/Command Prompt: For compiling and running the application.
Text Editor/IDE: Optional (e.g., VS Code, IntelliJ IDEA, Eclipse) for easier code management.

# Setup Instructions

Clone or Download the Project:

If using a version control system, clone the repository:git clone <repository-url>


Alternatively, download the project files and extract them to a directory (e.g., ICMS).


Verify Directory Structure:

Ensure all .java files are organized in the src directory under their respective packages (ds, model, auth, admin, student, faculty, library, course, main).


Install JDK:

Download and install JDK from Oracle or use an open-source alternative like OpenJDK.
Verify installation:java -version
javac -version

Ensure both commands return version information.



# How to Run the Application

Navigate to the Project Directory:

Open a terminal and change to the project directory:cd path/to/ICMS




Compile the Java Files:

Compile all .java files in the src directory:javac src/*/*.java -d bin


This creates a bin directory with compiled .class files.
Note: Ensure the bin directory exists, or create it using mkdir bin.




Run the Application:

Execute the main class (ICMS) from the bin directory:java -cp bin main.ICMS


The -cp bin flag sets the classpath to the bin directory.




Interact with the Application:

The console displays a menu:Integrated College Management System
1. Admin
2. Student
3. Faculty
4. Library
5. Courses
6. Exit


Use the following credentials for testing:
Admin: Username: admin1, Password: admin123
Student: Username: student1, Password: student123
Faculty: Username: faculty1, Password: faculty123
Library: Username: library1, Password: library123


Follow the prompts to perform operations based on the selected role.



Troubleshooting

Compilation Errors:
Ensure all .java files are in the correct package directories.
Verify JDK is properly installed and configured.


ClassNotFoundException:
Confirm the bin directory contains compiled .class files.
Check the classpath (-cp bin) in the run command.


No Main Method Error:
Ensure you are running main.ICMS from the bin directory.



Notes

The application is console-based and does not persist data (data is reset on restart).
Extend functionality by adding features like attendance tracking, timetable management, or file-based persistence.
For development, consider using an IDE to simplify compilation and debugging.

Contributing
Feel free to fork the project, make improvements, and submit pull requests. Suggestions for optimizing DSA implementations or adding new features are welcome!
License
This project is licensed under the MIT License.
