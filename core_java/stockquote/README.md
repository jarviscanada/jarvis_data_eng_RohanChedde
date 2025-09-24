# About this Project
This project focused on the java backend services.
The purpose of this project is to develop a stock tracking application using java that creates, reads, updates and deletes data against an RDBMS data JDBC and fetches stock data from an API.

1) First we call Alpha Vantage REST API and process the JSON in Java
2) A DAO (Database Access Object) was implemented to separate from the business services. This layer only handles data with external storage. The QuoteHttpHelper fetches data from the API
3) The tables were created in psql. The QuoteDao & PositionDao were implemented from the CrudDao interface.
4) Service Layer was implemented to handle the business logic of the applicationand JUnit and Mockito test were ran on it.
5) The app and controller were implemented where this layer consumes input and call the corresponding service layer method. And is used for orchestration
6) Lastly, logging was added and the application was dockerized

#Quick Start
1) Change the values in: src/resources/properties.txt, to suit your values for database connection.
2) Setup in SQL:
#### Create a new database
    
    ```sql
    DROP DATABASE IF EXISTS stock_quote;
    CREATE DATABASE stock_quote;
    ```
    
####  Create `Quote` table
    
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
        timestamp           TIMESTAMP DEFAULT CURRENT_TIMESTAMP  NOT NULL,
    );
    ```
    
####  Create `Position` table
    
    ```sql
    DROP TABLE IF EXISTS position;
    CREATE TABLE position (
        symbol                VARCHAR(10) PRIMARY KEY,
        number_of_shares      INT NOT NULL,
        value_paid            DECIMAL(10, 2) NOT NULL,
        CONSTRAINT symbol_fk	FOREIGN KEY (symbol) REFERENCES quote(symbol)
    );
   ```
3) This project can be accessed in Docker Hub:
	```rohanc679/myapp```
