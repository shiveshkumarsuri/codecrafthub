CodeCraftHub - Personalized Learning Platform 🚀
A simple REST API built with Spring Boot that allows developers to track their learning courses. Perfect for beginners learning Spring Boot and REST API development!

📋 Table of Contents
Features
Prerequisites
Installation
Running the Application
API Documentation
Testing with cURL
Testing with Postman
Project Structure
Troubleshooting
Learning Resources
✨ Features
✅ CRUD Operations: Create, Read, Update, and Delete courses
✅ JSON File Storage: No database setup required - uses simple JSON file
✅ Auto-generated IDs: Automatic course ID assignment
✅ Status Tracking: Track courses as "Not Started", "In Progress", or "Completed"
✅ Input Validation: Ensures all required fields are provided
✅ Error Handling: Comprehensive error messages for debugging
✅ RESTful Design: Follows REST API best practices
🔧 Prerequisites
Before you begin, ensure you have the following installed:

Java 17 or higher (Download here)
Maven 3.6+ (Download here)
Git (optional, for cloning the repository)
cURL or Postman (for testing APIs)
Verify Installation
# Check Java version
java -version

# Check Maven version
mvn -version
📥 Installation
Option 1: Clone from Git (if using version control)
git clone https://github.com/yourusername/CodeCraftHub.git
cd CodeCraftHub
Option 2: Create Project Manually
Create a new directory:
mkdir CodeCraftHub
cd CodeCraftHub
Create the project structure:
CodeCraftHub/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── codecrafthub/
│       │           ├── CodeCraftHubApplication.java
│       │           ├── controller/
│       │           │   └── CourseController.java
│       │           ├── model/
│       │           │   └── Course.java
│       │           ├── service/
│       │           │   └── CourseService.java
│       │           └── exception/
│       │               ├── CourseNotFoundException.java
│       │               └── GlobalExceptionHandler.java
│       └── resources/
│           ├── application.properties
│           └── data/
│               └── (courses.json will be auto-created)
└── pom.xml
Copy all the Java files and
pom.xml
into their respective directories
🚀 Running the Application
Step 1: Build the Project
mvn clean install
This command will:

Download all dependencies
Compile your code
Run tests (if any)
Package the application
Step 2: Run the Application
mvn spring-boot:run
Alternative method (using the JAR file):

java -jar target/codecrafthub-1.0.0.jar
Step 3: Verify the Application is Running
You should see output like:

Started CodeCraftHubApplication in 2.345 seconds
Tomcat started on port(s): 8080 (http)
The API is now available at:
http://localhost:8080

📚 API Documentation
Base URL
http://localhost:8080/api/courses
Endpoints Overview
Method	Endpoint	Description
POST
/api/courses
Create a new course