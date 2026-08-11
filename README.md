# Journal Application

A backend application built using Spring Boot that allows users to securely create, manage, and maintain personal journal entries.

The application provides user authentication, role-based access, journal entry management, MongoDB persistence, and automated welcome emails for users who have configured an email address.

## Features

- User registration and authentication
- Password encryption using BCrypt
- Role-based authorization (USER / ADMIN)
- Create, update, retrieve and delete journal entries
- MongoDB integration for persistent data storage
- User-to-journal entry relationship management
- Automated welcome email after successful user registration
- HTML email templates using Thymeleaf
- Configurable email subject and SMTP properties
- RESTful APIs
- Exception handling and logging
- Environment-based configuration using application properties

- ## Technology Stack

| Technology | Purpose |
|------------|---------|
| Java | Backend development |
| Spring Boot | Application framework |
| Spring Security | Authentication & authorization |
| Spring Data MongoDB | Database integration |
| MongoDB | Data persistence |
| Maven | Dependency management & build |
| Thymeleaf | HTML email templates |
| JavaMailSender | Email delivery |
| Lombok | Boilerplate code reduction |
| JUnit | Unit & integration testing |

Architecture :

Client
   |
   v
REST Controller
   |
   v
Service Layer
   |
   +------> Repository
   |            |
   |            v
   |         MongoDB
   |
   +------> Email Service
                |
                v
          Thymeleaf Template
                |
                v
          JavaMailSender

## Welcome Email

When a new user successfully registers, the application checks whether an email address is configured for the user.

If an email address is available:

1. The user is saved successfully.
2. The welcome email template is loaded using Thymeleaf.
3. Dynamic user information is injected into the template.
4. The generated HTML content is converted into an email.
5. JavaMailSender sends the email to the registered address.

Email templates are maintained separately from the Java business logic, making them easy to modify without changing application code.

## API Endpoints

### User APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/user` | Create a new user |
| GET | `/user` | Retrieve users |
| GET | `/user/{username}` | Retrieve user details |
| DELETE | `/user/{id}` | Delete user |

### Journal APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/journal` | Create journal entry |
| GET | `/journal` | Retrieve journal entries |
| PUT | `/journal/{id}` | Update journal entry |
| DELETE | `/journal/{id}` | Delete journal entry |

## Running the Application

### Prerequisites

- Java 21+
- Maven
- MongoDB
- SMTP-enabled email account (optional)

### Clone the repository

git clone https://github.com/mrab5583/journalApplication.git

### Navigate to the project

cd journalApplication

### Build the application

mvn clean install

### Run the application

mvn spring-boot:run

## Future Enhancements

- JWT-based authentication
- Email verification during registration
- Password reset functionality
- Pagination and sorting for journal entries
- API documentation using Swagger / OpenAPI
- Global exception handling
- Input validation
- Docker support
- Redis-based caching
- Automated CI/CD pipeline
- Comprehensive unit and integration test coverage
