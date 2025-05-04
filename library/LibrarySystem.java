package library;

import model.Book;
import model.Student;
import model.Faculty;
import ds.CustomQueue;
import ds.CustomBST;
import ds.CustomArrayList;
import util.TableFormatter;

public class LibrarySystem {
    private CustomQueue<String> issueQueue;
    private CustomBST<Book> booksBST;
    private CustomArrayList<String> issuedBooks;

    public LibrarySystem() {
        issueQueue = new CustomQueue<>();
        booksBST = new CustomBST<>();
        issuedBooks = new CustomArrayList<>();
    }

    public void addBook(Book book) {
        booksBST.insert(book);
        System.out.println("Book added successfully: " + book.getTitle() + " by " + book.getAuthor());
    }

    public void issueBook(Student student, Book book) {
        if (!book.isIssued()) {
            book.setIssued(true);
            student.issueBook(book.getId());
            String record = student.getId() + ":" + book.getId();
            issueQueue.enqueue(record);
            issuedBooks.add(record);
            System.out.println("Book '" + book.getTitle() + "' issued to " + student.getName());
        } else {
            System.out.println("Sorry, this book is already issued.");
        }
    }

    public void issueBook(Faculty faculty, Book book) {
        if (!book.isIssued()) {
            book.setIssued(true);
            faculty.issueBook(book.getId());
            String record = faculty.getId() + ":" + book.getId();
            issueQueue.enqueue(record);
            issuedBooks.add(record);
            System.out.println("Book '" + book.getTitle() + "' issued to " + faculty.getName());
        } else {
            System.out.println("Sorry, this book is already issued.");
        }
    }

    public void returnBook(Student student, Book book) {
        if (book.isIssued()) {
            book.setIssued(false);
            student.returnBook(book.getId());
            
            // Remove from issued books list
            for (int i = 0; i < issuedBooks.size(); i++) {
                String record = issuedBooks.get(i);
                if (record.equals(student.getId() + ":" + book.getId())) {
                    issuedBooks.remove(i);
                    break;
                }
            }
            
            System.out.println("Book '" + book.getTitle() + "' returned by " + student.getName());
        } else {
            System.out.println("This book was not issued.");
        }
    }
 
    public void returnBook(Faculty faculty, Book book) {
        if (book.isIssued()) {
            book.setIssued(false);
            faculty.returnBook(book.getId());
            
            // Remove from issued books list
            for (int i = 0; i < issuedBooks.size(); i++) {
                String record = issuedBooks.get(i);
                if (record.equals(faculty.getId() + ":" + book.getId())) {
                    issuedBooks.remove(i);
                    break;
                }
            }
            
            System.out.println("Book '" + book.getTitle() + "' returned by " + faculty.getName());
        } else {
            System.out.println("This book was not issued.");
        }
    }

    public boolean searchBook(Book book) {
        return booksBST.search(book);
    }
    
    public void viewAvailableBooks(CustomArrayList<Book> allBooks) {
        System.out.println("\nAvailable Books:");
        int count = 0;
        
        TableFormatter table = new TableFormatter("No.", "ID", "Title", "Author");
        
        for (int i = 0; i < allBooks.size(); i++) {
            Book book = allBooks.get(i);
            if (!book.isIssued()) {
                count++;
                table.addRow(
                    String.valueOf(count),
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor()
                );
            }
        }
        
        if (count == 0) {
            System.out.println("No books available at the moment.");
        } else {
            System.out.println(table.toString());
        }
    }
    
    public void viewIssuedBooks(CustomArrayList<Book> allBooks) {
        System.out.println("\nIssued Books:");
        int count = 0;
        
        TableFormatter table = new TableFormatter("No.", "ID", "Title", "Author", "Issued To");
        
        for (int i = 0; i < allBooks.size(); i++) {
            Book book = allBooks.get(i);
            if (book.isIssued()) {
                count++;
                
                // Find the student who has this book
                String studentId = "Unknown";
                for (int j = 0; j < issuedBooks.size(); j++) {
                    String record = issuedBooks.get(j);
                    if (record.endsWith(":" + book.getId())) {
                        studentId = record.substring(0, record.indexOf(":"));
                        break;
                    }
                }
                
                table.addRow(
                    String.valueOf(count),
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    "Student ID: " + studentId
                );
            }
        }
        
        if (count == 0) {
            System.out.println("No books are currently issued.");
        } else {
            System.out.println(table.toString());
        }
    }
    
    // Linear search for books by title
    public Book linearSearchByTitle(CustomArrayList<Book> allBooks, String title) {
        for (int i = 0; i < allBooks.size(); i++) {
            Book book = allBooks.get(i);
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }
    
    // Binary search for books by title (assumes sorted array)
    public Book binarySearchByTitle(CustomArrayList<Book> allBooks, String title) {
        // First create a sorted copy of allBooks by title
        CustomArrayList<Book> sortedBooks = new CustomArrayList<>();
        for (int i = 0; i < allBooks.size(); i++) {
            sortedBooks.add(allBooks.get(i));
        }
        
        // Simple bubble sort to sort by title
        for (int i = 0; i < sortedBooks.size() - 1; i++) {
            for (int j = 0; j < sortedBooks.size() - i - 1; j++) {
                if (sortedBooks.get(j).getTitle().compareTo(sortedBooks.get(j + 1).getTitle()) > 0) {
                    // Swap
                    Book temp = sortedBooks.get(j);
                    sortedBooks.remove(j);
                    sortedBooks.add(j + 1, temp);
                }
            }
        }
        
        // Now do binary search
        int left = 0;
        int right = sortedBooks.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            Book midBook = sortedBooks.get(mid);
            int comparison = midBook.getTitle().compareToIgnoreCase(title);
            
            if (comparison == 0) {
                // Found the book, return the original book from allBooks
                String foundId = midBook.getId();
                for (int i = 0; i < allBooks.size(); i++) {
                    if (allBooks.get(i).getId().equals(foundId)) {
                        return allBooks.get(i);
                    }
                }
                return midBook; // Should not happen, but just in case
            } else if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return null; 
    }
    
    // Method to display queue of issue requests
    public void displayIssueQueue() {
        System.out.println("\nCurrent Book Issue Queue:");
        if (issueQueue.isEmpty()) {
            System.out.println("No books in issue queue.");
            return;
        }
        
        // Create temporary queue to preserve original
        CustomQueue<String> tempQueue = new CustomQueue<>();
        int count = 1;
        
        TableFormatter table = new TableFormatter("No.", "Issue Record");
        
        while (!issueQueue.isEmpty()) {
            String record = issueQueue.dequeue();
            table.addRow(String.valueOf(count++), record);
            tempQueue.enqueue(record);
        }
        
        // Restore queue
        while (!tempQueue.isEmpty()) {
            issueQueue.enqueue(tempQueue.dequeue());
        }
        
        System.out.println(table.toString());
    }
    
    // View books issued to a specific user (student or faculty)
    public void viewUserIssuedBooks(CustomArrayList<Book> allBooks, Student student) {
        System.out.println("\nYour Issued Books:");
        CustomArrayList<String> issuedBooks = student.getIssuedBooks();
        
        if (issuedBooks.size() == 0) {
            System.out.println("You don't have any books issued currently.");
            return;
        }
        
        TableFormatter table = new TableFormatter("No.", "Book Title", "Author", "Issue Date");
        int count = 0;
        
        for (int i = 0; i < issuedBooks.size(); i++) {
            String bookId = issuedBooks.get(i);
            for (int j = 0; j < allBooks.size(); j++) {
                Book book = allBooks.get(j);
                if (book.getId().equals(bookId)) {
                    count++;
                    table.addRow(
                        String.valueOf(count),
                        book.getTitle(),
                        book.getAuthor(),
                        "N/A"  // Would track issue date in a real system
                    );
                }
            }
        }
        
        System.out.println(table.toString());
    }
    
    // Overloaded method for faculty
    public void viewUserIssuedBooks(CustomArrayList<Book> allBooks, Faculty faculty) {
        System.out.println("\nYour Issued Books:");
        CustomArrayList<String> issuedBooks = faculty.getIssuedBooks();
        
        if (issuedBooks.size() == 0) {
            System.out.println("You don't have any books issued currently.");
            return;
        }
        
        TableFormatter table = new TableFormatter("No.", "Book Title", "Author", "Issue Date");
        int count = 0;
        
        for (int i = 0; i < issuedBooks.size(); i++) {
            String bookId = issuedBooks.get(i);
            for (int j = 0; j < allBooks.size(); j++) {
                Book book = allBooks.get(j);
                if (book.getId().equals(bookId)) {
                    count++;
                    table.addRow(
                        String.valueOf(count),
                        book.getTitle(),
                        book.getAuthor(),
                        "N/A" 
                    );
                }
            }
        }
        
        System.out.println(table.toString());
    }
    
    // View all books (for admin)
    public void viewAllBooks(CustomArrayList<Book> allBooks) {
        System.out.println("\nAll Books in Library:");
        
        if (allBooks.size() == 0) {
            System.out.println("No books in the library.");
            return;
        }
        
        TableFormatter table = new TableFormatter("No.", "ID", "Title", "Author", "Status");
        
        for (int i = 0; i < allBooks.size(); i++) {
            Book book = allBooks.get(i);
            table.addRow(
                String.valueOf(i + 1),
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.isIssued() ? "Issued" : "Available"
            );
        }
        
        System.out.println(table.toString());
    }
    
    // View detailed information about issued books for admins
    public void viewDetailedIssuedBooks(CustomArrayList<Book> allBooks, admin.AdminPanel adminPanel) {
        System.out.println("\nDetailed Issued Books Report:");
        int count = 0;
        
        TableFormatter table = new TableFormatter("No.", "Book ID", "Title", "Author", "Issued To", "User Type");
        
        for (int i = 0; i < allBooks.size(); i++) {
            Book book = allBooks.get(i);
            if (book.isIssued()) {
                count++;
                
                // Find who has this book
                String userInfo = "Unknown";
                String userType = "Unknown";
                
                for (int j = 0; j < issuedBooks.size(); j++) {
                    String record = issuedBooks.get(j);
                    if (record.endsWith(":" + book.getId())) {
                        String userId = record.substring(0, record.indexOf(":"));
                        
                        // Try to find if it's a student
                        Student student = adminPanel.searchStudent(userId);
                        if (student != null) {
                            userInfo = student.getName() + " (ID: " + student.getId() + ")";
                            userType = "Student";
                        } else {
                            // Try faculty if not a student
                            Faculty faculty = adminPanel.searchFaculty(userId);
                            if (faculty != null) {
                                userInfo = faculty.getName() + " (ID: " + faculty.getId() + ")";
                                userType = "Faculty";
                            }
                        }
                        break;
                    }
                }
                
                table.addRow(
                    String.valueOf(count),
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    userInfo,
                    userType
                );
            }
        }
        
        if (count == 0) {
            System.out.println("No books are currently issued.");
        } else {
            System.out.println(table.toString());
        }
    }
    
    // Search books by title or author
    public void searchBooks(CustomArrayList<Book> allBooks, String searchTerm) {
        System.out.println("\nSearch Results for: " + searchTerm);
        boolean found = false;
        
        TableFormatter table = new TableFormatter("No.", "ID", "Title", "Author", "Status");
        int count = 0;
        
        searchTerm = searchTerm.toLowerCase();
        
        for (int i = 0; i < allBooks.size(); i++) {
            Book book = allBooks.get(i);
            if (book.getTitle().toLowerCase().contains(searchTerm) || 
                book.getAuthor().toLowerCase().contains(searchTerm)) {
                count++;
                found = true;
                table.addRow(
                    String.valueOf(count),
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.isIssued() ? "Issued" : "Available"
                );
            }
        }
        
        if (!found) {
            System.out.println("No books found matching: " + searchTerm);
        } else {
            System.out.println(table.toString());
        }
    }
}