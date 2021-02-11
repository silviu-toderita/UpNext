package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

    // EFFECTS: Returns the size of the task list
    public int size() {
        return taskList.size();
    }

    // REQUIRES: name must be >= 1 character in size
    // EFFECTS: Returns the id of the first task with matching name, or -1 if it's not in list
    public int find(String name) {
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    // MODIFIES: this
    // EFFECT: Sorts the task list in order from smallest due date to largest due date
    public void sort() {
        Collections.sort(taskList);
    }

    // REQUIRES: id must be a valid index in the task list
    // EFFECTS: Returns the task based on id
    public Task get(int id) {
        return taskList.get(id);
    }




}
