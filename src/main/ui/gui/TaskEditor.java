package ui.gui;

import com.toedter.calendar.JDateChooser;
import exceptions.InvalidJsonFileException;
import exceptions.LabelLengthException;
import exceptions.NoDueDateException;
import model.Task;
import model.TaskList;
import persistence.ReaderWriter;
import ui.UpNext;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

// Represents the editor for tasks in the task list
public class TaskEditor {

    private static final String SAVE_DATA_PATH = "./data/savedata.json";

    private TaskList taskList;
    private UpNext parent;
    private ReaderWriter readerWriter;

    // EFFECTS: Initialize the editor
    public TaskEditor(TaskList taskList, UpNext parent) {
        this.taskList = taskList;
        this.parent = parent;
        readerWriter = new ReaderWriter(SAVE_DATA_PATH);
    }

    // MODIFIES: this
    // EFFECTS: Loads tasks from memory, displays error dialog for any errors that occur
    public TaskList loadTasks() {
        parent.setSaveStatus("Loading Tasks...", Color.YELLOW);
        try {
            taskList = readerWriter.read();
            parent.setSaveStatus("Tasks Loaded!", Color.GREEN);
        } catch (IOException e) {
            parent.setSaveStatus("Unable to read from file: " + SAVE_DATA_PATH, Color.RED);
            taskList = new TaskList();
        } catch (InvalidJsonFileException e) {
            parent.setSaveStatus("Invalid file: " + SAVE_DATA_PATH + ", Deleting...", Color.RED);
            taskList = new TaskList();
        }
        return taskList;
    }

    // MODIFIES: this
    // EFFECTS: Saves tasks to memory, displays error dialog for any errors that occur. Triggers rendering of parent
    //          JFrame again
    private void saveTasks() {
        parent.setSaveStatus("Saving Tasks...", Color.YELLOW);
        parent.renderWindow();
        try {
            readerWriter.write(taskList);
            parent.setSaveStatus("Tasks Saved!", Color.GREEN);
        } catch (FileNotFoundException e) {
            parent.setSaveStatus("Unable to write to file: " + SAVE_DATA_PATH, Color.RED);
        }
        parent.renderWindow();
    }

    // MODIFIES: this
    // EFFECTS: Launches dialog requesting a label for the task.
    //          If this is a new task and cancel was pressed, throws NullPointerException to signal cancel creating task
    public void editLabel(Task task, Boolean newTask) throws NullPointerException {
        while (true) {
            String label = JOptionPane.showInputDialog(parent, "Enter Task Name:",task.getLabel());
            try {
                task.setLabel(label);
                saveTasks();
                break;
            } catch (LabelLengthException e) {
                JOptionPane.showMessageDialog(parent, "Name cannot be empty!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NullPointerException e) {
                if (newTask) {
                    throw new NullPointerException();
                }
                break;
            }
        }

    }

    // MODIFIES: this
    // EFFECTS: Launches dialog requesting a due date for the task. Defaults to today's date if task has no due date.
    public void editDate(Task task) {
        JDateChooser dateChooser = new JDateChooser();
        String message;

        try {
            dateChooser.setDate(task.getDueDate());
            message = "Enter Due Date or Press Cancel to Delete Date";
        } catch (NoDueDateException e) {
            dateChooser.setDate(Calendar.getInstance().getTime());
            message = "Enter Due Date or Press Cancel for None";
        }

        int result = JOptionPane.showConfirmDialog(parent, new Object[] {message, dateChooser},
                dateChooser.getDateFormatString(), JOptionPane.OK_CANCEL_OPTION);

        if (result == 2) {
            task.removeDueDate();
        } else {
            task.setDueDate(dateChooser.getDate());
        }
        saveTasks();
    }

    // MODIFIES: this
    // EFFECTS: Removes the given task from the task list
    public void removeTask(Task task) {
        taskList.complete(task);
        saveTasks();
    }
}
