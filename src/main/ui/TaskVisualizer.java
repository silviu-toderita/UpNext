package ui;

import model.Task;
import model.TaskList;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;
import static java.time.temporal.ChronoUnit.DAYS;


public class TaskVisualizer {
    private static final int MAX_CHAR_WIDTH = 150;

    private static final int DAYS_RED_THRESHOLD = 0;
    private static final int DAYS_YELLOW_THRESHOLD = 3;

    private static final String TASK_LIST_HEADER = "Here's what's on your plate: ";

    TaskList taskList;

    public TaskVisualizer(TaskList taskList) {
        this.taskList = taskList;
    }

    public String showTasks() {
        Calendar todayDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy");
        String output = "\nToday's Date: ";
        output = output.concat(colorize(sdf.format(todayDate.getTime()),BLUE_TEXT()));

        output = output.concat("\n");
        output = output.concat(TASK_LIST_HEADER);
        output = output.concat("\n");

        output = output.concat(horizontalDivider(MAX_CHAR_WIDTH));
        if (taskList.size() == 0) {
            output = output.concat("\uD83C\uDF34 You're all caught up, take a break! \uD83C\uDF34 \n");
        } else {
            output = output.concat(allTasks());
        }
        output = output.concat(horizontalDivider(MAX_CHAR_WIDTH));

        return output;
    }

    public String allTasks() {
        int maxNameLength = longestTaskName();
        String output = "";
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            output = output.concat("[");
            output = output.concat(String.valueOf(i + 1));
            output = output.concat("]");
            if (i < 9) {
                output = output.concat(" ");
            }
            output = output.concat(eachTask(task, maxNameLength));
            output = output.concat("\n");
        }
        return output;
    }

    private String eachTask(Task task, int maxNameLength) {
        String name = task.getName();
        int spacesRequiredAfterName = maxNameLength - name.length();
        int chartLength = MAX_CHAR_WIDTH - maxNameLength - 25;
        String output = "";

        int daysLeft = daysUntilDue(task);

        output = output.concat(colorizeDeadline(name,daysLeft));
        output = output.concat(addSpaces(spacesRequiredAfterName));
        output = output.concat("|");
        output = output.concat(generateChart(task, chartLength));
        output = output.concat("|Due ");
        output = output.concat(colorizeDeadline(task.getDueDateString(), daysLeft));

        return output;
    }

    private String colorizeDeadline(String name, int daysLeft) {
        String output = "";
        if (daysLeft <= DAYS_RED_THRESHOLD) {
            output = colorize(name,RED_TEXT());
        } else if (daysLeft <= DAYS_YELLOW_THRESHOLD) {
            output = colorize(name,YELLOW_TEXT());
        } else {
            output = colorize(name,GREEN_TEXT());
        }

        return output;
    }

    private String generateChart(Task task, int length) {
        String output = "";
        for (int i = 0; i < length; i++) {
            output = output.concat("+");
        }
        return output;
    }

    private String horizontalDivider(int length) {
        String output = "";
        for (int i = 0; i < length; i++) {
            output = output.concat("-");
        }
        return output.concat("\n");
    }

    private String addSpaces(int length) {
        String output = "";
        for (int i = 0; i < length; i++) {
            output = output.concat(" ");
        }
        return output;
    }

    private int longestTaskName() {
        int length = 0;
        for (int i = 0; i < taskList.size(); i++) {
            int taskNameLength = taskList.get(i).getName().length();
            if (taskNameLength > length) {
                length = taskNameLength;
            }
        }

        return length;
    }

    private int daysUntilDue(Task task) {
        Calendar dueDate = Calendar.getInstance();
        dueDate.setTime(task.getDueDate());
        Calendar todayDate = Calendar.getInstance();
        return (int) ChronoUnit.DAYS.between(todayDate.toInstant(), dueDate.toInstant()) + 1;
    }

}
