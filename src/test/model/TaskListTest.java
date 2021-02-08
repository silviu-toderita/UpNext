package model;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TaskListTest {

    TaskList taskList;
    Task testTaskA;
    Task testTaskB;
    Task testTaskC;

    @Before
    public void setUp(){
        taskList = new TaskList();
        Date testDate = new Date(2020,03,23);
        testTaskA = new Task("Groceries", testDate, 3);
        testTaskB = new Task("Volunteering", testDate, 2);
        testTaskC = new Task("CPSC 210 Project", testDate, 5);

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
    public void testCompleteOnlyTask(){
        taskList.add(testTaskB);
        assertEquals(1, taskList.size());

        taskList.complete(0);
        assertEquals(0, taskList.size());
    }

    @Test
    public void testCompleteOneTaskOfMany(){
        taskList.add(testTaskA);
        taskList.add(testTaskB);
        assertEquals(2, taskList.size());
        assertEquals(testTaskA, taskList.get(0));

        taskList.complete(0);
        assertEquals(1, taskList.size());
        assertEquals(testTaskB, taskList.get(0));
    }

    @Test
    public void testCompleteTwoTasksOfMany(){
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
    public void testCompleteManyTasks(){
        taskList.add(testTaskA);
        taskList.add(testTaskB);
        taskList.add(testTaskC);
        assertEquals(3, taskList.size());

        taskList.complete(2);
        taskList.complete(0);
        taskList.complete(1);
        assertEquals(0, taskList.size());
        assertEquals(testTaskB, taskList.get(0));
    }

    @Test
    public void testAddTaskAndCompleteTwice(){
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
    public void testAddTasksAndCompleteTasksMultiple(){
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
    public void testFindTaskEmptyList(){
        assertEquals(-1, taskList.find("Groceries"));
    }

    @Test
    public void testFindOnlyTask(){
        taskList.add(testTaskA);
        assertEquals(0, taskList.find("Groceries"));
    }

    @Test
    public void testCantFindOnlyTask(){
        taskList.add(testTaskA);
        assertEquals(-1, taskList.find("Volunteering"));
    }

    @Test
    public void testFindTaskAmongMultiple(){
        taskList.add(testTaskA);
        taskList.add(testTaskC);
        taskList.add(testTaskB);
        assertEquals(1, taskList.find("Volunteering"));
    }

    @Test
    public void testCantFindTaskAmongMultiple(){
        taskList.add(testTaskA);
        taskList.add(testTaskB);
        taskList.add(testTaskC);
        assertEquals(-1, taskList.find("ENGL 301 Homework"));
    }

    @Test
    public void testFindTaskAndComplete(){
        taskList.add(testTaskA);
        taskList.add(testTaskB);
        taskList.add(testTaskC);
        assertEquals(3, taskList.size());

        int id = taskList.find("Volunteering");
        assertEquals(1, id);
        assertEquals(testTaskB, taskList.get(id));
        taskList.complete(id);
        assertEquals(2, taskList.size());
    }

}