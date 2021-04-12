package edu.ucalgary.ensf409;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.ArrayList;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Scanner;

// Please download the inventory.sql file provided in the D2L shell: www
// Please run Source filepath/inventory.sql in the command line for mysql
// And 
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InventoryTest
{
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    /** 
     * This tests calls the isValid method, which is part of the InputReader class.
     * It passes in an incorrect type of chair ("small"). The test expects
     * isValid to return false.
     */
    @Test
    public void testAInvalidTypePassedIntoIsValidMethod()
    {
        boolean invalidType = InputReader.isValid("chair", "small", 3);
        assertEquals(false, invalidType);
    }

    /** 
     * This tests calls the isValid method, which is part of the InputReader class.
     * It passes in an incorrect type of furniture("table"). The test expects
     * isValid to return false.
     */
    @Test
    public void testBInvalidFurniturePassedIntoIsValidMethod()
    {
        boolean invalidFurniture=InputReader.isValid("table", "Swing Arm", 20);
        assertEquals(false, invalidFurniture);
    }

    /**
     * This tests calls the isValid method, which is part of the InputReader class.
     * It passes in an invalid quantity ordered (0). The test expects
     * isValid to return false.
     */
    @Test
    public void testCInvalidQuantityPassedIntoIsValidMethod()
    {
        boolean invalidQuantity = InputReader.isValid("filing", "Large", 0);
        assertEquals(false, invalidQuantity);
    }

    /** 
     * This test calls upon the isValid method passing in
     * valid arguments for the furniture, the type, and the
     * quantity. This test expects a return value of true
     * as all arguments are valid.
     */
    @Test
    public void testDValidIsValidMethodCall()
    {
        boolean valid=InputReader.isValid("lamp", "Study", 2);
        assertEquals(true, valid);
    }

    /** 
     * This test calls upon the constructor of InputReader
     * passing in an incorrect spelling of "chair" (invalid input).
     * The test expects this constructor to produce a System.exit(1)
     * as this is the correct handling of this situation.
     */
    @Test
    public void testEInvalidInputForConstructor()
    {
        exit.expectSystemExitWithStatus(1);
        InputReader obj= new InputReader("chiar" , "Ergonomic" , 4); 
    }

    /** 
     * This test calls upon the constructor of InputReader, passing
     * in valid inputs for furniture, type, and quantity.
     * It then stores the expected values in a String array
     * and compares it to the member variables of InputReader,
     * to check if they were correctly assigned the inputs.
     */
    @Test
    public void testFConstructorCorrectlyAssignsMemberFields()
    {
        InputReader obj = new InputReader("filing", "Medium", 2);
        String[] expected = {"filing", "Medium", "2"};
        String[] actual = new String[3];
        actual[0] = obj.getFurnitureChosen();
        actual[1] = obj.getTypeChosen();
        String quantity = "" + obj.getQuantity();
        actual[2] = quantity;
        assertEquals(true, Arrays.equals(expected, actual));
    }

    /**
     * This tests the getter method getUSERNAME, and checks to see if the returned
     * String matches the input String provided to the constructor.
     */
    @Test
    public void testGGetterForUsername()
    {
        DataBaseManipulator obj = new DataBaseManipulator("chair" , "Executive", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        String username = obj.getUSERNAME();
        assertEquals("scm", username);
    }

    /** 
     * This tests the getter method getPASSWORD, and checks to see if the returned
     * String matches the input String provided to the constructor.
     */
    @Test
    public void testHGetterForPassword()
    {
        DataBaseManipulator obj = new DataBaseManipulator("desk" , "Standing", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        String password = obj.getPASSWORD();
        assertEquals("ensf409", password);
    }

    /** 
     * This tests the getter method getURL, and checks to see if the returned
     * String matches the input String provided to the constructor.
     */
    @Test
    public void testIGetterForUrl()
    {
        DataBaseManipulator obj = new DataBaseManipulator("filing" , "Small", 3, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        String url = obj.getURL();
        assertEquals("jdbc:mysql://localhost/inventory", url);
    }

    /**
     * This tests the getter method getDataBaseConnection, and checks to see if the returned
     * Connection is not null, which indicates that a connection was established. 
     */
    @Test
    public void testJGetterForConnection()
    {
        DataBaseManipulator obj = new DataBaseManipulator("chair" , "Ergonomic", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        obj.initializeConnection();
        Connection connection = obj.getDataBaseConnection();
        assertNotEquals(null, connection);
    }

    /**
     * This tests the setter method setQuantityStored and the getter method getQuantityStored 
     * by checking to see if the returned integer value from the getter matches the input integer
     * to the setter.
     */
    @Test
    public void testKGetterForQuantityStored(){
        DataBaseManipulator obj = new DataBaseManipulator("chair" , "Kneeling", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        obj.setQuantityStored(5);
        int quantityObtained=obj.getQuantityStored();
        assertEquals(5,quantityObtained);
    }

    /** 
     * This tests the method sumAllRows and checks to see if the lowestPrice variable 
     * contains the correct sum of all rows for adjustable desks. 
     */
    @Test
    public void testLSumAllRows()
    {
        DataBaseManipulator obj = new DataBaseManipulator("desk" , "Adjustable", 4, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        obj.initializeConnection();
        obj.sumAllRows("desk", "Adjustable");
        assertEquals(1200, obj.lowestPrice);
    }

    /** 
     * This tests the method loopMethod by checking to see if the number of Ys (non "-1")
     * in the first column (column 0) is 2, based on the given 2D array, which is passed
     * into the storage variable.
     */
    @Test
    public void testMLoopMethod()
    {
        String array [][] = {{"C123","-1"}, {"-1","D52"}, {"E62","-1"}};
        DataBaseManipulator obj = new DataBaseManipulator("desk" , "Adjustable", 4, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        obj.initializeConnection();
        obj.storage = array;
        int numOfYInCol0 = obj.loopMethod(0);
        assertEquals(2,numOfYInCol0);
    }

    /** 
     * This tests the method deleteAllRows by first calling deleteAllRows, passing in
     * "lamp" and "Study", and then calling the constructor to see if there are any possible
     * combinations of this furniture type that can be created. Since that is impossible, it 
     * checks for System.exit(1).
     */
    @Test
    public void testNDeleteAllRows()
    {
        exit.expectSystemExitWithStatus(1);
        DataBaseManipulator obj = new DataBaseManipulator("lamp" , "Study", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        obj.initializeConnection();
        obj.deleteAllRows("lamp", "Study");
        obj = new DataBaseManipulator("lamp" , "Study", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
    }

    /** 
     * This tests the method deleteFromDatabase. After the connection has
     * been established, a new ArrayList of type String is created and stores
     * the IDs that need to be deleted from the database. This test is passing
     * in invalid IDs (the ones that don't exist in the database). The rowsAffected
     * variable is expected to be 0 as no rows were affected in the database.
     */
    @Test
    public void testOPassingInvalidCodesToDelete()
    {
        DataBaseManipulator obj = new DataBaseManipulator("filing" , "Small", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        obj.initializeConnection();
        ArrayList <String> codes = new ArrayList <String>();
        codes.add("F9999");
        codes.add("F1234");
        obj.deleteFromDataBase(codes);
        assertEquals(0,obj.rowsAffected);
    }
   
    /** 
     * This tests the method deleteFromDatabase. After the connection has
     * been established, a new ArrayList of type String is created and stores
     * the IDs that need to be deleted from the database. This test is passing
     * in a valid ID (one that does exist in the database). The rowsAffected
     * variable is expected to be 1 as 1 row was affected in the database.
     */
    @Test
    public void testPPassingValidCodeToDelete()
    {
        DataBaseManipulator obj = new DataBaseManipulator("desk" , "Standing", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        obj.initializeConnection();
        ArrayList <String> codes = new ArrayList <String>();
        codes.add("D9387");
        obj.deleteFromDataBase(codes);
        assertEquals(1,obj.rowsAffected);
    }

    /** 
     * This tests the algorithm for finding the lowest price of 1 order of a mesh chair.
     * After the connection to the database has been established the algorithm
     * is run and compared to the expected value.
     */
    @Test
    public void testQLowestPriceOf1MeshChair()
    {
        OrderForm obj = new OrderForm("chair" , "Mesh", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
        assertEquals(200, obj.lowestPrice);
    }

    /**
     * This tests the algorithm for finding the lowest price of 2 orders of ergonomic
     * chairs. After the connection to the database has been established the algorithm
     * is run and a system exit value of 1 is expected as it is an invalid order.
     */
    @Test
    public void testRInvalidOrderOf2ErgonomicChairs()
    {
        exit.expectSystemExitWithStatus(1);
        OrderForm obj = new OrderForm("chair" , "Ergonomic", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
    }

    /** 
     * This tests the algorithm for finding the lowest price of 1 order of a kneeling
     * chair. After the connection to the database has been established the algorithm
     * is run and a system exit value of 1 is expected as it is an invalid order.
     */
    @Test
    public void testSInvalidOrderOf1KneelingChair()
    {
        exit.expectSystemExitWithStatus(1);
        OrderForm obj = new OrderForm("chair" , "Kneeling", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
    }

    /** 
     * This tests the algorithm for finding the IDs of 2 orders of standing
     * desks. After the connection to the database has been established the algorithm
     * is run and the IDs that make up the order are stored in an ArrayList and then 
     * compared to the expected values.
     */
    @Test
    public void testTIDsOf2StandingDesks()
    {
        OrderForm obj = new OrderForm("desk" , "Standing", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
        ArrayList<String> codesToCheck = new ArrayList<String>();
        codesToCheck.add("D1927");
        codesToCheck.add("D2341");
        codesToCheck.add("D3820");
        codesToCheck.add("D4438");
        assertEquals(true, codesToCheck.equals(obj.codes));
    }

    /** 
     * This tests the algorithm for finding the lowest price of 1 order of a standing
     * desk. After the connection to the database has been established the algorithm
     * is run and a system exit value of 1 is expected as it is an invalid order.
     */
    @Test
    public void testUInvalidOrderOf1StandingDesk()
    {
        exit.expectSystemExitWithStatus(1);
        OrderForm obj = new OrderForm("desk" , "Standing", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
    }

    /** 
     * This tests the algorithm for finding the lowest price of 1 order of a adjustable
     * desk. After the connection to the database has been established the algorithm
     * is run and compared to the expected value from the original database.
     */
    @Test
    public void testVLowestPriceOf1AdjustableDesk()
    {
        OrderForm obj = new OrderForm("desk" , "Adjustable", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
        assertEquals(400, obj.lowestPrice);
    }

    /** 
     * This tests the algorithm for finding the lowest price of 1 order of a adjustable
     * desk. After the connection to the database has been established the algorithm
     * is run and a system exit value of 1 is expected as it is an invalid order.
     */
    @Test
    public void testWLowestPriceOf1AdjustableDeskAgain()
    {
        OrderForm obj = new OrderForm("desk" , "Adjustable", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
        assertEquals(400, obj.lowestPrice);
    }

    /** 
     * This tests the algorithm for finding the lowest price of 3 orders of medium
     * filings. After the connection to the database has been established the algorithm
     * is run and compared to the expected value.
     */
    @Test
    public void testXLowestPriceOf3MediumFilings()
    {
        OrderForm obj = new OrderForm("filing" , "Medium", 3, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
        assertEquals(600, obj.lowestPrice);
    }

    /** 
     * This tests the algorithm for finding the IDs that make up the 2 orders of small
     * filings. After the connection to the database has been established the algorithm
     * is run and compared to the expected value.
     */
    @Test
    public void testYIDsOf2SmallFilings()
    {
        OrderForm obj = new OrderForm("filing" , "Small", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
        ArrayList<String> codesToCheck = new ArrayList<String>();
        codesToCheck.add("F001");
        codesToCheck.add("F013");
        codesToCheck.add("F005");
        codesToCheck.add("F006");
        assertEquals(true, codesToCheck.equals(obj.codes));
    }

    /** 
     * This tests the algorithm for finding the lowest price that make up the 2 orders of 
     * small filings. After the connection to the database has been established the 
     * algorithm is run and compared to the expected value.
     */
    @Test
    public void testZInvalidOrderOf2SmallFiling()
    {
        exit.expectSystemExitWithStatus(1);
        OrderForm obj = new OrderForm("filing" , "Small", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
    }

    /** 
     * This tests the algorithm for an invalid input of 1 study lamp.  
     * After the connection to the database has been established the algorithm 
     * is run and compared to the expected value of a system exit value of 1.
     */
    @Test
    public void testZZInvalidOrderOf1StudyLamp()
    {
        exit.expectSystemExitWithStatus(1);
        OrderForm obj = new OrderForm("lamp" , "Study", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
    }

    /** 
     * This tests the algorithm for finding the lowest price of the 2 orders of 
     * small filings. After the connection to the database has been established the 
     * algorithm is run and compared to the expected value.
     */
    @Test
    public void testZZZLowestPriceOf2SwingArmLamps()
    {
        OrderForm obj = new OrderForm("lamp" , "Swing Arm", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
        assertEquals(60, obj.lowestPrice);
    }

    /**
     * This tests the algorithm for finding the IDs that make up the 3 orders of 
     * desk lamps. After the connection to the database has been established the 
     * algorithm is run and compared to the expected IDs.
     */
    @Test
    public void testZZZZIDsOf3DeskLamps()
    {
        OrderForm obj = new OrderForm("lamp" , "Desk", 3, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
        ArrayList<String> codesToCheck = new ArrayList<String>();
        codesToCheck.add("L013");
        codesToCheck.add("L208");
        codesToCheck.add("L112");
        codesToCheck.add("L342");
        codesToCheck.add("L564");
        assertEquals(true, codesToCheck.equals(obj.codes));
    }

    /**
     * This tests the algorithm for finding the IDS that make up the 1 order of 
     * desk lamps. After the connection to the database has been established the 
     * algorithm is run and compared to the expected value of system exit 1.
     */
    @Test
    public void testZZZZZIDsOf1DeskLamp()
    {
        exit.expectSystemExitWithStatus(1);
        OrderForm obj = new OrderForm("lamp" , "Desk", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
        
    }

    @Test
    public void testIfOrderFormExists()
    {
        OrderForm myOrderForm = new OrderForm("desk" , "Traditional", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
        File fileChecker = new File("OrderForm.txt");
        boolean fileExists = fileChecker.exists();
        assertEquals(true, fileExists);
    }

    @Test
    public void testCheckOutputOfOrderForm() throws FileNotFoundException
    {
        OrderForm myOrderForm = new OrderForm("filing" , "Large", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
        File inputFile = new File("OrderForm.txt");
		Scanner scan = new Scanner(inputFile); //uses the scanner class on the input file
		String outputMessage = "";
        while(scan.hasNextLine()) //checks to see when a line exists.
		{
			outputMessage += scan.nextLine();
        }
        boolean correctStringMessage = outputMessage.equals(myOrderForm.getOrderFormCode());
        assertEquals(true, correctStringMessage);
    }
}
