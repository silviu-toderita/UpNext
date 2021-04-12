package ui.gui;

import exceptions.NoDueDateException;
import model.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Represents a panel for a chart visually displaying time til due
public class ChartPanel extends JPanel {

    private static final String NO_DUE_DATE_LABEL = "-----";
    private static final int DUE_DATE_LABEL_WIDTH = 150;
    private static final int MINIMUM_BAR_WIDTH = 120;
    private static final int DAYS_YELLOW_THRESHOLD = 3;
    private static final int DAYS_ORANGE_THRESHOLD = 1;
    private static final int DAYS_RED_THRESHOLD = 0;
    private static final Color BAR_COLOR_GRAY = Color.GRAY;
    private static final Color BAR_COLOR_GREEN = new Color(31,170,31);
    private static final Color BAR_COLOR_YELLOW = Color.YELLOW;
    private static final Color BAR_COLOR_ORANGE = new Color(255, 160, 0);
    private static final Color BAR_COLOR_RED = Color.RED;

    private static final Font DAYS_LEFT_FONT = new Font("Helvetica", Font.PLAIN, 12);

    private TaskEditor editor;
    private Task task;
    private int width;
    private int height;
    private Color backgroundColor;
    private int maxDaysUntilDue;
    private int maxDaysThreshold;
    private Font labelFont;

    // EFFECTS: Initializes the chart panel
    public ChartPanel(TaskEditor editor, int width, int height, Task task, Color backgroundColor,
                      int maxDaysUntilDue, int maxDaysThreshold, Font labelFont) {
        this.editor = editor;
        this.task = task;
        this.width = width;
        this.height = height;
        this.backgroundColor = backgroundColor;
        this.maxDaysUntilDue = maxDaysUntilDue;
        this.maxDaysThreshold = maxDaysThreshold;
        this.labelFont = labelFont;

        generateChartPanel();
    }

    // MODIFIES: this
    // EFFECTS: Generates a chart panel with a bar chart and a due date label
    private void generateChartPanel() {
        setLayout(null);
        setBackground(backgroundColor);

        // Calculate the size of this bar in the chart compared to other tasks
        float widthPercent;
        try {
            widthPercent = (float) (task.getDaysUntilDue() + 1) / (maxDaysUntilDue + 2);
        } catch (NoDueDateException e) {
            widthPercent = 1;
        }

        if (widthPercent < 0) {
            widthPercent = 0;
        } else if (widthPercent > 1) {
            widthPercent = 1;
        }

        int barWidth = Math.round(widthPercent * (width - MINIMUM_BAR_WIDTH - DUE_DATE_LABEL_WIDTH))
                + MINIMUM_BAR_WIDTH;
        if (barWidth < MINIMUM_BAR_WIDTH) {
            barWidth = MINIMUM_BAR_WIDTH;
        }

        add(getBarPanel(barWidth));
        add(getDueDateLabel(barWidth));
    }

    // EFFECTS: Generate a bar chart with a coloured bar and a "days until due" label
    private JPanel getBarPanel(int width) {
        JPanel barPanel = new JPanel();

        barPanel.setLayout(null);
        barPanel.setBackground(backgroundColor);
        barPanel.setBounds(0,0,width,height);

        Color barColor = getBarColor(task);

        barPanel.add(getDaysLeftLabel(task, barColor));

        barPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                editDate();
            }

        });

        Bar bar = new Bar(barColor, width - 20, 15);
        bar.setBounds(10, 15, width - 20, 15);
        barPanel.add(bar);

        return barPanel;
    }

    // EFFECTS: Generates a text label to superimpose on the bar showing the number of days left until due
    private JLabel getDaysLeftLabel(Task task, Color backgroundColor) {
        JLabel daysLeftLabel;

        try {
            daysLeftLabel = new JLabel(getDaysLeftText(task.getDaysUntilDue()));
        } catch (NoDueDateException e) {
            daysLeftLabel = new JLabel("");
        }


        Color textColor = Color.WHITE;
        if (backgroundColor == Color.YELLOW) {
            textColor = Color.BLACK;
        }

        daysLeftLabel.setFont(DAYS_LEFT_FONT);
        daysLeftLabel.setForeground(textColor);
        daysLeftLabel.setBackground(backgroundColor);
        daysLeftLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        daysLeftLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                editDate();
            }

        });

        daysLeftLabel.setBounds(20, 16, 80, 14);
        daysLeftLabel.setFocusable(false);

        return daysLeftLabel;
    }

    // EFFECTS: Returns a formatted string with the number of days left
    private String getDaysLeftText(int daysLeft) {
        if (daysLeft > 1) {
            return daysLeft + " Days Left";
        } else if (daysLeft == 1) {
            return "1 Day Left";
        } else if (daysLeft == 0) {
            return "Today";
        } else if (daysLeft == -1) {
            return "Yesterday";
        }

        return Math.abs(daysLeft) + " Days Ago";

    }

    // EFFECTS: Generates a label for the due date
    private JLabel getDueDateLabel(int positionX) {
        String text;

        try {
            text = task.getDueDateString();
        } catch (NoDueDateException e) {
            text = NO_DUE_DATE_LABEL;
        }

        JLabel getDueDateLabel = new JLabel(text);

        getDueDateLabel.setFont(labelFont);
        getDueDateLabel.setForeground(Color.WHITE);
        getDueDateLabel.setBackground(backgroundColor);
        getDueDateLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder());

        getDueDateLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                editDate();
            }

        });

        getDueDateLabel.setBounds(positionX,2,DUE_DATE_LABEL_WIDTH,height - 4);
        getDueDateLabel.setFocusable(false);

        return getDueDateLabel;
    }

    // EFFECTS: Returns the appropriate colour for the bar based on the number of days left
    private Color getBarColor(Task task) {
        int days;

        try {
            days = task.getDaysUntilDue();
        } catch (NoDueDateException e) {
            return BAR_COLOR_GRAY;
        }

        if (days >= maxDaysThreshold) {
            return BAR_COLOR_GRAY;
        } else if (days >= DAYS_YELLOW_THRESHOLD) {
            return BAR_COLOR_GREEN;
        } else if (days >= DAYS_ORANGE_THRESHOLD) {
            return BAR_COLOR_YELLOW;
        } else if (days >= DAYS_RED_THRESHOLD) {
            return BAR_COLOR_ORANGE;
        } else {
            return BAR_COLOR_RED;
        }
    }

    // MODIFIES: this
    // EFFECTS: Launch the edit date dialog
    private void editDate() {
        editor.editDate(task);
    }


}
