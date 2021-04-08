package edu.ucalgary.ensf409;

import org.junit.*;
import static org.junit.Assert.*;


public class TestFile
{
    @Test
    public void test1()
    {
        int hi = 0;
        System.out.println(hi);
        assertEquals(1,hi);
    }
}