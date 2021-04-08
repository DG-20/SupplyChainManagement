package edu.ucalgary.ensf409;

import org.junit.*;
import static org.junit.Assert.*;

// Please download the inventory.sql file provided in the D2L shell: www
// Please run Source filepath/inventory.sql in the command line for mysql
// And 
public class TestFile
{
    @Test
    public void test1()
    {
        DataBaseManipulator obj2 = new DataBaseManipulator("filing", "Large", 1, "jdbc:mysql://localhost/inventory", "scm", "ensf409");
        System.out.println(obj2.lowestPrice);
    }

  


}
