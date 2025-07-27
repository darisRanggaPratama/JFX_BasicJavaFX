# Customer Data Management Application

A JavaFX application for managing customer data with features including:
- CRUD operations for customer records
- Search functionality
- Pagination
- Import/Export to CSV and Excel
- PDF report generation
- MySQL database integration

## Prerequisites

- Java 17 or higher
- MySQL Server
- Maven

## Setup Instructions

1. Create the MySQL database:
   - Run the SQL script located at `src/main/resources/com/tama/customer/database/schema.sql`
   - This will create the database and tables with sample data

2. Build the application:
   ```bash
   mvn clean package
   ```

3. Run the application:
   ```bash
   mvn javafx:run
   ```

4. When the application starts:
   - Click "Connect" in the menu and enter your database credentials
   - The default database name is "customerdb"

## Features

- **Customer Management**
  - Add new customers
  - Edit existing customer details
  - View customer list with pagination
  - Search customers by any field

- **Data Import/Export**
  - Import from CSV
  - Import from Excel
  - Export to CSV
  - Export to Excel
  - Generate PDF reports

- **Database Connection**
  - Configurable database connection
  - Connection status indicator
  - Automatic reconnection support

## Data Fields

- NIK (Primary Key)
- Name
- Birth Date
- Active Status
- Salary
