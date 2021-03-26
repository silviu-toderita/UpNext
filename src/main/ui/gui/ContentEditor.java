package ui.gui;

import com.toedter.calendar.JDateChooser;
import exceptions.InvalidJsonFileException;
import exceptions.LabelLengthException;
import model.Task;
import model.TaskList;
import persistence.ReaderWriter;
import ui.UpNext;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

public class ContentEditor {

    private static final String SAVE_DATA_PATH = "./data/savedata.json";

    private TaskList taskList;
    private UpNext parent;
    private ReaderWriter readerWriter;

    public ContentEditor(TaskList taskList, UpNext parent) {
        this.taskList = taskList;
        this.parent = parent;
        readerWriter = new ReaderWriter(SAVE_DATA_PATH);
    }

    // MODIFIES: this
    // EFFECTS: Loads tasks from memory, displays error dialog for any errors that occur
    public TaskList loadTasks() {
        try {
            taskList = readerWriter.read();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to read from file: " + SAVE_DATA_PATH,
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidJsonFileException e) {
            JOptionPane.showMessageDialog(null, "Invalid File: " + SAVE_DATA_PATH
                    + ", Deleting...","Error", JOptionPane.ERROR_MESSAGE);
        }

        return taskList;
    }

    private void saveTasks() {
        try {
            readerWriter.write(taskList);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "Unable to write to file: " + readerWriter.getPath(),"Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        parent.renderWindow();
    }

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

    public void editDate(Task task, boolean noDueDate) {
        JDateChooser dateChooser = new JDateChooser();
        if (noDueDate) {
            dateChooser.setDate(Calendar.getInstance().getTime());
        } else {
            dateChooser.setDate(task.getDueDate());
        }

        String message;
        if (noDueDate) {
            message = "Enter Due Date or Press Cancel for None";
        } else {
            message = "Enter Due Date or Press Cancel to Delete Date";
        }

        int result = JOptionPane.showConfirmDialog(parent, new Object[] {message, dateChooser}, task.getDueDateString(),
                JOptionPane.OK_CANCEL_OPTION);

        if (result == 2) {
            task.removeDueDate();
        } else {
            task.setDueDate(dateChooser.getDate());
        }
        saveTasks();
    }

    public void removeTask(Task task) {
        taskList.complete(task);
        saveTasks();
    }
}
