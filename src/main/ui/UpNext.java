package ui;

import exceptions.LabelLengthException;
import model.Task;
import model.TaskList;
import ui.gui.ContentEditor;
import ui.gui.TasksPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;

// Represents the UI for UpNext
public class UpNext extends JFrame {


    private static final Color backgroundColor = Color.DARK_GRAY;

    private static final int BORDER = 20;
    private static final int TASKS_PANEL_Y_POSITION = BORDER + 40;
    private static final int TASKS_PANEL_MAX_HEIGHT = 3000;

    private static final Font titleFont = new Font("Helvetica", Font.BOLD, 20);
    private static final Font buttonFont = new Font("Helvetica", Font.BOLD, 30);
    private static final Font dateFont = new Font("Helvetica", Font.ITALIC, 16);

    private TaskList taskList;
    private ContentEditor editor;
    private int width;
    private int height;

    // EFFECTS: Starts the app
    public static void main(String[] args) {
        new UpNext();
    }

    // EFFECTS: Initializes the GUI by setting initial size, loading tasks from memory, and launching the window
    public UpNext() {
        super("UpNext");
        width = 1100;
        height = 700;
        setMinimumSize(new Dimension(600,400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        editor = new ContentEditor(taskList, this);
        taskList = editor.loadTasks();

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
    // EFFECTS: Renders the window by removing all elements, drawing title label, and drawing scrollable tasks pane
    public void renderWindow() {
        getContentPane().removeAll();
        getContentPane().setBackground(backgroundColor);
        setLayout(null);

        JPanel topPanel = getTopPanel();
        topPanel.setBounds(BORDER, BORDER, width - (BORDER * 2), 25);
        add(topPanel);

        int tasksPanelWidth = width - (BORDER * 2);
        int tasksPanelHeight = height - (TASKS_PANEL_Y_POSITION * 2);
        JScrollPane tasksPane = new TasksPane(editor, taskList, tasksPanelWidth,
                TASKS_PANEL_MAX_HEIGHT, backgroundColor);
        tasksPane.setBounds(BORDER,TASKS_PANEL_Y_POSITION, tasksPanelWidth, tasksPanelHeight);
        add(tasksPane);

        Calendar todayDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy");
        JLabel todayDateLabel = new JLabel("Today: " + sdf.format(todayDate.getTime()));
        todayDateLabel.setFont(dateFont);
        todayDateLabel.setForeground(Color.WHITE);
        todayDateLabel.setBounds(width - 200, TASKS_PANEL_Y_POSITION + tasksPanelHeight + 5,200,25);
        add(todayDateLabel);

        setSize(width, height);
        setVisible(true);
    }



    // MODIFIES: this
    // EFFECTS: When window is resized, gets new width and height and redraws window
    private void windowResizedListener() {
        setVisible(false);
        width = getWidth();
        height = getHeight();
        renderWindow();
    }

    // EFFECTS: Generates the top panel of the app
    private JPanel getTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(null);
        topPanel.setBackground(backgroundColor);

        JLabel titleLabel = new JLabel("Up Next:");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0,0,200, 25);
        topPanel.add(titleLabel);



        JButton newButton = new JButton("+");
        newButton.setBackground(Color.GRAY);
        newButton.setFont(buttonFont);
        newButton.setForeground(Color.WHITE);
        newButton.setBorder(null);
        newButton.setOpaque(false);
        newButton.setContentAreaFilled(false);
        newButton.setBorderPainted(false);
        newButton.setBounds(width - BORDER - 60, 0, 30, 30);
        topPanel.add(newButton);

        newButton.addActionListener(e -> addTask());

        return topPanel;
    }

    private void addTask() {
        try {
            Task task = new Task("New Task");
            editor.editLabel(task, true);
            taskList.add(task);
            editor.editDate(task,true);
        } catch (LabelLengthException e) {
            // Do nothing
        } catch (NullPointerException e) {
            // Do nothing
        }

    }
}
