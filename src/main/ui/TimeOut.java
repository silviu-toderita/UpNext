package ui;

import exceptions.InvalidJsonFileException;
import model.TaskList;
import persistence.ReaderWriter;
import ui.gui.TasksPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

// Represents the UI for TimeOut
public class TimeOut extends JFrame {

    private static final String SAVE_DATA_PATH = "./data/savedata.json";
    private static final Color backgroundColor = Color.DARK_GRAY;

    private static final int BORDER = 20;
    private static final int TASKS_PANEL_Y_POSITION = BORDER + 40;
    private static final int TASKS_PANEL_MAX_HEIGHT = 3000;


    private static TaskList taskList;
    private static ReaderWriter readerWriter;
    private int width;
    private int height;

    Font titleFont = new Font("Helvetica", Font.BOLD, 20);
    Font buttonFont = new Font("Helvetica", Font.BOLD, 30);

    // EFFECTS: Starts the app
    public static void main(String[] args) {
        new TimeOut();
    }

    // EFFECTS: Initializes the GUI by setting initial size, loading tasks from memory, and launching the window
    public TimeOut() {
        super("TimeOut");
        width = 1100;
        height = 700;
        setMinimumSize(new Dimension(600,400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        readerWriter = new ReaderWriter(SAVE_DATA_PATH);
        loadTasks();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                windowResizedListener();
            }
        });

        renderWindow();

    }

    public static void saveTasks() {
        try {
            readerWriter.write(taskList);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "Unable to write to file: " + readerWriter.getPath(),"Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: Loads tasks from memory, displays error dialog for any errors that occur
    private void loadTasks() {
        try {
            taskList = readerWriter.read();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to read from file: " + SAVE_DATA_PATH,
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidJsonFileException e) {
            JOptionPane.showMessageDialog(this, "Invalid File: " + SAVE_DATA_PATH
                            + ", Deleting...","Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: Renders the window by removing all elements, drawing title label, and drawing scrollable tasks pane
    private void renderWindow() {
        getContentPane().setBackground(backgroundColor);
        setLayout(null);

        JPanel topPanel = getTopPanel();
        topPanel.setBounds(BORDER, BORDER, width - (BORDER * 2), 25);
        add(topPanel);

        int tasksPanelWidth = width - (BORDER * 2);
        int tasksPanelHeight = height - (TASKS_PANEL_Y_POSITION * 2);
        JScrollPane tasksPane = new TasksPane(taskList, tasksPanelWidth, TASKS_PANEL_MAX_HEIGHT, backgroundColor);
        tasksPane.setBounds(BORDER,TASKS_PANEL_Y_POSITION, tasksPanelWidth, tasksPanelHeight);
        add(tasksPane);

        setSize(width, height);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: When window is resized, gets new width and height and redraws window
    private void windowResizedListener() {
        width = getWidth();
        height = getHeight();
        getContentPane().removeAll();
        renderWindow();
    }

    // EFFECTS: Generates the top panel of the app
    private JPanel getTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(null);
        topPanel.setBackground(backgroundColor);

        JLabel titleLabel = new JLabel("Coming Up:");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0,0,200, 25);
        topPanel.add(titleLabel);

        JButton newButton = new JButton("+");
        newButton.setFont(buttonFont);
        newButton.setForeground(Color.WHITE);
        newButton.setBorder(null);
        newButton.setOpaque(false);
        newButton.setContentAreaFilled(false);
        newButton.setBorderPainted(false);
        newButton.setBounds(width - BORDER - 60, 0, 25, 25);
        topPanel.add(newButton);

        return topPanel;
    }
}
