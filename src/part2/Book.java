package part2;

public class Book {

    // Fields
    String title;
    boolean borrowed;


    // Constructor

    /**
     * The constructor of the class.
     *
     * @param bookTitle The title of the book
     */
    public Book(String bookTitle) {
        title = bookTitle;
        borrowed = false;
    }


    // Methods

    /**
     * Marks the book as borrowed
     */
    public void borrowed() {
        borrowed = true;
    }

    /**
     * Marks the book as available
     */
    public void returned() {
        borrowed = false;
    }

    //Returns true if the book is borrowed, false otherwise

    /**
     * Returns whether or not the book is borrowed.
     *
     * @return True if the book is borrowed, false otherwise
     */
    public boolean isBorrowed() {
        return borrowed;
    }

    /**
     * Gets the title of the book
     *
     * @return The title of the book
     */
    public String getTitle() {
        return title;
    }

    /**
     * Tests the methods of the class.
     */
    public static void main(String[] args) {
        //Small test on the Book class
        Book example = new Book("Harry Potter");
        System.out.println("Title (Should be Harry Potter): " + example.getTitle());
        System.out.println("Borrowed? (should be false:) " + example.isBorrowed());
        example.borrowed();
        System.out.println("Borrowed? (should be true:) " + example.isBorrowed());
        example.returned();
        System.out.println("Borrowed? (should be false:) " + example.isBorrowed());
    }


}
