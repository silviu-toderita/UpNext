package ui;

import model.Task;
import model.TaskList;
import persistence.ReaderWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;

// Time Out Application UI
public class TimeOut {
    private static final String NEW_COMMAND = "new";
    private static final String COMPLETE_COMMAND = "complete";
    private static final String EDIT_COMMAND = "edit";
    private static final String QUIT_COMMAND = "quit";
    private static final String SAVE_DATA_PATH = "./data/savedata.json";

    private TaskList taskList;
    private Scanner input;
    private ReaderWriter readerWriter;

    // EFFECTS: Initializes the UI by adding some sample tasks and starting the UI loop
    public TimeOut() {
        taskList = new TaskList();
        input = new Scanner(System.in);
        readerWriter = new ReaderWriter(SAVE_DATA_PATH);
        runUILoop();
    }

    // EFFECTS: Runs a loop to generate the UI and capture user input
    private void runUILoop() {
        System.out.println(colorize("\n#############################################################",
                MAGENTA_TEXT()));
        System.out.println(colorize("####  ⌛⌛  TimeOut: Chronological Task-Management  ⌛⌛  ####",
                MAGENTA_TEXT()));
        System.out.println(colorize("#############################################################",
                MAGENTA_TEXT()));
        loadTasks();

        TaskVisualizer taskVisualizer = new TaskVisualizer(taskList);

        System.out.println(taskVisualizer.showTasks());

        while (true) {
            System.out.println(formatCommands());

            String command = processInput(input.nextLine());
            if (command.equals(QUIT_COMMAND)) {
                System.out.println(randomGoodbye());
                break;
            } else {
                if (processCommand(command)) {
                    System.out.println(taskVisualizer.showTasks());
                }
            }

        }
    }

    // EFFECTS: Returns text instructions for the user on commands they can enter
    private String formatCommands() {
        String commands = "Please type one of these commands and press enter:  ";
        commands = commands.concat(formatCommand(NEW_COMMAND));
        commands = commands.concat(formatCommand(COMPLETE_COMMAND));
        commands = commands.concat(formatCommand(EDIT_COMMAND));
        commands = commands.concat(formatCommand(QUIT_COMMAND));
        return colorize(commands,MAGENTA_TEXT());
    }

    // EFFECTS: Returns a command formatted as "[c]ommand"
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
        } else if (inputClean.equals(EDIT_COMMAND.substring(0,1))) {
            output = EDIT_COMMAND;
        } else if (inputClean.equals(QUIT_COMMAND.substring(0,1))) {
            output = QUIT_COMMAND;
        }

        return output;
    }

    // MODIFIES: this
    // EFFECTS: Process the command and return true if it's a valid command
    private boolean processCommand(String command) {
        switch (command) {
            case NEW_COMMAND:
                addTask();
                save();
                break;
            case COMPLETE_COMMAND:
                if (taskList.size() > 0) {
                    completeTask();
                    save();
                }
                break;
            case EDIT_COMMAND:
                if (taskList.size() > 0) {
                    editTask();
                    save();
                }
                break;
            default:
                System.out.println(colorize("Sorry, I didn't recognize that command!",RED_TEXT()));
                return false;
        }
        return true;
    }

    // MODIFIES: this
    // EFFECTS: Captures user input to create a new task
    private void addTask() {
        String name;

        while (true) {
            System.out.println(colorize("Please enter a name for the new task: ", MAGENTA_TEXT()));
            name = input.nextLine();
            if (name.length() > 0) {
                break;
            } else {
                System.out.println(colorize("The name of the task can't be blank!",RED_TEXT()));
            }
        }

        Task task = new Task(name);

        addDate(task);

        taskList.add(task);
        System.out.print(name);
        System.out.println(colorize(" has been added to TimeOut, get to work!",GREEN_TEXT()));
    }

    // MODIFIES: this
    // EFFECTS: Captures user input to complete a task
    private void completeTask() {
        System.out.print(colorize("Please enter the task number you have completed [",MAGENTA_TEXT()));
        System.out.print("1");
        int size = taskList.size();
        if (size > 1) {
            System.out.print("-");
            System.out.print(size);
        }
        System.out.println(colorize("] or press enter to cancel: ", MAGENTA_TEXT()));
        String command = input.nextLine();
        try {
            if (command.length() > 0) {
                int taskNum = Integer.parseInt(command) - 1;
                String name = taskList.get(taskNum).getLabel();
                taskList.complete(taskNum);
                System.out.print(colorize("The following task has been marked as completed: ", GREEN_TEXT()));
                System.out.print(name);
                System.out.println(colorize(" - Great work!", GREEN_TEXT()));

            }
        } catch (Exception e) {
            System.out.println(colorize("Invalid task number!", RED_TEXT()));
        }
    }

    // MODIFIES: this
    // EFFECTS: Captures user input to modify a task
    private void editTask() {
        System.out.print(colorize("Please enter the task number you would like to edit [", MAGENTA_TEXT()));
        System.out.print("1");
        int size = taskList.size();
        if (size > 1) {
            System.out.print("-");
            System.out.print(size);
        }
        System.out.println(colorize("] or press enter to cancel: ",MAGENTA_TEXT()));
        String command = input.nextLine();
        try {
            if (command.length() > 0) {
                int taskNum = Integer.parseInt(command) - 1;
                Task task = taskList.get(taskNum);
                editLabel(task);
                editDate(task);
                System.out.println(colorize("Task successfully updated!",GREEN_TEXT()));
            }
        } catch (Exception e) {
            System.out.println(colorize("Invalid task number!",RED_TEXT()));
        }
    }

    // MODIFIES: task
    // EFFECTS: Captures user input to modify a task name
    private void editLabel(Task task) {
        System.out.print(colorize("You've selected to edit: ", MAGENTA_TEXT()));
        System.out.print(task.getLabel());
        System.out.println(colorize(" - Please enter a new name, or press enter to keep the current name: ",
                MAGENTA_TEXT()));
        String labelInput = input.nextLine();
        if (labelInput.length() > 0) {
            task.setLabel(labelInput);
            System.out.print(colorize("Task name updated to: ", GREEN_TEXT()));
            System.out.println(labelInput);
        }
    }

    // MODIFIES: task
    // EFFECTS: Captures user input to modify a due date for a task
    private void editDate(Task task) {
        System.out.print(colorize("The due date for the selected task is: ", MAGENTA_TEXT()));
        System.out.println(task.getDueDateString());
        while (true) {
            System.out.print(colorize("Please enter a new due date as DD, DD-MM, or DD-MM-YYYY, ",
                    MAGENTA_TEXT()));
            System.out.println(colorize("or press enter if you don't want to edit the date: ", MAGENTA_TEXT()));
            if (captureDate(task)) {
                System.out.print(colorize("Due date updated to: ", GREEN_TEXT()));
                System.out.println(task.getDueDateString());
                break;
            }
        }
    }

    // MODIFIES: task
    // EFFECTS: Captures user input to get a due date for a new task
    private void addDate(Task task) {
        do {
            System.out.print(colorize("Please enter a date for the new task as DD, DD-MM, or DD-MM-YYYY, ",
                    MAGENTA_TEXT()));
            System.out.println(colorize("or press enter if the task has no due date: ",MAGENTA_TEXT()));
        } while (!captureDate(task));

    }

    // MODIFIES: task
    // EFFECTS: Captures user input for due date, returns true if it was successful
    private boolean captureDate(Task task) {
        String dateInput = input.nextLine();
        if (dateInput.length() > 0) {
            try {
                task.setDueDate(parseDate(dateInput));
                return true;
            } catch (Exception e) {
                System.out.println(colorize("Date format invalid!",RED_TEXT()));
                return false;
            }
        }
        return true;
    }

    // EFFECTS: Parses an input string to generate a valid date
    private Date parseDate(String input) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day;

        if (input.length() <= 2) {
            day = Integer.parseInt(input);
        } else if (input.length() <= 5) {
            int hyphenPosition = input.indexOf('-');
            day = Integer.parseInt(input.substring(0,hyphenPosition));
            month = Integer.parseInt(input.substring(hyphenPosition + 1)) - 1;
        } else {
            int firstHyphenPosition = input.indexOf('-');
            int secondHyphenPosition = input.lastIndexOf('-');
            day = Integer.parseInt(input.substring(0,firstHyphenPosition));
            month = Integer.parseInt(input.substring(firstHyphenPosition + 1, secondHyphenPosition)) - 1;
            year = Integer.parseInt(input.substring(secondHyphenPosition + 1));
        }
        cal.set(year, month, day,23,59,59);
        return cal.getTime();
    }

    // MODIFIES: this
    // EFFECTS: Loads a task list from a file
    private void loadTasks() {
        try {
            taskList = readerWriter.read();
            System.out.println(colorize("Tasks loaded successfully from file " + SAVE_DATA_PATH, GREEN_TEXT()));
        } catch (IOException e) {
            System.out.println(colorize("ERROR: Unable to read from file " + SAVE_DATA_PATH, RED_TEXT()));
        }
    }

    // EFFECTS: Writes the current task list to a file
    private void save() {
        try {
            readerWriter.write(taskList);
        } catch (FileNotFoundException e) {
            System.out.println(colorize("ERROR: Unable to write to file " + SAVE_DATA_PATH, RED_TEXT()));
        }
    }

    // EFFECTS: Returns a goodbye message in one of 10 random languages
    private String randomGoodbye() {
        Random rand = new Random();
        int language = rand.nextInt(10);
        switch (language) {
            case 1: return colorize("Au revoir!",MAGENTA_TEXT());
            case 2: return colorize("Adiós!",MAGENTA_TEXT());
            case 3: return colorize("Zài jiàn!",MAGENTA_TEXT());
            case 4: return colorize("Alwida!",MAGENTA_TEXT());
            case 5: return colorize("Ma'as-salama!",MAGENTA_TEXT());
            case 6: return colorize("La revedere!",MAGENTA_TEXT());
            case 7: return colorize("Biday!",MAGENTA_TEXT());
            case 8: return colorize("Adeus!",MAGENTA_TEXT());
            case 9: return colorize("Sampai Jumpa!",MAGENTA_TEXT());
        }
        return colorize("Bye-bye!",YELLOW_TEXT());
    }
}
