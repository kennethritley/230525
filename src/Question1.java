import java.sql.*;

public class Question1 {

    /******************************************************************************
     * QUESTION 2: 1 Point
     * The SQL command to print out the time in SQLite is
     *       SELECT CURRENT_TIMESTAMP
     * Complete this method to print out the time. Don't be confused since this
     * command shows the time in UTC not CET.
     * 
     */
    public void printCurrentTime(String url) {

        // TODO: YOUR CODE GOES HERE --------------------
        String sql = "your SqL query goes here";
        // --------------------

        // try-with-resources
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            // Print out the number of accounts in the 'bankaccounts' table
            if (rs.next()) {

                // Display time
                System.out.printf("the current time is: " + rs.getString(1));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } // End of try

    }

} // End of class
