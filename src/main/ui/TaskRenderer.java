package ui;

import model.Task;
import model.TaskList;

import javax.swing.*;
import java.awt.*;

public class TaskRenderer extends JPanel {

    private static final int TASK_HEIGHT = 35;
    private static final int DUE_DATE_LABEL_WIDTH = 130;
    private static final int DISTANT_THRESHOLD = 60;

    private static final int SMALL_DOT_RADIUS = 5;
    private static final int LARGE_DOT_RADIUS = 10;
    private static final int SMALL_DOT_Y = (TASK_HEIGHT / 2) - SMALL_DOT_RADIUS;



    private Font font = new Font("Helvetica", Font.PLAIN, 16);

    private TaskList taskList;
    private int tasksPanelWidth;
    private int tasksPanelHeight;
    private int maxDaysUntilDue;


    public TaskRenderer(TaskList taskList, int tasksPanelWidth, int tasksPanelHeight) {
        this.taskList = taskList;
        this.tasksPanelWidth = tasksPanelWidth;
        this.tasksPanelHeight = tasksPanelHeight;
    }

    public JPanel getTasksPanel() {
        maxDaysUntilDue = taskList.getMaxDaysUntilDue(DISTANT_THRESHOLD);
        JPanel tasksPanel = new JPanel();

        tasksPanel.setLayout(null);
        tasksPanel.setBackground(Color.WHITE);

        int numTasks = taskList.size();
        int labelLength = taskList.getMaxLabelLength();

        tasksPanel.setBounds(20,60,tasksPanelWidth,tasksPanelHeight);

        for (int i = 0;i < numTasks;i++) {
            Task task = taskList.get(i);
            tasksPanel.add(getTaskPanel(task,i,labelLength));
        }

        return tasksPanel;
    }

    public JPanel getTaskPanel(Task task,int taskNum,int labelLength) {
        JPanel taskPanel = new JPanel();

        taskPanel.setLayout(null);
        taskPanel.setBackground(Color.GRAY);
        int positionY = taskNum * TASK_HEIGHT;
        taskPanel.setBounds(0,positionY,tasksPanelWidth,TASK_HEIGHT);

        int labelWidth = labelLength * 10;
        taskPanel.add(getLabelField(task.getLabel(),labelWidth));
        taskPanel.add(getChartPanel(labelWidth, tasksPanelWidth - labelWidth, task));

        return taskPanel;
    }

    private JTextField getLabelField(String label, int width) {
        JTextField labelField = new JTextField(label);

        labelField.setFont(font);
        labelField.setHorizontalAlignment(SwingConstants.RIGHT);
        labelField.setForeground(Color.WHITE);
        labelField.setBackground(Color.DARK_GRAY);
        labelField.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        labelField.setBounds(0,0,width,TASK_HEIGHT);
        labelField.setFocusable(false);

        return labelField;
    }

    private JPanel getChartPanel(int positionX, int width, Task task) {
        JPanel chartPanel = new JPanel();

        chartPanel.setLayout(null);
        chartPanel.setBackground(Color.DARK_GRAY);
        chartPanel.setBounds(positionX,0,width,TASK_HEIGHT);

        Circle circle = new Circle(SMALL_DOT_RADIUS);
        circle.setBounds(0,SMALL_DOT_Y,SMALL_DOT_RADIUS * 2,SMALL_DOT_RADIUS * 2);
        chartPanel.add(circle);

        chartPanel.add(getDueDateField(width - DUE_DATE_LABEL_WIDTH, task));

        return chartPanel;

    }

    private JTextField getDueDateField(int positionX, Task task) {
        JTextField dueDateField = new JTextField(task.getDueDateString());

        dueDateField.setFont(font);
        dueDateField.setForeground(Color.WHITE);
        dueDateField.setBackground(Color.DARK_GRAY);
        dueDateField.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        dueDateField.setBounds(positionX,0,DUE_DATE_LABEL_WIDTH,TASK_HEIGHT);
        dueDateField.setFocusable(false);

        return dueDateField;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);


    }

}
