package model;

/**
 * Represents a book in the library system.
 */
public class Book implements Comparable<Book> {
    private String id;
    private String title;
    private String author;
    private boolean isIssued;

    /**
     * Constructor for Book
     * @param id Unique identifier for the book
     * @param title Title of the book
     * @param author Author of the book
     */
    public Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isIssued = false;
    }

    /**
     * Get the book ID
     * @return Book ID
     */
    public String getId() {
        return id;
    }

    /**
     * Get the book title
     * @return Book title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the book author
     * @return Book author
     */
    public String getAuthor() {
        return author;
    }
    
    /**
     * Set the book title
     * @param title New title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Set the book author
     * @param author New author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Check if the book is issued
     * @return true if issued, false otherwise
     */
    public boolean isIssued() {
        return isIssued;
    }

    /**
     * Set the issued status of the book
     * @param issued New issued status
     */
    public void setIssued(boolean issued) {
        isIssued = issued;
    }

    @Override
    public int compareTo(Book other) {
        return this.title.compareTo(other.title);
    }
}