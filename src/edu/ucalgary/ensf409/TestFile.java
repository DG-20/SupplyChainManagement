package edu.ucalgary.ensf409;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
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
    public void testA()
    {
        boolean testerA = InputReader.isValid("chair", "small", 3);
        assertEquals(false, testerA);
    }

    @Test

    public void testB(){
        boolean testerB=InputReader.isValid("table", "Swing Arm", 20);
        assertEquals(false, testerB);
    }

    @Test
    public void testC(){
        boolean testerC = InputReader.isValid("filing", "Large", -1);
        assertEquals(false, testerC);
    }


    @Test
    public void testD(){

        boolean testerD=InputReader.isValid("lamp", "Study", 2);
        assertEquals(true, testerD);

    }

    @Test
    public void testE(){
        exit.expectSystemExitWithStatus(1);
        InputReader obj= new InputReader("chiar" , "Ergonomic" , 4); 
    }

    @Test
    public void testF(){
        InputReader obj = new InputReader("filing", "Medium", 2);
        String[] expected = {"filing", "Medium", "2"};
        String[] actual = new String[3];
        actual[0] = obj.furnitureChosen;
        actual[1] = obj.typeChosen;
        String quantity = "" + obj.quantity;
        actual[2] = quantity;
        assertEquals(true, Arrays.equals(expected, actual));

    }

    @Test
    public void testG(){

        DataBaseManipulator obj = new DataBaseManipulator("lamp" , "Study", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        obj.initializeConnection();
        assertNotEquals(null, obj.dataBaseConnection);
    }

    @Test
    public void testH()
    {
        DataBaseManipulator obj = new DataBaseManipulator("desk" , "Adjustable", 4, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        obj.initializeConnection();
        obj.sumAllRows("desk", "Adjustable");
        assertEquals(1200, obj.lowestPrice);
    }

    @Test
    public void testI()
    {
        String array [][] = {{"C123","-1"}, {"-1","D52"}, {"E62","-1"}};
        DataBaseManipulator obj = new DataBaseManipulator("desk" , "Adjustable", 4, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        obj.initializeConnection();
        obj.storage = array;
        int numOfYInCol0 = obj.loopMethod(0);
        assertEquals(2,numOfYInCol0);
    }

    @Test
    public void testJ()
    {
        exit.expectSystemExitWithStatus(1);
        DataBaseManipulator obj = new DataBaseManipulator("lamp" , "Study", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        obj.initializeConnection();
        obj.deleteAllRows("lamp", "Study");
        obj = new DataBaseManipulator("lamp" , "Study", 2, "jdbc:mysql://localhost/inventory", "scm", "ensf409", true);
    }

    @Test
    public void testK()
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
    public void testL()
    {
        DataBaseManipulator obj = new DataBaseManipulator("desk" , "Standing", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        obj.initializeConnection();
        ArrayList <String> codes = new ArrayList <String>();
        codes.add("D9387");
        obj.deleteFromDataBase(codes);
        assertEquals(1,obj.rowsAffected);
    }

    @Test
    public void testM()
    {
        DataBaseManipulator obj = new DataBaseManipulator("desk" , "Standing", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        obj.initializeConnection();
        ArrayList <String> codes = new ArrayList <String>();
        codes.add("D9387");
        obj.deleteFromDataBase(codes);
        assertEquals(1,obj.rowsAffected);
    }
}
