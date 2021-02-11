package ui;

import model.Task;
import model.TaskList;

import java.util.*;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;

// Time Out Application UI
public class TimeOut {
    private static final String NEW_COMMAND = "new";
    private static final String COMPLETE_COMMAND = "complete";
    private static final String MODIFY_COMMAND = "modify";
    private static final String QUIT_COMMAND = "quit";

    private static final TaskList TASK_LIST = new TaskList();
    private static final Scanner INPUT = new Scanner(System.in);

    // EFFECTS: Initializes the UI by adding some sample tasks and starting the UI loop
    public TimeOut() {
        addSampleTasks();
        runUILoop();
    }

    // EFFECTS: Adds some sample tasks to the task list for demonstration
    public void addSampleTasks() {
        Calendar cal = Calendar.getInstance();
        cal.set(2021,Calendar.MARCH,7);
        Task sampleTaskA = new Task("CPSC210 Project Phase 2", cal.getTime(), 3);
        cal.set(2021,Calendar.MARCH,1);
        Task sampleTaskB = new Task("CPEN311 Lab 3", cal.getTime(),4);
        cal.set(2021,Calendar.FEBRUARY,25);
        Task sampleTaskC = new Task("CPSC121 Assignment 2", cal.getTime());
        TASK_LIST.add(sampleTaskA);
        TASK_LIST.add(sampleTaskB);
        TASK_LIST.add(sampleTaskC);
    }

    // EFFECTS: Runs a loop to generate the UI and capture user input
    private void runUILoop() {
        TaskVisualizer taskVisualizer = new TaskVisualizer(TASK_LIST);
        System.out.println(colorize("\n#############################################################",
                MAGENTA_TEXT()));
        System.out.println(colorize("####  ⌛⌛  TimeOut: Chronological Task-Management  ⌛⌛  ####",
                MAGENTA_TEXT()));
        System.out.println(colorize("#############################################################",
                MAGENTA_TEXT()));
        System.out.println(taskVisualizer.showTasks());

        while (true) {
            System.out.println(formatCommands());

            String command = processInput(INPUT.nextLine());
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
        String commands = "Please choose from one of these commands:  ";
        commands = commands.concat(formatCommand(NEW_COMMAND));
        commands = commands.concat(formatCommand(COMPLETE_COMMAND));
        commands = commands.concat(formatCommand(MODIFY_COMMAND));
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
        } else if (inputClean.equals(MODIFY_COMMAND.substring(0,1))) {
            output = MODIFY_COMMAND;
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
                break;
            case COMPLETE_COMMAND:
                if (TASK_LIST.size() > 0) {
                    completeTask();
                }
                break;
            case MODIFY_COMMAND:
                if (TASK_LIST.size() > 0) {
                    modifyTask();
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
            System.out.println(colorize("Please enter the name of the new task: ", MAGENTA_TEXT()));
            name = INPUT.nextLine();
            if (name.length() > 0) {
                break;
            } else {
                System.out.println(colorize("The name of the task can't be empty!",RED_TEXT()));
            }
        }

        Task task = new Task(name);

        addDate(task);
        addWeight(task);

        TASK_LIST.add(task);
        System.out.print(name);
        System.out.println(colorize(" has been added to TimeOut, get to work!",GREEN_TEXT()));
    }

    // MODIFIES: this
    // EFFECTS: Captures user input to complete a task
    private void completeTask() {
        System.out.print(colorize("Please enter the task number you would like to complete [",MAGENTA_TEXT()));
        System.out.print("1");
        int size = TASK_LIST.size();
        if (size > 1) {
            System.out.print("-");
            System.out.print(size);
        }
        System.out.println(colorize("] or press enter to cancel: ", MAGENTA_TEXT()));
        String command = INPUT.nextLine();
        try {
            if (command.length() > 0) {
                int taskNum = Integer.parseInt(command) - 1;
                String name = TASK_LIST.get(taskNum).getName();
                TASK_LIST.complete(taskNum);
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
    private void modifyTask() {
        System.out.print(colorize("Please enter the task number you would like to modify [", MAGENTA_TEXT()));
        System.out.print("1");
        int size = TASK_LIST.size();
        if (size > 1) {
            System.out.print("-");
            System.out.print(size);
        }
        System.out.println(colorize("] or press enter to cancel: ",MAGENTA_TEXT()));
        String command = INPUT.nextLine();
        try {
            if (command.length() > 0) {
                int taskNum = Integer.parseInt(command) - 1;
                Task task = TASK_LIST.get(taskNum);
                modifyName(task);
                modifyDate(task);
                modifyWeight(task);
                System.out.println(colorize("Task successfully updated!",GREEN_TEXT()));
            }
        } catch (Exception e) {
            System.out.println(colorize("Invalid task number!",RED_TEXT()));
        }
    }

    // MODIFIES: task
    // EFFECTS: Captures user input to modify a task name
    private void modifyName(Task task) {
        System.out.print(colorize("You've selected to modify: ", MAGENTA_TEXT()));
        System.out.print(task.getName());
        System.out.println(colorize(" - Please enter a new name (or press enter to keep the current name): ",
                MAGENTA_TEXT()));
        String nameInput = INPUT.nextLine();
        if (nameInput.length() > 0) {
            task.setName(nameInput);
            System.out.print(colorize("Name updated to: ", GREEN_TEXT()));
            System.out.println(nameInput);
        }
    }

    // MODIFIES: task
    // EFFECTS: Captures user input to modify a due date for a task
    private void modifyDate(Task task) {
        System.out.print(colorize("The due date for the selected task is: ", MAGENTA_TEXT()));
        System.out.println(task.getDueDateString());
        while (true) {
            System.out.print(colorize("Please enter a new due date as DD, DD-MM, or DD-MM-YYYY ", MAGENTA_TEXT()));
            System.out.println(colorize("(or press enter to keep the current date): ", MAGENTA_TEXT()));
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
            System.out.print(colorize("Please enter a date for the new task as DD, DD-MM, or DD-MM-YYYY ",
                    MAGENTA_TEXT()));
            System.out.println(colorize("(or press enter to skip): ",MAGENTA_TEXT()));
        } while (!captureDate(task));

    }

    // MODIFIES: task
    // EFFECTS: Captures user input for due date, returns true if it was successful
    private boolean captureDate(Task task) {
        String dateInput = INPUT.nextLine();
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
        cal.set(year, month, day);
        return cal.getTime();
    }

    // MODIFIES: task
    // EFFECTS: Captures user input to modify a task weight
    private void modifyWeight(Task task) {
        System.out.print(colorize("The importance of the selected task is: ",MAGENTA_TEXT()));
        System.out.println(task.getWeight());
        while (true) {
            System.out.print(colorize("Please enter a new importance for this task from 1-5 ",MAGENTA_TEXT()));
            System.out.println(colorize("(or press enter to keep current importance): ",MAGENTA_TEXT()));
            if (captureWeight(task)) {
                System.out.print(colorize("Importance updated to: ",GREEN_TEXT()));
                System.out.println(task.getWeight());
                break;
            }
        }
    }

    // MODIFIES: task
    // EFFECTS: Captures user input to add a weight to a new task
    private void addWeight(Task task) {
        do {
            System.out.println(colorize("Please enter how important this task is from 1-5 ",MAGENTA_TEXT()));
            System.out.println(colorize("(or press enter to skip): ",MAGENTA_TEXT()));
        } while (!captureWeight(task));
    }

    // MODIFIES: task
    // EFFECTS: Captures user input for weight, returns true if it was successful
    private boolean captureWeight(Task task) {
        String weightInput = INPUT.nextLine();
        if (weightInput.length() > 0) {
            try {
                int weightInputInt = Integer.parseInt(weightInput);
                if (weightInputInt < 1 | weightInputInt > 5) {
                    System.out.println(colorize("Invalid importance value!",RED_TEXT()));
                    return false;
                }
                task.setWeight(weightInputInt);
            } catch (Exception e) {
                System.out.println(colorize("Invalid importance value!",RED_TEXT()));
                return false;
            }

        }
        return true;
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
