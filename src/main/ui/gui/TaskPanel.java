package ui.gui;

import exceptions.LabelLengthException;
import model.Task;
import ui.TimeOut;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// Represents a panel for one task
public class TaskPanel extends JPanel {


    private static final int DIVIDER_LINE_WIDTH = 300;
    private static final int MAX_LABEL_LENGTH = 32;
    private static final int CHAR_WIDTH = 8;

    private static final Font LABEL_FONT = new Font("Helvetica", Font.PLAIN, 16);

    Task task;
    private Color backgroundColor;
    private int height;

    // EFFECTS: Initializes a panel for a task
    public TaskPanel(Task task, Color backgroundColor, int width, int height,
                     int maxDaysUntilDue, int maxDaysThreshold, int maxLabelLength) {
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
        if (maxLabelLength > MAX_LABEL_LENGTH) {
            maxLabelWidth = MAX_LABEL_LENGTH * CHAR_WIDTH;
        }

        int chartWidth = width - maxLabelWidth;
        JPanel chartPanel = new ChartPanel(chartWidth, height, task, backgroundColor,
                maxDaysUntilDue, maxDaysThreshold, LABEL_FONT);
        chartPanel.setBounds(maxLabelWidth, 0, chartWidth, height);
        add(chartPanel);

        add(getLabelField(task.getLabel(),maxLabelWidth));
    }

    // EFFECTS: Generates a field for the task label
    private JTextField getLabelField(String label, int width) {
        String text = label;
        if (label.length() > MAX_LABEL_LENGTH) {
            text = label.substring(0,MAX_LABEL_LENGTH - 3) + "...";
        }

        JTextField labelField = new JTextField(text);

        labelField.setFont(LABEL_FONT);
        labelField.setHorizontalAlignment(SwingConstants.RIGHT);
        labelField.setForeground(Color.WHITE);
        labelField.setBackground(backgroundColor);
        labelField.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        labelField.setBounds(0,0,width,height);
        labelField.setFocusable(false);

        labelField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                editLabel(labelField);
            }

        });

        return labelField;
    }

    private void editLabel(JTextField labelField) {
        while (true) {
            String label = JOptionPane.showInputDialog(this, "Enter Task Name:",task.getLabel());
            try {
                task.setLabel(label);
                labelField.setText(label);
                TimeOut.saveTasks();
                break;
            } catch (LabelLengthException e) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NullPointerException e) {
                break;
            }
        }


    }

}
