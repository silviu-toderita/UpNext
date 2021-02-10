package ui;

import model.Task;
import model.TaskList;

import java.util.*;

// Time Out Application UI
public class TimeOut {
    private static final String NEW_COMMAND = "new";
    private static final String COMPLETE_COMMAND = "complete";
    private static final String MODIFY_COMMAND = "modify";
    private static final String QUIT_COMMAND = "quit";

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
        Task sampleTaskA = new Task("CPSC210 Project Phase 2", new Date(121,2,7), 3);
        Task sampleTaskB = new Task("CPEN311 Lab 3", new Date(121,1,28),4);
        Task sampleTaskC = new Task("CPSC121 Assignment 2", new Date(121,1,25));
        taskList.add(sampleTaskA);
        taskList.add(sampleTaskB);
        taskList.add(sampleTaskC);
    }

    // EFFECTS: Runs a loop to generate the UI and capture user input
    private void runUILoop() {
        TaskVisualizer taskVisualizer = new TaskVisualizer(taskList);

        System.out.println(taskVisualizer.allTasks());

        while (true) {
            System.out.println(formatCommands());

            String command = processInput(input.nextLine());
            if (command.equals(QUIT_COMMAND)) {
                System.out.println(randomGoodbye());
                break;
            } else {
                if (command.equals(NEW_COMMAND)) {
                    addTask();
                } else if (command.equals(COMPLETE_COMMAND)) {
                    if (taskList.size() > 0) {
                        completeTask();
                    }
                } else if (command.equals(MODIFY_COMMAND)) {
                    if (taskList.size() > 0) {
                        modifyTask();
                    }
                } else {
                    System.out.println("Sorry, I didn't recognize that command!");
                }
                System.out.println(taskVisualizer.allTasks());
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

    // MODIFIES: this
    // EFFECTS: Captures user input to create a new task
    private void addTask() {
        System.out.println("Please enter the name of the new task: ");
        String name = input.nextLine();

        Date date = addTaskDate();
        int weight = addTaskWeight();

        Task task;
        if (date.equals(0)) {
            task = new Task(name,weight);
        } else {
            task = new Task(name,date,weight);
        }

        taskList.add(task);
        System.out.print(name);
        System.out.println(" has been added to TimeOut, get to work!");
    }

    // MODIFIES: this
    // EFFECTS: Captures user input to complete a task
    private void completeTask() {
        System.out.print("Please enter the task number you would like to complete [1");
        int size = taskList.size();
        if (size > 1) {
            System.out.print("-");
            System.out.print(size);
        }
        System.out.println("] or press enter to cancel: ");
        String command = input.nextLine();
        try {
            if (command.length() > 0) {
                int taskNum = Integer.parseInt(command) - 1;
                String name = taskList.get(taskNum).getName();
                taskList.complete(taskNum);
                System.out.print("The following task has been marked as completed: ");
                System.out.print(name);
                System.out.println(" - Great work!");

            }
        } catch (Exception e) {
            System.out.println("Invalid task number!");
        }
    }

    // MODIFIES: this
    // EFFECTS: Captures user input to modify a task
    private void modifyTask() {
        System.out.print("Please enter the task number you would like to modify [1");
        int size = taskList.size();
        if (size > 1) {
            System.out.print("-");
            System.out.print(size);
        }
        System.out.println("] or press enter to cancel: ");
        String command = input.nextLine();
        try {
            if (command.length() > 0) {
                int taskNum = Integer.parseInt(command) - 1;
                Task task = taskList.get(taskNum);
                modifyName(task);
                modifyDueDate(task);
                modifyWeight(task);
                System.out.println("Task successfully updated!");
            }
        } catch (Exception e) {
            System.out.println("Invalid task number!");
        }
    }

    // MODIFIES: task
    // EFFECTS: Captures user input to modify a task name
    private void modifyName(Task task) {
        System.out.print("You've selected to modify: ");
        System.out.print(task.getName());
        System.out.println(" - Please enter a new name (or press enter to keep the current name): ");
        String nameInput = input.nextLine();
        if (nameInput.length() > 0) {
            task.setName(nameInput);
            System.out.print("Name updated to: ");
            System.out.println(nameInput);
        }
    }

    // MODIFIES: task
    // EFFECTS: Captures user input to modify a task due date
    private void modifyDueDate(Task task) {
        System.out.print("The due date for the selected task is: ");
        System.out.print(task.getDueDateString());
        System.out.print(" - Please enter a new due date as DD, DD-MM, or DD-MM-YYYY ");
        System.out.println("(or press enter to keep the current date): ");
        String dateInput = input.nextLine();
        if (dateInput.length() > 0) {
            try {
                task.setDueDate(parseDate(dateInput));
                System.out.print("Due date updated to: ");
                System.out.println(task.getDueDateString());
            } catch (Exception e) {
                System.out.println("Date format invalid, keeping current date!");
            }
        }
    }

    // MODIFIES: task
    // EFFECTS: Captures user input to modify a task weight
    private void modifyWeight(Task task) {
        System.out.print("The importance of the selected task is: ");
        System.out.print(task.getWeight());
        System.out.print(" - Please enter a new importance for this task from 1-5 ");
        System.out.println("(or press enter to keep current importance): ");
        String weightInput = input.nextLine();
        if (weightInput.length() > 0) {
            int weightInputInt = Integer.parseInt(weightInput);
            if (weightInputInt < 1 | weightInputInt > 5) {
                System.out.println("Invalid importance value, keeping current importance!");
            } else {
                task.setWeight(weightInputInt);
                System.out.print("Importance updated to: ");
                System.out.println(weightInputInt);
            }
        }
    }

    // EFFECTS: Captures user input to get a date, and returns a date
    private Date addTaskDate() {
        Date date = new Date(0);

        while (true) {

            System.out.print("Please enter a date for the new task as DD, DD-MM, or DD-MM-YYYY ");
            System.out.println("(or press enter to skip): ");
            String dateInput = input.nextLine();

            if (dateInput.length() > 0) {
                try {
                    date = parseDate(dateInput);
                    break;
                } catch (Exception e) {
                    System.out.println("Date format invalid!");
                }
            } else {
                break;
            }
        }

        return date;
    }

    // EFFECTS: Captures user input to get a weight, and returns a weight
    private int addTaskWeight() {
        int weight = 3;
        while (true) {
            System.out.println("Please enter how important this task is from 1-5 (or press enter to skip): ");
            String weightInput = input.nextLine();
            if (weightInput.length() > 0) {
                int weightInputInt = Integer.parseInt(weightInput);
                if (weightInputInt < 1 | weightInputInt > 5) {
                    System.out.println("Invalid importance value!");
                } else {
                    weight = weightInputInt;
                    break;
                }
            } else {
                break;
            }
        }
        return weight;
    }

    // EFFECTS: Parses an input string to generate a valid date
    private Date parseDate(String input) {
        Date date = new Date(2020,02,10);
        if (input.length() <= 2) {
            int day = Integer.parseInt(input);
            date = new Date(121,01,day);
        } else if (input.length() <= 5) {
            int hyphenPosition = input.indexOf('-');
            int day = Integer.parseInt(input.substring(0,hyphenPosition));
            int month = Integer.parseInt(input.substring(hyphenPosition + 1)) - 1;
            date = new Date(121,month,day);
        } else {
            int firstHyphenPosition = input.indexOf('-');
            int secondHyphenPosition = input.lastIndexOf('-');
            int day = Integer.parseInt(input.substring(0,firstHyphenPosition));
            int month = Integer.parseInt(input.substring(firstHyphenPosition + 1, secondHyphenPosition)) - 1;
            int year = Integer.parseInt(input.substring(secondHyphenPosition + 1)) - 1900;
            date = new Date(year,month,day);
        }
        return date;
    }

    // EFFECTS: Returns a goodbye message in one of 10 random languages
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
