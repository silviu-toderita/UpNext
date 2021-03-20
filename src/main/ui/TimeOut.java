package ui;

import exceptions.InvalidJsonFileException;
import exceptions.LabelLengthException;
import model.Task;
import model.TaskList;
import persistence.ReaderWriter;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static com.diogonunes.jcolor.Ansi.colorize;

public class TimeOut extends JFrame {

    private static final String SAVE_DATA_PATH = "./data/savedata.json";
    private static final int WIDTH = 900;
    private static final int HEIGHT = 600;

    private TaskList taskList;
    private ReaderWriter readerWriter;
    private TaskRenderer taskRenderer;

    Font titleFont = new Font("Helvetica", Font.BOLD, 20);

    public static void main(String[] args) throws LabelLengthException {
        new TimeOut();
    }

    public TimeOut() throws LabelLengthException {
        super("TimeOut");
        readerWriter = new ReaderWriter(SAVE_DATA_PATH);

        loadTasks();
        taskRenderer = new TaskRenderer(taskList, WIDTH - 40, HEIGHT - 110);

        initializeWindow();

    }

    private void loadTasks() {
        try {
            taskList = readerWriter.read();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to read from file: " + SAVE_DATA_PATH,
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidJsonFileException e) {
            JOptionPane.showMessageDialog(null, "Invalid File: " + SAVE_DATA_PATH
                            + ", Deleting...","Error", JOptionPane.ERROR_MESSAGE);;
        }
    }

    private void initializeWindow() throws LabelLengthException {
        getContentPane().setBackground(Color.DARK_GRAY);
        setLayout(null);
        JLabel titleLabel = new JLabel("Coming Up:");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(25,20,200,25);
        add(titleLabel);
        add(taskRenderer.getTasksPanel());

        setSize(WIDTH, HEIGHT);
        setVisible(true);
    }
}
