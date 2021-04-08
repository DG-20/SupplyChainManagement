package edu.ucalgary.ensf409;

import org.junit.*;
import static org.junit.Assert.*;


public class TestFile
{
    @Test
    public void test1()
    {
        DataBaseManipulator obj2 = new DataBaseManipulator("filing", "Large", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        System.out.println(obj2.lowestPrice);
        //obj2.resetDataBase("C:\\Users\\Div\\Desktop\\Supply_Chain_Management\\SupplyChainManagement\\src\\edu\\ucalgary\\ensf409\\inventory.sql");
    }

    public void writeData1()
    {
        try {
            
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}
