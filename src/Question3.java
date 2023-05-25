import java.sql.*;

public class Question3 {

/******************************************************************************
 * QUESTION 3: 2 Points
 * Print out the data in the 'bankaccount' table
 *   
 */
public void printAccountsTable( String url) {

    // TODO 1 of 2 YOUR CODE GOES HERE--------------------
    String sql = "put your SQL here";
    //--------------------

    // try-with-resources
    try (Connection conn = DriverManager.getConnection(url);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) 
    {        
        // Print table header
        System.out.printf("%-10s %-10s \n", "ID", "Amount");

        // Loop through the result set
        int id;
        float amount;
        while (rs.next()) {

            // TODO 2 of 2 YOUR CODE GOES HERE--------------------
            id  = 99;  // replace 99 with the code to get the data out of ResultSet
            amount = 99; // replace 99 with the code to get the data out of ResultSet
            //--------------------

            // Display values
            System.out.printf("%-10d %-10.2f \n", id, amount);
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
    } // End of try
} // End of method

} // End of class
