package data;

import model.Book;
import ds.CustomArrayList;

public class BookData {
    
    /**
     * Load sample books into the library system
     * @param allBooks The CustomArrayList to load books into
     */
    public static void loadSampleBooks(CustomArrayList<Book> allBooks) {
        // Computer Science books
        allBooks.add(new Book("B001", "Introduction to Algorithm", "Thomas H. Cormen"));
        allBooks.add(new Book("B002", "Clean Code", "Robert C. Martin"));
        allBooks.add(new Book("B003", "Design Patterns", "Erich Gamma"));
        allBooks.add(new Book("B004", "The Pragmatic Programmer", "Andrew Hunt"));
        allBooks.add(new Book("B005", "Code Complete", "Steve McConnell"));
        
        // Mathematics books
        allBooks.add(new Book("B006", "Calculus: Early Transcendentals", "James Stewart"));
        allBooks.add(new Book("B007", "Linear Algebra", "Gilbert Strang"));
        allBooks.add(new Book("B008", "Discrete Mathematics", "Richard Johnsonbaugh"));
        
        // Physics books
        allBooks.add(new Book("B009", "Concepts of Physics Vol. 1", "H.C. Verma"));
        allBooks.add(new Book("B010", "University Physics", "Hugh D. Young"));
        
        // Literature books
        allBooks.add(new Book("B011", "To Kill a Mockingbird", "Harper Lee"));
        allBooks.add(new Book("B012", "Pride and Prejudice", "Jane Austen"));
        allBooks.add(new Book("B013", "The Great Gatsby", "F. Scott Fitzgerald"));
        
        // History books
        allBooks.add(new Book("B014", "Sapiens: A Brief History of Humankind", "Yuval Noah Harari"));
        allBooks.add(new Book("B015", "A People's History", "Howard Zinn"));
        
        System.out.println("Sample books loaded: " + allBooks.size() + " books");
    }
}