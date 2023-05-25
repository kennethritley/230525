import java.sql.*;

public class Question2 {

    /******************************************************************************
     * QUESTION 2: 2 Points
     * Print out how many accounts there are in the 'bankaccounts' table
     * 
     */
    public void printNumberOfAccounts(String url) {

        // TODO 1 of 2: YOUR CODE GOES HERE --------------------
        String sql = "put your SQL here";
        // --------------------

        // try-with-resources
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            // Print out the number of accounts in the 'bankaccounts' table
            if (rs.next()) {

                // TODO 2 of 2: YOUR CODE GOES HERE--------------------
                int numberOfAccounts = 99; // replace 99 with the code to get the data out of ResultSet
                // --------------------

                // Display values
                System.out.printf("Number of accounts in table bankaccounts: " + numberOfAccounts);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } // End of try

    }
} // End of class
