package model;

import exceptions.LabelLengthException;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.EqualityTests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;


// Tests for TaskList class
public class TaskListTest extends EqualityTests {

    public static final int THRESHOLD_DAYS = 30;

    private TaskList taskList;
    private Task testTaskA;
    private Task testTaskB;
    private Task testTaskC;

    @BeforeEach
    public void setUp() throws LabelLengthException {
        taskList = new TaskList();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR,1);
        Date testDate = cal.getTime();
        testTaskA = new Task("Groceries", testDate);
        testTaskB = new Task("Volunteering", testDate);
        testTaskC = new Task("CPSC 210 Project", testDate);

        assertEquals(0, taskList.size());
    }

    @Test
    public void testAddTaskEmptyList() {
        taskList.add(testTaskA);
        assertEquals(1, taskList.size());

        assertEquals(testTaskA, taskList.get(0));

    }

    @Test
    public void testAddMultipleTasks() {
        taskList.add(testTaskA);
        assertEquals(1, taskList.size());

        assertEquals(testTaskA, taskList.get(0));

        taskList.add(testTaskB);
        assertEquals(2, taskList.size());
        assertEquals(testTaskA, taskList.get(0));
        assertEquals(testTaskB, taskList.get(1));

        taskList.add(testTaskC);
        assertEquals(3, taskList.size());
        assertEquals(testTaskA, taskList.get(0));
        assertEquals(testTaskB, taskList.get(1));
        assertEquals(testTaskC, taskList.get(2));

    }

    @Test
    public void testCompleteOnlyTask() {
        taskList.add(testTaskB);
        assertEquals(1, taskList.size());

        taskList.complete(0);
        assertEquals(0, taskList.size());

    }

    @Test
    public void testCompleteNoTasks() {
        assertEquals(0, taskList.size());

        try {
            taskList.complete(0);
            fail("Expected Index Out Of Bounds Exception");
        } catch (IndexOutOfBoundsException e) {
            assertEquals(0, taskList.size());
        }
    }

    @Test
    public void testCompleteOneTaskOfMany() {
        taskList.add(testTaskA);
        taskList.add(testTaskB);
        assertEquals(2, taskList.size());

        assertEquals(testTaskA, taskList.get(0));
        taskList.complete(0);
        assertEquals(1, taskList.size());
        assertEquals(testTaskB, taskList.get(0));

    }

    @Test
    public void testCompleteTwoTasksOfMany() {
        taskList.add(testTaskA);
        taskList.add(testTaskB);
        taskList.add(testTaskC);
        assertEquals(3, taskList.size());

        assertEquals(testTaskA, taskList.get(0));
        taskList.complete(0);
        taskList.complete(1);
        assertEquals(1, taskList.size());
        assertEquals(testTaskB, taskList.get(0));

    }

    @Test
    public void testCompleteManyTasks() {
        taskList.add(testTaskA);
        taskList.add(testTaskB);
        taskList.add(testTaskC);
        assertEquals(3, taskList.size());

        taskList.complete(2);
        taskList.complete(0);
        taskList.complete(0);
        assertEquals(0, taskList.size());

    }

    @Test
    public void testAddTaskAndCompleteTwice() {
        taskList.add(testTaskC);
        assertEquals(1, taskList.size());

        taskList.complete(0);
        assertEquals(0,taskList.size());

        taskList.add(testTaskB);
        assertEquals(1, taskList.size());
        assertEquals(testTaskB, taskList.get(0));

        taskList.complete(0);
        assertEquals(0,taskList.size());

    }

    @Test
    public void testAddTasksAndCompleteTasksMultiple() {
        taskList.add(testTaskC);
        taskList.add(testTaskA);
        assertEquals(2, taskList.size());

        taskList.complete(1);
        assertEquals(1,taskList.size());
        assertEquals(testTaskC, taskList.get(0));

        taskList.add(testTaskB);
        taskList.add(testTaskA);
        assertEquals(3, taskList.size());
        assertEquals(testTaskB, taskList.get(1));

        taskList.complete(2);
        taskList.complete(0);
        assertEquals(1,taskList.size());
        assertEquals(testTaskB, taskList.get(0));

    }

    @Test
    public void testSortEmpty() {
        assertEquals(0,taskList.size());

        taskList.sort();
        assertEquals(0,taskList.size());
    }

    @Test
    public void testSortOne() {
        taskList.add(testTaskA);

        assertEquals(1,taskList.size());
        assertEquals(testTaskA,taskList.get(0));

        taskList.sort();
        assertEquals(1,taskList.size());
        assertEquals(testTaskA,taskList.get(0));

    }

    @Test
    public void testSortThreeAllSame() {
        taskList.add(testTaskA);
        taskList.add(testTaskB);
        taskList.add(testTaskC);
        assertEquals(3,taskList.size());

        assertEquals(testTaskA,taskList.get(0));
        assertEquals(testTaskB,taskList.get(1));
        assertEquals(testTaskC,taskList.get(2));

        taskList.sort();
        assertEquals(3,taskList.size());
        assertEquals(testTaskA,taskList.get(0));
        assertEquals(testTaskB,taskList.get(1));
        assertEquals(testTaskC,taskList.get(2));

    }

    @Test
    public void testSortThreeTwoSame() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,5);
        testTaskB.setDueDate(cal.getTime());

        taskList.add(testTaskA);
        taskList.add(testTaskB);
        taskList.add(testTaskC);
        assertEquals(3,taskList.size());

        assertEquals(testTaskA,taskList.get(0));
        assertEquals(testTaskB,taskList.get(1));
        assertEquals(testTaskC,taskList.get(2));

        taskList.sort();
        assertEquals(3,taskList.size());
        assertEquals(testTaskA,taskList.get(0));
        assertEquals(testTaskC,taskList.get(1));
        assertEquals(testTaskB,taskList.get(2));

    }

    @Test
    public void testSortThreeDifferent() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,5);
        testTaskA.setDueDate(cal.getTime());
        cal.add(Calendar.DATE,-10);
        testTaskB.setDueDate(cal.getTime());

        taskList.add(testTaskA);
        taskList.add(testTaskB);
        taskList.add(testTaskC);
        assertEquals(3,taskList.size());

        assertEquals(testTaskA,taskList.get(0));
        assertEquals(testTaskB,taskList.get(1));
        assertEquals(testTaskC,taskList.get(2));

        taskList.sort();
        assertEquals(3,taskList.size());
        assertEquals(testTaskB,taskList.get(0));
        assertEquals(testTaskC,taskList.get(1));
        assertEquals(testTaskA,taskList.get(2));

    }

    @Test
    public void testMaxDaysUntilDueOneTask() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,5);
        testTaskA.setDueDate(cal.getTime());

        taskList.add(testTaskA);
        assertEquals(5,taskList.getMaxDaysUntilDue(THRESHOLD_DAYS));
    }

    @Test
    public void testMaxDaysUntilDueThreeTasksTwoSame() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,7);
        testTaskA.setDueDate(cal.getTime());
        testTaskC.setDueDate(cal.getTime());

        taskList.add(testTaskA);
        taskList.add(testTaskB);
        taskList.add(testTaskC);
        assertEquals(7,taskList.getMaxDaysUntilDue(THRESHOLD_DAYS));
    }

    @Test
    public void testMaxDaysUntilDueThreeTasksDifferent() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,2);
        testTaskA.setDueDate(cal.getTime());
        cal.add(Calendar.DATE,-15);
        testTaskB.setDueDate(cal.getTime());

        taskList.add(testTaskA);
        taskList.add(testTaskB);
        taskList.add(testTaskC);
        assertEquals(2,taskList.getMaxDaysUntilDue(THRESHOLD_DAYS));
    }

    @Test
    public void testMaxDaysUntilDueThreeTasksDifferentAllNegative() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-4);
        testTaskA.setDueDate(cal.getTime());
        cal.add(Calendar.DATE,-15);
        testTaskB.setDueDate(cal.getTime());
        cal.add(Calendar.DATE,-100);
        testTaskC.setDueDate(cal.getTime());

        taskList.add(testTaskA);
        taskList.add(testTaskB);
        taskList.add(testTaskC);
        assertEquals(0,taskList.getMaxDaysUntilDue(THRESHOLD_DAYS));
    }

    @Test
    public void testMaxDaysUntilDueThreeTasksOneFarFuture() throws LabelLengthException {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-15);
        testTaskB.setDueDate(cal.getTime());
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE,100);
        testTaskC.setDueDate(cal.getTime());
        testTaskA = new Task("Groceries");

        taskList.add(testTaskA);
        taskList.add(testTaskB);
        taskList.add(testTaskC);
        assertEquals(0,taskList.getMaxDaysUntilDue(THRESHOLD_DAYS));
    }

    @Test
    public void testGetMaxLabelLengthNoTasks() {
        assertEquals(0,taskList.getMaxLabelLength());
    }

    @Test
    public void testGetMaxLabelLengthOneTask() {
        taskList.add(testTaskB);

        assertEquals(12,taskList.getMaxLabelLength());
    }

    @Test
    public void testGetMaxLabelLengthThreeTasksAllDifferent() {
        taskList.add(testTaskA);
        taskList.add(testTaskB);
        taskList.add(testTaskC);

        assertEquals(16,taskList.getMaxLabelLength());
    }

    @Test
    public void testGetMaxLabelLengthThreeTasksTwoSameMax() throws LabelLengthException {
        taskList.add(testTaskA);
        testTaskB.setLabel("CPEN 311 Project");
        taskList.add(testTaskB);
        taskList.add(testTaskC);

        assertEquals(16,taskList.getMaxLabelLength());
    }

    @Test
    public void testGetMaxLabelLengthThreeTasksTwoSameNotMax() throws LabelLengthException {
        taskList.add(testTaskA);
        testTaskB.setLabel("Gardening");
        taskList.add(testTaskB);
        taskList.add(testTaskC);

        assertEquals(16,taskList.getMaxLabelLength());
    }

    @Test
    public void testSerializeEmpty() {
        assertEquals(0,taskList.size());
        JSONArray jsonArray = taskList.serialize();
        assertTrue(jsonArray.isEmpty());
    }

    @Test
    public void testSerializeSingleTask() {
        taskList.add(testTaskA);
        JSONArray jsonArray = taskList.serialize();
        assertEquals(1,jsonArray.length());


        Task newTask = new Task(jsonArray.getJSONObject(0));
        assertEquals(testTaskA.getLabel(), newTask.getLabel());
        assertEqualDate(testTaskA.getDueDate(), newTask.getDueDate());

    }

    @Test
    public void testSerializeMultipleTasks() {
        taskList.add(testTaskA);
        taskList.add(testTaskB);
        taskList.add(testTaskC);
        JSONArray jsonArray = taskList.serialize();
        assertEquals(3,jsonArray.length());

        Task newTaskA = new Task(jsonArray.getJSONObject(0));
        Task newTaskB = new Task(jsonArray.getJSONObject(1));
        Task newTaskC = new Task(jsonArray.getJSONObject(2));
        assertEqualTask(testTaskA, newTaskA);
        assertEqualTask(testTaskB, newTaskB);
        assertEqualTask(testTaskC, newTaskC);

    }

}