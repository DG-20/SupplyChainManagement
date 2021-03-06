/**
 * @author Divyansh Goyal <a href="mailto:divyansh.goyal@ucalgary.ca">divyansh.goyal@ucalgary.ca</a>
 * 
 * @author Maheen Hossain <a href="mailto:maheen.hossain@ucalgary.ca">maheen.hossain@ucalgary.ca</a>
 * 
 * @author Liam Parmar <a href="mailto:liam.parmar@ucalgary.ca">liam.parmar@ucalgary.ca</a>
 * 
 * @author Curtis Silva <a href="mailto:curtis.silva@ucalgary.ca">curtis.silva@ucalgary.ca</a>
 * 
 * @version 85.0
 * 
 * @since 1.0
 */
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

// Please follow the instructions outlined in the readMe file. 

/**
 * InventoryTest is a JUnit test file which tests the code configuration through
 * all types of possibilities, including edge cases, error checks, and algorithm 
 * checks. This test file is designed to run in a fixed alphabetical order as the
 * database gets affected on some runs and other runs after that depend on those 
 * changes to succeed.
 */
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
        InputReader incorrectChairName= new InputReader("chiar" , "Ergonomic" , 4); 
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
        InputReader correctInput = new InputReader("filing", "Medium", 2);
        String[] expected = {"filing", "Medium", "2"};
        String[] actual = new String[3];
        actual[0] = correctInput.getFurnitureChosen();
        actual[1] = correctInput.getTypeChosen();
        String quantity = "" + correctInput.getQuantity();
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
        DataBaseManipulator usernameGetter = new DataBaseManipulator("chair" , "Executive", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        String username =usernameGetter.getUSERNAME();
        assertEquals("scm", username);
    }

    /** 
     * This tests the getter method getPASSWORD, and checks to see if the returned
     * String matches the input String provided to the constructor.
     */
    @Test
    public void testHGetterForPassword()
    {
        DataBaseManipulator passwordGetter = new DataBaseManipulator("desk" , "Standing", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        String password = passwordGetter.getPASSWORD();
        assertEquals("ensf409", password);
    }

    /** 
     * This tests the getter method getURL, and checks to see if the returned
     * String matches the input String provided to the constructor.
     */
    @Test
    public void testIGetterForUrl()
    {
        DataBaseManipulator urlGetter = new DataBaseManipulator("filing" , "Small", 3, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        String url = urlGetter.getURL();
        assertEquals("jdbc:mysql://localhost/inventory", url);
    }

    /**
     * This tests the getter method getDataBaseConnection, and checks to see if the returned
     * Connection is not null, which indicates that a connection was established. 
     */
    @Test
    public void testJGetterForConnection()
    {
        DataBaseManipulator connectionGetter = new DataBaseManipulator("chair" , "Ergonomic", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        connectionGetter.initializeConnection();
        Connection connection = connectionGetter.getDataBaseConnection();
        assertNotEquals(null, connection);
    }

    /**
     * This tests the setter method setQuantityStored and the getter method getQuantityStored 
     * by checking to see if the returned integer value from the getter matches the input integer
     * to the setter.
     */
    @Test
    public void testKGetterForQuantityStored(){
        DataBaseManipulator quantityStoredGetter = new DataBaseManipulator("chair" , "Kneeling", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        quantityStoredGetter.setQuantityStored(5);
        int quantityObtained=quantityStoredGetter.getQuantityStored();
        assertEquals(5,quantityObtained);
    }

    /** 
     * This tests the method sumAllRows and checks to see if the lowestPrice variable 
     * contains the correct sum of all rows for adjustable desks. 
     */
    @Test
    public void testLSumAllRows()
    {
        DataBaseManipulator sumAllRowsTest = new DataBaseManipulator("desk" , "Adjustable", 4, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        sumAllRowsTest.initializeConnection();
        sumAllRowsTest.sumAllRows("desk", "Adjustable");
        assertEquals(1200, sumAllRowsTest.getLowestPrice());
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
        DataBaseManipulator loopMethodTest = new DataBaseManipulator("desk" , "Adjustable", 4, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        loopMethodTest.initializeConnection();
        loopMethodTest.storage = array;
        int numOfYInCol0 = loopMethodTest.loopMethod(0);
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
        DataBaseManipulator deleteAllRowsTest = new DataBaseManipulator("lamp" , "Study", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        deleteAllRowsTest.initializeConnection();
        deleteAllRowsTest.deleteAllRows("lamp", "Study");
        deleteAllRowsTest = new DataBaseManipulator("lamp" , "Study", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
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
        DataBaseManipulator invalidIdTest = new DataBaseManipulator("filing" , "Small", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        invalidIdTest.initializeConnection();
        ArrayList <String> codes = new ArrayList <String>();
        codes.add("F9999");
        codes.add("F1234");
        invalidIdTest.deleteFromDataBase(codes);
        assertEquals(0,invalidIdTest.getRowsAffected());
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
        DataBaseManipulator validIDTest = new DataBaseManipulator("desk" , "Standing", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        validIDTest.initializeConnection();
        ArrayList <String> codes = new ArrayList <String>();
        codes.add("D9387");
        validIDTest.deleteFromDataBase(codes);
        assertEquals(1,validIDTest.getRowsAffected());
    }

    /** 
     * This tests the algorithm for finding the lowest price of 1 order of a mesh chair.
     * After the connection to the database has been established the algorithm
     * is run and compared to the expected value.
     */
    @Test
    public void testQLowestPriceOf1MeshChair()
    {
        OrderForm lowestMeshChairPrice = new OrderForm("chair" , "Mesh", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
        assertEquals(200, lowestMeshChairPrice.getLowestPrice());
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
        OrderForm invalidErgonomicChairOrder = new OrderForm("chair" , "Ergonomic", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
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
        OrderForm invalidKneelingChairOrder = new OrderForm("chair" , "Kneeling", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
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
        OrderForm StandingDesksIDTest = new OrderForm("desk" , "Standing", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
        ArrayList<String> codesToCheck = new ArrayList<String>();
        codesToCheck.add("D1927");
        codesToCheck.add("D2341");
        codesToCheck.add("D3820");
        codesToCheck.add("D4438");
        assertEquals(true, codesToCheck.equals(StandingDesksIDTest.getCodes()));
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
        OrderForm invalidStandingDeskOrder = new OrderForm("desk" , "Standing", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
    }

    /** 
     * This tests the algorithm for finding the lowest price of 1 order of a adjustable
     * desk. After the connection to the database has been established the algorithm
     * is run and compared to the expected value from the original database.
     */
    @Test
    public void testVLowestPriceOf1AdjustableDesk()
    {
        OrderForm lowestAdjustableDeskPrice = new OrderForm("desk" , "Adjustable", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
        assertEquals(400, lowestAdjustableDeskPrice.getLowestPrice());
    }

    /** 
     * This tests the algorithm for finding the lowest price of 1 order of a adjustable
     * desk. After the connection to the database has been established the algorithm
     * is run and a system exit value of 1 is expected as it is an invalid order.
     */
    @Test
    public void testWLowestPriceOf1AdjustableDeskAgain()
    {
        OrderForm lowestAdjustableDeskPriceAgain = new OrderForm("desk" , "Adjustable", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
        assertEquals(400, lowestAdjustableDeskPriceAgain.getLowestPrice());
    }

    /** 
     * This tests the algorithm for finding the lowest price of 3 orders of medium
     * filings. After the connection to the database has been established the algorithm
     * is run and compared to the expected value.
     */
    @Test
    public void testXLowestPriceOf3MediumFilings()
    {
        OrderForm lowestMediumFilingPrice = new OrderForm("filing" , "Medium", 3, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
        assertEquals(600, lowestMediumFilingPrice.getLowestPrice());
    }

    /** 
     * This tests the algorithm for finding the IDs that make up the 2 orders of small
     * filings. After the connection to the database has been established the algorithm
     * is run and compared to the expected value.
     */
    @Test
    public void testYIDsOf2SmallFilings()
    {
        OrderForm smallFilingsIDTest = new OrderForm("filing" , "Small", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
        ArrayList<String> codesToCheck = new ArrayList<String>();
        codesToCheck.add("F001");
        codesToCheck.add("F013");
        codesToCheck.add("F005");
        codesToCheck.add("F006");
        assertEquals(true, codesToCheck.equals(smallFilingsIDTest.getCodes()));
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
        OrderForm invalidSmallFilingOrder = new OrderForm("filing" , "Small", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
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
        OrderForm invalidStudyLampOrder = new OrderForm("lamp" , "Study", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
    }

    /** 
     * This tests the algorithm for finding the lowest price of the 2 orders of 
     * small filings. After the connection to the database has been established the 
     * algorithm is run and compared to the expected value.
     */
    @Test
    public void testZZZLowestPriceOf2SwingArmLamps()
    {
        OrderForm lowestSwingArmLampPrice = new OrderForm("lamp" , "Swing Arm", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
        assertEquals(60, lowestSwingArmLampPrice.getLowestPrice());
    }

    /**
     * This tests the algorithm for finding the IDs that make up the 3 orders of 
     * desk lamps. After the connection to the database has been established the 
     * algorithm is run and compared to the expected IDs.
     */
    @Test
    public void testZZZZIDsOf3DeskLamps()
    {
        OrderForm deskLampIDTest = new OrderForm("lamp" , "Desk", 3, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
        ArrayList<String> codesToCheck = new ArrayList<String>();
        codesToCheck.add("L013");
        codesToCheck.add("L208");
        codesToCheck.add("L112");
        codesToCheck.add("L342");
        codesToCheck.add("L564");
        assertEquals(true, codesToCheck.equals(deskLampIDTest.getCodes()));
    }

    /**
     * This tests the algorithm for finding the IDS that make up the 1 order of 
     * desk lamps. After the connection to the database has been established the 
     * algorithm is run and compared to the expected value of system exit 1.
     */
    @Test
    public void testZZZZZInvalidOrderOf1DeskLamp()
    {
        exit.expectSystemExitWithStatus(1);
        OrderForm obj = new OrderForm("lamp" , "Desk", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
    }

    /**
     * This tests calls the overloaded constructor OrderForm, that implements the DatabaseManipulator constructor
     * for tests purposes. In the OrderForm constructor, the furniture "desk", type "Traditional", and quantity of 2 are
     * passed in. The URL, PASSWORD, USERNAME are the standard values passed in. In the constructor the finalOrderTextFile
     * method is called and uses the arguments that are passed into the constructor. This tests checks to see if a
     * text file with the name OrderForm exists.
     */
    @Test
    public void testZZZZZZIfOrderFormExists()
    {
        OrderForm myOrderForm = new OrderForm("desk" , "Traditional", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
        File fileChecker = new File("OrderForm.txt");
        boolean fileExists = fileChecker.exists();
        assertEquals(true, fileExists);
    }

    /**
     * This tests calls the overloaded constructor OrderForm, that implements the DatabaseManipulator constructor
     * for tests purposes. In the OrderForm constructor, the furniture "filing", type "Large", and quantity of 1 are
     * passed in. The URL, PASSWORD, USERNAME are the standard values passed in. In the constructor the finalOrderTextFile
     * method is called and uses the arguments that are passed into the constructor and an OrderForm.txt file is created with
     * information. Once the text file is created a Scanner object is created to scan the OrderForm.txt file, using a while
     * loop and the hasNextLine() method, the line within the text file is concatenated to a string. Once every line is 
     * concatenated it is compared to the private String outputFormInfo to check if they are equal.
     */
    @Test
    public void testZZZZZZZCheckOutputOfOrderForm() throws FileNotFoundException
    {
        OrderForm myOrderForm = new OrderForm("filing" , "Large", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
        File inputFile = new File("OrderForm.txt");
		Scanner scan = new Scanner(inputFile); //uses the scanner class on the input file
		String outputMessage = "";
        while(scan.hasNextLine()) //checks to see when a line exists.
		{
			outputMessage = outputMessage.concat(scan.nextLine());
            //newline is concatenated to the outputMessage string as the nextLine needs to be scanned
            //and the nextLine does not add a newline charachter to the string
            outputMessage = outputMessage.concat("\n");
        }
        boolean correctStringMessage = outputMessage.equals(myOrderForm.getOrderFormCode());
        assertEquals(true, correctStringMessage);
    }
}
