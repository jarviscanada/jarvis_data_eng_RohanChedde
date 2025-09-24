# About this Project

This project focuses on **Java backend services**.
The purpose is to develop a **stock tracking application** that performs Create, Read, Update, and Delete (CRUD) operations against a relational database using **JDBC**, while also fetching stock data from the **Alpha Vantage API**.

## Project Overview

1. The Alpha Vantage REST API is called, and the JSON response is processed in Java.
2. A **DAO (Data Access Object)** layer was implemented to separate persistence logic from business services. This layer only handles interaction with external storage.

   * `QuoteHttpHelper` fetches data from the API.
3. Database tables were created in PostgreSQL.

   * `QuoteDao` and `PositionDao` were implemented from the `CrudDao` interface.
4. A **Service Layer** was implemented to handle the business logic of the application.

   * JUnit and Mockito tests were written and executed for validation.
5. The **Controller Layer** was implemented to consume input, orchestrate calls to the service layer, and return results.
6. **Logging** was added, and the application was **dockerized** for easy deployment.

---

# Quick Start

## 1. Configure Database Connection

Update the database configuration in:

```
src/resources/properties.txt
```

## 2. Setup PostgreSQL Database

### Create a new database

```sql
DROP DATABASE IF EXISTS stock_quote;
CREATE DATABASE stock_quote;
```

### Create `Quote` table

```sql
DROP TABLE IF EXISTS quote;
CREATE TABLE quote (
    symbol              VARCHAR(10) PRIMARY KEY,
    open                DECIMAL(10, 2) NOT NULL,
    high                DECIMAL(10, 2) NOT NULL,
    low                 DECIMAL(10, 2) NOT NULL,
    price               DECIMAL(10, 2) NOT NULL,
    volume              INT NOT NULL,
    latest_trading_day  DATE NOT NULL,
    previous_close      DECIMAL(10, 2) NOT NULL,
    change              DECIMAL(10, 2) NOT NULL,
    change_percent      VARCHAR(10) NOT NULL,
    timestamp           TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
```

### Create `Position` table

```sql
DROP TABLE IF EXISTS position;
CREATE TABLE position (
    symbol              VARCHAR(10) PRIMARY KEY,
    number_of_shares    INT NOT NULL,
    value_paid          DECIMAL(10, 2) NOT NULL,
    CONSTRAINT symbol_fk FOREIGN KEY (symbol) REFERENCES quote(symbol)
);
```

---

## 3. Run with Docker

This project is available on Docker Hub:

```bash
docker pull rohanc679/myapp:1.0
```

---

