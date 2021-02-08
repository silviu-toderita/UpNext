package ui;

import model.TaskList;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Time Out Application UI
public class TimeOut {
    private static String NEW_COMMAND = "New";
    private static String COMPLETE_COMMAND = "Complete";
    private static String MODIFY_COMMAND = "Modify";
    private static String QUIT_COMMAND = "Quit";

    private TaskList taskList;
    private Scanner input;


    public TimeOut() {
        taskList = new TaskList();
        input = new Scanner(System.in);
        runUILoop();
    }

    private void runUILoop() {
        boolean running = true;

        while (running) {
            TaskVisualizer taskVisualizer = new TaskVisualizer(taskList);
            System.out.println(taskVisualizer.print());
            System.out.println(printCommands());
            String command = input.next();
        }
    }

    private String printCommands() {
        String toPrint = "Please choose from one of these commands:  ";
        toPrint = toPrint.concat(formatCommand(NEW_COMMAND));
        toPrint = toPrint.concat(formatCommand(COMPLETE_COMMAND));
        toPrint = toPrint.concat(formatCommand(MODIFY_COMMAND));
        toPrint = toPrint.concat(formatCommand(QUIT_COMMAND));
        return toPrint;
    }

    private String formatCommand(String command) {
        String formattedCommand = "[";
        formattedCommand = formattedCommand.concat(command.substring(0,1));
        formattedCommand = formattedCommand.concat("]");
        formattedCommand = formattedCommand.concat(command.substring(1));
        formattedCommand = formattedCommand.concat("  ");
        return formattedCommand;
    }
}
