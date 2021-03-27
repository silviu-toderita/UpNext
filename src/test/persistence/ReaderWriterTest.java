package persistence;

import exceptions.DuplicateTaskException;
import exceptions.InvalidJsonFileException;
import exceptions.LabelLengthException;
import test.EqualityTests;
import model.Task;
import model.TaskList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ReaderWriterTest extends EqualityTests {

    public static final String VALID_PATH = "./data/test.json";

    private ReaderWriter readerWriter;
    private TaskList taskListEmpty;
    private TaskList taskListOneTask;
    private TaskList taskListThreeTasks;
    private Task testTaskA;
    private Task testTaskB;
    private Task testTaskC;

    @BeforeEach
    public void setUp() throws LabelLengthException {
        taskListEmpty = new TaskList();
        taskListOneTask = new TaskList();
        taskListThreeTasks = new TaskList();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR,1);
        Date testDate = cal.getTime();
        testTaskA = new Task("Groceries", testDate);
        testTaskB = new Task("Volunteering", testDate);
        testTaskC = new Task("CPSC 210 Project", testDate);

        try {
            taskListOneTask.add(testTaskA);
            taskListThreeTasks.add(testTaskA);
            taskListThreeTasks.add(testTaskB);
            taskListThreeTasks.add(testTaskC);
        } catch (DuplicateTaskException e) {
            fail("Unexpected DuplicateTaskException");
        }

    }

    @Test
    public void testWriteInvalidFile() {
        readerWriter = new ReaderWriter("./\ndata/test.___");
        try {
            readerWriter.write(taskListOneTask);
            fail("Expected File Not Found Exception");
        } catch (FileNotFoundException e) {
            // pass
        }

    }

    @Test
    public void testWriteAndReadEmptyTaskList() {
        readerWriter = new ReaderWriter(VALID_PATH);
        try {
            readerWriter.write(taskListEmpty);
        } catch (FileNotFoundException e) {
            fail("File Not Found Exception");
        }

        try {
            TaskList newTaskList = readerWriter.read();
            assertEquals(0,newTaskList.size());
        } catch (IOException e) {
            fail("IO Exception");
        } catch (InvalidJsonFileException e) {
            fail("Invalid JSON File Exception");
        }

        assertEquals(VALID_PATH, readerWriter.getPath());
    }

    @Test
    public void testWriteAndReadTaskListThreeTasks() {
        readerWriter = new ReaderWriter(VALID_PATH);
        try {
            readerWriter.write(taskListThreeTasks);
        } catch (FileNotFoundException e) {
            fail("File Not Found Exception");
        }

        try {
            TaskList newTaskList = readerWriter.read();
            assertEquals(3,newTaskList.size());
            assertEqualTask(testTaskA, newTaskList.get(0));
            assertEqualTask(testTaskB, newTaskList.get(1));
            assertEqualTask(testTaskC, newTaskList.get(2));
        } catch (IOException e) {
            fail("IO Exception");
        } catch (InvalidJsonFileException e) {
            fail("Invalid JSON File Exception");
        }
    }

    @Test
    public void testReadFileInvalidJson() throws FileNotFoundException {
        File file = new File(VALID_PATH);
        PrintWriter writer = new PrintWriter(file);
        writer.print("This is a bunch of non-JSON garbage!");
        writer.close();

        assertTrue(file.exists());

        readerWriter = new ReaderWriter(VALID_PATH);

        try {
            readerWriter.read();
            fail("Expected Invalid JSON File Exception");
        } catch (IOException e) {
            fail("IO Exception");
        } catch (InvalidJsonFileException e) {
            assertFalse(file.exists());
        }
    }

    @Test
    public void testReadFileNonExistent() {
        readerWriter = new ReaderWriter(VALID_PATH);

        try {
            readerWriter.read();
            fail("Expected IO Exception");
        } catch (InvalidJsonFileException e) {
            fail("Invalid JSON File Exception");
        } catch (IOException e) {
            // pass
        }

    }

    @AfterEach
    public void finishUp() {
        readerWriter = new ReaderWriter(VALID_PATH);
        readerWriter.delete();
    }
}