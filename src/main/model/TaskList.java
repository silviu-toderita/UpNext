package model;

import org.json.JSONArray;
import org.json.JSONObject;

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

    // REQUIRES: At least one task must be in the task list
    // EFFECTS: Returns the maximum number of days until a task is due. Tasks due > 5 years in the future do not count
    //          towards the maximum. Always returns >=0.
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

    // REQUIRES: id must be a valid index in the task list
    // EFFECTS: Returns the task based on id
    public Task get(int id) {
        return taskList.get(id);
    }


}
