package model;

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
    public void add(Task task) {
        taskList.add(task);
    }

    // REQUIRES: id must be a valid index in the task list
    // MODIFIES: this
    // EFFECTS: Removes task with given id from task list
    public void complete(int id) {
        taskList.remove(id);
    }

    // MODIFIES: this
    // EFFECT: Sorts the task list in order from smallest due date to largest due date
    public void sort() {
        Collections.sort(taskList);
    }


    // EFFECTS: Returns the maximum number of days until a task is due. Tasks due > 5 years in the future do not count
    //          towards the maximum
    public int getMaxDaysUntilDue() {
        int maxDaysLeft = 0;
        for (Task each: taskList) {
            int daysLeft = each.getDaysUntilDue();
            if (daysLeft > maxDaysLeft & daysLeft < 1825) {
                maxDaysLeft = daysLeft;
            }
        }

        return maxDaysLeft;
    }

    // EFFECTS: Returns the char length of the longest task label
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

    // EFFECTS: Returns the size of the task list
    public int size() {
        return taskList.size();
    }

    // REQUIRES: id must be a valid index in the task list
    // EFFECTS: Returns the task based on id
    public Task get(int id) {
        return taskList.get(id);
    }


}
