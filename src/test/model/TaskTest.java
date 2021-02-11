package model;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

// Tests for Task class
public class TaskTest {

    Task taskNoDueDate;
    Task taskDueDate;
    Date testDateA;
    Date testDateB;
    Date blankDate;

    @Before
    public void setUp() {
        taskNoDueDate = new Task("Groceries");
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.YEAR,10);
        blankDate = cal.getTime();
        cal.set(2020,Calendar.MARCH,23);
        testDateA = cal.getTime();
        cal.set(2021,Calendar.APRIL,20);
        testDateB = cal.getTime();
    }

    @Test
    public void testMakeTaskNoDueDate() {
        assertEquals("Groceries", taskNoDueDate.getLabel());
        assertEquals(blankDate,taskNoDueDate.getDueDate());
    }

    @Test
    public void testMakeTaskWithDueDate() {
        taskDueDate = new Task("Groceries", testDateA);
        assertEquals("Groceries", taskDueDate.getLabel());
        assertEquals(testDateA, taskDueDate.getDueDate());
    }

    @Test
    public void testChangeLabel() {
        assertEquals("Groceries", taskNoDueDate.getLabel());
        assertEquals(blankDate,taskNoDueDate.getDueDate());

        taskNoDueDate.setLabel("Volunteering");
        assertEquals("Volunteering", taskNoDueDate.getLabel());
        assertEquals(blankDate,taskNoDueDate.getDueDate());
    }

    @Test

    public void testChangeLabelTwice() {
        taskNoDueDate.setLabel("Volunteering");
        assertEquals("Volunteering", taskNoDueDate.getLabel());

        taskNoDueDate.setLabel("Shenanigans");
        assertEquals("Shenanigans", taskNoDueDate.getLabel());
    }

    @Test
    public void testSetDueDate() {
        assertEquals("Groceries", taskNoDueDate.getLabel());
        assertEquals(blankDate,taskNoDueDate.getDueDate());

        taskNoDueDate.setDueDate(testDateA);
        assertEquals("Groceries", taskNoDueDate.getLabel());
        assertEquals(testDateA, taskNoDueDate.getDueDate());
    }

    @Test
    public void testChangeDueDate() {
        taskDueDate = new Task("Groceries", testDateA);
        assertEquals("Groceries", taskDueDate.getLabel());
        assertEquals(testDateA, taskDueDate.getDueDate());

        taskDueDate.setDueDate(testDateB);
        assertEquals("Groceries", taskDueDate.getLabel());
        assertEquals(testDateB, taskDueDate.getDueDate());
    }

    @Test
    public void testSetAndChangeDueDate() {
        taskNoDueDate.setDueDate(testDateB);
        assertEquals("Groceries", taskNoDueDate.getLabel());
        assertEquals(testDateB, taskNoDueDate.getDueDate());

        taskNoDueDate.setDueDate(testDateA);
        assertEquals("Groceries", taskNoDueDate.getLabel());
        assertEquals(testDateA, taskNoDueDate.getDueDate());
    }

    @Test
    public void testChangeDueDateAndLabel() {
        taskNoDueDate.setDueDate(testDateA);
        taskNoDueDate.setLabel("shenanigans");
        assertEquals("shenanigans", taskNoDueDate.getLabel());
        assertEquals(testDateA, taskNoDueDate.getDueDate());
    }

    @Test
    public void testGetDueDateStringEmpty() {
        assertEquals("No Due Date",taskNoDueDate.getDueDateString());
    }

    @Test
    public void testGetDueDateString() {
        taskDueDate = new Task("Homework", testDateA);
        assertEquals("Mon 23 Mar 2020",taskDueDate.getDueDateString());
    }

    @Test
    public void testGetDaysUntilDueFuture() {
        assertTrue(taskNoDueDate.getDaysUntilDue() == 3652 | taskNoDueDate.getDaysUntilDue() == 3651);
    }

    @Test
    public void testGetDaysUntilDueToday() {
        Calendar calToday = Calendar.getInstance();
        taskDueDate = new Task("Shenanigans",calToday.getTime());

        assertEquals(0,taskDueDate.getDaysUntilDue());
    }

    @Test
    public void testGetDaysUntilDuePast() {
        Calendar calPast = Calendar.getInstance();
        calPast.add(Calendar.DATE,-5);
        taskDueDate = new Task("Shenanigans",calPast.getTime());

        assertEquals(-5,taskDueDate.getDaysUntilDue());
    }

    @Test
    public void testCompareToLaterTask() {
        taskDueDate = new Task("Shenanigans",testDateA);
        assertEquals(-1,taskDueDate.compareTo(taskNoDueDate));
    }

    @Test
    public void testCompareToSameDayTask() {
        taskDueDate = new Task("Shenanigans",testDateA);
        Task taskDueDateSame = new Task("Homework",testDateA);
        assertEquals(0,taskDueDate.compareTo(taskDueDateSame));
    }

    @Test
    public void testCompareToPastTask() {
        taskDueDate = new Task("Shenanigans",testDateA);
        assertEquals(1,taskNoDueDate.compareTo(taskDueDate));
    }

}
