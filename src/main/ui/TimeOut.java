package ui;

import model.Task;
import model.TaskList;

import java.util.*;

// Time Out Application UI
public class TimeOut {
    private static String NEW_COMMAND = "new";
    private static String COMPLETE_COMMAND = "complete";
    private static String MODIFY_COMMAND = "modify";
    private static String QUIT_COMMAND = "quit";

    private TaskList taskList;
    private Scanner input;

    // EFFECTS: Initializes the UI by generating a task list, scanner, adding some sample tasks and starting the UI loop
    public TimeOut() {
        taskList = new TaskList();
        input = new Scanner(System.in);

        addSampleTasks();

        runUILoop();
    }

    // EFFECTS: Adds some sample tasks to the task list for demonstration
    public void addSampleTasks() {
        Task sampleTaskA = new Task("CPSC210 Project Phase 2", new Date(2021,3,7), 3);
        Task sampleTaskB = new Task("CPEN311 Lab 3", new Date(2021,2,28),4);
        Task sampleTaskC = new Task("CPSC121 Assignment 2", new Date(2021,2,25));
        taskList.add(sampleTaskA);
        taskList.add(sampleTaskB);
        taskList.add(sampleTaskC);
    }

    // EFFECTS: Runs a loop to generate the UI and capture user input
    private void runUILoop() {
        boolean running = true;

        TaskVisualizer taskVisualizer = new TaskVisualizer(taskList);
        System.out.println(taskVisualizer.print());

        while (running) {
            System.out.println(formatCommands());

            String command = processInput(input.next());
            if (command.equals(NEW_COMMAND)) {
                System.out.println("Adding new task!");
            } else if (command.equals(COMPLETE_COMMAND)) {
                System.out.println("Completing task!");
            } else if (command.equals(MODIFY_COMMAND)) {
                System.out.println("Modifying task!");
            } else if (command.equals(QUIT_COMMAND)) {
                System.out.println(randomGoodbye());
                break;
            } else {
                System.out.println("Sorry, I didn't recognize that command");
            }
        }
    }

    // EFFECTS: Returns text instructions for the user on commands they can enter
    private String formatCommands() {
        String commands = "Please choose from one of these commands:  ";
        commands = commands.concat(formatCommand(NEW_COMMAND));
        commands = commands.concat(formatCommand(COMPLETE_COMMAND));
        commands = commands.concat(formatCommand(MODIFY_COMMAND));
        commands = commands.concat(formatCommand(QUIT_COMMAND));
        return commands;
    }

    // EFFECTS: Returns a command formatted as "[C]ommand"
    private String formatCommand(String command) {
        String formattedCommand = "[";
        formattedCommand = formattedCommand.concat(command.substring(0,1));
        formattedCommand = formattedCommand.concat("]");
        formattedCommand = formattedCommand.concat(command.substring(1));
        formattedCommand = formattedCommand.concat("  ");
        return formattedCommand;
    }

    // EFFECTS: Returns a command based on user input. Accepts first letter of a command or the entire command,
    //          case-insensitive. Removes leading/trailing spaces
    private String processInput(String input) {
        String inputClean = input.toLowerCase().trim();
        String output = inputClean;
        if (inputClean.equals(NEW_COMMAND.substring(0,1))) {
            output = NEW_COMMAND;
        } else if (inputClean.equals(COMPLETE_COMMAND.substring(0,1))) {
            output = COMPLETE_COMMAND;
        } else if (inputClean.equals(MODIFY_COMMAND.substring(0,1))) {
            output = MODIFY_COMMAND;
        } else if (inputClean.equals(QUIT_COMMAND.substring(0,1))) {
            output = QUIT_COMMAND;
        }

        return output;
    }

    // EFFECTS: Returns a goodbye message in a random language
    private String randomGoodbye() {
        Random rand = new Random();
        int language = rand.nextInt(10);
        switch (language) {
            case 1: return "Au revoir!";
            case 2: return "Adiós!";
            case 3: return "Zài jiàn!";
            case 4: return "Alwida!";
            case 5: return "Ma'as-salama!";
            case 6: return "La revedere!";
            case 7: return "Biday!";
            case 8: return "Adeus!";
            case 9: return "Sampai Jumpa!";
        }
        return "Bye-bye!";
    }
}
