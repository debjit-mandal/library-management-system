
import java.util.*;

class Book {
    private int bookId;
    private String title;
    private String author;
    private boolean isAvailable;

    public Book(int bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}

class Borrower {
    private int borrowerId;
    private String name;
    private String address;
    private List<Book> borrowedBooks;

    public Borrower(int borrowerId, String name, String address) {
        this.borrowerId = borrowerId;
        this.name = name;
        this.address = address;
        this.borrowedBooks = new ArrayList<>();
    }

    public int getBorrowerId() {
        return borrowerId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }

    public void returnBook(Book book) {
        borrowedBooks.remove(book);
    }
}

class Transaction {
    private int transactionId;
    private Book book;
    private Borrower borrower;
    private Date borrowDate;
    private Date dueDate;
    private Date returnDate;
    private boolean isReturned;

    public Transaction(int transactionId, Book book, Borrower borrower, Date borrowDate, Date dueDate) {
        this.transactionId = transactionId;
        this.book = book;
        this.borrower = borrower;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.isReturned = false;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public Book getBook() {
        return book;
    }

    public Borrower getBorrower() {
        return borrower;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }
}

class Library {
    private List<Book> books;
    private List<Borrower> borrowers;
    private List<Transaction> transactions;

    public Library() {
        books = new ArrayList<>();
        borrowers = new ArrayList<>();
        transactions = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void registerBorrower(Borrower borrower) {
        borrowers.add(borrower);
    }

    public void borrowBook(Book book, Borrower borrower) {
        if (book.isAvailable()) {
            book.setAvailable(false);
            Transaction transaction = new Transaction(
                    generateTransactionId(),
                    book,
                    borrower,
                    new Date(),
                    calculateDueDate()
            );
            transactions.add(transaction);
            borrower.borrowBook(book);
            System.out.println("Book borrowed successfully.");
        } else {
            System.out.println("The book is not available for borrowing.");
        }
    }

    public void returnBook(Book book, Borrower borrower) {
        Transaction transaction = findTransaction(book, borrower);
        if (transaction != null) {
            transaction.setReturned(true);
            transaction.setReturnDate(new Date());
            book.setAvailable(true);
            borrower.returnBook(book);
            System.out.println("Book returned successfully.");
        } else {
            System.out.println("No active transaction found for the given book and borrower.");
        }
    }

    public List<Book> searchBooks(String keyword) {
        List<Book> searchResults = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(keyword.toLowerCase())
                    || book.getAuthor().toLowerCase().contains(keyword.toLowerCase())) {
                searchResults.add(book);
            }
        }
        return searchResults;
    }

    public List<Transaction> getOverdueTransactions() {
        List<Transaction> overdueTransactions = new ArrayList<>();
        Date currentDate = new Date();
        for (Transaction transaction : transactions) {
            if (!transaction.isReturned() && transaction.getDueDate().before(currentDate)) {
                overdueTransactions.add(transaction);
            }
        }
        return overdueTransactions;
    }

    public void generateReport() {
        System.out.println("Books:");
        for (Book book : books) {
            System.out.println(book.getBookId() + " - " + book.getTitle() + " by " + book.getAuthor());
        }

        System.out.println("\nBorrowers:");
        for (Borrower borrower : borrowers) {
            System.out.println(borrower.getBorrowerId() + " - " + borrower.getName() + " - " + borrower.getAddress());
        }

        System.out.println("\nTransactions:");
        for (Transaction transaction : transactions) {
            System.out.println(transaction.getTransactionId()
                    + " - " + transaction.getBook().getTitle()
                    + " - " + transaction.getBorrower().getName()
                    + " - Borrowed: " + transaction.getBorrowDate()
                    + " - Due: " + transaction.getDueDate()
                    + " - Returned: " + (transaction.isReturned() ? transaction.getReturnDate() : "Not yet returned"));
        }
    }

    private int generateTransactionId() {
        UUID uuid = UUID.randomUUID(); //Generates random UUID
        return uuid.hashCode();
    }

    private Date calculateDueDate() {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DAY_OF_YEAR, 40);
        return date.getTime();
    }

    private Transaction findTransaction(Book book, Borrower borrower) {
        for (Transaction transaction : transactions) {
            if (!transaction.isReturned() && transaction.getBook().equals(book) && transaction.getBorrower().equals(borrower)) {
                return transaction;
            }
        }
        return null;
    }
}

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Library library = new Library();
        // Add books to the library
        Book book1 = new Book(1, "Merchant Of Venice", "William Shakespeare");
        Book book2 = new Book(2, "A Passage to India", "E. M. Forster");
        library.addBook(book1);
        library.addBook(book2);

        // Register borrowers
        Borrower borrower1 = new Borrower(1, "Ankit De", "KIIT KP 7C 1C-56");
        Borrower borrower2 = new Borrower(2, "Nilotpal Basu", "KIIT KP 7C 4C-158");
        library.registerBorrower(borrower1);
        library.registerBorrower(borrower2);

        // Borrow books
        library.borrowBook(book1, borrower1);
        library.borrowBook(book2, borrower2);
        // Return books
        library.returnBook(book1, borrower1);
        library.returnBook(book2, borrower2);

        // Search books
        List<Book> searchResults = library.searchBooks("India");
        for (Book book : searchResults) {
            System.out.println(book.getTitle() + " by " + book.getAuthor());
        }

        // Generate overdue reminders
        List<Transaction> overdueTransactions = library.getOverdueTransactions();
        for (Transaction transaction : overdueTransactions) {
            System.out.println("Overdue transaction: " + transaction.getTransactionId());
        }

        // Generate reports
        library.generateReport();
    }
}
