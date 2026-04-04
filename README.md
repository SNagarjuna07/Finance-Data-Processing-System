# Finance Data Processing and Access Control Backend

The system is built around a **role-based access control model** with three distinct roles — `VIEWER`, `ANALYST`, and `ADMIN`. Each role has clearly defined permissions that govern what data they can see and what actions they can perform. A VIEWER can browse financial records, an ANALYST can additionally access dashboard-level insights such as income summaries and monthly trends, and an ADMIN has full control over the system — managing users, assigning roles, and creating or modifying financial records.

Financial records represent individual transactions, each carrying details like the amount, type (income or expense), category, date, and optional notes. The system supports filtering records by category, type, or date range through a single unified endpoint, and returns results in a paginated format for efficiency. Records are never permanently deleted — instead, a soft delete mechanism is used, preserving data integrity while hiding deleted entries from all queries.

Authentication is handled via **JWT tokens**. Users register publicly and are assigned the VIEWER role by default. An Admin can then promote users to higher roles or deactivate accounts as needed. A seed Admin account is created on startup to bootstrap the system.

The backend is documented using **Swagger UI**, providing a live, interactive interface to explore and test all available APIs without needing an external client.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4 |
| Security | Spring Security + JWT |
| Database | H2 |
| ORM | Spring Data JPA + Hibernate |
| Validation | Jakarta Validation |
| Documentation | Springdoc OpenAPI (Swagger UI) |
| Build Tool | Maven |

---

## Project Structure

```
com.finance.dashboard
├── config/          # Security config, JWT filter, beans
├── controller/      # REST controllers
├── service/         # Business logic
├── repository/      # JPA repositories
├── entity/          # JPA entities
├── dto/             # Request and response records
├── enums/           # Role, Status, Type, Category
└── exception/       # Custom exceptions + GlobalExceptionHandler
```

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+

### Setup

**1. Clone the repository**
```bash
git clone https://github.com/SNagarjuna07/finance-dashboard.git
cd finance-dashboard
```

**2. Configure the database**

Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:h2:file:./data/financedb
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=update
app.jwt.secret=your_jwt_secret_key
app.jwt.expiration=86400000
```

**3. Run the application**
```bash
mvn spring-boot:run
```

**4. Access Swagger UI**
```
http://localhost:8080/swagger-ui/index.html
```

---

## Roles

| Role | Description |
|---|---|
| `VIEWER` | Can only view financial records |
| `ANALYST` | Can view records and access dashboard summaries |
| `ADMIN` | Full access — manage users, records, and dashboard |

Public registration always assigns the `VIEWER` role by default. An `ADMIN` can promote users via the role update endpoint.

---

## API Overview

### Auth — `/auth`
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/auth/register` | Public | Register as VIEWER |
| POST | `/auth/login` | Public | Login and receive JWT |

### Financial Records — `/records`
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/records` | ADMIN | Create a record for a user |
| GET | `/records` | VIEWER, ANALYST, ADMIN | Get records with optional filters |
| PATCH | `/records/{id}` | ADMIN | Update a record |
| DELETE | `/records/{id}` | ADMIN | Soft delete a record |

**Supported filters on `GET /records`:**
```
?category=FOOD
?type=INCOME
?from=2024-01-01&to=2024-12-31
?category=FOOD&type=EXPENSE&from=2024-01-01&to=2024-12-31
?page=0&size=10&sortBy=amount
```

### Dashboard — `/dashboard`
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/dashboard/summary` | ANALYST, ADMIN | Total income, expenses, net balance |
| GET | `/dashboard/by-category` | ANALYST, ADMIN | Totals grouped by category |
| GET | `/dashboard/trends` | ANALYST, ADMIN | Monthly income and expense trends |
| GET | `/dashboard/recent` | ANALYST, ADMIN | Recent records |

### Admin — `/admin`
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/admin/create-admin` | ADMIN | Create a new ADMIN user |
| POST | `/admin/create-analyst` | ADMIN | Create a new ANALYST user |
| GET | `/admin` | ADMIN | List all users (paginated) |
| PATCH | `/admin/{id}/role` | ADMIN | Update a user's role |
| PATCH | `/admin/{id}/status` | ADMIN | Activate or deactivate a user |

---

## Potential Enhancements

- Per-user dashboard summaries for ANALYST role
- Refresh token support
- Rate limiting on auth endpoints
- Search support across records
- Audit log of all admin actions
- Export records as CSV or PDF

---

## Author

**S Nagarjuna**  
Backend Developer Intern Assignment — Zorvyn FinTech Pvt. Ltd.
