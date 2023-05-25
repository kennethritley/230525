import java.sql.*;

public class Question4 {

    /******************************************************************************
     * QUESTION 4: 2 Points
     * Use PreparedStatement fill the table with dummy data
     */
    public void populateAccountsTable(String url) {

        // TODO 1 of 2 YOUR CODE GOES HERE--------------------
        String sql = "put your SQL query here";
        //--------------------      
        // try-with-resources
        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Insert 10 rows of dummy data
            for (int i = 1; i <= 10; i++) {
                float amountToUpdate = (float) i * 100f;

                pstmt.setInt(1, i); // statement for id goes here
                pstmt.setFloat(2, amountToUpdate); // statement for amount

                // TO-DO 2 of 2 YOUR CODE GOES HERE --------------------
                // put the needed statement to execute the update here
                
                // --------------------
            }

        } catch (SQLException ex) {
            // Control will come here if the table is already populated,
            // so no reason to print out any error messages.
            // ex.printStackTrace();
        } // End of try
    } // End of method

} // End of class
