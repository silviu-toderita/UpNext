package model;

import exceptions.InvalidJsonException;
import exceptions.LabelLengthException;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

// Represents a task with a label and due date
public class Task implements Comparable<Task> {

    private String label;
    private Date dueDate;

    // EFFECTS: Creates a task with given label and a due date 10 years in the future
    //          Throws LabelLengthException if label is less than 1 character long
    public Task(String label) throws LabelLengthException {
        if (label.length() < 1) {
            throw new LabelLengthException();
        }

        this.label = label;
        Calendar cal = Calendar.getInstance();
        cal.set(2030,Calendar.DECEMBER,31,23,59,59);
        this.dueDate = cal.getTime();
    }

    // EFFECTS: Creates a task with given label and due date
    //          Throws LabelLengthException if label is less than 1 character long
    public Task(String label, Date dueDate) throws LabelLengthException {
        if (label.length() < 1) {
            throw new LabelLengthException();
        }

        this.label = label;
        this.dueDate = dueDate;
    }

    // EFFECTS: Creates a task based on the data in the given JSON object
    //          Throws InvalidJsonException when there is invalid JSON data in the given object
    public Task(JSONObject jsonObject) throws InvalidJsonException {
        try {
            this.label = jsonObject.getString("label");

            int year = jsonObject.getInt("year");
            int month = jsonObject.getInt("month");
            int day = jsonObject.getInt("day");

            Calendar calDue = Calendar.getInstance();
            calDue.set(year,month,day,23,59,59);
            this.dueDate = calDue.getTime();
        } catch (JSONException e) {
            throw new InvalidJsonException();
        }

    }

    // MODIFIES: this
    // EFFECTS: Changes the label of this task
    //          Throws LabelLengthException if label is less than 1 character long
    public void setLabel(String label) throws LabelLengthException {
        if (label.length() < 1) {
            throw new LabelLengthException();
        }

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

    // EFFECTS: Returns a JSON object that represents this task
    public JSONObject makeJsonObject() {
        JSONObject jsonObject = new JSONObject();
        Calendar calDue = Calendar.getInstance();
        calDue.setTime(dueDate);
        jsonObject.put("label", label);
        jsonObject.put("year", calDue.get(Calendar.YEAR));
        jsonObject.put("month", calDue.get(Calendar.MONTH));
        jsonObject.put("day", calDue.get(Calendar.DAY_OF_MONTH));
        return jsonObject;
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
