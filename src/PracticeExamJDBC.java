import java.sql.*;

/******************************************************************************
 * To make it as easy as possible for you on the exam, this Java application
 * automatically looks for the SQLite database file in the /db directory
 * that you should have obtained when you cloned the repository.
 * 
 * @author Dr. Ken and his little green friend
 * @version 1.0
 * @since 2023-05-24
 */
public class PracticeExamJDBC {
    public static void main(String[] args) {

        // Where is the .db file located?
        String pathToDb = "/Users/kenritley/Documents/BHF/MODULE/DB/LESSONS/JAVA/230525/db/practice_exam_jdbc.db";

        System.out.println("PATH: " + pathToDb);
        // JDBC SQLite connection string
        String myURL = "jdbc:sqlite:" + pathToDb;
        String myUsername = "not needed for SQLite";
        String myPassword = "not needed for SQLite";

        testDB(
                myURL, // DB connection URL
                myUsername, // DB username
                myPassword); // DB password
    }

    /******************************************************************************
     * This is the main method that calls all the other "exam question" methods.
     * 
     * @author Dr. Ken and his little green friend
     * @version 1.0
     * @since 2023-05-24
     */
    public static void testDB(Object db_url, Object user, Object pass) {

        String url = (String) db_url;

        // TEST THAT THIS IS WORKING CORRECTLY
        System.out.println("\n--------------------");
        System.out.println("If this is working correctly, you will see the time in UTC");
        printCurrentTime(url);

        // QUESTION 1
        System.out.println("\n--------------------");
        System.out.println("QUESTION 1");
        Question1 q1 = new Question1();
        q1.printCurrentTime(url);

        // QUESTION 2
        System.out.println("\n--------------------");
        System.out.println("QUESTION 2");
        Question2 q2 = new Question2();
        q2.printNumberOfAccounts(url);

        // QUESTION 3
        System.out.println("\n--------------------");
        System.out.println("QUESTION 3");
        Question3 q3 = new Question3();
        q3.printAccountsTable(url);

        // QUESTION 4
        System.out.println("\n--------------------");
        System.out.println("QUESTION 4");
        Question4 q4 = new Question4();
        q4.populateAccountsTable(url);

    } // End of method

    /**
     * Test method to check that your SQLite library is working OK
     */
    public static void printCurrentTime(String url) {
        String sql = "SELECT CURRENT_TIMESTAMP";
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                System.out.printf("the current time is: " + rs.getString(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } // End of try

    }

} // End of class
