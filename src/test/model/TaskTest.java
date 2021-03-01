package model;

import exceptions.InvalidJsonException;
import exceptions.LabelLengthException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;

// Tests for Task class
public class TaskTest extends DateTest {

    Task taskNoDueDate;
    Task taskDueDate;
    Date testDateA;
    Date testDateB;
    Date blankDate;

    @BeforeEach
    public void setUp() {
        try {
            taskNoDueDate = new Task("Groceries");
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }
        Calendar cal = Calendar.getInstance();

        cal.set(2030,Calendar.DECEMBER,31,23,59,59);
        blankDate = cal.getTime();
        cal.set(2020,Calendar.MARCH,23,23,59,59);
        testDateA = cal.getTime();
        cal.set(2021,Calendar.APRIL,20,23,59,59);
        testDateB = cal.getTime();
    }

    @Test
    public void testMakeTaskNoDueDate() {
        assertEquals("Groceries", taskNoDueDate.getLabel());
        assertEqualDate(blankDate,taskNoDueDate.getDueDate());
    }

    @Test
    public void testMakeTaskNoDueDateEmptyLabel() {
        try {
            taskNoDueDate = new Task("");
            fail("Expected Label Length Exception");
        } catch (LabelLengthException e) {
            // Expected Behaviour
        }
    }

    @Test
    public void testMakeTaskWithDueDate() {
        try {
            taskDueDate = new Task("Groceries", testDateA);
            // Expected Behaviour
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }
        assertEquals("Groceries", taskDueDate.getLabel());
        assertEqualDate(testDateA, taskDueDate.getDueDate());
    }

    @Test
    public void testMakeTaskWithDueDateEmptyLabel() {
        try {
            taskDueDate = new Task("",testDateA);
            fail("Expected Label Length Exception");
        } catch (LabelLengthException e) {
            // Expected Behaviour
        }
    }

    @Test
    public void testMakeTaskJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("label","test label");
        jsonObject.put("year",2021);
        jsonObject.put("month",Calendar.APRIL);
        jsonObject.put("day",20);

        try {
            taskDueDate = new Task(jsonObject);
            assertEquals("test label",taskDueDate.getLabel());

            Calendar cal = Calendar.getInstance();
            cal.setTime(taskDueDate.getDueDate());
            assertEquals(2021, cal.get(Calendar.YEAR));
            assertEquals(Calendar.APRIL, cal.get(Calendar.MONTH));
            assertEquals(20, cal.get(Calendar.DATE));
            // Expected Behaviour
        } catch (InvalidJsonException e) {
            fail("Invalid JSON Exception");
        }
    }

    @Test
    public void testMakeTaskJSONInvalid() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("label","test label");
        jsonObject.put("yer",2021);
        jsonObject.put("month",Calendar.APRIL);
        jsonObject.put("day",20);

        try {
            taskDueDate = new Task(jsonObject);
            fail("Expected Invalid JSON Exception");
        } catch (InvalidJsonException e) {
            // Expected Behaviour
        }
    }

    @Test
    public void testChangeLabel() {
        assertEquals("Groceries", taskNoDueDate.getLabel());

        try {
            taskNoDueDate.setLabel("Volunteering");
            // Expected Behaviour
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }

        assertEquals("Volunteering", taskNoDueDate.getLabel());
        assertEqualDate(blankDate,taskNoDueDate.getDueDate());
    }

    @Test
    public void testChangeLabelEmptyLabel() {
        assertEquals("Groceries", taskNoDueDate.getLabel());
        assertEqualDate(blankDate,taskNoDueDate.getDueDate());

        try {
            taskNoDueDate.setLabel("");
            fail("Expected Label Length Exception");
        } catch (LabelLengthException e) {
            assertEquals("Groceries", taskNoDueDate.getLabel());
            assertEqualDate(blankDate,taskNoDueDate.getDueDate());
            // Expected Behaviour
        }

    }

    @Test
    public void testChangeLabelTwice() {
        try {
            taskNoDueDate.setLabel("Volunteering");
            // Expected Behaviour
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
        assertEqualDate(blankDate,taskNoDueDate.getDueDate());

        taskNoDueDate.setDueDate(testDateA);
        assertEquals("Groceries", taskNoDueDate.getLabel());
        assertEqualDate(testDateA, taskNoDueDate.getDueDate());
    }

    @Test
    public void testChangeDueDate() {
        try {
            taskDueDate = new Task("Groceries", testDateA);
            // Expected Behaviour
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }
        assertEquals("Groceries", taskDueDate.getLabel());
        assertEqualDate(testDateA, taskDueDate.getDueDate());

        taskDueDate.setDueDate(testDateB);
        assertEquals("Groceries", taskDueDate.getLabel());
        assertEqualDate(testDateB, taskDueDate.getDueDate());
    }

    @Test
    public void testSetAndChangeDueDate() {
        taskNoDueDate.setDueDate(testDateB);
        assertEquals("Groceries", taskNoDueDate.getLabel());
        assertEqualDate(testDateB, taskNoDueDate.getDueDate());

        taskNoDueDate.setDueDate(testDateA);
        assertEquals("Groceries", taskNoDueDate.getLabel());
        assertEqualDate(testDateA, taskNoDueDate.getDueDate());
    }

    @Test
    public void testChangeDueDateAndLabel() {
        taskNoDueDate.setDueDate(testDateA);
        try {
            taskNoDueDate.setLabel("shenanigans");
            // Expected Behaviour
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }
        assertEquals("shenanigans", taskNoDueDate.getLabel());
        assertEqualDate(testDateA, taskNoDueDate.getDueDate());
    }

    @Test
    public void testGetDueDateStringEmpty() {
        assertEquals("No Due Date",taskNoDueDate.getDueDateString());
    }

    @Test
    public void testGetDueDateString() {
        try {
            taskDueDate = new Task("Homework", testDateA);
            // Expected Behaviour
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }
        assertEquals("Mon 23 Mar 2020",taskDueDate.getDueDateString());
    }

    @Test
    public void testGetDaysUntilDueFuture() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 100);
        taskNoDueDate.setDueDate(cal.getTime());
        assertEquals(100, taskNoDueDate.getDaysUntilDue());
    }

    @Test
    public void testGetDaysUntilDueToday() {
        Calendar calToday = Calendar.getInstance();
        calToday.add(Calendar.HOUR, 1);
        try {
            taskDueDate = new Task("Shenanigans",calToday.getTime());
            // Expected Behaviour
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }

        assertEquals(0,taskDueDate.getDaysUntilDue());
    }

    @Test
    public void testMakeJsonObject() {
        try {
            taskDueDate = new Task("Groceries", testDateA);
            // Expected Behaviour
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }

        JSONObject jsonObject = taskDueDate.makeJsonObject();

        try {
            Task newTask = new Task(jsonObject);
            assertEquals("Groceries", newTask.getLabel());
            assertEqualDate(testDateA, newTask.getDueDate());
            // Expected Behaviour
        } catch (InvalidJsonException e) {
            fail("Invalid JSON Exception");
        }

    }

    @Test
    public void testMakeJsonObjectNoDueDate() {
        JSONObject jsonObject = taskNoDueDate.makeJsonObject();

        try {
            Task newTask = new Task(jsonObject);
            assertEquals("Groceries", newTask.getLabel());
            assertEqualDate(blankDate, newTask.getDueDate());
            // Expected Behaviour
        } catch (InvalidJsonException e) {
            fail("Invalid JSON Exception");
        }

    }

    @Test
    public void testGetDaysUntilDuePast() {
        Calendar calPast = Calendar.getInstance();
        calPast.add(Calendar.DATE,-5);
        try {
            taskDueDate = new Task("Shenanigans",calPast.getTime());
            // Expected Behaviour
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }

        assertEquals(-4,taskDueDate.getDaysUntilDue());
    }

    @Test
    public void testCompareToLaterTask() {
        try {
            taskDueDate = new Task("Shenanigans",testDateA);
            // Expected Behaviour
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
            // Expected Behaviour
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }
    }

    @Test
    public void testCompareToPastTask() {
        try {
            taskDueDate = new Task("Shenanigans",testDateA);
            // Expected Behaviour
        } catch (LabelLengthException e) {
            fail("Label Length Exception");
        }
        assertEquals(1,taskNoDueDate.compareTo(taskDueDate));
    }

}
