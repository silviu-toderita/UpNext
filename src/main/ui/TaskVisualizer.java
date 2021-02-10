package ui;

import model.Task;
import model.TaskList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskVisualizer {
    TaskList taskList;

    public TaskVisualizer(TaskList taskList) {
        this.taskList = taskList;
    }

    public String allTasks() {
        if (taskList.size() == 0) {
            return "You're all caught up, there's nothing to do!\n";
        } else {
            String output = "\nTASK LIST:\n--------------------------------\n";
            for (int i = 0; i < taskList.size(); i++) {
                Task task = taskList.get(i);
                output = output.concat("[");
                output = output.concat(String.valueOf(i + 1));
                output = output.concat("] - ");
                output = output.concat(eachTask(task));
                output = output.concat("\n");
            }
            return output;
        }
    }

    private String eachTask(Task task) {
        String output = "";

        output = output.concat(task.getName());
        output = output.concat(" - Due: ");
        output = output.concat(task.getDueDateString());
        output = output.concat(" - Weight: ");
        output = output.concat(String.valueOf(task.getWeight()));

        return output;
    }


}
