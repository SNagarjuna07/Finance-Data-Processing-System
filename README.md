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
├── config/          # Security config, Swagger config, beans
├── controller/      # REST controllers
├── service/         # Business logic
├── repository/      # JPA repositories
├── entity/          # JPA entities
├── dto/             # Request and response records
├── enums/           # Role, Status, Type, Category
├── filter/          # JWT authentication filter
├── security/        # Custom 401 (AuthenticationEntryPoint) and 403 (AccessDeniedHandler) handlers
└── exception/       # Custom exceptions + GlobalExceptionHandler
```

---

## Getting Started

### Prerequisites

- Java 21
- Maven 3.8+

### Setup

**1. Clone the repository**
```bash
git clone https://github.com/SNagarjuna07/finance-dashboard.git
cd finance-dashboard
```


**2. Run the application**
```bash
mvn spring-boot:run
```

**3. Access Swagger UI**
```
http://localhost:8080/api/swagger-ui/index.html
```

---

## Quick Start Guide

On startup, a seed **ADMIN** account is automatically created:

| Field | Value |
|---|---|
| Email | `admin@finance.com` |
| Password | `admin123` |

### Step 1 — Login as ADMIN

Call `POST /api/auth/login` with the seed credentials to get a JWT token:
```json
{
  "email": "admin@finance.com",
  "password": "admin123"
}
```

Copy the token from the response.

### Step 2 — Authorize in Swagger

Click the **Authorize** button at the top right of Swagger UI and paste the token:

---

## Roles

| Role | Description |
|---|---|
| `VIEWER` | Can only view financial records |
| `ANALYST` | Can view records and access dashboard summaries |
| `ADMIN` | Full access — manage users, records, and dashboard |

Public registration always assigns the `VIEWER` role by default. An `ADMIN` can promote users via the role update endpoint.

---

## Access Control Matrix

| Action | VIEWER | ANALYST | ADMIN |
|---|---|---|---|
| View records | ✅ | ✅ | ✅ |
| Filter records | ✅ | ✅ | ✅ |
| View dashboard | ❌ | ✅ | ✅ |
| Create record | ❌ | ❌ | ✅ |
| Update record | ❌ | ❌ | ✅ |
| Delete record | ❌ | ❌ | ✅ |
| Manage users | ❌ | ❌ | ✅ |

---

## API Overview

### Auth — `/api/auth`
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/auth/login` | Public | Login and receive JWT |

### Financial Records — `/api/records`
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/records` | ADMIN | Create a record for a user |
| GET | `/api/records` | VIEWER, ANALYST, ADMIN | Get records with optional filters |
| PATCH | `/api/records/{id}` | ADMIN | Update a record |
| DELETE | `/api/records/{id}` | ADMIN | Soft delete a record |

**Supported filters on `GET /api/records`:**
```
?category=FOOD
?type=INCOME
?from=2024-01-01&to=2024-12-31
?category=FOOD&type=EXPENSE&from=2024-01-01&to=2024-12-31
?page=0&size=10&sortBy=amount
```

### Dashboard — `/api/dashboard`
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/dashboard/summary` | ANALYST, ADMIN | Total income, expenses, net balance |
| GET | `/api/dashboard/by-category` | ANALYST, ADMIN | Totals grouped by category |
| GET | `/api/dashboard/trends` | ANALYST, ADMIN | Monthly income and expense trends |
| GET | `/api/dashboard/recent` | ANALYST, ADMIN | Recent records |

### Admin — `/api/admin`
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/admin/create-user` | ADMIN | Create a new VIEWER user |
| POST | `/api/admin/create-admin` | ADMIN | Create a new ADMIN user |
| POST | `/api/admin/create-analyst` | ADMIN | Create a new ANALYST user |
| GET | `/api/admin/users` | ADMIN | List all users (paginated) |
| PATCH | `/api/admin/{id}/role` | ADMIN | Update a user's role |
| PATCH | `/api/admin/{id}/status` | ADMIN | Activate or deactivate a user |

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
