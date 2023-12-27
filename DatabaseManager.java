package LibraryManager;

import java.io.File;
import java.sql.*;
import java.util.*;

import Enums.*;
import Item.Factory.AbstractFactory;
import User.Client.Customer;
import User.Employee.Employee;
import Item.Book.*;
import Item.Comic.*;
import User.Employee.EmployeeMaker;
import User.Employee.LibrarianEmpBuilder;
import User.Employee.ManagerEmpBuilder;

public class DatabaseManager {
    public static final String DB_NAME = "library.db";
    public static final String URL = "jdbc:sqlite:" + System.getProperty("user.dir") + File.separator + DB_NAME;
    private String  currentUserID;
    private Scanner scanner = new Scanner(System.in);
    public DatabaseManager(){
        initializeDatabase();
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
    public void initializeDatabase() {
        createTableBooks();
        createTableComics();
        createTableCustomers();
        createTableEmployees();
        createTableBorrowedBooks();
        createTableReservedBooks();
        createTableBorrowedComics();
        createTableReservedComics();
        insertSampleData();
    }

    // ***************** Tables *******************
    private void createTableBooks() {
        String sqlCreateBooks =
                "CREATE TABLE IF NOT EXISTS books (" +
                        "id TEXT PRIMARY KEY, " +
                        "title TEXT NOT NULL, " +
                        "author TEXT NOT NULL, " +
                        "publicDate TEXT NOT NULL, " +
                        "genre TEXT NOT NULL, " +
                        "language TEXT NOT NULL, " +
                        "state TEXT NOT NULL" +
                        ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlCreateBooks);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private void createTableComics() {
        String sqlCreateComics =
                "CREATE TABLE IF NOT EXISTS comics (" +
                        "id TEXT PRIMARY KEY, " +
                        "title TEXT NOT NULL, " +
                        "author TEXT NOT NULL, " +
                        "illustrator TEXT NOT NULL, " +
                        "publicDate TEXT NOT NULL, " +
                        "genre TEXT NOT NULL, " +
                        "language TEXT NOT NULL, " +
                        "state TEXT NOT NULL" +
                        ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlCreateComics);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private void createTableCustomers() {
        String sqlCreateCustomers =
                "CREATE TABLE IF NOT EXISTS customers (" +
                        "id TEXT PRIMARY KEY, " +
                        "birthdate TEXT NOT NULL, " +
                        "fname TEXT NOT NULL, " +
                        "lname TEXT NOT NULL, " +
                        "password TEXT NOT NULL, " +
                        "state TEXT NOT NULL" +
                        ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlCreateCustomers);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private void createTableEmployees() {
        String sqlCreateCustomers =
                "CREATE TABLE IF NOT EXISTS employees (" +
                        "id TEXT PRIMARY KEY, " +
                        "birthdate TEXT NOT NULL, " +
                        "fname TEXT NOT NULL, " +
                        "lname TEXT NOT NULL, " +
                        "password TEXT NOT NULL, " +
                        "state TEXT NOT NULL, " +
                        "role TEXT NOT NULL, " +
                        "yearsOfExp INTEGER NOT NULL" +
                        ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlCreateCustomers);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private void createTableBorrowedBooks() {
        String sqlCreateBorrowedBooks =
                "CREATE TABLE IF NOT EXISTS borrowed_books (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "book_id TEXT NOT NULL, " +
                        "customer_id TEXT NOT NULL, " +
                        "borrow_date DATE NOT NULL, " +
                        "return_date DATE, " +
                        "FOREIGN KEY (book_id) REFERENCES books (id), " +
                        "FOREIGN KEY (customer_id) REFERENCES customers (id)" +
                        ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlCreateBorrowedBooks);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private void createTableBorrowedComics() {
        String sqlCreateBorrowedComics =
                "CREATE TABLE IF NOT EXISTS borrowed_comics (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "comic_id TEXT NOT NULL, " +
                        "customer_id TEXT NOT NULL, " +
                        "borrow_date DATE NOT NULL, " +
                        "return_date DATE, " +
                        "FOREIGN KEY (comic_id) REFERENCES comics (id), " +
                        "FOREIGN KEY (customer_id) REFERENCES customers (id)" +
                        ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlCreateBorrowedComics);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private void createTableReservedBooks() {
        String sqlCreateReservedBooks =
                "CREATE TABLE IF NOT EXISTS reserved_books (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "book_id TEXT NOT NULL, " +
                        "customer_id TEXT NOT NULL, " +
                        "reservation_date DATE NOT NULL, " +
                        "FOREIGN KEY (book_id) REFERENCES books (id), " +
                        "FOREIGN KEY (customer_id) REFERENCES customers (id)" +
                        ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlCreateReservedBooks);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private void createTableReservedComics() {
        String sqlCreateReservedComics =
                "CREATE TABLE IF NOT EXISTS reserved_comics (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "comic_id TEXT NOT NULL, " +
                        "customer_id TEXT NOT NULL, " +
                        "reservation_date DATE NOT NULL, " +
                        "FOREIGN KEY (comic_id) REFERENCES comics (id), " +
                        "FOREIGN KEY (customer_id) REFERENCES customers (id)" +
                        ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlCreateReservedComics);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // ********************** Users & Login *******************
    public Customer getCustomerByID(String id) {
        String query = "SELECT * FROM customers WHERE id = ?";

        try(Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, id);
            try(ResultSet rs = pstmt.executeQuery()) {
                if(rs.next())
                {
                    Customer customer = new Customer();
                    customer.setId(rs.getString("id"));
                    customer.setBirthdate(rs.getString("birthdate"));
                    customer.setFirstName(rs.getString("fname"));
                    customer.setLastName(rs.getString("lname"));
                    customer.setPassword(rs.getString("password"));
                    if (UserState.ACTIVE.toString().equalsIgnoreCase(rs.getString("state"))){
                        customer.setState(UserState.ACTIVE);
                    }
                    else customer.setState(UserState.INACTIVE);

                    return customer;
                }
            }
        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Employee getEmployeeByID(String id) {
        String query = "SELECT * FROM employees WHERE id = ?";

        try(Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, id);
            try(ResultSet rs = pstmt.executeQuery()) {
                if(rs.next())
                {
                    Employee employee = new Employee();
                    employee.setId(rs.getString("id"));
                    employee.setBirthdate(rs.getString("birthdate"));
                    employee.setFirstName(rs.getString("fname"));
                    employee.setLastName(rs.getString("lname"));
                    employee.setPassword(rs.getString("password"));
                    if (UserState.ACTIVE.toString().equalsIgnoreCase(rs.getString("state"))){
                        employee.setState(UserState.ACTIVE);
                    }
                    else employee.setState(UserState.INACTIVE);
                    if (Role.LIBRARIAN.toString().equalsIgnoreCase(rs.getString("role"))){
                        employee.setRole(Role.LIBRARIAN);
                    }
                    else employee.setRole(Role.MANAGER);
                    employee.setYearsOfExp(rs.getInt("yearsOfExp"));
                    return employee;
                }
            }
        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }
    //Perform login
    public boolean performCustomerLogin() {
        System.out.print("Enter ID: ");
        String id = "CST" + scanner.nextLine(); // Automatically concatenate to the ID
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Customer customer = getCustomerByID(id);
        if (customer != null && customer.getPassword().equals(password)) {
            System.out.println("\nLogged in successfully as " + customer.getLastName() + ", " +
                    customer.getFirstName());
            currentUserID = customer.getId();
            return true;
        } else {
            System.out.println("Invalid credentials or not authorized as customer.\n");
            return false;
        }
    }
    public boolean performEmployeeLogin() {
        System.out.print("Enter ID: ");
        String id = "EMP" + scanner.nextLine(); // Automatically concatenate "EMP" to the ID
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Employee employee = getEmployeeByID(id);
        if (employee != null && employee.getPassword().equals(password)) {
            System.out.println("\nLogged in successfully as " + employee.getLastName() + ", " +
                    employee.getFirstName());
            currentUserID = employee.getId();
            return true;
        } else {
            System.out.println("Invalid credentials or not authorized as employee.\n");
            return false;
        }
    }
    //Method to return the role of the currently logged-in employee
    public String getCurrentEmployeeRole() {
        String query = "SELECT role FROM employees WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            //according to currentUserID to retrieve the role the current user
            pstmt.setString(1, this.currentUserID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null; // Default return if no role found or error
    }



    // ************************* Items ************************

    // Display all items
    public void getAllBooks() {
        String query = "SELECT * FROM books";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n********** All Books **********\n");
            while(rs.next())
            {
                String id = rs.getString("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String pubDate = rs.getString("publicDate");
                String genre = rs.getString("genre");
                String language = rs.getString("language");
                String state = rs.getString("state");

                System.out.println("\tID: " + id + "\n\tTitle: " + title + "\n\tAuthor: " + author);
                System.out.println("\tPublication date: " + pubDate + "\n\tGenre: " + genre);
                System.out.println("\tLanguage: " + language + "\n\tState: " + state + "\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void getAllComics() {
        String query = "SELECT * FROM comics";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n********** All Comics **********\n");
            while(rs.next())
            {
                String id = rs.getString("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String illustrator = rs.getString("illustrator");
                String pubDate = rs.getString("publicDate");
                String genre = rs.getString("genre");
                String language = rs.getString("language");
                String state = rs.getString("state");

                System.out.println("\tID: " + id + "\n\tTitle: " + title + "\n\tAuthor: " + author);
                System.out.println("\tIllustrator: " + illustrator + "\n\tPublication date: " + pubDate);
                System.out.println("\tGenre: " + genre + "\n\tLanguage: " + language + "\n\tState: " + state + "\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Display all users
    public void getAllEmployees() {
        String query = "SELECT * FROM employees";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n********** All Employees **********\n");
            while(rs.next())
            {
                String id = rs.getString("id");
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                String bdate = rs.getString("birthdate");
                String state = rs.getString("state");
                String role = rs.getString("role");
                String yoe = rs.getString("yearsOfExp");

                System.out.println("\tID: " + id + "\n\tFirst name: " + fname + "\n\tLast Name: " + lname);
                System.out.println("\tBirthdate: " + bdate + "\n\tState: " + state);
                System.out.println("\tRole: " + role + "\n\tYears of Experience: " + yoe + "\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void getAllCustomers() {
        String query = "SELECT * FROM customers";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n********** All Customers **********\n");
            while(rs.next())
            {
                String id = rs.getString("id");
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                String bdate = rs.getString("birthdate");
                String state = rs.getString("state");

                System.out.println("\tID: " + id + "\n\tFirst name: " + fname + "\n\tLast Name: " + lname);
                System.out.println("\tBirthdate: " + bdate + "\n\tState: " + state + "\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Display available items
    public void displayAvailableBooks() {
        String query = "SELECT * FROM books WHERE state = 'AVAILABLE'";
        try(Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n********** Available Books **********\n");
            while(rs.next())
            {
                String id = rs.getString("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String pubDate = rs.getString("publicDate");
                String genre = rs.getString("genre");
                String language = rs.getString("language");
                String state = rs.getString("state");

                System.out.println("\tID: " + id + "\n\tTitle: " + title + "\n\tAuthor: " + author);
                System.out.println("\tPublication date: " + pubDate + "\n\tGenre: " + genre);
                System.out.println("\tLanguage: " + language + "\n\tState: " + state + "\n");
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    public void displayAvailableComics() {
        String query = "SELECT * FROM comics WHERE state = 'AVAILABLE'";
        try(Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n********** Available Comics **********\n");
            while(rs.next())
            {
                String id = rs.getString("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String illustrator = rs.getString("illustrator");
                String pubDate = rs.getString("publicDate");
                String genre = rs.getString("genre");
                String language = rs.getString("language");
                String state = rs.getString("state");

                System.out.println("\tID: " + id + "\n\tTitle: " + title + "\n\tAuthor: " + author);
                System.out.println("\tIllustrator: " + illustrator + "\tPublication date: " + pubDate);
                System.out.println("\tGenre: " + genre + "\tLanguage: " + language + "\n\tState: " + state + "\n");
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // Display borrowed items
    public boolean displayBorrowedBooks() {
        String query = "SELECT b.id as book_id, b.title, b.author FROM books b " +
                "JOIN borrowed_books bb ON b.id = bb.book_id " +
                "WHERE bb.customer_id = ? AND bb.return_date IS NULL";
        boolean hasBorrowedBooks = false;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, this.currentUserID);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\n********** Books currently borrowed by User ID " + this.currentUserID + " **********\n");
            while (rs.next()) {
                hasBorrowedBooks = true;
                System.out.println("\tBook ID: " + rs.getString("book_id") + "\n\tTitle: " + rs.getString("title") +
                        "\n\tAuthor: " + rs.getString("author"));
            }

            if (!hasBorrowedBooks) {
                System.out.println("\tNo books currently borrowed by User ID " + this.currentUserID + ".\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hasBorrowedBooks;
    }
    public boolean displayBorrowedComics() {
        String query = "SELECT c.id as comic_id, c.title, c.author FROM comics c " +
                "JOIN borrowed_comics bb ON c.id = bb.comic_id " +
                "WHERE bb.customer_id = ? AND bb.return_date IS NULL";
        boolean hasBorrowedComics = false;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, this.currentUserID);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\n********** Comics currently borrowed by Customer ID " + this.currentUserID + " **********\n");
            while (rs.next()) {
                hasBorrowedComics = true;
                System.out.println("\tComic ID: " + rs.getString("comic_id") +
                        "\n\tTitle: " + rs.getString("title") +
                        "\n\tAuthor: " + rs.getString("author"));
            }

            if (!hasBorrowedComics) {
                System.out.println("\tNo comics currently borrowed by Customer ID " + this.currentUserID + ".\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hasBorrowedComics;
    }


    // Add items of the library (books and comis)
    public void addBook() {
        System.out.println("\n********** Adding Book **********\n");

        Book book = null;

        System.out.println("Enter title:");
        String title = scanner.nextLine();

        System.out.println("Enter author:");
        String author = scanner.nextLine();

        System.out.println("Enter publication date:");
        String pubDate = scanner.nextLine();

        boolean isValid = false;
        while (!isValid) {
            System.out.println("---------Choose a section--------\n");
            System.out.println("\t1. English Literature");
            System.out.println("\t2. French Literature");
            System.out.println("\nEnter an option: ");
            String sectionChoice = scanner.nextLine(); // Read the line once for the choice

            if ("1".equals(sectionChoice)) {
                book = AbstractFactory.factory(Genre.EnglishLiterature).createBook(
                        title, author, pubDate);
                isValid = true;
            } else if ("2".equals(sectionChoice)) {
                book = AbstractFactory.factory(Genre.FrenchLiterature).createBook(
                        title, author, pubDate);
                isValid = true;
            } else {
                System.out.println("Invalid decision.");
            }
        }

        if (book != null) {
            insertBook(book);
        }
    }

    public void addComic() {
        System.out.println("\n********** Adding Comic **********\n");

        Comic comic = null;

        System.out.println("Enter title:");
        String title = scanner.nextLine();

        System.out.println("Enter author:");
        String author = scanner.nextLine();

        System.out.println("Enter illustrator:");
        String illustrator = scanner.nextLine();

        System.out.println("Enter publication date:");
        String pubDate = scanner.nextLine();

        boolean isValid = false;
        while (!isValid) {
            System.out.println("---------Choose a section--------\n");
            System.out.println("\t1. English Literature");
            System.out.println("\t2. French Literature");
            System.out.println("\nEnter an option: ");
            String sectionChoice = scanner.nextLine(); // Read the line once for the choice

            if ("1".equals(sectionChoice)) {
                comic = AbstractFactory.factory(Genre.EnglishLiterature).createComic(
                        title, author, illustrator, pubDate);
                isValid = true;
            } else if ("2".equals(sectionChoice)) {
                comic = AbstractFactory.factory(Genre.FrenchLiterature).createComic(
                        title, author, illustrator, pubDate);
                isValid = true;
            } else {
                System.out.println("Invalid decision.");
            }
        }

        if (comic != null) {
            insertComic(comic);
        }
    }

    public void insertBook(Book book) {
        String checkQuery = "SELECT id FROM books WHERE id = ?";
        String insertSql = "INSERT INTO books(id, title, author, publicDate, genre, language, state) VALUES(?,?,?,?,?,?,?)";

        try(Connection conn = getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            checkStmt.setString(1, book.getId());
            ResultSet checkRs = checkStmt.executeQuery();

            // If book ID does not exist, insert new book
            if (!checkRs.next()) {
                insertStmt.setString(1, book.getId());
                insertStmt.setString(2, book.getTitle());
                insertStmt.setString(3, book.getAuthorName());
                insertStmt.setString(4, book.getPublicationDate());
                insertStmt.setString(5, book.getGenre().toString());
                insertStmt.setString(6, book.getLanguage().toString());
                insertStmt.setString(7, book.getItemState().toString());

                int affectedRows = insertStmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Data inserted successfully!");
                } else {
                    System.out.println("Failed to insert data.");
                }
            } else {
                System.out.println("Book ID " + book.getId() + " already existes.");
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertComic(Comic comic) {
        String checkQuery = "SELECT id FROM comics WHERE id = ?";
        String insertSql = "INSERT INTO comics(id, title, author, illustrator, publicDate, genre, language, state) VALUES(?,?,?,?,?,?,?,?)";

        try(Connection conn = getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            // Check if comic ID already exists in the database
            checkStmt.setString(1, comic.getId());
            ResultSet checkRs = checkStmt.executeQuery();

            if (!checkRs.next()) { // If the comic ID does not exist, proceed to insert
                insertStmt.setString(1, comic.getId());
                insertStmt.setString(2, comic.getTitle());
                insertStmt.setString(3, comic.getAuthorName());
                insertStmt.setString(4, comic.getIllustratorName());
                insertStmt.setString(5, comic.getPublicationDate());
                insertStmt.setString(6, comic.getGenre().toString());
                insertStmt.setString(7, comic.getLanguage().toString());
                insertStmt.setString(8, comic.getItemState().toString());

                int affectedRows = insertStmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Comic inserted successfully!");
                } else {
                    System.out.println("Failed to insert comic data.");
                }
            } else { // If the comic ID exists, inform the user
                System.out.println("Comic with ID " + comic.getId() + " already exists.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\nEnsure you are not repeating an ID number!");
        }
    }

    // Search items (books and comics)
    public List<Book> searchBooks(String searchQuery) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR id LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Preparing the statement with search query for title, author, and id
            pstmt.setString(1, "%" + searchQuery + "%");
            pstmt.setString(2, "%" + searchQuery + "%");
            pstmt.setString(3, "%" + searchQuery + "%");

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getString("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthorName(rs.getString("author"));
                book.setPublicationDate(rs.getString("publicDate"));
                if (Genre.EnglishLiterature.toString().equalsIgnoreCase(rs.getString("genre"))){
                    book.setGenre(Genre.EnglishLiterature);
                }
                else book.setGenre(Genre.FrenchLiterature);
                if (Language.EN.toString().equalsIgnoreCase(rs.getString("language"))){
                    book.setLanguage(Language.EN);
                }
                else book.setLanguage(Language.EN);
                if (ItemState.AVAILABLE.toString().equalsIgnoreCase(rs.getString("state"))){
                    book.setItemState(ItemState.AVAILABLE);
                }
                else book.setItemState(ItemState.AVAILABLE);
                books.add(book);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return books;
    }
    public List<Comic> searchComics(String searchQuery) {
        List<Comic> comics = new ArrayList<>();
        String sql = "SELECT * FROM comics WHERE title LIKE ? OR author LIKE ? OR id LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Preparing the statement with search query for title, author, and id
            pstmt.setString(1, "%" + searchQuery + "%");
            pstmt.setString(2, "%" + searchQuery + "%");
            pstmt.setString(3, "%" + searchQuery + "%");

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Comic comic = new Comic();
                comic.setId(rs.getString("id"));
                comic.setTitle(rs.getString("title"));
                comic.setAuthorName(rs.getString("author"));
                comic.setIllustratorName(rs.getString("illustrator"));
                comic.setPublicationDate(rs.getString("publicDate"));
                if (Genre.EnglishLiterature.toString().equalsIgnoreCase(rs.getString("genre"))){
                    comic.setGenre(Genre.EnglishLiterature);
                }
                else comic.setGenre(Genre.FrenchLiterature);
                if (Language.EN.toString().equalsIgnoreCase(rs.getString("language"))){
                    comic.setLanguage(Language.EN);
                }
                else comic.setLanguage(Language.EN);
                if (ItemState.AVAILABLE.toString().equalsIgnoreCase(rs.getString("state"))){
                    comic.setItemState(ItemState.AVAILABLE);
                }
                else comic.setItemState(ItemState.AVAILABLE);
                comics.add(comic);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return comics;
    }
    public void performSearchBooks() {
        System.out.print("\nEnter search term for book (ID, Title, or Author): ");
        String searchQuery = scanner.nextLine();
        List<Book> foundBooks = searchBooks(searchQuery);
        if (foundBooks.isEmpty())
        {
            System.out.println("No books found with the given search criteria.\n");
        }
        else
        {
            System.out.println("\n********** Books Match With Search Criteria **********\n");
            for (Book book : foundBooks) {
                System.out.println("\tID: " + book.getId() +
                        "\n\tTitle: " + book.getTitle() +
                        "\n\tAuthor: " + book.getAuthorName() +
                        "\n\tPublication date: " + book.getPublicationDate() +
                        "\n\tGenre: " + book.getGenre().toString() +
                        "\n\tLanguage: " + book.getLanguage().toString() +
                        "\n\tState: " + book.getItemState().toString() + "\n");
            }
        }

    }
    public void performSearchComics() {
        System.out.print("\nEnter search term for comic (ID, Title, or Author): ");
        String searchQuery = scanner.nextLine();
        List<Comic> foundComics = searchComics(searchQuery);
        if (foundComics.isEmpty())
        {
            System.out.println("No comics found with the given search criteria.\n");
        }
        else
        {
            System.out.println("\n********** Comics Match With Search Criteria **********\n");
            for (Comic comic : foundComics) {
                System.out.println("\tID: " + comic.getId() +
                        "\n\tTitle: " + comic.getTitle() +
                        "\n\tAuthor: " + comic.getAuthorName() +
                        "\n\tIllustrator: " + comic.getIllustratorName() +
                        "\n\tPublication date: " + comic.getPublicationDate() +
                        "\n\tGenre: " + comic.getGenre().toString() +
                        "\n\tLanguage: " + comic.getLanguage().toString() +
                        "\n\tState: " + comic.getItemState().toString() + "\n");
            }
        }

    }

    // Search users

    public List<Employee> searchEmployees(String searchQuery) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE id LIKE ? OR fname LIKE ? OR lname LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Preparing the statement with search query for title, author, and id
            pstmt.setString(1, "%" + searchQuery + "%");
            pstmt.setString(2, "%" + searchQuery + "%");
            pstmt.setString(3, "%" + searchQuery + "%");

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setId(rs.getString("id"));
                employee.setFirstName(rs.getString("fname"));
                employee.setLastName(rs.getString("lname"));
                employee.setBirthdate(rs.getString("birthdate"));
                if (UserState.ACTIVE.toString().equalsIgnoreCase(rs.getString("state"))){
                    employee.setState(UserState.ACTIVE);
                }
                else employee.setState(UserState.INACTIVE);
                if (Role.LIBRARIAN.toString().equalsIgnoreCase(rs.getString("role"))){
                    employee.setRole(Role.LIBRARIAN);
                }
                else employee.setRole(Role.MANAGER);
                employee.setYearsOfExp(Integer.parseInt(rs.getString("yearsOfExp")));

                employees.add(employee);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return employees;
    }

    public void performSearchEmployees() {
        System.out.print("\nEnter search term for employee (ID, First name, or Last name): ");
        String searchQuery = scanner.nextLine();
        List<Employee> foundEmployees = searchEmployees(searchQuery);
        if (foundEmployees.isEmpty())
        {
            System.out.println("No employees found with the given search criteria.\n");
        }
        else
        {
            System.out.println("\n********** Employees Match With Search Criteria **********\n");
            for (Employee e : foundEmployees) {
                System.out.println("\tID: " + e.getId() + "\n\tFirst name: " + e.getFirstName() + "\n\tLast Name: " + e.getLastName());
                System.out.println("\tBirthdate: " + e.getBirthdate() + "\n\tState: " + e.getState());
                System.out.println("\tRole: " + e.getRole() + "\n\tYears of Experience: " + e.getYearsOfExp() + "\n");
            }
        }

    }
    public List<Customer> searchCustomers(String searchQuery) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE id LIKE ? OR fname LIKE ? OR lname LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Preparing the statement with search query for title, author, and id
            pstmt.setString(1, "%" + searchQuery + "%");
            pstmt.setString(2, "%" + searchQuery + "%");
            pstmt.setString(3, "%" + searchQuery + "%");

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getString("id"));
                customer.setFirstName(rs.getString("fname"));
                customer.setLastName(rs.getString("lname"));
                customer.setBirthdate(rs.getString("birthdate"));
                if (UserState.ACTIVE.toString().equalsIgnoreCase(rs.getString("state"))){
                    customer.setState(UserState.ACTIVE);
                }
                else customer.setState(UserState.INACTIVE);

                customers.add(customer);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return customers;
    }

    public void performSearchCustomers() {
        System.out.print("\nEnter search term for customer (ID, First name, or Last name): ");
        String searchQuery = scanner.nextLine();
        List<Customer> foundCustomers = searchCustomers(searchQuery);
        if (foundCustomers.isEmpty())
        {
            System.out.println("No customers found with the given search criteria.\n");
        }
        else
        {
            System.out.println("\n********** Customers Match With Search Criteria **********\n");
            for (Customer c : foundCustomers) {
                System.out.println("\tID: " + c.getId() + "\n\tFirst name: " + c.getFirstName() + "\n\tLast Name: " + c.getLastName());
                System.out.println("\tBirthdate: " + c.getBirthdate() + "\n\tState: " + c.getState() + "\n");
            }
        }

    }
    // Reserve items (books and comics)
    public void reserveBook(String bookId) {
        String checkQuery = "SELECT state FROM books WHERE id = ?";
        String updateQuery = "UPDATE books SET state = 'UNAVAILABLE' WHERE id = ? AND state = 'AVAILABLE'";
        String insertQuery = "INSERT INTO reserved_books (book_id, customer_id, reservation_date) VALUES (?, ?, CURRENT_DATE)";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                 PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                 PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

                checkStmt.setString(1, bookId);
                try (ResultSet checkRs = checkStmt.executeQuery()) {
                    if (!checkRs.next() || !"AVAILABLE".equalsIgnoreCase(checkRs.getString("state"))) {
                        System.out.println("Book ID " + bookId + " is not available for reservation.");
                        return;
                    }
                }

                updateStmt.setString(1, bookId);
                int rowsAffected = updateStmt.executeUpdate();
                if (rowsAffected == 1) {
                    insertStmt.setString(1, bookId);
                    insertStmt.setString(2, currentUserID);
                    insertStmt.executeUpdate();
                    conn.commit();
                    System.out.println("\tBook ID " + bookId + " has been successfully reserved.");
                } else {
                    System.out.println("Could not reserve the book. It may already be reserved or borrowed.");
                    conn.rollback();
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void reserveComic(String comicId) {
        String checkQuery = "SELECT state FROM comics WHERE id = ?";
        String updateQuery = "UPDATE comics SET state = 'UNAVAILABLE' WHERE id = ? AND state = 'AVAILABLE'";
        String insertQuery = "INSERT INTO reserved_comics (comic_id, customer_id, reservation_date) VALUES (?, ?, CURRENT_DATE)";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                 PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                 PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

                checkStmt.setString(1, comicId);
                try (ResultSet checkRs = checkStmt.executeQuery()) {
                    if (!checkRs.next() || !"AVAILABLE".equals(checkRs.getString("state"))) {
                        System.out.println("Comic ID " + comicId + " is not available for reservation.");
                        return;
                    }
                }

                updateStmt.setString(1, comicId);
                int rowsAffected = updateStmt.executeUpdate();
                if (rowsAffected == 1) {
                    insertStmt.setString(1, comicId);
                    insertStmt.setString(2, currentUserID);
                    insertStmt.executeUpdate();
                    conn.commit();
                    System.out.println("\tComic ID " + comicId + " has been successfully reserved.");
                } else {
                    System.out.println("Could not reserve the comic. It may already be reserved or borrowed.");
                    conn.rollback();
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void performBookReservation() {
        boolean continueReservation = true;
        while (continueReservation) {
            displayAvailableBooks(); // Only display books that are not borrowed or reserved
            System.out.print("\nEnter the ID of the book you want to reserve, or type 'n' to stop: ");
            String input = scanner.nextLine().trim();

            if ("n".equalsIgnoreCase(input)) {
                continueReservation = false;
                System.out.println("\nExiting reservation process...\n");
            } else {
                String bookId = input;
                reserveBook(bookId);

                System.out.print("Do you want to reserve another book? (y/n): ");
                String answer = scanner.nextLine().trim();
                if (!"y".equalsIgnoreCase(answer)) {
                    continueReservation = false;
                    System.out.println("\nExiting reservation process...\n");
                }
            }
        }
    }
    public void performComicReservation() {
        boolean continueReservation = true;
        while (continueReservation) {
            displayAvailableComics(); // Only display comics that are not borrowed or reserved
            System.out.print("\nEnter the ID of the comic you want to reserve, or type 'n' to stop: ");
            String input = scanner.nextLine().trim();

            if ("n".equalsIgnoreCase(input)) {
                continueReservation = false;
                System.out.println("\nExiting reservation process...\n");
            } else {
                String comicId = input;
                reserveComic(comicId);

                System.out.print("Do you want to reserve another comic? (y/n): ");
                String answer = scanner.nextLine().trim();
                if (!"y".equalsIgnoreCase(answer)) {
                    continueReservation = false;
                    System.out.println("\nExiting reservation process...\n");
                }
            }
        }
    }

    // Borrow items (books and comics)
    public void borrowBook(String bookId, String customerId) {
        String checkQuery = "SELECT state FROM books WHERE id = ?";
        String updateQuery = "UPDATE books SET state = 'UNAVAILABLE' WHERE id = ? AND state = 'AVAILABLE'";
        String insertQuery = "INSERT INTO borrowed_books (book_id, customer_id, borrow_date) VALUES (?, ?, CURRENT_DATE)";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                 PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                 PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

                // Check if the book is available
                checkStmt.setString(1, bookId);
                try (ResultSet checkRs = checkStmt.executeQuery()) {
                    if (!checkRs.next() || !"AVAILABLE".equals(checkRs.getString("state"))) {
                        System.out.println("Book ID " + bookId + " is not available for borrowing.");
                        return;
                    }
                }

                // Update book state to 'Borrowed'
                updateStmt.setString(1, bookId);
                if (updateStmt.executeUpdate() == 1) {
                    // Insert record into borrowed_books
                    insertStmt.setString(1, bookId);
                    insertStmt.setString(2, customerId);
                    insertStmt.executeUpdate();
                    conn.commit();
                    System.out.println("\tBook ID " + bookId + " has been successfully borrowed by Customer ID " + customerId);
                } else {
                    System.out.println("Could not borrow the book. It may already be reserved or borrowed.");
                    conn.rollback();
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void borrowComic(String comicId, String customerId) {
        String checkQuery = "SELECT state FROM comics WHERE id = ?";
        String updateQuery = "UPDATE comics SET state = 'UNAVAILABLE' WHERE id = ? AND state = 'AVAILABLE'";
        String insertQuery = "INSERT INTO borrowed_comics (comic_id, customer_id, borrow_date) VALUES (?, ?, CURRENT_DATE)";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                 PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                 PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

                // Check if the comic is available
                checkStmt.setString(1, comicId);
                try (ResultSet checkRs = checkStmt.executeQuery()) {
                    if (!checkRs.next() || !"AVAILABLE".equals(checkRs.getString("state"))) {
                        System.out.println("Comic ID " + comicId + " is not available for borrowing.");
                        return;
                    }
                }

                // Update comic state to 'Borrowed'
                updateStmt.setString(1, comicId);
                if (updateStmt.executeUpdate() == 1) {
                    // Insert record into borrowed_comics
                    insertStmt.setString(1, comicId);
                    insertStmt.setString(2, customerId);
                    insertStmt.executeUpdate();
                    conn.commit();
                    System.out.println("\tComic ID " + comicId + " has been successfully borrowed by Customer ID " + customerId);
                } else {
                    System.out.println("Could not borrow the comic. It may already be reserved or borrowed.");
                    conn.rollback();
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void performBookBorrowing() {
        boolean continueBorrowing = true;
        while (continueBorrowing) {
            displayAvailableBooks();
            System.out.print("\nEnter the ID of the book you want to borrow, or type 'n' to stop: ");
            String input = scanner.nextLine().trim();

            if ("n".equalsIgnoreCase(input)) {
                continueBorrowing = false;
                System.out.println("Exiting borrow process...\n");

            } else {
                String bookId = input;
                borrowBook(bookId, currentUserID);
            }
        }
    }
    public void performComicBorrowing() {
        boolean continueBorrowing = true;
        while (continueBorrowing) {
            displayAvailableComics();
            System.out.print("\nEnter the ID of the comic you want to borrow, or type 'n' to stop: ");
            String input = scanner.nextLine().trim();

            if ("n".equalsIgnoreCase(input)) {
                continueBorrowing = false;
                System.out.println("Exiting borrow process...\n");

            } else {
                String comicId = input;
                borrowComic(comicId, currentUserID);
            }
        }
    }
    private boolean isBookBorrowedByUser(String bookId, String customerId) {
        String query = "SELECT COUNT(*) FROM borrowed_books WHERE book_id = ? AND customer_id = ? AND return_date IS NULL";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, bookId);
            pstmt.setString(2, customerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // If the count is 0, then the book is not borrowed by the user
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    private boolean isComicBorrowedByUser(String comicId, String customerId) {
        String query = "SELECT COUNT(*) FROM borrowed_comics WHERE comic_id = ? AND customer_id = ? AND return_date IS NULL";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, comicId);
            pstmt.setString(2, customerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // If the count is 0, then the book is not borrowed by the user
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Return items (books and comics)
    private void performBookReturn(String bookId) {
        Connection conn = null;
        PreparedStatement updateBookStmt = null;
        PreparedStatement updateBorrowStmt = null;

        String updateBookQuery = "UPDATE books SET state = 'AVAILABLE' WHERE id = ?";
        String updateBorrowQuery = "UPDATE borrowed_books SET return_date = CURRENT_DATE WHERE book_id = ? AND customer_id = ? AND return_date IS NULL";

        try {
            conn = getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Update the book's state to 'Available'
            updateBookStmt = conn.prepareStatement(updateBookQuery);
            updateBookStmt.setString(1, bookId);
            int bookRowsAffected = updateBookStmt.executeUpdate();

            // Update the borrowed_books record to set the return_date
            updateBorrowStmt = conn.prepareStatement(updateBorrowQuery);
            updateBorrowStmt.setString(1, bookId);
            updateBorrowStmt.setString(2, currentUserID);
            int borrowRowsAffected = updateBorrowStmt.executeUpdate();

            if (bookRowsAffected == 1 && borrowRowsAffected == 1) {
                conn.commit(); // Commit transaction
                System.out.println("\tBook ID " + bookId + " has been successfully returned by Customer ID " + currentUserID);
            } else {
                System.out.println("Could not return the book. It may not be borrowed or does not exist.");
                conn.rollback(); // Rollback transaction
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
    private void performComicReturn(String comicId) {
        Connection conn = null;
        PreparedStatement updateComicStmt = null;
        PreparedStatement updateBorrowStmt = null;

        String updateComicQuery = "UPDATE comics SET state = 'AVAILABLE' WHERE id = ?";
        String updateBorrowQuery = "UPDATE borrowed_comics SET return_date = CURRENT_DATE WHERE comic_id = ? AND customer_id = ? AND return_date IS NULL";

        try {
            conn = getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Update the comic's state to 'Available'
            updateComicStmt = conn.prepareStatement(updateComicQuery);
            updateComicStmt.setString(1, comicId);
            int comicRowsAffected = updateComicStmt.executeUpdate();

            // Update the borrowed_comics record to set the return_date
            updateBorrowStmt = conn.prepareStatement(updateBorrowQuery);
            updateBorrowStmt.setString(1, comicId);
            updateBorrowStmt.setString(2, currentUserID);
            int borrowRowsAffected = updateBorrowStmt.executeUpdate();

            if (comicRowsAffected == 1 && borrowRowsAffected == 1) {
                conn.commit(); // Commit transaction
                System.out.println("\tComic ID " + comicId + " has been successfully returned by Customer ID " + currentUserID);
            } else {
                System.out.println("Could not return the comic. It may not be borrowed or does not exist.");
                conn.rollback(); // Rollback transaction
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void returnBook() {
        // Display borrowed books once and check if there are any to return
        if (!displayBorrowedBooks()) {
            return;
        }

        boolean returning = true;
        while (returning) {
            System.out.print("\nEnter the ID of the book you want to return, or type 'n' to stop: ");
            String input = scanner.nextLine().trim();

            if ("n".equalsIgnoreCase(input)) {
                returning = false; // Exit the while loop to stop returning books
                System.out.println("Exiting return process...\n");
            } else {
                String bookId = input;
                if (isBookBorrowedByUser(bookId, currentUserID)) {
                    performBookReturn(bookId); // Perform the actual return operation
                    // After returning a book, check if there are still books borrowed by the user
                    if (!displayBorrowedBooks()) {
                        returning = false; // No more books to return, exit the loop
                    }
                } else {
                    System.out.println("This book is not borrowed by you or doesn't exist.");
                }
            }
        }
    }
    public void returnComic() {
        // Display borrowed comics once and check if there are any to return
        if (!displayBorrowedComics()) {
            return;
        }

        boolean returning = true;
        while (returning) {
            System.out.print("\nEnter the ID of the comic you want to return, or type 'n' to stop: ");
            String input = scanner.nextLine().trim();

            if ("n".equalsIgnoreCase(input)) {
                returning = false; // Exit the while loop to stop returning comics
                System.out.println("Exiting return process...\n");
            } else {
                String comicId = input;
                if (isComicBorrowedByUser(comicId, currentUserID)) {
                    performComicReturn(comicId); // Perform the actual return operation
                    // Call displayBorrowedComics() again to update the list of borrowed comics
                    if (!displayBorrowedComics()) {
                        returning = false;
                    }
                } else {
                    System.out.println("This comic is not borrowed by you or doesn't exist.");
                }
            }
        }
    }

    // Add users
    public void addEmployee() {
        System.out.println("\n********** Adding Employees **********\n");

        Employee employee = null;

        System.out.println("Enter first name:");
        String fname = scanner.nextLine();

        System.out.println("Enter last name:");
        String lname = scanner.nextLine();

        System.out.println("Enter password:");
        String password = scanner.nextLine();

        System.out.println("Enter birthdate:");
        String bdate = scanner.nextLine();

        int yOfEx = 0;
        boolean isValid = false;
        while (!isValid) {
            try {
                System.out.println("Enter years of experience:");
                yOfEx = scanner.nextInt();
                scanner.nextLine(); // clear scanner after reading an int
                isValid = true;
            } catch (InputMismatchException e) {
                System.err.println("Invalid input. Please enter a number.\n");
                scanner.nextLine(); // consume the invalid input
            }
        }

        System.out.println("---------Choose a role--------\n");
        System.out.println("\t1. Librarian");
        System.out.println("\t2. Manager\n");
        System.out.println("Enter an option: ");
        int roleChoice = scanner.nextInt();
        scanner.nextLine();

        switch (roleChoice) {
            case 1:
                EmployeeMaker employeeMaker = new EmployeeMaker(new LibrarianEmpBuilder());
                employeeMaker.makeEmployee(fname, lname, password, bdate, yOfEx);
                employee = employeeMaker.getEmployee();
                break;
            case 2:
                employeeMaker = new EmployeeMaker(new ManagerEmpBuilder());
                employeeMaker.makeEmployee(fname, lname, password, bdate, yOfEx);
                employee = employeeMaker.getEmployee();
                break;
            default:
                System.out.println("Invalid decision. Please enter 1 for Librarian or 2 for Manager.");
                return;
        }

        insertEmployee(employee); // Insert employee into the database
    }
    public void addCustomer(){
        System.out.println("\n********** Adding Customer **********\n");

        Customer customer = null;

        System.out.println("Enter first name:");
        String fname = scanner.nextLine();

        System.out.println("Enter last name:");
        String lname = scanner.nextLine();

        System.out.println("Enter password:");
        String password = scanner.nextLine();

        System.out.println("Enter birthdate:");
        String bdate = scanner.nextLine();

        customer = new Customer();
        customer.setId();
        customer.setFirstName(fname);
        customer.setLastName(lname);
        customer.setPassword(password);
        customer.setBirthdate(bdate);
        customer.setState();

        insertCustomer(customer);
    }
    public void insertEmployee(Employee employee) {
        String checkQuery = "SELECT id FROM employees WHERE id = ?";
        String insertSql = "INSERT INTO employees(id, birthdate, fname, lname, password, state, role, yearsOfExp) VALUES(?,?,?,?,?,?,?,?)";

        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            // Check if employee ID already exists in the database
            checkStmt.setString(1, employee.getId());
            ResultSet checkRs = checkStmt.executeQuery();

            if (!checkRs.next()) { // If the employee ID does not exist, proceed to insert
                insertStmt.setString(1, employee.getId());
                insertStmt.setString(2, employee.getBirthdate());
                insertStmt.setString(3, employee.getFirstName());
                insertStmt.setString(4, employee.getLastName());
                insertStmt.setString(5, employee.getPassword());
                insertStmt.setString(6, employee.getState().toString());
                insertStmt.setString(7, employee.getRole().toString());
                insertStmt.setInt(8, employee.getYearsOfExp());

                int affectedRows = insertStmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Employee inserted successfully!");
                } else {
                    System.out.println("Failed to insert employee data.");
                }
            } else { // If the employee ID exists, inform the user
                System.out.println("Employee with ID " + employee.getId() + " already exists.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\nEnsure you are not repeating an ID number!");
        }
    }
    public void insertCustomer(Customer customer) {
        String checkQuery = "SELECT id FROM customers WHERE id = ?";
        String insertSql = "INSERT INTO customers(id, birthdate, fname, lname, password, state) VALUES(?,?,?,?,?,?)";

        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            // Check if customer ID already exists in the database
            checkStmt.setString(1, customer.getId());
            ResultSet checkRs = checkStmt.executeQuery();

            if (!checkRs.next()) { // If the customer ID does not exist, proceed to insert
                insertStmt.setString(1, customer.getId());
                insertStmt.setString(2, customer.getBirthdate());
                insertStmt.setString(3, customer.getFirstName());
                insertStmt.setString(4, customer.getLastName());
                insertStmt.setString(5, customer.getPassword());
                insertStmt.setString(6, customer.getState().toString());

                int affectedRows = insertStmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Customer inserted successfully!");
                } else {
                    System.out.println("Failed to insert customer data.");
                }
            } else { // If the customer ID exists, inform the user
                System.out.println("Customer with ID " + customer.getId() + " already exists.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "\nEnsure you are not repeating an ID number!");
        }
    }

    // ***** INSERT SAMPLE DATA *****
    public void insertSampleData() {
        insertSampleBooks();
        insertSampleComics();
        insertSampleCustomers();
        insertSampleEmployees();
    }
    public void insertSampleBooks(){

        List<Book> books = new ArrayList<>();

        Book book1 = AbstractFactory.factory(Genre.EnglishLiterature).createBook(
                "1984", "George Orwell",
                "08-06-1949");
        books.add(book1);

        Book book2 = AbstractFactory.factory(Genre.EnglishLiterature).createBook(
                "Dune", "Frank Herbert",
                "06-05-1965");
        books.add(book2);

        Book book3 = AbstractFactory.factory(Genre.EnglishLiterature).createBook(
                "The Da Vinci Code", "Dan Brown",
                "03-18-2003");
        books.add(book3);

        Book book4 = AbstractFactory.factory(Genre.EnglishLiterature).createBook(
                "The Hobbit", "J.R.R. Tolkien",
                "09-21-1937");
        books.add(book4);

        Book book5 = AbstractFactory.factory(Genre.EnglishLiterature).createBook(
                "All the Light We Cannot See", "Anthony Doerr",
                "05-06-2014");
        books.add(book5);

        Book book6 = AbstractFactory.factory(Genre.EnglishLiterature).createBook(
                "Pride and Prejudice", "Jane Austen",
                "01-28-1813");
        books.add(book6);

        Book book7 = AbstractFactory.factory(Genre.FrenchLiterature).createBook(
                "Les Misérables", "Victor Hugo",
                "03-04-1862");
        books.add(book7);

        Book book8 = AbstractFactory.factory(Genre.FrenchLiterature).createBook(
                "Madame Bovary", "Gustave Flaubert",
                "01-15-1857");
        books.add(book8);

        Book book9 = AbstractFactory.factory(Genre.FrenchLiterature).createBook(
                "Le Fantôme de l'Opéra", "Gaston Leroux",
                "01-08-1910");
        books.add(book9);

        Book book10 = AbstractFactory.factory(Genre.FrenchLiterature).createBook(
                "Une Histoire de Deux Cités", "Charles Dickens",
                "11-26-1859");
        books.add(book10);

        Book book11 = AbstractFactory.factory(Genre.FrenchLiterature).createBook(
                "Le Petit Prince", "Antoine de Saint-Exupéry",
                "04-06-1943");
        books.add(book11);

        for (Book book : books) {
            insertBook(book);
        }
    }
    public void insertSampleComics(){

        List<Comic> comics = new ArrayList<>();

        Comic comic1 = AbstractFactory.factory(Genre.EnglishLiterature).createComic(
                        "Maus II", "Art Spiegelman",
                    "Art Spiegelman", "01-09-1986");
        comics.add(comic1);

        Comic comic2 = AbstractFactory.factory(Genre.EnglishLiterature).createComic(
                "The Dark Knight Returns", "Frank Miller",
                "Frank Miller", "02-1986");
        comics.add(comic2);

        Comic comic3 = AbstractFactory.factory(Genre.EnglishLiterature).createComic(
                "Persepolis", "Marjane Satrapi",
                "Marjane Satrapi", "01-2000");
        comics.add(comic3);

        Comic comic4 = AbstractFactory.factory(Genre.EnglishLiterature).createComic(
                "Akira", "Katsuhiro Otomo",
                "Katsuhiro Otomo", "09-21-1984");
        comics.add(comic4);

        Comic comic5 = AbstractFactory.factory(Genre.EnglishLiterature).createComic(
                "Watchmen", "Alan Moore",
                "Dave Gibbons", "09-1986");
        comics.add(comic5);

        Comic comic6 = AbstractFactory.factory(Genre.EnglishLiterature).createComic(
                "Fun Home", "Alison Bechdel",
                "Alison Bechdel", "06-2006");
        comics.add(comic6);

        Comic comic7 = AbstractFactory.factory(Genre.FrenchLiterature).createComic(
                "Astérix le Gaulois", "René Goscinny",
                "Albert Uderzo", "10-29-1959");
        comics.add(comic7);

        Comic comic8 = AbstractFactory.factory(Genre.FrenchLiterature).createComic(
                "Tintin au pays des Soviets", "Hergé",
                "Hergé", "09-10-1929");
        comics.add(comic8);

        Comic comic9 = AbstractFactory.factory(Genre.FrenchLiterature).createComic(
                "Le Chat du Rabbin", "Joann Sfar",
                "Joann Sfar", "06-2002");
        comics.add(comic9);

        Comic comic10 = AbstractFactory.factory(Genre.FrenchLiterature).createComic(
                "Blueberry: Fort Navajo", "Jean-Michel Charlier",
                "Jean Giraud", "11-1965");
        comics.add(comic10);

        Comic comic11 = AbstractFactory.factory(Genre.FrenchLiterature).createComic(
                "Valérian et Laureline: La Cité des eaux mouvantes", "Pierre Christin",
                "Jean-Claude Mézières", "11-1970");
        comics.add(comic11);

        for (Comic comic : comics) {
            insertComic(comic);
        }
    }
    public void insertSampleEmployees(){

        List<Employee> employees = new ArrayList<>();
        EmployeeMaker librarianMaker = new EmployeeMaker(new LibrarianEmpBuilder());
        EmployeeMaker mgrMaker = new EmployeeMaker(new ManagerEmpBuilder());

        librarianMaker.makeEmployee("Miguel", "Cortes",
                                    "123456", "19-01-2004", 5);
        Employee emp1 = librarianMaker.getEmployee();
        librarianMaker.reset(new LibrarianEmpBuilder());
        employees.add(emp1);

        librarianMaker.makeEmployee("Anna", "Smith",
                            "password123", "02-15-1990", 8);
        Employee emp2 = librarianMaker.getEmployee();
        librarianMaker.reset(new LibrarianEmpBuilder());
        employees.add(emp2);

        librarianMaker.makeEmployee("Carlos", "Rodriguez",
                        "pass456", "10-08-1985", 3);
        Employee emp3 = librarianMaker.getEmployee();
        librarianMaker.reset(new LibrarianEmpBuilder());
        employees.add(emp3);

        librarianMaker.makeEmployee("Sophie", "Dupont",
                        "secret321", "05-21-1995", 6);
        Employee emp4 = librarianMaker.getEmployee();
        librarianMaker.reset(new LibrarianEmpBuilder());
        employees.add(emp4);

        librarianMaker.makeEmployee("David", "Brown",
                            "securepwd", "12-03-1982", 10);
        Employee emp5 = librarianMaker.getEmployee();
        librarianMaker.reset(new LibrarianEmpBuilder());
        employees.add(emp5);

        librarianMaker.makeEmployee("Isabelle", "Lefevre",
                            "p@ssWord", "08-17-1978", 7);
        Employee emp6 = librarianMaker.getEmployee();
        librarianMaker.reset(new LibrarianEmpBuilder());
        employees.add(emp6);

        mgrMaker.makeEmployee("John", "Doe",
                "pass@123", "04-25-1980", 12);
        Employee emp7 = mgrMaker.getEmployee();
        mgrMaker.reset(new ManagerEmpBuilder());
        employees.add(emp7);

        mgrMaker.makeEmployee("Emily", "Johnson",
                "securePwd456", "11-12-1975", 15);
        Employee emp8 = mgrMaker.getEmployee();
        mgrMaker.reset(new ManagerEmpBuilder());
        employees.add(emp8);

        for (Employee employee : employees) {
            insertEmployee(employee);
        }
    }
    public void insertSampleCustomers(){

        List<Customer> customers = new ArrayList<>();

        Customer c1 = new Customer("Jon", "Jones", "password", "10-10-1980");
        customers.add(c1);

        Customer c2 = new Customer("Alice", "Anderson", "securePass123", "05-18-1992");
        customers.add(c2);

        Customer c3 = new Customer("Michael", "Miller", "pass1234", "08-07-1985");
        customers.add(c3);

        Customer c4 = new Customer("Sophia", "Smith", "myPwd567", "12-15-1998");
        customers.add(c4);

        Customer c5 = new Customer("Daniel", "Davis", "danielPass", "03-29-1982");
        customers.add(c5);

        Customer c6 = new Customer("Emma", "Evans", "emmaPwd789", "06-22-1990");
        customers.add(c6);

        Customer c7 = new Customer("Liam", "Lewis", "liamPassword", "09-14-1987");
        customers.add(c7);

        Customer c8 = new Customer("Olivia", "Owen", "oliviaPass456", "02-03-1995");
        customers.add(c8);

        Customer c9 = new Customer("William", "Wilson", "williamPwd", "11-20-1989");
        customers.add(c9);

        Customer c10 = new Customer("Tu", "Anh", "123", "05-26-2001");
        customers.add(c10);

        for (Customer customer : customers) {
            insertCustomer(customer);
        }
    }

}
