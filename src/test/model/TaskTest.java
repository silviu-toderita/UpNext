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
public class TaskTest {

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
            fail("LabelLengthException thrown when label was long enough.");
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
        assertEquals(blankDate.getTime() / 1000,taskNoDueDate.getDueDate().getTime() / 1000);
    }

    @Test
    public void testMakeTaskNoDueDateEmptyLabel() {
        try {
            taskNoDueDate = new Task("");
            fail("Uncaught exception: LabelLengthException");
        } catch (LabelLengthException e) {
            return;
        }
    }

    @Test
    public void testMakeTaskWithDueDate() {
        try {
            taskDueDate = new Task("Groceries", testDateA);
        } catch (LabelLengthException e) {
            fail("LabelLengthException thrown when label was long enough.");
        }
        assertEquals("Groceries", taskDueDate.getLabel());
        assertEquals(testDateA, taskDueDate.getDueDate());
    }

    @Test
    public void testMakeTaskWithDueDateEmptyLabel() {
        try {
            taskDueDate = new Task("",testDateA);
            fail("Uncaught exception: LabelLengthException");
        } catch (LabelLengthException e) {
            return;
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
        } catch (InvalidJsonException e) {
            fail("Invalid JSON!");
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
            fail("Uncaught InvalidJsonException!");
        } catch (InvalidJsonException e) {
            return;
        }
    }

    @Test
    public void testChangeLabel() {
        assertEquals("Groceries", taskNoDueDate.getLabel());

        try {
            taskNoDueDate.setLabel("Volunteering");
        } catch (LabelLengthException e) {
            fail("LabelLengthException thrown when label was long enough.");
        }

        assertEquals("Volunteering", taskNoDueDate.getLabel());
        assertEquals(blankDate.getTime() / 1000,taskNoDueDate.getDueDate().getTime() / 1000);
    }

    @Test
    public void testChangeLabelEmptyLabel() {
        assertEquals("Groceries", taskNoDueDate.getLabel());
        assertEquals(blankDate.getTime(),taskNoDueDate.getDueDate().getTime());

        try {
            taskNoDueDate.setLabel("");
            fail("Uncaught exception: LabelLengthException");
        } catch (LabelLengthException e) {
            assertEquals("Groceries", taskNoDueDate.getLabel());
            assertEquals(blankDate,taskNoDueDate.getDueDate());
        }

    }

    @Test
    public void testChangeLabelTwice() {
        try {
            taskNoDueDate.setLabel("Volunteering");
        } catch (LabelLengthException e) {
            fail("LabelLengthException thrown when label was long enough.");
        }
        assertEquals("Volunteering", taskNoDueDate.getLabel());

        try {
            taskNoDueDate.setLabel("Shenanigans");
        } catch (LabelLengthException e) {
            fail("LabelLengthException thrown when label was long enough.");
        }
        assertEquals("Shenanigans", taskNoDueDate.getLabel());
    }

    @Test
    public void testSetDueDate() {
        assertEquals("Groceries", taskNoDueDate.getLabel());
        assertEquals(blankDate.getTime() / 1000,taskNoDueDate.getDueDate().getTime() / 1000);

        taskNoDueDate.setDueDate(testDateA);
        assertEquals("Groceries", taskNoDueDate.getLabel());
        assertEquals(testDateA, taskNoDueDate.getDueDate());
    }

    @Test
    public void testChangeDueDate() {
        try {
            taskDueDate = new Task("Groceries", testDateA);
        } catch (LabelLengthException e) {
            fail("LabelLengthException thrown when label was long enough.");
        }
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
        try {
            taskNoDueDate.setLabel("shenanigans");
        } catch (LabelLengthException e) {
            fail("LabelLengthException thrown when label was long enough.");
        }
        assertEquals("shenanigans", taskNoDueDate.getLabel());
        assertEquals(testDateA, taskNoDueDate.getDueDate());
    }

    @Test
    public void testGetDueDateStringEmpty() {
        assertEquals("No Due Date",taskNoDueDate.getDueDateString());
    }

    @Test
    public void testGetDueDateString() {
        try {
            taskDueDate = new Task("Homework", testDateA);
        } catch (LabelLengthException e) {
            fail("LabelLengthException thrown when label was long enough.");
        }
        assertEquals("Mon 23 Mar 2020",taskDueDate.getDueDateString());
    }

    @Test
    public void testGetDaysUntilDueFuture() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 100);
        taskNoDueDate.setDueDate(cal.getTime());
        assertEquals(99, taskNoDueDate.getDaysUntilDue());
    }

    @Test
    public void testGetDaysUntilDueToday() {
        Calendar calToday = Calendar.getInstance();
        calToday.add(Calendar.HOUR, 1);
        try {
            taskDueDate = new Task("Shenanigans",calToday.getTime());
        } catch (LabelLengthException e) {
            fail("LabelLengthException thrown when label was long enough.");
        }

        assertEquals(0,taskDueDate.getDaysUntilDue());
    }

    @Test
    public void testMakeJsonObject() {
        try {
            taskDueDate = new Task("Groceries", testDateA);
        } catch (LabelLengthException e) {
            fail("LabelLengthException thrown when label was long enough.");
        }

        JSONObject jsonObject = taskDueDate.makeJsonObject();

        try {
            Task newTask = new Task(jsonObject);
            assertEquals("Groceries", newTask.getLabel());
            assertEquals(testDateA.getTime() / 1000, newTask.getDueDate().getTime() / 1000);
        } catch (InvalidJsonException e) {
            fail("Invalid JSON!");
        }

    }

    @Test
    public void testMakeJsonObjectNoDueDate() {
        JSONObject jsonObject = taskNoDueDate.makeJsonObject();

        try {
            Task newTask = new Task(jsonObject);
            assertEquals("Groceries", newTask.getLabel());
            assertEquals(blankDate.getTime(), newTask.getDueDate().getTime());
        } catch (InvalidJsonException e) {
            fail("Invalid JSON!");
        }

    }

    @Test
    public void testGetDaysUntilDuePast() {
        Calendar calPast = Calendar.getInstance();
        calPast.add(Calendar.DATE,-5);
        try {
            taskDueDate = new Task("Shenanigans",calPast.getTime());
        } catch (LabelLengthException e) {
            fail("LabelLengthException thrown when label was long enough.");
        }

        assertEquals(-5,taskDueDate.getDaysUntilDue());
    }

    @Test
    public void testCompareToLaterTask() {
        try {
            taskDueDate = new Task("Shenanigans",testDateA);
        } catch (LabelLengthException e) {
            fail("LabelLengthException thrown when label was long enough.");
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
            fail("LabelLengthException thrown when label was long enough.");
        }
    }

    @Test
    public void testCompareToPastTask() {
        try {
            taskDueDate = new Task("Shenanigans",testDateA);
        } catch (LabelLengthException e) {
            fail("LabelLengthException thrown when label was long enough.");
        }
        assertEquals(1,taskNoDueDate.compareTo(taskDueDate));
    }

}
