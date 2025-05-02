package library;

import model.Book;
import ds.CustomQueue;
import ds.CustomBST;

public class LibrarySystem {
    private CustomQueue<String> issueQueue;
    private CustomBST<Book> books;

    public LibrarySystem() {
        issueQueue = new CustomQueue<>();
        books = new CustomBST<>();
    }

    public void addBook(Book book) {
        books.insert(book);
    }

    public void issueBook(String studentId, Book book) {
        if (!book.isIssued()) {
            book.setIssued(true);
            issueQueue.enqueue(studentId + ":" + book.getId());
        }
    }

    public void returnBook(Book book) {
        book.setIssued(false);
    }

    public boolean searchBook(Book book) {
        return books.search(book);
    }
}