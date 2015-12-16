/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tk.zugbug.utils;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author zugbug
 */
public class TupleIT {
    
    public TupleIT() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of of method, of class Tuple.
     */
    @Test
    public void testOf_2args_1() {
        System.out.println("of");
        Object l = null;
        Object r = null;
        Tuple expResult = null;
        Tuple result = Tuple.of(l, r);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mono method, of class Tuple.
     */
    @Test
    public void testMono() {
        System.out.println("mono");
        Object l = null;
        Object r = null;
        Tuple.MonoTuple expResult = null;
        Tuple.MonoTuple result = Tuple.mono(l, r);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of of method, of class Tuple.
     */
    @Test
    public void testOf_2args_2() {
        System.out.println("of");
        Object[] l = null;
        Object[] r = null;
        Tuple<List<B>, List<B>> expResult = null;
        Tuple<List<B>, List<B>> result = Tuple.of(l, r);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of listFromCollectionTuples method, of class Tuple.
     */
    @Test
    public void testListFromCollectionTuples() {
        System.out.println("listFromCollectionTuples");
        List<Tuple<V, V>> expResult = null;
        List<Tuple<V, V>> result = Tuple.listFromCollectionTuples(null);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of listFromStreamTuples method, of class Tuple.
     */
    @Test
    public void testListFromStreamTuples() {
        System.out.println("listFromStreamTuples");
        List<Tuple<V, V>> expResult = null;
        List<Tuple<V, V>> result = Tuple.listFromStreamTuples(null);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLeft method, of class Tuple.
     */
    @Test
    public void testGetLeft() {
        System.out.println("getLeft");
        Tuple instance = null;
        Object expResult = null;
        Object result = instance.getLeft();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRight method, of class Tuple.
     */
    @Test
    public void testGetRight() {
        System.out.println("getRight");
        Tuple instance = null;
        Object expResult = null;
        Object result = instance.getRight();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of map method, of class Tuple.
     */
    @Test
    public void testMap() {
        System.out.println("map");
        Tuple instance = null;
        Object expResult = null;
        Object result = instance.map(null);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mapToTuple method, of class Tuple.
     */
    @Test
    public void testMapToTuple() {
        System.out.println("mapToTuple");
        Tuple instance = null;
        Tuple expResult = null;
        Tuple result = instance.mapToTuple(null);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of samePairsPercentage method, of class Tuple.
     */
    @Test
    public void testSamePairsPercentage_List() {
        System.out.println("samePairsPercentage");
        double expResult = 0.0;
        double result = Tuple.samePairsPercentage(null);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of samePairsPercentage method, of class Tuple.
     */
    @Test
    public void testSamePairsPercentage_Stream() {
        System.out.println("samePairsPercentage");
        double expResult = 0.0;
        double result = Tuple.samePairsPercentage(null);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Tuple.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Tuple instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
