package model;


import exceptions.LabelLengthException;
import exceptions.NoDueDateException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.EqualityTests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;

// Tests for Task class
public class TaskTest extends EqualityTests {

    Task taskNoDueDate;
    Task taskDueDate;
    Date testDateA;
    Date testDateB;

    @BeforeEach
    public void setUp() {
        try {
            taskNoDueDate = new Task("Groceries");
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }
        Calendar cal = Calendar.getInstance();
        cal.set(2020,Calendar.MARCH,23);
        testDateA = cal.getTime();
        cal.set(2021,Calendar.APRIL,20);
        testDateB = cal.getTime();
    }

    @Test
    public void testMakeTaskNoDueDate() {
        assertEquals("Groceries", taskNoDueDate.getLabel());
        assertFalse(taskNoDueDate.getHasDueDate());
        try {
            taskNoDueDate.getDueDate();
            fail("Expected NoDueDateException");
        } catch (NoDueDateException e) {
            // Pass
        }
    }

    @Test
    public void testMakeTaskNoDueDateEmptyLabel() {
        try {
            taskNoDueDate = new Task("");
            fail("Expected Label Length Exception");
        } catch (LabelLengthException e) {
            // pass
        }
    }

    @Test
    public void testMakeTaskWithDueDate() {
        try {
            taskDueDate = new Task("Groceries", testDateA);
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }
        assertEquals("Groceries", taskDueDate.getLabel());

        try {
            assertEqualDate(testDateA, taskDueDate.getDueDate());
        } catch (NoDueDateException e) {
            fail("Unexpected NoDueDateException");
        }

        assertTrue(taskDueDate.getHasDueDate());
    }

    @Test
    public void testMakeTaskWithDueDateEmptyLabel() {
        try {
            taskDueDate = new Task("",testDateA);
            fail("Expected Label Length Exception");
        } catch (LabelLengthException e) {
            // pass
        }
    }

    @Test
    public void testMakeTaskJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("label","test label");
        jsonObject.put("year",2021);
        jsonObject.put("month",Calendar.APRIL);
        jsonObject.put("day",20);
        jsonObject.put("hasDueDate", true);

        taskDueDate = new Task(jsonObject);
        assertEquals("test label",taskDueDate.getLabel());

        Calendar cal = Calendar.getInstance();
        cal.set(2021,Calendar.APRIL,20);
        try {
            assertEqualDate(cal.getTime(), taskDueDate.getDueDate());
        } catch (NoDueDateException e) {
            fail("Unexpected NoDueDateException");
        }

    }

    @Test
    public void testMakeTaskJSONNoDueDate() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("label","test label");
        jsonObject.put("hasDueDate", false);

        taskDueDate = new Task(jsonObject);
        assertEquals("test label",taskDueDate.getLabel());
        assertFalse(taskDueDate.getHasDueDate());
        try {
            taskDueDate.getDueDate();
            fail("Expected NoDueDateException");
        } catch (NoDueDateException e) {
            // Pass
        }

    }

    @Test
    public void testChangeLabel() {
        assertEquals("Groceries", taskNoDueDate.getLabel());

        try {
            taskNoDueDate.setLabel("Volunteering");
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }

        assertEquals("Volunteering", taskNoDueDate.getLabel());
        assertFalse(taskNoDueDate.getHasDueDate());
    }

    @Test
    public void testChangeLabelEmptyLabel() {
        assertEquals("Groceries", taskNoDueDate.getLabel());

        try {
            taskNoDueDate.setLabel("");
            fail("Expected Label Length Exception");
        } catch (LabelLengthException e) {
            assertEquals("Groceries", taskNoDueDate.getLabel());
        }

    }

    @Test
    public void testChangeLabelTwice() {
        try {
            taskNoDueDate.setLabel("Volunteering");
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }
        assertEquals("Volunteering", taskNoDueDate.getLabel());

        try {
            taskNoDueDate.setLabel("Shenanigans");
            // Expected Behaviour
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }
        assertEquals("Shenanigans", taskNoDueDate.getLabel());
    }

    @Test
    public void testSetDueDate() {
        assertEquals("Groceries", taskNoDueDate.getLabel());
        assertFalse(taskNoDueDate.getHasDueDate());

        taskNoDueDate.setDueDate(testDateA);
        assertEquals("Groceries", taskNoDueDate.getLabel());
        try {
            assertEqualDate(testDateA, taskNoDueDate.getDueDate());
        } catch (NoDueDateException e) {
            fail("Unexpected NoDueDateException");
        }

        assertTrue(taskNoDueDate.getHasDueDate());
    }

    @Test
    public void testChangeDueDate() {
        try {
            taskDueDate = new Task("Groceries", testDateA);
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }
        assertEquals("Groceries", taskDueDate.getLabel());

        try {
            assertEqualDate(testDateA, taskDueDate.getDueDate());
        } catch (NoDueDateException e) {
            fail("Unexpected NoDueDateException");
        }
        assertTrue(taskDueDate.getHasDueDate());

        taskDueDate.setDueDate(testDateB);
        assertEquals("Groceries", taskDueDate.getLabel());
        try {
            assertEqualDate(testDateB, taskDueDate.getDueDate());
        } catch (NoDueDateException e) {
            fail("Unexpected NoDueDateException");
        }
        assertTrue(taskDueDate.getHasDueDate());

    }

    @Test
    public void testSetAndChangeDueDate() {
        taskNoDueDate.setDueDate(testDateB);
        assertEquals("Groceries", taskNoDueDate.getLabel());
        try {
            assertEqualDate(testDateB, taskNoDueDate.getDueDate());
        } catch (NoDueDateException e) {
            fail("Unexpected NoDueDateException");
        }
        assertTrue(taskNoDueDate.getHasDueDate());

        taskNoDueDate.setDueDate(testDateA);
        assertEquals("Groceries", taskNoDueDate.getLabel());
        try {
            assertEqualDate(testDateA, taskNoDueDate.getDueDate());
        } catch (NoDueDateException e) {
            fail("Unexpected NoDueDateException");
        }
        assertTrue(taskNoDueDate.getHasDueDate());
    }

    @Test
    public void testChangeDueDateAndLabel() {
        taskNoDueDate.setDueDate(testDateA);
        try {
            taskNoDueDate.setLabel("shenanigans");
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }
        assertEquals("shenanigans", taskNoDueDate.getLabel());
        try {
            assertEqualDate(testDateA, taskNoDueDate.getDueDate());
        } catch (NoDueDateException e) {
            fail("Unexpected NoDueDateException");
        }
        assertTrue(taskNoDueDate.getHasDueDate());
    }

    @Test
    public void testGetDueDateString() {
        try {
            taskDueDate = new Task("Homework", testDateA);
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }
        try {
            assertEquals("Mon 23 Mar 2020",taskDueDate.getDueDateString());
        } catch (NoDueDateException e) {
            fail("Unexpected NoDueDateException");
        }
    }

    @Test
    public void testGetDueDateStringNoDueDate() {
        try {
            taskNoDueDate.getDueDateString();
            fail("Expected NoDueDateException");
        } catch (NoDueDateException e) {
            // Pass
        }
    }

    @Test
    public void testGetDaysUntilDueFuture() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 100);
        taskNoDueDate.setDueDate(cal.getTime());
        try {
            assertEquals(100, taskNoDueDate.getDaysUntilDue());
        } catch (NoDueDateException e) {
            fail("Unexpected NoDueDateException");
        }

    }

    @Test
    public void testGetDaysUntilDueToday() {
        Calendar calToday = Calendar.getInstance();
        calToday.add(Calendar.HOUR, 1);
        try {
            taskDueDate = new Task("Shenanigans",calToday.getTime());
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }

        try {
            assertEquals(0,taskDueDate.getDaysUntilDue());
        } catch (NoDueDateException e) {
            fail("Unexpected NoDueDateException");
        }

    }

    @Test
    public void testGetDaysUntilDueNoDueDate() {
        try {
            taskNoDueDate.getDaysUntilDue();
            fail("Expected NoDueDateException");
        } catch (NoDueDateException e) {
            // Pass
        }
    }

    @Test
    public void testMakeJsonObject() {
        try {
            taskDueDate = new Task("Groceries", testDateA);
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }

        JSONObject jsonObject = taskDueDate.makeJsonObject();

        Task newTask = new Task(jsonObject);
        assertEquals("Groceries", newTask.getLabel());
        try {
            assertEqualDate(testDateA, newTask.getDueDate());
        } catch (NoDueDateException e) {
            fail("Unexpected NoDueDateException");
        }

    }

    @Test
    public void testMakeJsonObjectNoDueDate() {
        JSONObject jsonObject = taskNoDueDate.makeJsonObject();

        Task newTask = new Task(jsonObject);
        assertEquals("Groceries", newTask.getLabel());

        try {
            newTask.getDueDate();
            fail("Expected NoDueDateException");
        } catch (NoDueDateException e){
            assertFalse(newTask.getHasDueDate());
        }


    }

    @Test
    public void testGetDaysUntilDuePast() {
        Calendar calPast = Calendar.getInstance();
        calPast.add(Calendar.DATE,-5);
        try {
            taskDueDate = new Task("Shenanigans",calPast.getTime());
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }

        try {
            assertEquals(-5,taskDueDate.getDaysUntilDue());
        } catch (NoDueDateException e) {
            fail("Unexpected NoDueDateException");
        }
    }

    @Test
    public void testCompareToLaterTask() {
        try {
            taskDueDate = new Task("Shenanigans",testDateA);
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }
        assertEquals(-1,taskDueDate.compareTo(taskNoDueDate));
    }

    @Test
    public void testCompareToSameDayTask() {
        try {
            taskDueDate = new Task("Shenanigans",testDateA);
            Task taskDueDateSame = new Task("Homework",testDateA);
            assertEquals(0,taskDueDate.compareTo(taskDueDateSame));
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }
    }

    @Test
    public void testCompareToPastTask() {
        try {
            taskDueDate = new Task("Shenanigans",testDateA);
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }
        assertEquals(1,taskNoDueDate.compareTo(taskDueDate));
    }

    @Test
    public void testRemoveDueDate() {
        try {
            taskDueDate = new Task("Shenanigans",testDateA);
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }

        taskDueDate.removeDueDate();
        assertFalse(taskDueDate.getHasDueDate());
    }

}
