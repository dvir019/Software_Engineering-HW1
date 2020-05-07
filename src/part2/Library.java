package part2;

import java.util.ArrayList;

public class Library {

    private String address;
    private ArrayList<Book> books;

    /**
     * The constructor of the class.
     *
     * @param address The address of the library
     */
    public Library(String address) {
        this.address = address;
        books = new ArrayList<Book>();
    }

    /**
     * Prints the opening hours of the libraries.
     */
    public static void printOpeningHours() {
        System.out.println("Libraries are open daily from 9:00 to 17:00.");
    }

    /**
     * Adds a given book to the library.
     *
     * @param bookToAdd The book to add
     */
    public void addBook(Book bookToAdd) {
        books.add(bookToAdd);
    }

    /**
     * Prints the address of the library.
     */
    public void printAddress() {
        System.out.println(address);
    }

    /**
     * Tries to borrow a given book by his title, and prints the result.
     *
     * @param bookTitle The title of the book
     */
    public void borrowBook(String bookTitle) {
        boolean isBookInCatalog = false;

        for (Book book : books) {
            if (book.getTitle().equals(bookTitle)) {
                isBookInCatalog = true;
                if (book.isBorrowed()) {
                    System.out.println("Sorry, this book is already borrowed.");
                } else {
                    book.borrowed();
                    System.out.println("You successfully borrowed " + bookTitle);
                }
            }
        }
        if (!isBookInCatalog) {
            System.out.println("Sorry, this book is not in our catalog.");
        }
    }

    /**
     * Returns a given book by his title, and prints a message.
     *
     * @param bookTitle The title of the book
     */
    public void returnBook(String bookTitle) {
        for (Book book : books) {
            if (book.getTitle().equals(bookTitle)) {
                book.returned();
                System.out.println("You successfully returned " + bookTitle);
            }
        }
    }

    /**
     * Prints the available books in the library.
     */
    public void printAvailableBooks() {
        boolean noAvailableBooks = true;

        for (Book book : books) {
            if (!book.isBorrowed()) {
                noAvailableBooks = false;
                System.out.println(book.getTitle());
            }
        }

        if (noAvailableBooks) {
            System.out.println("No books in catalog.");
        }
    }

    /**
     * Tests the functions of the class.
     */
    public static void main(String[] args) {
        //Create two libraries
        Library firstLibrary = new Library("CS Faculty");
        Library secondLibrary = new Library("IE&M Faculty");
        //Add four books to the first library
        firstLibrary.addBook(new Book("Harry Potter"));
        firstLibrary.addBook(new Book("To Kill a Mockingbird"));
        firstLibrary.addBook(new Book("JAVA"));
        firstLibrary.addBook(new Book("Calculus For Dummies"));
        //Print opening hours and the addresses
        System.out.println("Library hours:");
        printOpeningHours();
        System.out.println();
        System.out.println("Library addresses:");
        firstLibrary.printAddress();
        secondLibrary.printAddress();
        System.out.println();
        //Try to borrow Harry Potter from both libraries
        System.out.println("Borrowing Harry Potter:");
        firstLibrary.borrowBook("Harry Potter");
        firstLibrary.borrowBook("Harry Potter");
        secondLibrary.borrowBook("Harry Potter");
        System.out.println();
        //Print the title of all available books from both libraries
        System.out.println("Books available in the first library");
        firstLibrary.printAvailableBooks();
        System.out.println();
        System.out.println("Books available in the second library");
        secondLibrary.printAvailableBooks();
        System.out.println();
        //Return Harry Potter
        System.out.println("Returning Harry Potter");
        firstLibrary.returnBook("Harry Potter");
        System.out.println();
        // Print the titles of available books from the first library
        System.out.println("Books available in the first library");
        firstLibrary.printAvailableBooks();
    }
}

