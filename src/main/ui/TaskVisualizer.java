package ui;

import model.Task;
import model.TaskList;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;

// Represents an object that generates the chronological task visualizer
public class TaskVisualizer {
    private static final int MAX_CHAR_WIDTH = 200;

    private static final int DAYS_RED_THRESHOLD = 0;
    private static final int DAYS_YELLOW_THRESHOLD = 3;
    private static final int DISTANT_THRESHOLD = 60;

    private static final String TASK_LIST_HEADER = "Here's what's on your plate: ";
    private static final String DONE_MESSAGE = "\uD83C\uDFDD You're all caught up, take a break! \uD83C\uDFDD"; //

    private TaskList taskList;

    // EFFECT: Creates a taskVisualizer with a given taskList
    public TaskVisualizer(TaskList taskList) {
        this.taskList = taskList;
    }

    // EFFECT: Return a string for a chronological task chart
    public String showTasks() {
        Calendar todayDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy");
        String output = "\nToday's Date: ";
        output = output.concat(colorize(sdf.format(todayDate.getTime()),BLUE_TEXT()));

        output = output.concat("\n");
        output = output.concat(TASK_LIST_HEADER);
        output = output.concat("\n");

        output = output.concat(generateHorizontalDivider(MAX_CHAR_WIDTH));
        if (taskList.size() == 0) {
            output = output.concat(DONE_MESSAGE);
            output = output.concat("\n");
        } else {
            output = output.concat(allTasks());
        }
        output = output.concat(generateHorizontalDivider(MAX_CHAR_WIDTH));

        return output;
    }

    // EFFECT: Return a string representing all tasks in a chronological task chart
    public String allTasks() {
        taskList.sort();

        int maxNameLength = taskList.getMaxLabelLength();
        int maxDaysLeft = taskList.getMaxDaysUntilDue(DISTANT_THRESHOLD);
        String output = "";
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            output = output.concat("[");
            output = output.concat(String.valueOf(i + 1));
            output = output.concat("]");
            if (i < 9) {
                output = output.concat(" ");
            }
            output = output.concat(eachTask(task, maxNameLength, maxDaysLeft));
            output = output.concat("\n");
        }
        return output;
    }

    // EFFECT: Return a string for each row representing one task in a chronological task chart
    private String eachTask(Task task, int maxNameLength, int maxDaysLeft) {
        String name = task.getLabel();
        int spacesRequiredAfterName = maxNameLength - name.length();
        int chartLength = MAX_CHAR_WIDTH - maxNameLength - 27;
        String output = "";

        int daysLeft = task.getDaysUntilDue();

        output = output.concat(colorizeDeadline(name,daysLeft));
        output = output.concat(generateSpaces(spacesRequiredAfterName));
        output = output.concat(colorize("||",BLUE_TEXT()));
        output = output.concat(generateChart(daysLeft, maxDaysLeft, chartLength));
        output = output.concat("  ");
        output = output.concat(colorizeDeadline(task.getDueDateString(), daysLeft));

        return output;
    }

    // EFFECT: Return the input string colorized based on the number of days left to a deadline
    private String colorizeDeadline(String input, int daysLeft) {
        String output;
        if (daysLeft <= DAYS_RED_THRESHOLD) {
            output = colorize(input,RED_TEXT());
        } else if (daysLeft <= DAYS_YELLOW_THRESHOLD) {
            output = colorize(input,YELLOW_TEXT());
        } else if (daysLeft <= DISTANT_THRESHOLD) {
            output = colorize(input,GREEN_TEXT());
        } else {
            output = input;
        }
        return output;
    }

    // EFFECT: Generate a chart for each task that shows a chronological view of the time left until a task is due
    private String generateChart(int daysLeft, int maxDaysLeft, int length) {
        if (daysLeft > 0 & daysLeft < DISTANT_THRESHOLD) {
            float percentOfMax = (float) daysLeft / maxDaysLeft;

            String output = generateChartUpcomingTasks((percentOfMax * length) - 5);
            return output.concat(colorizeDeadline(">|=|", daysLeft));
        } else if (daysLeft >= DISTANT_THRESHOLD) {
            return generateChartDistantTasks(length);
        } else if (daysLeft == 0) {
            return (colorizeDeadline("DUE TODAY:", 0));
        } else {
            String output = "OVERDUE BY ";
            output = output.concat(Integer.toString(daysLeft * -1));
            if (daysLeft == -1) {
                output = output.concat(" DAY:");
            } else {
                output = output.concat(" DAYS:");
            }
            return (colorizeDeadline(output, -1));
        }
    }

    // EFFECT: Generate a chart for tasks in the next year
    private String generateChartUpcomingTasks(float charsUntilDue) {
        String output = "";
        for (int i = 0; i < charsUntilDue; i++) {
            output = output.concat("+");
        }
        return output;
    }

    // EFFECT: Generate a chart for tasks further away than the distant threshold
    private String generateChartDistantTasks(int length) {
        String output = "";
        for (int i = 0; i < length; i++) {
            output = output.concat(" ");
        }
        output = output.concat(" |=|");
        return output;
    }

    // EFFECT: Return a string of hyphens with given length
    private String generateHorizontalDivider(int length) {
        String output = "";
        for (int i = 0; i < length; i++) {
            output = output.concat("-");
        }
        return output.concat("\n");
    }

    // EFFECT: Return a string of spaces with given length
    private String generateSpaces(int length) {
        String output = "";
        for (int i = 0; i < length; i++) {
            output = output.concat(" ");
        }
        return output;
    }

}
