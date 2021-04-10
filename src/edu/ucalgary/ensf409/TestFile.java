package edu.ucalgary.ensf409;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

import java.sql.Connection;
import java.util.ArrayList;


import static org.junit.Assert.*;

import java.util.Arrays;

// Please download the inventory.sql file provided in the D2L shell: www
// Please run Source filepath/inventory.sql in the command line for mysql
// And 
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestFile
{
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void testAInvalidTypePassedIntoIsValidMethod()
    {
        boolean invalidType = InputReader.isValid("chair", "small", 3);
        assertEquals(false, invalidType);
    }

    @Test

    public void testBInvalidFurniturePassedIntoIsValidMethod()
    {
        boolean invalidFurniture=InputReader.isValid("table", "Swing Arm", 20);
        assertEquals(false, invalidFurniture);
    }

    @Test
    public void testCInvalidQuantityPassedIntoIsValidMethod()
    {
        boolean invalidQuantity = InputReader.isValid("filing", "Large", -1);
        assertEquals(false, invalidQuantity);
    }


    @Test
    public void testDValidIsValidMethodCall()
    {
        boolean valid=InputReader.isValid("lamp", "Study", 2);
        assertEquals(true, valid);
    }

    @Test
    public void testEInvalidInputForConstructor()
    {
        exit.expectSystemExitWithStatus(1);
        InputReader obj= new InputReader("chiar" , "Ergonomic" , 4); 
    }

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

    /*
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

    /*
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

     /*
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

     /*
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

     /*
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

    /*
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

    /*
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

    /*
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

     /*
     * This tests the method deleteFromDatabase by checking
     * 
     * 
     * 
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
}
