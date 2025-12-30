# Carlus Electum - E-commerce System

## 1. Project Overview
This project is the Final Assignment for the Software Engineering course. It implements a functional E-commerce back-end in Java, focusing on a modular shopping cart and a dynamic promotion engine.

The system demonstrates:
* **Object-Oriented Design**: Minimum of 10 well-designed classes.
* **Design Patterns**: Implementation of Strategy, Repository, and Factory patterns.
* **Layered Architecture**: Separation of Presentation, Service, and Data layers.
* **Database Persistence**: Integration with MySQL for product management.

## 2. Git & Project Management Workflow
To follow professional development practices required by the assignment, I adopted a strict Git workflow.

### Branching Strategy
Even as a solo developer, I use a professional naming convention:
`type/trigram/CEL-ID-description`
* **Type**: `feature/` for new modules or `fix/` for bug corrections.
* **Trigram**: `jof` (identifying the developer initials as per professional standards).
* **CEL-ID**: The unique ticket ID from the project backlog (e.g., CEL-05).

### Requirements Traceability
Every commit is linked to a specific requirement using the "fixes #ID" syntax[cite: 67, 200]. This ensures that functional requirements are directly supported by the implementation.

## 3. Architecture & Design Patterns
The project follows a **Layered Architecture** to ensure the code is maintainable and testable[cite: 126, 132].

### Implemented Patterns
1. **Strategy Pattern (CEL-05)**: Handles different discount logic (Winter Sale vs. Buy 2 Get 3) without cluttered conditional statements.
2. **Repository Pattern (CEL-03)**: Decouples business logic from MySQL database access, allowing the UI to fetch products seamlessly.
3. **Factory Pattern (CEL-06)**: Used to instantiate different payment processors based on user choice.

## 4. Technical Stack
* **Language**: Java 25.
* **Build System**: Maven.
* **Database**: MySQL (hosted via XAMPP).
* **Testing**: JUnit 5.

## 5. How to Setup and Run

### Database Configuration
1. Start **MySQL** and **Apache** in your XAMPP Control Panel.
2. Open `http://localhost/phpmyadmin` and create a database named `ecommerce_db`.
3. Import the SQL initialization script found in the `docs/` folder.

### Build and Execution
Navigate to the project root and use the following Maven commands:
```bash
# To compile the project
mvn clean compile

# To run the interactive store CLI
mvn exec:java
``` 
