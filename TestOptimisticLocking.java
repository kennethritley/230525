import java.sql.*;
import java.util.Scanner; // needed to get input from the user

/******************************************************************************
 * You can use this file to experiment with JDBC. As long as it contains
 * the "testDB" method, then the instructor can automatically test
 * this file, e.g. in a midterm examination or final examination.
 * 
 * @author Dr. Ken and his little green friend
 * @version 1.0
 * @since 2023-04-29
 */
public class TestOptimisticLocking {

    // You have to change these for your own PostgreSQL
    private static String myURL = "jdbc:postgresql://127.0.0.1:5431/postgres";
    private static String myUsername = "postgres";
    private static String myPassword = "kenpostgres";

    public static void main(String[] args) {
        testDB(
                myURL, // DB connection URL
                myUsername, // DB username
                myPassword); // DB password
    }

    /******************************************************************************
     * This is where you put your JDBC code. Feel free to call other methods.
     * This is the method that will be invoked from a different application
     * for testing or grading.
     * 
     * @author Dr. Ken and his little green friend
     * @version 1.0
     * @since 2023-05-24
     */
    public static void testDB(Object db_url, Object user, Object pass) {
        String url = (String) db_url;
        String username = (String) user;
        String password = (String) pass;

        // Create the accounts table
        System.out.println("\n--------------------");
        System.out.println("Creating the accounts table . . . ");
        createAccountsTable(url, username, password);

        // Populate the accounts table
        System.out.println("\n--------------------");
        System.out.println("Populate the accounts table . . . ");
        populateAccountsTable(url, username, password);

        // Keep running the main loop until the user enters 99 stop

        Scanner scanner = new Scanner(System.in);
        whileLoop: while (true) {
            // Print the current accounts table
            printAccountsTable(url, username, password);

            // Get input from the user about which account to update
            System.out.println("Enter an account ID, or 99 to quit:");
            int id = scanner.nextInt();
            // if id = 99 break out of the loop
            if (id == 99)
                break whileLoop;

            System.out.println("Enter an amount to add:");
            float amount = scanner.nextFloat();

            // Update the account
            updateAccount(id, amount, url, username, password);
            // scanner.close();
        }

        // Control will only come here if the user entered 99 to exit the loop
        scanner.close();

    } // End of main testDB

    /******************************************************************************
     * Method to create a database table for testing optimistic locking
     * accounts( id INT PRIMARY KEY, amount FLOAT, version INT)
     * 
     * @author (your name here)
     * @version 1.0
     * @since (your date here)
     */
    public static void createAccountsTable(String url, String username, String password) {

        // try-with-resources
        try (Connection conn = DriverManager.getConnection(url, username, password);
                Statement stmt = conn.createStatement()) {
            // add your code here to create the SQL string to create the table
            String sql = "CREATE TABLE IF NOT EXISTS accounts " +
                    "(id INTEGER not NULL, " +
                    " amount FLOAT, " +
                    " version INTEGER, " +
                    " PRIMARY KEY ( id ))";

            // add your code here to execute this statement
            stmt.executeUpdate(sql);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } // End of try
    } // End of method

    /******************************************************************************
     * Method to populate the 'accounts' table with dummy data
     * 
     * @author (your name here)
     * @version 1.0
     * @since (your date here)
     */
    public static void populateAccountsTable(String url, String username, String password) {

        String sql = "INSERT INTO accounts (id, amount, version) VALUES (?, ?, ?)";

        // try-with-resources
        try (Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // add your code here
            // Insert 10 rows of dummy data
            for (int i = 1; i <= 10; i++) {
                pstmt.setInt(1, i); // id
                pstmt.setFloat(2, i * 100.0f); // amount
                pstmt.setInt(3, 1); // version
                pstmt.executeUpdate();
            }

        } catch (SQLException ex) {
            // Control will come here if the table is already populated,
            // so no reason to print out any error messages.
            // ex.printStackTrace();
        } // End of try
    } // End of method

    /******************************************************************************
     * Here is a method that prints the values in the 'accounts' table to the screen
     * 
     * @author (your name here)
     * @version 1.0
     * @since (your date here)
     */
    public static void printAccountsTable(String url, String username, String password) {

        // SQL select statement
        String sql = "SELECT * FROM accounts";

        // try-with-resources
        try (Connection conn = DriverManager.getConnection(url, username, password);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            // add your code here
            // Print table header
            System.out.printf("%-10s %-10s %-10s\n", "ID", "Amount", "Version");

            // Loop through the result set
            while (rs.next()) {
                // Retrieve by column name
                int id = rs.getInt("id");
                float amount = rs.getFloat("amount");
                int version = rs.getInt("version");

                // Display values
                System.out.printf("%-10d %-10.2f %-10d\n", id, amount, version);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } // End of try
    } // End of method printAccountsTable

    /******************************************************************************
     * This is a example of how you can use optimistic locking to update a
     * database table. The table must contain a "version" column.
     * 
     * @author (your name here)
     * @version 1.0
     * @since (your date here)
     */

    public static void updateAccount(int id, float addAmount, String url, String username, String password) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {

            // We are going to do this process (from SELECT to UPDATE) as a transaction.
            // So in order to start a transaction in JDBC, we need this next statement!
            conn.setAutoCommit(false);

            // Retry loop for optimistic locking
            while (true) {
                // Get the current state of the account
                try (PreparedStatement selectStmt = conn.prepareStatement(
                        "SELECT amount, version FROM accounts WHERE id = ?")) {
                    selectStmt.setInt(1, id);
                    try (ResultSet rs = selectStmt.executeQuery()) {
                        if (rs.next()) {
                            float amount = rs.getFloat("amount");
                            int version = rs.getInt("version");

                            // Update the account
                            // HERE YOU SEE OPTIMISTIC LOCKING AT WORK!
                            // This SQL statement does 2 things:
                            // (1) It only carries out the update if the version has not been changed
                            // (2) When it does update, it increases the version number by 1!
                            try (PreparedStatement updateStmt = conn.prepareStatement(
                                    "UPDATE accounts SET amount = ?, version = version + 1 WHERE id = ? AND version = ?")) {
                                updateStmt.setFloat(1, amount + addAmount);
                                updateStmt.setInt(2, id);
                                updateStmt.setInt(3, version);

                                int rowsUpdated = updateStmt.executeUpdate();
                                if (rowsUpdated == 1) {
                                    // Success! Commit the transaction!
                                    conn.commit();
                                    System.out.printf("Account %d updated successfully\n", id);
                                    return;
                                }
                            }
                        }
                    }
                }

                // If we got here, it means the update failed due to concurrent modification.
                // Roll back and retry
                System.out.printf("Locking error. Account %d could not be updated\n", id);
                conn.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } // End of method updateAccount

} // End of class TestOptimisticLocking
