package ui;

import model.TaskList;

public class TaskVisualizer {
    TaskList taskList;

    public TaskVisualizer(TaskList taskList) {
        this.taskList = taskList;
    }

    public String print() {
        if (taskList.size() == 0) {
            return "You're all caught up, there's nothing to do!";
        } else {
            String outputText = "";
            for (int i = 0; i < taskList.size(); i++) {
                outputText = outputText.concat(taskList.get(i).getName());
                outputText = outputText.concat("\n");
            }
            return outputText;
        }
    }
}
