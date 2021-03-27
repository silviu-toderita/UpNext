package model;

import exceptions.LabelLengthException;
import exceptions.NoDueDateException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// Represents a task with a label and due date
public class Task implements Comparable<Task> {

    private String label;
    private Date dueDate;
    private Boolean hasDueDate;

    // EFFECTS: Creates a task with given label and a due date 10 years in the future
    //          Throws LabelLengthException if label is less than 1 character long
    public Task(String label) throws LabelLengthException {
        if (label.length() < 1) {
            throw new LabelLengthException();
        }

        this.label = label;
        hasDueDate = false;
    }

    // EFFECTS: Creates a task with given label and due date
    //          Throws LabelLengthException if label is less than 1 character long
    public Task(String label, Date dueDate) throws LabelLengthException {
        if (label.length() < 1) {
            throw new LabelLengthException();
        }

        this.label = label;
        hasDueDate = true;
        this.dueDate = dueDate;
    }

    // EFFECTS: Creates a task based on the data in the given JSON object
    public Task(JSONObject jsonObject) {
        this.label = jsonObject.getString("label");
        this.hasDueDate = jsonObject.getBoolean("hasDueDate");

        if (hasDueDate) {
            int year = jsonObject.getInt("year");
            int month = jsonObject.getInt("month");
            int day = jsonObject.getInt("day");
            Calendar cal = Calendar.getInstance();
            cal.set(year,month,day);
            this.dueDate = cal.getTime();
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
        hasDueDate = true;
        this.dueDate = dueDate;
    }

    // MODIFIES: this
    // EFFECTS:
    public void removeDueDate() {
        hasDueDate = false;
    }

    // EFFECTS: Returns this task's due date as a string. For example: "Thu 11 Feb 2021".
    //          If there is no due date, throws NoDueDateException
    public String getDueDateString() throws NoDueDateException {
        if (!hasDueDate) {
            throw new NoDueDateException();
        }

        Calendar calDue = Calendar.getInstance();
        calDue.setTime(dueDate);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy");
        return sdf.format(calDue.getTime());

    }

    // EFFECTS: Returns the number of days until this task is due
    //          If there is no due date, throws NoDueDateException
    public int getDaysUntilDue() throws NoDueDateException {
        if (!hasDueDate) {
            throw new NoDueDateException();
        }
        Calendar cal = Calendar.getInstance();
        int thisYear = cal.get(Calendar.YEAR);
        int thisDay = cal.get(Calendar.DAY_OF_YEAR);

        cal.setTime(dueDate);
        int dueYear = cal.get(Calendar.YEAR);
        int dueDay = cal.get(Calendar.DAY_OF_YEAR);

        int yearDifference = dueYear - thisYear;
        int dayDifference = dueDay - thisDay;

        return dayDifference + (yearDifference * 365);
    }

    // EFFECTS: Returns a JSON object that represents this task
    public JSONObject makeJsonObject() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("label", label);
        jsonObject.put("hasDueDate", hasDueDate);

        if (hasDueDate) {
            Calendar calDue = Calendar.getInstance();
            calDue.setTime(dueDate);
            jsonObject.put("year", calDue.get(Calendar.YEAR));
            jsonObject.put("month", calDue.get(Calendar.MONTH));
            jsonObject.put("day", calDue.get(Calendar.DAY_OF_MONTH));
        }

        return jsonObject;
    }

    // EFFECTS: Compares this task to another task by due date. If either task has no due date, compare to a far away
    //          date 10 years away
    @Override
    public int compareTo(Task o) {
        Calendar farAwayCal = Calendar.getInstance();
        farAwayCal.add(Calendar.YEAR, 10);
        Date farAwayDate = farAwayCal.getTime();

        try {
            return this.getDueDate().compareTo(o.getDueDate());
        } catch (NoDueDateException e) {
            try {
                if (!hasDueDate) {
                    return farAwayDate.compareTo(o.getDueDate());
                } else {
                    return this.getDueDate().compareTo(farAwayDate);
                }
            } catch (NoDueDateException x) {
                // Do nothing
            }

        }

        return 0;

    }

    // EFFECTS: Returns this task's label
    public String getLabel() {
        return label;
    }

    // EFFECTS: Returns this task's due date as a raw date
    //          If there is no due date, throws NoDueDateException
    public Date getDueDate() throws NoDueDateException {
        if (!hasDueDate) {
            throw new NoDueDateException();
        }
        return dueDate;
    }

    // EFFECTS: Returns whether or not this task has a due date
    public Boolean getHasDueDate() {
        return hasDueDate;
    }

}
