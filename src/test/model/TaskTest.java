package model;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TaskTest {

    private static int DEFAULT_WEIGHT = 3;

    Task task;
    Date testDateA;
    Date testDateB;
    Date zeroDate;

    @Before
    public void setUp(){
        testDateA = new Date(2020,3,23);
        testDateB = new Date(2021, 4, 20);
        zeroDate = new Date(0);
    }

    @Test
    public void testMakeTaskNameOnly(){
        task = new Task("Groceries");
        assertEquals("Groceries", task.getName());
        assertEquals(zeroDate,task.getDueDate());
        assertEquals(DEFAULT_WEIGHT, task.getWeight());
    }

    @Test
    public void testMakeTaskNameDueDate(){
        task = new Task("Groceries", testDateA);
        assertEquals("Groceries", task.getName());
        assertEquals(testDateA, task.getDueDate());
        assertEquals(DEFAULT_WEIGHT, task.getWeight());
    }

    @Test
    public void testMakeTaskNameWeight(){
        task = new Task("Groceries", 3);
        assertEquals("Groceries", task.getName());
        assertEquals(zeroDate,task.getDueDate());
        assertEquals(3,task.getWeight());
    }

    @Test
    public void testMakeTaskNameDueDateWeight(){
        task = new Task("Groceries", testDateA, 2);
        assertEquals("Groceries", task.getName());
        assertEquals(testDateA, task.getDueDate());
        assertEquals(2, task.getWeight());
    }

    @Test
    public void testChangeName(){
        task = new Task("Groceries");
        assertEquals("Groceries", task.getName());
        assertEquals(zeroDate,task.getDueDate());
        assertEquals(DEFAULT_WEIGHT, task.getWeight());

        task.setName("Volunteering");
        assertEquals("Volunteering", task.getName());
        assertEquals(zeroDate,task.getDueDate());
        assertEquals(DEFAULT_WEIGHT, task.getWeight());
    }

    @Test
    public void testSetDueDate(){
        task = new Task("Groceries");
        assertEquals("Groceries", task.getName());
        assertEquals(zeroDate,task.getDueDate());
        assertEquals(DEFAULT_WEIGHT, task.getWeight());

        task.setDueDate(testDateA);
        assertEquals("Groceries", task.getName());
        assertEquals(testDateA, task.getDueDate());
        assertEquals(DEFAULT_WEIGHT, task.getWeight());
    }

    @Test
    public void testChangeDueDate(){
        task = new Task("Groceries", testDateA);
        assertEquals("Groceries", task.getName());
        assertEquals(testDateA, task.getDueDate());
        assertEquals(DEFAULT_WEIGHT, task.getWeight());

        task.setDueDate(testDateB);
        assertEquals("Groceries", task.getName());
        assertEquals(testDateB, task.getDueDate());
        assertEquals(DEFAULT_WEIGHT, task.getWeight());
    }

    @Test
    public void testSetWeight(){
        task = new Task("Groceries");
        assertEquals("Groceries", task.getName());
        assertEquals(zeroDate,task.getDueDate());
        assertEquals(DEFAULT_WEIGHT, task.getWeight());

        task.setWeight(4);
        assertEquals("Groceries", task.getName());
        assertEquals(zeroDate,task.getDueDate());
        assertEquals(4, task.getWeight());
    }

    @Test
    public void testChangeWeight(){
        task = new Task("Groceries", testDateA, 5);
        assertEquals("Groceries", task.getName());
        assertEquals(testDateA, task.getDueDate());
        assertEquals(5, task.getWeight());

        task.setWeight(1);
        assertEquals("Groceries", task.getName());
        assertEquals(testDateA, task.getDueDate());
        assertEquals(1, task.getWeight());
    }

}
