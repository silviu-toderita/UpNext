package model;

import exceptions.DuplicateTaskException;
import exceptions.NoDueDateException;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Represents a list of Tasks
public class TaskList {
    private List<Task> taskList;

    // EFFECT: Create a new list of tasks
    public TaskList() {
        taskList = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: Adds a task to the task list
    //          If there is a task with the same name in the task list, throw DuplicateTask Exception
    public void add(Task task) throws DuplicateTaskException {
        for (Task each : taskList) {
            if (each.getLabel().equals(task.getLabel())) {
                throw new DuplicateTaskException();
            }
        }
        taskList.add(task);
    }

    // MODIFIES: this
    // EFFECTS: Removes task with given index from task list
    //          Throws IndexOutOfBoundsException if the given index does not exist in the list
    public void complete(int index) throws IndexOutOfBoundsException {
        taskList.remove(index);
    }

    // MODIFIES: this
    // EFFECTS: Removes given task from task list. If it's not in the list, do nothing.
    public void complete(Task task) {
        taskList.remove(task);
    }

    // MODIFIES: this
    // EFFECT: Sorts the task list in order from smallest due date to largest due date
    public void sort() {
        Collections.sort(taskList);
    }

    // EFFECTS: Returns the maximum number of days until a task is due. Tasks due > threshold days away are not counted.
    //          Always returns >=0, and will return 0 if no tasks in list.
    public int getMaxDaysUntilDue(int threshold) {
        int maxDaysLeft = 0;
        for (Task each: taskList) {
            int daysLeft;
            try {
                daysLeft = each.getDaysUntilDue();
            } catch (NoDueDateException e) {
                daysLeft = threshold + 1;
            }
            if (daysLeft > maxDaysLeft & daysLeft <= threshold) {
                maxDaysLeft = daysLeft;
            }
        }

        return maxDaysLeft;
    }

    // EFFECTS: Returns the char length of the longest task label. Returns 0 if there are no tasks in list
    public int getMaxLabelLength() {
        int maxLength = 0;
        for (Task each: taskList) {
            int taskLabelLength = each.getLabel().length();
            if (taskLabelLength > maxLength) {
                maxLength = taskLabelLength;
            }
        }

        return maxLength;
    }

    // EFFECTS: Generates a JSON array that represents this task list
    public JSONArray serialize() {
        JSONArray jsonArray = new JSONArray();
        for (Task each : taskList) {
            jsonArray.put(each.makeJsonObject());
        }

        return jsonArray;
    }

    // EFFECTS: Returns the size of the task list
    public int size() {
        return taskList.size();
    }

    // EFFECTS: Returns the task based on index
    //          Throws IndexOutOfBoundsException if the given index does not exist in the list
    public Task get(int index) throws IndexOutOfBoundsException {
        return taskList.get(index);
    }


}
