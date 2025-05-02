package library;

import model.Book;
import model.Student;
import ds.CustomQueue;
import ds.CustomBST;
import ds.CustomArrayList;

public class LibrarySystem {
    private CustomQueue<String> issueQueue;
    private CustomBST<Book> booksBST;
    private CustomArrayList<String> issuedBooks; // Format: "studentId:bookId"

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

    public boolean searchBook(Book book) {
        return booksBST.search(book);
    }
    
    public void viewAvailableBooks(CustomArrayList<Book> allBooks) {
        System.out.println("\nAvailable Books:");
        int count = 0;
        
        for (int i = 0; i < allBooks.size(); i++) {
            Book book = allBooks.get(i);
            if (!book.isIssued()) {
                System.out.println("\nBook #" + (++count) + ":");
                System.out.println("ID: " + book.getId());
                System.out.println("Title: " + book.getTitle());
                System.out.println("Author: " + book.getAuthor());
            }
        }
        
        if (count == 0) {
            System.out.println("No books available at the moment.");
        }
    }
    
    public void viewIssuedBooks(CustomArrayList<Book> allBooks) {
        System.out.println("\nIssued Books:");
        int count = 0;
        
        for (int i = 0; i < allBooks.size(); i++) {
            Book book = allBooks.get(i);
            if (book.isIssued()) {
                System.out.println("\nBook #" + (++count) + ":");
                System.out.println("ID: " + book.getId());
                System.out.println("Title: " + book.getTitle());
                System.out.println("Author: " + book.getAuthor());
                
                // Find the student who has this book
                for (int j = 0; j < issuedBooks.size(); j++) {
                    String record = issuedBooks.get(j);
                    if (record.endsWith(":" + book.getId())) {
                        String studentId = record.substring(0, record.indexOf(":"));
                        System.out.println("Issued to Student ID: " + studentId);
                        break;
                    }
                }
            }
        }
        
        if (count == 0) {
            System.out.println("No books are currently issued.");
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
        
        return null; // Book not found
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
        
        while (!issueQueue.isEmpty()) {
            String record = issueQueue.dequeue();
            System.out.println(count++ + ". " + record);
            tempQueue.enqueue(record);
        }
        
        // Restore queue
        while (!tempQueue.isEmpty()) {
            issueQueue.enqueue(tempQueue.dequeue());
        }
    }
}