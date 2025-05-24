# Customer Management API

A Spring Boot REST API for managing customer data. It supports full CRUD operations, including dynamic membership tier logic based on a customer's annual spending.

## Technology Stack

- Java 21
- Spring Boot 3.5.0
- Spring Data JPA
- H2 Database (in-memory)
- Bean Validation (JSR-380)
- OpenAPI (springdoc-openapi)
- JUnit 5
- Maven

## Prerequisites

- Java 21 or higher
- Maven 3.8 or higher

## How to Run

1. Clone the repository:

```
git clone https://github.com/your-username/customer-api.git
cd customer-api
```

2. Build the application:

```
mvn clean install
```

3. Start the application:

```
mvn spring-boot:run
```

Application will start on: `http://localhost:8080`

## API Documentation

- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- OpenAPI Spec: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Sample API Usage

### Create Customer

**POST** `/customers`

```
{
  "name": "Alice",
  "email": "alice@example.com",
  "annualSpend": 1200.50,
  "lastPurchaseDate": "2025-04-20T10:30:00"
}
```

### Get Customer by ID

**GET** `/customers/{uuid}`

### Get Customer by Name

**GET** `/customers?name=Alice`

### Get Customer by Email

**GET** `/customers?email=alice@example.com`

### Update Customer

**PUT** `/customers/{uuid}`

```
{
  "name": "Alice Smith",
  "email": "alice.smith@example.com",
  "annualSpend": 1500.00,
  "lastPurchaseDate": "2025-05-10T15:00:00"
}
```

### Partially Update Customer

**PATCH** `/customers/{uuid}`

```
{
  "annualSpend": 2000.00
}
```

### Delete Customer

**DELETE** `/customers/{uuid}`

## Tier Calculation Logic

The tier is calculated dynamically based on the `annualSpend` and `lastPurchaseDate`. It is not stored in the database.

- **Silver**: Default tier for customers with no spend or low activity.
- **Gold**: Annual spend >= $1000 and purchase within last 12 months.
- **Platinum**: Annual spend >= $10000 and purchase within last 6 months.

## Running Tests

Execute the following to run unit tests:

```
mvn test
```

Tests cover:

- CRUD operations
- Validation rules
- Tier assignment logic

## H2 Database Console

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave blank)

## Project Structure

```
src/
├── controller/
├── service/
├── dto/
├── entity/
├── repository/
├── exception/
└── util/
```

## Notes

- UUID is generated server-side and not needed in POST request.
- Tier logic is read-only and computed on retrieval.
- Only basic format validation is applied to email addresses.