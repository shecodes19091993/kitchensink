# KitchenSink Project

The KitchenSink project is a Java-based application built using the Spring framework which has 2 panels one for Admin & other for user to register Member, update and delete them.

## Features

- **Spring Framework**: Utilizes Spring Boot for application configuration and management.
- **MongoDB Integration**: Employs Spring Data MongoDB for data persistence.
- **RESTful Services**: Exposes RESTful APIs for client interactions.

## Prerequisites

- **Java 21** or higher
- **Maven 3.6** or higher
- **MongoDB 5.2.1**: Ensure MongoDB is installed and running on your local machine.

## Getting Started

1. **Clone the repository**:

   ```bash
   git clone https://github.com/shecodes19091993/kitchensink.git
   cd kitchensink

 2.  **Configure Mongo Version 5.2**:

Update the application.properties file located in src/main/resources/ with your MongoDB connection details:

```bash
   spring.data.mongodb.host=localhost
   spring.data.mongodb.port=27017
   spring.data.mongodb.database=kitchensink_db


3. **Build Maven project 3.6 or higher**:

Use Maven to build the project:

mvn clean install

Access the application:

The application should now be running at http://localhost:8080/login



