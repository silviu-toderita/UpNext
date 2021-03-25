package ui.gui;

import com.toedter.calendar.JDateChooser;
import exceptions.LabelLengthException;
import model.Task;
import ui.TimeOut;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

// Represents a panel for a chart visually displaying time til due
public class ChartPanel extends JPanel {

    private static final String NO_DUE_DATE_LABEL = "-----";

    private static final int DUE_DATE_LABEL_WIDTH = 140;
    private static final int MINIMUM_BAR_WIDTH = 120;

    private static final int DAYS_DISTANT_THRESHOLD = 1826;
    private static final int DAYS_YELLOW_THRESHOLD = 3;
    private static final int DAYS_ORANGE_THRESHOLD = 1;
    private static final int DAYS_RED_THRESHOLD = 0;

    private static final Font DAYS_LEFT_FONT = new Font("Helvetica", Font.PLAIN, 12);

    private Task task;
    private int height;
    private Color backgroundColor;
    private int maxDaysThreshold;
    private Font labelFont;

    // EFFECTS: Generates a panel for the bar chart
    public ChartPanel(int width, int height, Task task, Color backgroundColor,
                      int maxDaysUntilDue, int maxDaysThreshold, Font labelFont) {
        this.task = task;
        this.height = height;
        this.backgroundColor = backgroundColor;
        this.maxDaysThreshold = maxDaysThreshold;
        this.labelFont = labelFont;

        setLayout(null);
        setBackground(backgroundColor);

        float widthPercent = (float) (task.getDaysUntilDue() + 1) / (maxDaysUntilDue + 2);
        if (widthPercent < 0) {
            widthPercent = 0;
        } else if (widthPercent > 1) {
            widthPercent = 1;
        }

        Color barColor = getBarColor(task.getDaysUntilDue());

        add(getDaysLeftField(task.getDaysUntilDue(), barColor));

        int barWidth = Math.round(widthPercent * (width - MINIMUM_BAR_WIDTH - DUE_DATE_LABEL_WIDTH))
                + MINIMUM_BAR_WIDTH;
        if (barWidth < MINIMUM_BAR_WIDTH) {
            barWidth = MINIMUM_BAR_WIDTH;
        }
        Bar bar = new Bar(barColor, barWidth - 20, 15);
        bar.setBounds(10, 15, barWidth - 20, 15);
        add(bar);

        add(getDueDateField(barWidth, task.getDueDateString()));
    }

    // EFFECTS: Generates a text field for the due date
    private JTextField getDueDateField(int positionX, String dueDate) {
        String text = dueDate;
        if (task.getDaysUntilDue() >= DAYS_DISTANT_THRESHOLD) {
            text = NO_DUE_DATE_LABEL;
        }
        JTextField dueDateField = new JTextField(text);

        dueDateField.setFont(labelFont);
        dueDateField.setForeground(Color.WHITE);
        dueDateField.setBackground(backgroundColor);
        dueDateField.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        dueDateField.setBounds(positionX,2,DUE_DATE_LABEL_WIDTH,height - 4);
        dueDateField.setFocusable(false);

        dueDateField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                editDate(dueDateField);
            }

        });

        return dueDateField;
    }

    // EFFECTS: Returns the appropriate colour for the number of days left
    private Color getBarColor(int days) {
        if (days >= maxDaysThreshold) {
            return Color.GRAY;
        } else if (days >= DAYS_YELLOW_THRESHOLD) {
            return new Color(31,170,31);
        } else if (days >= DAYS_ORANGE_THRESHOLD) {
            return Color.YELLOW;
        } else if (days >= DAYS_RED_THRESHOLD) {
            return new Color(255, 160, 0);
        } else {
            return Color.RED;
        }
    }

    // EFFECTS: Generates a text field for the number of days left
    private JTextField getDaysLeftField(int daysLeft, Color backgroundColor) {
        JTextField daysLeftField = new JTextField(getDaysLeftText(daysLeft));

        Color textColor = Color.WHITE;
        if (backgroundColor == Color.YELLOW) {
            textColor = Color.BLACK;
        }

        daysLeftField.setFont(DAYS_LEFT_FONT);
        daysLeftField.setForeground(textColor);
        daysLeftField.setBackground(backgroundColor);
        daysLeftField.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        daysLeftField.setBounds(20, 16, 80, 14);
        daysLeftField.setFocusable(false);

        return daysLeftField;
    }

    // EFFECTS: Returns a formatted string with the number of days left
    private String getDaysLeftText(int daysLeft) {
        if (daysLeft > 1 && daysLeft < DAYS_DISTANT_THRESHOLD) {
            return daysLeft + " Days Left";
        } else if (daysLeft == 1) {
            return "1 Day Left";
        } else if (daysLeft == 0) {
            return "Today";
        } else if (daysLeft == -1) {
            return "Yesterday";
        } else if (daysLeft < -1) {
            return Math.abs(daysLeft) + " Days Ago";
        }

        return "";
    }

    private void editDate(JTextField dueDateField) {
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDate(task.getDueDate());

        Object[] parameters = {"Enter Due Date or Press Cancel To Delete Due Date:", dateChooser};
        int result = JOptionPane.showConfirmDialog(this, parameters, task.getDueDateString(),
                JOptionPane.CANCEL_OPTION);

        if (result == 2) {
            task.removeDueDate();
            dueDateField.setText(NO_DUE_DATE_LABEL);
        } else {
            task.setDueDate(dateChooser.getDate());
            dueDateField.setText(task.getDueDateString());
        }
        TimeOut.saveTasks();
        repaint();
    }
}
