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
        boolean testerA = InputReader.isValid("chair", "small", 3);
        assertEquals(false, testerA);
    }

    @Test

    public void testBInvalidFurniturePassedIntoIsValidMethod()
    {
        boolean testerB=InputReader.isValid("table", "Swing Arm", 20);
        assertEquals(false, testerB);
    }

    @Test
    public void testCInvalidQuantityPassedIntoIsValidMethod()
    {
        boolean testerC = InputReader.isValid("filing", "Large", -1);
        assertEquals(false, testerC);
    }


    @Test
    public void testDValidIsValidMethodCall()
    {
        boolean testerD=InputReader.isValid("lamp", "Study", 2);
        assertEquals(true, testerD);
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

    @Test
    public void testGGetterForUserName()
    {
        DataBaseManipulator obj = new DataBaseManipulator("chair" , "Executive", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        String tester1 = obj.getUSERNAME();
        assertEquals("scm", tester1);
    }

    @Test
    public void testHGetterForPassword()
    {
        DataBaseManipulator obj = new DataBaseManipulator("desk" , "Standing", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        String tester2 = obj.getPASSWORD();
        assertEquals("ensf409", tester2);
    }

    @Test
    public void testIGetterForUrl()
    {
        DataBaseManipulator obj = new DataBaseManipulator("filing" , "Small", 3, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        String tester3 = obj.getURL();
        assertEquals("jdbc:mysql://localhost/inventory", tester3);
    }

    @Test
    public void testJGetterForConnection()
    {
        DataBaseManipulator obj = new DataBaseManipulator("chair" , "Ergonomic", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        obj.initializeConnection();
        Connection testerP = obj.getDataBaseConnection();
        assertNotEquals(null, testerP);
    }

    @Test
    public void testKGetterForQuantityStored(){
        DataBaseManipulator obj = new DataBaseManipulator("chair" , "Kneeling", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        obj.setQuantityStored(5);
        int quantityObtained=obj.getQuantityStored();
        assertEquals(5,quantityObtained);
    }

    @Test
    public void testLSumAllRows()
    {
        DataBaseManipulator obj = new DataBaseManipulator("desk" , "Adjustable", 4, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        obj.initializeConnection();
        obj.sumAllRows("desk", "Adjustable");
        assertEquals(1200, obj.lowestPrice);
    }

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

    @Test
    public void testNDeleteAllRows()
    {
        exit.expectSystemExitWithStatus(1);
        DataBaseManipulator obj = new DataBaseManipulator("lamp" , "Study", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        obj.initializeConnection();
        obj.deleteAllRows("lamp", "Study");
        obj = new DataBaseManipulator("lamp" , "Study", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
    }

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
