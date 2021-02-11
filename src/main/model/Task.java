package model;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

// Represents a task with a label and due date
public class Task implements Comparable<Task> {

    private String label;
    private Date dueDate;

    // REQUIRES: label must be >= 1 character in length and contain at least one non-numeric character
    // EFFECTS: Creates a task with given label and a due date 10 years in the future
    public Task(String label) {
        this.label = label;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR,10);
        this.dueDate = cal.getTime();
    }

    // REQUIRES: label must be >= 1 character in length and contain at least one non-numeric character
    // EFFECTS: Creates a task with given label and due date
    public Task(String label, Date dueDate) {
        this.label = label;
        this.dueDate = dueDate;
    }

    // REQUIRES: name must be >= 1 character in length and contain at least one non-numeric character
    // MODIFIES: this
    // EFFECTS: Changes the label of this task
    public void setLabel(String label) {
        this.label = label;
    }

    // MODIFIES: this
    // EFFECTS: Changes the due date of this task
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    // EFFECTS: Returns this task's due date as a string. For example: "Thu 11 Feb 2021". If due date is over 5 years in
    //          the future, returns "No Due Date"
    public String getDueDateString() {
        Calendar calDue = Calendar.getInstance();
        calDue.setTime(dueDate);
        Calendar calToday = Calendar.getInstance();
        if (calDue.get(Calendar.YEAR) >= calToday.get(Calendar.YEAR) + 5) {
            return "No Due Date";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy");
        return sdf.format(calDue.getTime());
    }

    // EFFECTS: Returns the number of days until this task is due
    public int getDaysUntilDue() {
        Calendar calDue = Calendar.getInstance();
        calDue.setTime(dueDate);
        Calendar calToday = Calendar.getInstance();
        long daysUntilDue = ChronoUnit.DAYS.between(calToday.toInstant(), calDue.toInstant());
        return (int)daysUntilDue;
    }

    // EFFECTS: Compares this task to another task by due date
    @Override
    public int compareTo(Task o) {
        return this.getDueDate().compareTo(o.getDueDate());
    }

    // EFFECTS: Returns this task's label
    public String getLabel() {
        return label;
    }

    // EFFECTS: Returns this task's due date as a raw date
    public Date getDueDate() {
        return dueDate;
    }
}
