package model;

import java.util.Date;

// Represents a task with a name, due date, and weight
public class Task {

    // REQUIRES: name must be >= 1 character in length and contain at least one non-numeric character
    // EFFECTS: Creates a task with a name, no due date, and no weight
    public Task(String name){
        // Stub
    }

    // REQUIRES: name must be >= 1 character in length and contain at least one non-numeric character
    // EFFECTS: Creates a task with a name, a due date, and no weight
    public Task(String name, Date dueDate) {
        // Stub
    }

    // REQUIRES: name must be >= 1 character in length and contain at least one non-numeric character,
    //          and weight must be in [1,5]
    // EFFECTS: Creates a task with a name, a weight, and no due date
    public Task(String name, int weight) {
        // Stub
    }

    // REQUIRES: name must be >= 1 character in length and contain at least one non-numeric character,
    //          and weight must be in [1,5]
    // EFFECTS: Creates a task with a name, a due date , and a weight
    public Task(String name, Date dueDate, int weight) {
        // Stub
    }

    // REQUIRES: name must be >= 1 character in length and contain at least one non-numeric character
    // MODIFIES: this
    // EFFECTS: Changes the name of this task
    public void setName(String name) {
        // Stub
    }

    // MODIFIES: this
    // EFFECTS: Changes the due date of this task
    public void setDueDate(Date dueDate) {
        // Stub
    }

    // REQUIRES: weight must be in [1,5]
    // MODIFIES: this
    // EFFECTS: Changes the weight of this task
    public void setWeight(int weight) {
        // Stub
    }

    // Getter Methods:
    public String getName() {
        return ""; // Stub
    }

    public Date getDueDate() {
        return null; // Stub
    }

    public int getWeight() {
        return 0; // Stub
    }

}
