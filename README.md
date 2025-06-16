# Customer Notification Service

A Spring Boot application for managing customer information and sending notifications based on their preferences.

## Technologies

- Java 24
- Spring Boot 3.5.0
- Spring Security with JWT authentication
- Spring Data JPA
- PostgreSQL
- Lombok
- OpenAPI/Swagger for API documentation

## Project Overview

This application provides a RESTful API for managing customers, their addresses, notification preferences, and sending notifications. It includes secure authentication using JWT tokens.

## Setup Instructions

### Prerequisites

- Java 24 JDK
- PostgreSQL
- Gradle

### Environment Variables

The application requires the following environment variables to be set:

- `DB_URL`: PostgreSQL database URL
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `JWT_SECRET`: Secret key for JWT token generation

### Running the Application

1. Clone the repository
2. Set the required environment variables
3. Build and run the application using Gradle:

```bash
./gradlew bootRun
```

Or run the JAR file after building:

```bash
./gradlew build
java -jar build/libs/prj-customer-notification-0.0.1-SNAPSHOT.jar
```

## API Documentation

The API documentation is available via Swagger UI at:

```
http://localhost:8080/swagger-ui/index.html
```

## API Endpoints

### Authentication

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Authenticate user and get JWT token

### Customers

- `GET /api/customers` - Get all customers
- `GET /api/customers/{id}` - Get customer by ID
- `POST /api/customers` - Create a new customer
- `PUT /api/customers/{id}` - Update a customer
- `DELETE /api/customers/{id}` - Delete a customer
- `GET /api/customers/search` - Search for customers with filtering, pagination and sorting

### Addresses

- `GET /api/customers/{customerId}/addresses` - Get all addresses for a customer
- `GET /api/customers/{customerId}/addresses/{id}` - Get specific address
- `GET /api/customers/{customerId}/addresses/type/{type}` - Get addresses by type
- `POST /api/customers/{customerId}/addresses` - Create a new address
- `PUT /api/customers/{customerId}/addresses/{id}` - Update an address
- `PUT /api/customers/{customerId}/addresses/{id}/primary` - Set address as primary
- `PUT /api/customers/{customerId}/addresses/{id}/verify` - Update address verification
- `DELETE /api/customers/{customerId}/addresses/{id}` - Delete an address

### Notification Preferences

- `GET /api/customers/{customerId}/preferences` - Get all notification preferences
- `GET /api/customers/{customerId}/preferences/{id}` - Get specific preference
- `POST /api/customers/{customerId}/preferences` - Create a notification preference
- `PUT /api/customers/{customerId}/preferences/{id}` - Update a notification preference
- `DELETE /api/customers/{customerId}/preferences/{id}` - Delete a notification preference

### Notifications

- `POST /api/notifications/send` - Send a new notification
- `GET /api/notifications/{id}` - Get notification by ID
- `GET /api/notifications/customer/{customerId}` - Get notifications by customer ID
- `GET /api/notifications/search` - Search notifications with filters
- `GET /api/notifications/customer/{customerId}/stats` - Get notification statistics for a customer
- `GET /api/notifications/stats` - Get overall notification statistics
- `PUT /api/notifications/{id}/status` - Update notification status
- `GET /api/notifications/opt-in-report` - Generate customer opt-in report

## Security

The application uses JWT (JSON Web Token) for authentication. Access to the API endpoints requires a valid JWT token, which can be obtained by authenticating through the `/api/auth/login` endpoint.

## Development

### Project Structure

- `controller`: Contains REST controllers
- `service`: Contains business logic
- `repository`: Database access layer
- `model`: Entity classes
- `dto`: Data Transfer Objects
- `security`: JWT authentication components
- `exception`: Custom exceptions and error handling
- `config`: Configuration classes
