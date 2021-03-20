package ui;

import exceptions.*;
import model.Task;
import model.TaskList;
import persistence.ReaderWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;

// TimeOut Application UI
public class ConsoleUI {
    private static final String NEW_COMMAND = "new";
    private static final String COMPLETE_COMMAND = "complete";
    private static final String EDIT_COMMAND = "edit";
    private static final String QUIT_COMMAND = "quit";

    private static final String DATE_FORMAT_MESSAGE = "You can type things like 'today', 'tomorrow', 'monday', 'mon',"
            + " '25', '25-12', or '25-12-2021':";

    private TaskList taskList;
    private Scanner input;
    private ReaderWriter readerWriter;
    private TaskVisualizer taskVisualizer;

    // EFFECTS: Initializes the UI by printing startup text, initializing objects, and starting UI loop
    public ConsoleUI(TaskList taskList,ReaderWriter readerWriter) {
        this.taskList = taskList;
        input = new Scanner(System.in);
        this.readerWriter = readerWriter;

        System.out.println(colorize("\n#############################################################",
                MAGENTA_TEXT()));
        System.out.println(colorize("####  ⌛⌛  TimeOut: Chronological Task-Management  ⌛⌛  ####",
                MAGENTA_TEXT()));
        System.out.println(colorize("#############################################################",
                MAGENTA_TEXT()));
        loadTasks();

        taskVisualizer = new TaskVisualizer(taskList);
        System.out.println(taskVisualizer.showTasks());

        runUILoop();
    }

    // EFFECTS: Runs a loop to generate the UI and capture user input
    private void runUILoop() {

        while (true) {
            System.out.println(formatCommands());

            String command = processCommandInput(input.nextLine());
            if (command.equals(QUIT_COMMAND)) {
                System.out.println(randomGoodbye());
                break;
            } else {
                if (!processCommand(command)) {
                    System.out.println(colorize("Sorry, I didn't recognize that command!",RED_TEXT()));
                }
            }
            System.out.println(taskVisualizer.showTasks());
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

    // MODIFIES: this
    // EFFECTS: Process the given command, return true if command recognized or false if command invalid
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
                return false;
        }
        return true;
    }

    // EFFECTS: Returns a command based on user input. Accepts first letter of a command or the entire command,
    //          case-insensitive. Removes leading/trailing spaces
    private String processCommandInput(String input) {
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
    // EFFECTS: Captures user input to create a new task
    private void addTask() {
        Task task;

        while (true) {
            System.out.println(colorize("Please enter a name for the new task: ", MAGENTA_TEXT()));

            try {
                task = new Task(input.nextLine());
                break;
            } catch (LabelLengthException e) {
                System.out.println(colorize("The name of the task can't be blank!",RED_TEXT()));
            }

        }

        addDate(task);

        taskList.add(task);
        System.out.print(task.getLabel());
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
        if (command.length() > 0) {
            int taskNum = Integer.parseInt(command) - 1;
            try {
                String name = taskList.get(taskNum).getLabel();
                taskList.complete(taskNum);
                System.out.print(colorize("The following task has been marked as completed: ", GREEN_TEXT()));
                System.out.print(name);
                System.out.println(colorize(" - Great work!", GREEN_TEXT()));
            } catch (IndexOutOfBoundsException e) {
                System.out.println(colorize("Invalid task number!", RED_TEXT()));
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Captures user input to edit a task
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
        if (command.length() > 0) {
            int taskNum = Integer.parseInt(command) - 1;
            try {
                Task task = taskList.get(taskNum);
                editLabel(task);
                editDate(task);
                System.out.println(colorize("Task successfully updated!", GREEN_TEXT()));
            } catch (IndexOutOfBoundsException e) {
                System.out.println(colorize("Invalid task number!",RED_TEXT()));
            }
        }
    }

    // MODIFIES: task
    // EFFECTS: Captures user input to edit a task name
    private void editLabel(Task task) {
        System.out.print(colorize("You've selected to edit: ", MAGENTA_TEXT()));
        System.out.print(task.getLabel());
        System.out.println(colorize(" - Please enter a new name, or press enter to keep the current name: ",
                MAGENTA_TEXT()));
        String labelInput = input.nextLine();

        try {
            task.setLabel(labelInput);
            System.out.print(colorize("Task name updated to: ", GREEN_TEXT()));
            System.out.println(labelInput);
        } catch (LabelLengthException e) {
            // Given label is zero-length, skip editing label
        }
    }

    // MODIFIES: task
    // EFFECTS: Captures user input to edit a due date for a task
    private void editDate(Task task) {
        System.out.print(colorize("The due date for the selected task is: ", MAGENTA_TEXT()));
        System.out.println(task.getDueDateString());
        while (true) {
            System.out.println(colorize("Please enter a new due date or press enter to keep current date. "
                    + DATE_FORMAT_MESSAGE, MAGENTA_TEXT()));
            try {
                if (captureDate(task)) {
                    System.out.print(colorize("Due date updated to: ", GREEN_TEXT()));
                    System.out.println(task.getDueDateString());
                }
                break;
            } catch (Exception e) {
                System.out.println(colorize("Date format invalid!",RED_TEXT()));
            }
        }
    }

    // MODIFIES: task
    // EFFECTS: Captures user input to get a due date for a new task
    private void addDate(Task task) {
        while (true) {
            System.out.println(colorize("Please enter a date for the new task, or press enter to skip. "
                            + DATE_FORMAT_MESSAGE, MAGENTA_TEXT()));
            if (captureDate(task)) {
                break;
            } else {
                System.out.println(colorize("Date format invalid!", RED_TEXT()));
            }
        }

    }

    // MODIFIES: task
    // EFFECTS: Captures user input for due date. Returns false only if date is empty
    private boolean captureDate(Task task) {
        String dateInput = input.nextLine();
        if (dateInput.length() > 0) {
            task.setDueDate(parseDate(dateInput));
            return true;
        }
        return false;
    }

    // EFFECTS: Parses an input string to generate a valid date
    private Date parseDate(String input) {
        String inputClean = input.toLowerCase().trim();
        Date date;

        if (Character.isDigit(inputClean.charAt(0))) {
            date =  parseDateNumeric(inputClean);
        } else {
            date = parseDateAlpha(inputClean);
        }

        return date;
    }

    // EFFECT: Parses a numeric input string to generate a valid date
    private Date parseDateNumeric(String input) {
        Calendar cal = Calendar.getInstance();

        if (input.length() <= 2) {
            cal.set(Calendar.DATE,Integer.parseInt(input));
        } else if (input.length() <= 5) {
            int hyphenPosition = input.indexOf('-');
            cal.set(Calendar.DATE,Integer.parseInt(input.substring(0,hyphenPosition)));
            cal.set(Calendar.MONTH,Integer.parseInt(input.substring(hyphenPosition + 1)) - 1);
        } else {
            int firstHyphenPosition = input.indexOf('-');
            int secondHyphenPosition = input.lastIndexOf('-');
            cal.set(Calendar.DATE,Integer.parseInt(input.substring(0,firstHyphenPosition)));
            cal.set(Calendar.MONTH,Integer.parseInt(input.substring(firstHyphenPosition + 1, secondHyphenPosition))
                    - 1);
            cal.set(Calendar.YEAR,Integer.parseInt(input.substring(secondHyphenPosition + 1)));
        }

        return cal.getTime();
    }

    // EFFECT: Parses an alphabetic input string to generate a valid date
    private Date parseDateAlpha(String input) {
        Calendar cal = Calendar.getInstance();
        int inputDay;

        if (input.equals("monday") | input.equals("mon")) {
            inputDay = Calendar.MONDAY;
        } else if (input.equals("tuesday") | input.equals("tue")) {
            inputDay = Calendar.TUESDAY;
        } else if (input.equals("wednesday") | input.equals("wed")) {
            inputDay = Calendar.WEDNESDAY;
        } else if (input.equals("thursday") | input.equals("thu")) {
            inputDay = Calendar.THURSDAY;
        } else if (input.equals("friday") | input.equals("fri")) {
            inputDay = Calendar.FRIDAY;
        } else if (input.equals("saturday") | input.equals("sat")) {
            inputDay = Calendar.SATURDAY;
        } else if (input.equals("sunday") | input.equals("sun")) {
            inputDay = Calendar.SUNDAY;
        } else if (input.equals("tomorrow")) {
            cal.add(Calendar.DATE, 1);
            return cal.getTime();
        } else {
            return cal.getTime();
        }

        return generateDateFromDayOfWeek(inputDay);
    }

    // EFFECT: Returns the next date matching the given dayOfWeek (including today)
    private Date generateDateFromDayOfWeek(int dayOfWeek) {
        Calendar cal = Calendar.getInstance();
        int dayDifference = dayOfWeek - cal.get(Calendar.DAY_OF_WEEK);
        if (dayDifference < 0) {
            dayDifference += 7;
        }
        cal.add(Calendar.DATE, dayDifference);
        return cal.getTime();
    }

    // MODIFIES: this
    // EFFECTS: Loads a task list from a file
    private void loadTasks() {
        try {
            taskList = readerWriter.read();
            System.out.println(colorize("Tasks loaded successfully from file " + readerWriter.getPath(), GREEN_TEXT()));
        } catch (IOException e) {
            System.out.println(colorize("ERROR: Unable to read from file " + readerWriter.getPath(), RED_TEXT()));
        } catch (InvalidJsonFileException e) {
            System.out.println(colorize("ERROR: Invalid file " + readerWriter.getPath() + ", deleting", RED_TEXT()));
        }
    }

    // EFFECTS: Writes the current task list to a file
    private void save() {
        try {
            readerWriter.write(taskList);
        } catch (FileNotFoundException e) {
            System.out.println(colorize("ERROR: Unable to write to file " + readerWriter.getPath(), RED_TEXT()));
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
