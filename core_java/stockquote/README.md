This project focused on the java backend services.
The purpose of this project is to develop a stock tracking application using java that creates, reads, updates and deletes data against an RDBMS data JDBC and fetches stock data from an API.

1) First we call Alpha Vantage REST API and process the JSON in Java
2) A DAO (Database Access Object) was implemented to separate from the business services. This layer only handles data with external storage. The QuoteHttpHelper fetches data from the API
3) The tables were created in psql. The QuoteDao & PositionDao were implemented from the CrudDao interface.
4) Service Layer was implemented to handle the business logic of the applicationand JUnit and Mockito test were ran on it.
5) The app and controller were implemented where this layer consumes input and call the corresponding service layer method. And is used for orchestration
6) Lastly, logging was added and the application was dockerized
