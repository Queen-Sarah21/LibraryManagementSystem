package LibraryManager;

import java.io.File;
import java.sql.*;
import java.util.*;

public class LibraryManager {
    private DatabaseManager dbMgr = new DatabaseManager();
    private String  currentUserID;
    private Scanner scanner = new Scanner(System.in);
    public void run(){
        displayMenu();
    }
    public void displayMenu() {
        System.out.println("\n********** Welcome to the Library System **********\n");

        while (true) {
            System.out.println("\t1. Login as Employee");
            System.out.println("\t2. Login as Customer");
            System.out.println("\t3. Exit");
            System.out.println("\nPlease choose an option:");

            try {
                int entryOption = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (entryOption) {
                    case 1:
                        if (dbMgr.performEmployeeLogin()) {
                            employeeMenu();
                        }
                        break;
                    case 2:
                        if (dbMgr.performCustomerLogin()) {
                            customerMenu();
                        }
                        break;
                    case 3:
                        System.out.println("\nExiting the system... Goodbye!");
                        return;
                    default:
                        System.err.println("Invalid option. Please try again.\n");
                        break;
                }
            } catch (InputMismatchException e) {
                System.err.println("Invalid input. Please enter a number.\n");
                scanner.nextLine(); // Consume the invalid input
            }
        }
    }
    private void employeeMenu() {
        int adminOption;
        boolean isManager = "MANAGER".equals(dbMgr.getCurrentEmployeeRole()); // This checks if the logged-in user is a manager

        System.out.println("\n********** Employee Menu **********\n");
        while (true) {
            System.out.println("\t--------- Items ---------");
            System.out.println("\t1. Display All Books");
            System.out.println("\t2. Display All Comics");
            System.out.println("\t3. Search Books");
            System.out.println("\t4. Search Comics");
            System.out.println("\t5. Add Book");
            System.out.println("\t6. Add Comic");
            if (isManager) {
                // These options are only for managers
                System.out.println("\n\t--------- Users ---------");
                System.out.println("\t7. Display All Employees");
                System.out.println("\t8. Display All Customers");
                System.out.println("\t9. Search Employees");
                System.out.println("\t10. Search Customers");
                System.out.println("\t11. Add Employee");
                System.out.println("\n\t12. Add Customer");
            }
            System.out.println("\t13. Logout");
            System.out.println("\nPlease choose an option:");

            try {
                adminOption = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (adminOption) {
                    case 1:
                        dbMgr.getAllBooks();
                        break;
                    case 2:
                        dbMgr.getAllComics();
                        break;
                    case 3:
                        dbMgr.performSearchBooks();
                        break;
                    case 4:
                        dbMgr.performSearchComics();
                        break;
                    case 5:
                        dbMgr.addBook();
                        break;
                    case 6:
                        dbMgr.addComic();
                        break;
                    case 7:
                        if (isManager) {
                            dbMgr.getAllEmployees();
                        } else {
                            System.out.println("Unauthorized action. This option is only for managers.");
                        }
                        break;
                    case 8:
                        if (isManager) {
                            dbMgr.getAllCustomers();
                        } else {
                            System.out.println("Unauthorized action. This option is only for managers.");
                        }
                        break;
                    case 9:
                        if (isManager) {
                            dbMgr.performSearchEmployees();
                        } else {
                            System.out.println("Unauthorized action. This option is only for managers.");
                        }
                        break;
                    case 10:
                        if (isManager) {
                            dbMgr.performSearchCustomers();
                        } else {
                            System.out.println("Unauthorized action. This option is only for managers.");
                        }
                        break;
                    case 11:
                        if (isManager) {
                            dbMgr.addEmployee();
                        } else {
                            System.out.println("Unauthorized action. This option is only for managers.");
                        }
                        break;
                    case 12:
                        if (isManager) {
                            dbMgr.addCustomer();
                        } else {
                            System.out.println("Unauthorized action. This option is only for managers.");
                        }
                        break;
                    case 13:
                        System.out.println("Logging out as employee...\n");
                        return; // Break out of the while loop to log out
                    default:
                        System.err.println("Invalid option. Please try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.err.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume the invalid input
            }
            System.out.println("Press enter to continue");
            scanner.nextLine();
        }
    }


    private void customerMenu() {
        int customerOption;
        System.out.println("\n********** Customer Menu **********\n");
        while (true) {
            System.out.println("------------- Book section -------------");
            System.out.println("\t1. Display All Books");
            System.out.println("\t2. Search Book");
            System.out.println("\t3. Reserve Book");
            System.out.println("\t4. Borrow Book");
            System.out.println("\t5. Return Book");
            System.out.println("\n------------- Comic section -------------");
            System.out.println("\t6. Display All Comics");
            System.out.println("\t7. Search Comic");
            System.out.println("\t8. Reserve Comic");
            System.out.println("\t9. Borrow Comic");
            System.out.println("\t10. Return Comic\n");
            System.out.println("\t11. Logout");
            System.out.println("\nPlease choose an option:");

            try {
                customerOption = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (customerOption) {
                    case 1:
                        dbMgr.getAllBooks();
                        break;
                    case 2:
                        dbMgr.performSearchBooks();
                        break;
                    case 3:
                        dbMgr.performBookReservation();
                        break;
                    case 4:
                        dbMgr.performBookBorrowing();
                        break;
                    case 5:
                        dbMgr.returnBook();
                        break;
                    case 6:
                        dbMgr.getAllComics();
                        break;
                    case 7:
                        dbMgr.performSearchComics();
                        break;
                    case 8:
                        dbMgr.performComicReservation();
                        break;
                    case 9:
                        dbMgr.performComicBorrowing();
                        break;
                    case 10:
                        dbMgr.returnComic();
                        break;
                    case 11:
                        System.out.println("Logging out as customer...\n");
                        return; // Break out of the while loop to log out
                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.err.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume the invalid input
            }
            System.out.println("Press enter to continue");
            scanner.nextLine();
        }
    }
}
