# CSV Data Wrangler

**CSV Data Wrangler** is a Java-based desktop application designed to simplify the viewing, editing, and management of CSV files. With an intuitive interface, it allows users to perform a variety of operations on CSV data, making data manipulation straightforward and efficient.

## Features

- **Create new CSV files**: Start with a blank CSV file by specifying the number of columns.
- **Load existing CSV files**: Open CSV files with automatic or manual delimiter detection (e.g., `,`, `;`, `|`, `\t`).
- **Edit data directly**: Modify cell values directly in the table view.
- **Filter data**: Apply filters like "Contains," "Starts with," "Ends with," or "Equals" to specific columns.
- **Manage columns**: Add, remove, and control the visibility of columns.
- **Edit column headers**: Change header names for better organization.
- **Save CSV files**: Export data with options for delimiter and header inclusion.
- **User-friendly GUI**: Interact with data through a table view and control panel.

## Installation

1. Ensure you have **Java 17 or higher** installed on your system.
2. Download the `CSV.jar` file from the [releases](https://github.com/username/repo/releases) page.
3. Run the application using the following command:
   ```bash
   java -jar CSV.jar
   ```

## Usage

1. **Create a new CSV file**:  
   Click the 'New' button and enter the number of columns. Headers will be automatically generated (e.g., "Column 1", "Column 2").

2. **Load a CSV file**:  
   Click 'Load CSV', select a file, and choose the delimiter and whether the file has headers.

3. **Edit data**:  
   Click on any cell in the table to edit its value. Changes are saved automatically in the data model.

4. **Filter data**:  
   Enter a keyword in the filter field, select a column and a filter type ('Contains', 'Starts with', 'Ends with', 'Equals'), then click 'Filter'.

5. **Add a row**:  
   Click 'Add row' and fill in the values in the dialog that appears.

6. **Delete a row**:  
   Select a row in the table and click 'Delete selected'.

7. **Add a column**:  
   Click 'Add column' and enter the name for the new column.

8. **Remove a column**:  
   Select a column from the dropdown menu and click 'Remove column'.

9. **Manage columns**:  
   Click 'Manage columns' to show or hide specific columns in the table.

10. **Edit headers**:  
    Click 'Edit headers' to modify the names of the column headers.

11. **Save a CSV file**:  
    Click 'Save CSV', choose a location, and select the delimiter and whether to include headers.

## Documentation

Detailed Javadoc documentation is available in the `doc` folder. You can open `doc/index.html` in a web browser to view it.

## Contributing

1. Fork the repository.
2. Create a new branch for your feature or bugfix.
3. Commit your changes.
4. Submit a pull request.

## Project Structure

The project is organized as follows:

- `data/`: Contains sample CSV files.
- `doc/`: Contains Javadoc documentation.
- `out/`: Contains the compiled JAR file.
- `src/`: Contains the source code, organized into packages:
  - `controller/`: Contains the controller class.
  - `model/`: Contains the model class.
  - `util/`: Contains utility classes.
  - `view/`: Contains the view classes.

## Code Organization

- **`Main.java`**: The entry point of the application. Creates and displays the main frame.
- **`CSVController.java`**: The controller class that manages operations on the CSV data.
- **`CSVModel.java`**: The model class that holds the CSV data and headers.
- **`CSVUtils.java`**: Utility class for reading and writing CSV files.
- **`ColumnManager.java`**: Dialog for managing column visibility.
- **`ControlPanel.java`**: Panel with controls for loading, saving, filtering, etc.
- **`MainFrame.java`**: The main window of the application.
- **`TablePanel.java`**: Panel that displays the CSV data in a table.