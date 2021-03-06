package ui.gui;

import model.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

// Represents a panel for one task
public class TaskPanel extends JPanel {

    private static final int DIVIDER_LINE_WIDTH = 300;
    private static final int MAX_LABEL_LENGTH = 32;
    private static final int CHAR_WIDTH = 9;

    private static final Font LABEL_FONT = new Font("Helvetica", Font.BOLD, 16);

    Timer timer;
    private TaskEditor editor;
    private Task task;
    private Color backgroundColor;
    private int height;

    // EFFECTS: Initializes a panel for a task
    public TaskPanel(TaskEditor editor, Task task, Color backgroundColor, int width, int height,
                     int maxDaysUntilDue, int maxDaysThreshold, int maxLabelLength) {
        timer = new Timer();
        this.editor = editor;
        this.task = task;
        this.backgroundColor = backgroundColor;
        this.height = height;

        setLayout(null);
        setBackground(backgroundColor);

        Bar bar = new Bar(Color.GRAY, width, 1);
        int dividerLinePositionX = (width / 2 - (DIVIDER_LINE_WIDTH / 2));
        if (dividerLinePositionX < 0) {
            dividerLinePositionX = 0;
        }

        bar.setBounds(dividerLinePositionX, 0, DIVIDER_LINE_WIDTH, 1);
        add(bar);

        int maxLabelWidth = maxLabelLength * CHAR_WIDTH;

        int chartWidth = width - maxLabelWidth;
        JPanel chartPanel = new ChartPanel(editor, chartWidth, height, task, backgroundColor,
                maxDaysUntilDue, maxDaysThreshold, LABEL_FONT);
        chartPanel.setBounds(maxLabelWidth + 15, 0, chartWidth, height);
        add(chartPanel);

        add(getDoneBox());
        add(getLabelField(task.getLabel(),maxLabelWidth));

    }

    // MODIFIES: this
    // EFFECTS: Generates a label for the task label
    private JLabel getLabelField(String label, int width) {
        String text = label;
        if (label.length() > MAX_LABEL_LENGTH) {
            text = label.substring(0,MAX_LABEL_LENGTH - 3) + "...";
        }

        JLabel labelField = new JLabel(text);

        labelField.setFont(LABEL_FONT);
        labelField.setHorizontalAlignment(SwingConstants.RIGHT);
        labelField.setForeground(Color.WHITE);
        labelField.setBackground(backgroundColor);
        labelField.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        labelField.setBounds(15,0,width,height);
        labelField.setFocusable(false);

        labelField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                editor.editLabel(task, false);
            }

        });

        return labelField;
    }

    // EFFECTS: Create a checkbox that the user can click to mark the task done and remove it from the list
    private JCheckBox getDoneBox() {
        JCheckBox doneBox = new JCheckBox();
        doneBox.setBounds(0,8,25,25);

        // When clicked, remove the task
        doneBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Set a timer for 1/2 second to remove the task, so there is some time to see the box getting checked
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        editor.removeTask(task);
                    }
                },500);
            }

        });

        return doneBox;
    }

}
