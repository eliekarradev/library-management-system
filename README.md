# Library Management System
### Author Info: 
- Elie Karra
- email: elie.karra.dev@gmail.com

This is a Spring Boot-based Library Management System that provides RESTful APIs for managing books, patrons, and
borrowing records. It includes authentication using JWT tokens and role-based access control.

## Features

- **Book Management:**
    - Add, update, delete, and view books.

- **Patron Management:**
    - Add, update, delete, and view patrons (library users).

- **Borrowing Records:**
    - Borrow books and record their return.
- **Validation:**
    - Making sure the data integrity and check if the format of some attributes are valid or not.

- **Authentication:**
    - Login using email and password and logout functionality with JWT token-based authentication .

- **Role-Based Authorization:**
    - Role-based access control (ADMIN, USER).
  
- **Caching :**
    - Get all and get by id for all entities are cached
  
- **Logging :**
    - Logging on the console like the execution time of the functions and the function name.
  
- **Exception Handling**
    - Using ControllerAdviser to catch all exceptions types

## Setup Instructions

### Prerequisites

- Java 17 or later.
- Mysql DB
- Maven 3.6 or later.
- Any IDE (IntelliJ, Eclipse, etc.).

### Running the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/eliekarradev/library-management-system.git
   cd library-management-system
   mvn clean install
   mvn spring-boot:run

The application will start on http://localhost:8000.

2. Add you database connection properties in application.properties.

3. On the First Run , the system will create two users :
   Admin and User
    - Admin email : admin@gmail.com , password: 12345678
    - User email: user@gmail.com , password: 12345678
4. After you have the users in the database you can now create books and patrons and make the patron borrow the book and after that return it.
### Postman Collection

- You can use the Postman collection provided in the /postman directory for easier interaction with the API.

1. Import the collection into Postman.
2. Set the base URL and JWT token in the environment variables.
3. Use the /auth/login endpoint to get a JWT token, then add it to the Authorization header for all subsequent requests.

**Notes** 

/auth/login route in postman will automatically set the jwt token inside the environment variable so just trigger the api and have fun.. ^-^


## How to Use
The system have three components:
1. Book
2. Patron
3. BorrowingRecord

####  APIs on Books:
1. Add new Book (POST)
2. Get all Books (GET)
3. Update existing Book (PUT)
4. delete Book (DELETE)
5. get Book by id (GET)

####  APIs on Patrons:
1. Add new Patron (POST)
2. Get all Patrons (GET)
3. Update existing Patron (PUT)
4. delete Patron (DELETE)
5. get Patron by id (GET)

#### APIs to Borrow and return the book
1. borrow the book ( POST)
2. return the book after borrow it (PUT)




- #### you have to log in using admin email to have the ability to add,update,delete books and patrons.
- #### then log in using user email , the user can view the books and patrons then borrow the book after that return it.