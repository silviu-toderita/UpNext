package ui;

import exceptions.DuplicateTaskException;
import model.Task;
import model.TaskList;
import ui.gui.TaskEditor;
import ui.gui.TasksPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

// Represents the GUI for the UpNext App
public class UpNext extends JFrame {

    private static final Color BACKGROUND_COLOR = Color.DARK_GRAY;
    private static final int BORDER = 20;
    private static final int TASKS_PANEL_Y_POSITION = BORDER + 40;
    private static final Font TITLE_FONT = new Font("Helvetica", Font.BOLD, 20);
    private static final Font BUTTON_FONT = new Font("Helvetica", Font.BOLD, 30);
    private static final Font DATE_FONT = new Font("Helvetica", Font.PLAIN, 16);
    private static final Font SAVE_STATUS_FONT = new Font("Helvetica", Font.ITALIC, 16);
    private static final Color DATE_COLOR = new Color(127,200,255);
    private static final Color SAVE_STATUS_GREEN = new Color(127,200,127);
    private static final Color SAVE_STATUS_YELLOW = new Color(200,200,0);
    private static final Color SAVE_STATUS_RED = new Color(255,127,127);
    private static final int SAVE_STATUS_DISPLAY_SECONDS = 5;

    Timer timer;
    private TaskList taskList;
    private TaskEditor editor;
    private int width;
    private int height;

    private String saveStatus;
    private Color saveStatusColor;

    // EFFECTS: Starts the app GUI
    public static void main(String[] args) {
        new UpNext();
    }

    // EFFECTS: Initializes the GUI
    public UpNext() {
        super("UpNext");
        timer = new Timer();
        width = 1100;
        height = 700;
        setMinimumSize(new Dimension(600,400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSaveStatus("Starting Application...", Color.YELLOW);

        editor = new TaskEditor(taskList, this);
        taskList = new TaskList();
        taskList = editor.loadTasks();

        // Listener for window resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                windowResizedListener();
            }
        });

        renderWindow();
    }

    // MODIFIES: this
    // EFFECTS: Renders the window by removing all elements and redrawing them
    public void renderWindow() {
        getContentPane().removeAll();
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(null);

        // Top Panel with title and "new task" Button
        JPanel topPanel = getTopPanel();
        topPanel.setBounds(BORDER, BORDER, width - (BORDER * 2), 25);
        add(topPanel);

        // Tasks Panel
        int tasksPanelWidth = width - (BORDER * 2);
        int tasksPanelHeight = height - (TASKS_PANEL_Y_POSITION * 2);
        JScrollPane tasksPane = new TasksPane(editor, taskList, tasksPanelWidth, BACKGROUND_COLOR);
        tasksPane.setBounds(BORDER,TASKS_PANEL_Y_POSITION, tasksPanelWidth, tasksPanelHeight);
        add(tasksPane);

        // Today's Date
        JLabel todayDateLabel = getTodayDateLabel();
        todayDateLabel.setBounds(width - 200, TASKS_PANEL_Y_POSITION + tasksPanelHeight + 5,200,25);
        add(todayDateLabel);

        // Save status
        JLabel saveStatusLabel = getSaveStatusLabel();
        saveStatusLabel.setBounds(40, TASKS_PANEL_Y_POSITION + tasksPanelHeight + 5,300,25);
        add(saveStatusLabel);

        update(getGraphics());
        setSize(width, height);
        setVisible(true);
    }



    // MODIFIES: this
    // EFFECTS: When window is resized, get new width and height, redraw window
    private void windowResizedListener() {
        setVisible(false);
        width = getWidth();
        height = getHeight();
        renderWindow();
    }

    // EFFECTS: Generates the top panel of the app with a title and "new task" button
    private JPanel getTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(null);
        topPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("UpNext");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0,0,200, 25);
        topPanel.add(titleLabel);

        JPanel titleUnderline = new JPanel();
        titleUnderline.setBackground(Color.WHITE);
        titleUnderline.setBounds(1,19, 69, 2); //nice
        topPanel.add(titleUnderline);

        topPanel.add(getNewButton());

        return topPanel;
    }

    // EFFECTS: Generates the "new task" button
    private JButton getNewButton() {
        JButton newButton = new JButton("+");
        newButton.setBackground(Color.GRAY);
        newButton.setFont(BUTTON_FONT);
        newButton.setForeground(Color.WHITE);
        newButton.setBorder(null);
        newButton.setOpaque(false);
        newButton.setContentAreaFilled(false);
        newButton.setBorderPainted(false);
        newButton.setBounds(width - BORDER - 60, 0, 30, 30);

        // When the "new task" button is pressed, add a task
        newButton.addActionListener(e -> addTask());

        return newButton;
    }

    // MODIFIES: this
    // EFFECTS: Run the prompts for user input and create new task
    private void addTask() {
        while (true) {
            try {
                Task task = new Task("New Task");
                editor.editLabel(task, true);
                taskList.add(task);
                editor.editDate(task);
                break;
            } catch (DuplicateTaskException e) {
                JOptionPane.showMessageDialog(null, "Task name already exists in list!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                break;
            }
        }
    }

    // EFFECTS: Generate a label with the current save status
    private JLabel getSaveStatusLabel() {
        JLabel saveStatusLabel = new JLabel(saveStatus);

        saveStatusLabel.setFont(SAVE_STATUS_FONT);
        saveStatusLabel.setForeground(saveStatusColor);


        return saveStatusLabel;
    }

    // EFFECT: Generate a label with today's date
    private JLabel getTodayDateLabel() {
        Calendar todayDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy");

        JLabel todayDateLabel = new JLabel("Today: " + sdf.format(todayDate.getTime()));

        todayDateLabel.setFont(DATE_FONT);
        todayDateLabel.setForeground(DATE_COLOR);


        return todayDateLabel;
    }

    // MODIFIES: this
    // EFFECT: Set the save status string and text color, and set a timer for 5 seconds to remove the string
    public void setSaveStatus(String saveStatus, Color color) {
        if (color == Color.YELLOW) {
            saveStatusColor = SAVE_STATUS_YELLOW;
        } else if (color == Color.RED) {
            saveStatusColor = SAVE_STATUS_RED;
        } else if (color == Color.GREEN) {
            saveStatusColor = SAVE_STATUS_GREEN;
        } else {
            saveStatusColor = color;
        }

        this.saveStatus = saveStatus;

        // Set a timer for a few seconds to remove the string, as this status is meant to display only momentarily
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                removeSaveStatus();
            }
        },SAVE_STATUS_DISPLAY_SECONDS * 1000);
    }

    // When timer reaches the end, remove the save status and re-render the window
    private void removeSaveStatus() {
        saveStatus = "";
        renderWindow();
    }
}
