package edu.ucalgary.ensf409;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;



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

        DataBaseManipulator obj = new DataBaseManipulator("lamp" , "study", "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        
    }

   
}
