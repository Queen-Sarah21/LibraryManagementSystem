# LibraryManagementSystem
Managing the borrowing and returning of books by clients in a library.
***** Library Management System *****

## Introduction

This Library Management System is implemented using Java and SQLite, demonstrating the use of the Factory and Builder design patterns. The system manages a collection of books and comics, offering functionalities such as user authentication, item management, user management, and transaction handling.

## Features

- User authentication for Employees and Customers
- Book and Comic management: Add, Search, Reserve, Borrow, and Return items functionalities
- Employee (Librarian and Manager) and Customer account management.
* There are different features according to the user type:
	- As a customer, they can only display, search, reserve, borrow, and return items
	- As a librarian, the user has functions to add new items (book and comic)
	- As a manager, the user has functions to add new users (employee and customer)
- Utilizes SQLite for database operations

## Design Patterns

- **Factory Pattern**
- **Builder Pattern** 

## Database

- SQLite is used for data persistence
- Created database name: Library.db. 
- Tables for books, comics, borrowed_books, borrowed_comics, reserved_books, reserved_comics, employees, customers.

## Usage
To run the application, you will need to have Java installed on your system along with the SQLite JDBC driver.

1. Download the SQLite JDBC driver (JAR file) from the official SQLite website or Maven repository.

2. Add the downloaded SQLite JAR file to Project Stucture.

3. Run the `Main` class to start the application.

5. Choose user type either employee or customer:
	+ Press 1 to login as an employee with credentials as follow:
		- A manager with ID is '1001' and password is 'pass@123'
		- A librarian with ID is '1000' with password is '123456'

	+ Press 2 to login as a customer with credentials as follow:
		- A customer with ID is '1000' and password is 'password'
		
6. Interact with the system through the provided console interface.

