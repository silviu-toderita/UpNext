package ui.gui;

import model.Task;
import model.TaskList;

import javax.swing.*;
import java.awt.*;

// Represents a scrollable tasks pane
public class TasksPane extends JScrollPane {

    private static final int TASK_HEIGHT = 45;
    private static final int DAYS_MAX_THRESHOLD = 60;
    private static final Font allDoneFont = new Font("Helvetica", Font.ITALIC, 20);

    private ContentEditor editor;
    private TaskList taskList;
    private int width;
    private int height;
    private Color backgroundColor;

    // EFFECTS: Initialize scrollable pane with no border
    public TasksPane(ContentEditor editor, TaskList taskList, int width, int height, Color backgroundColor) {
        super(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.editor = editor;
        this.taskList = taskList;
        this.width = width;
        this.height = height;
        this.backgroundColor = backgroundColor;

        if (taskList.size() == 0) {
            setViewportView(getAllDonePanel());
        } else {
            setViewportView(getTasksPanel());
        }

        setBorder(javax.swing.BorderFactory.createEmptyBorder());
    }

    // MODIFIES: this
    // EFFECTS: Generates a panel of all tasks
    private JPanel getTasksPanel() {
        taskList.sort();
        int numTasks = taskList.size();
        int maxDaysUntilDue = taskList.getMaxDaysUntilDue(DAYS_MAX_THRESHOLD);
        int labelLength = taskList.getMaxLabelLength();

        JPanel tasksPanel = new JPanel();
        tasksPanel.setLayout(null);
        tasksPanel.setBackground(backgroundColor);

        for (int i = 0;i < numTasks;i++) {
            Task task = taskList.get(i);
            JPanel taskPanel = new TaskPanel(editor,task, backgroundColor, width, TASK_HEIGHT,
                    maxDaysUntilDue, DAYS_MAX_THRESHOLD, labelLength);
            taskPanel.setBounds(0, i * TASK_HEIGHT, width, TASK_HEIGHT);
            tasksPanel.add(taskPanel);
        }

        tasksPanel.setPreferredSize(new Dimension(width, numTasks * TASK_HEIGHT));

        return tasksPanel;
    }

    private JPanel getAllDonePanel() {
        JPanel allDonePanel = new JPanel();
        JLabel allDoneLabel = new JLabel("You're all done, relax and kick back!");

        allDonePanel.setBackground(backgroundColor);

        allDoneLabel.setForeground(Color.WHITE);
        allDoneLabel.setFont(allDoneFont);
        allDoneLabel.setBounds(0,0,300,25);

        allDonePanel.add(allDoneLabel);

        return allDonePanel;
    }


}
