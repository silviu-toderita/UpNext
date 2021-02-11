package model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// Represents a task with a name, due date, and weight
public class Task {
    private static final int DEFAULT_WEIGHT = 3;

    private String name;
    private Date dueDate;
    private int weight;

    // REQUIRES: name must be >= 1 character in length and contain at least one non-numeric character
    // EFFECTS: Creates a task with a name, no due date, and no weight
    public Task(String name) {
        this.name = name;
        this.dueDate = new Date(0);
        this.weight = DEFAULT_WEIGHT;
    }

    // REQUIRES: name must be >= 1 character in length and contain at least one non-numeric character
    // EFFECTS: Creates a task with a name, a due date, and no weight
    public Task(String name, Date dueDate) {
        this.name = name;
        this.dueDate = dueDate;
        this.weight = DEFAULT_WEIGHT;
    }

    // REQUIRES: name must be >= 1 character in length and contain at least one non-numeric character,
    //          and weight must be in [1,5]
    // EFFECTS: Creates a task with a name, a weight, and no due date
    public Task(String name, int weight) {
        this.name = name;
        this.dueDate = new Date(0);
        this.weight = weight;
    }

    // REQUIRES: name must be >= 1 character in length and contain at least one non-numeric character,
    //          and weight must be in [1,5]
    // EFFECTS: Creates a task with a name, a due date , and a weight
    public Task(String name, Date dueDate, int weight) {
        this.name = name;
        this.dueDate = dueDate;
        this.weight = weight;
    }

    // REQUIRES: name must be >= 1 character in length and contain at least one non-numeric character
    // MODIFIES: this
    // EFFECTS: Changes the name of this task
    public void setName(String name) {
        this.name = name;
    }

    // MODIFIES: this
    // EFFECTS: Changes the due date of this task
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    // REQUIRES: weight must be in [1,5]
    // MODIFIES: this
    // EFFECTS: Changes the weight of this task
    public void setWeight(int weight) {
        this.weight = weight;
    }

    // EFFECTS: Returns this task's name
    public String getName() {
        return name;
    }

    // EFFECTS: Returns this task's due date as a raw date
    public Date getDueDate() {
        return dueDate;
    }

    // EFFECTS: Returns this task's due date as a formatted string
    public String getDueDateString() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dueDate);
        if (cal.get(Calendar.YEAR) == 1969) {
            return "NO DUE DATE";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy");
        return sdf.format(cal.getTime());
    }

    // EFFECTS: Returns this task's weight
    public int getWeight() {
        return weight;
    }

}
