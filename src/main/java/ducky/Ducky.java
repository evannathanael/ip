package ducky;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Represents the main chatbot application.
 */
public class Ducky {
    private static final String LINE = "____________________________________________________________";
    private static final String BANNER = " ____  _   _  ____ _  ____   __\n"
            + "|  _ \\| | | |/ ___| |/ /\\ \\ / /\n"
            + "| | | | | | | |   | ' /  \\ V / \n"
            + "| |_| | |_| | |___| . \\   | |  \n"
            + "|____/ \\___/ \\____|_|\\_\\  |_|  \n";

    private final Storage storage;
    private final List<Task> tasks;

    /**
     * Creates a chatbot and loads any previously saved tasks.
     *
     * @throws DuckyException if the saved tasks cannot be loaded
     */
    public Ducky() throws DuckyException {
        storage = new Storage();
        tasks = new ArrayList<>(storage.load());
    }

    /**
     * Adds a task to the task list and prints a confirmation message.
     *
     * @param task the task to add
     */
    public void addTask(Task task) {
        tasks.add(task);
        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println(String.format("Now you have %d tasks in the list.", tasks.size()));
        System.out.println(LINE);
    }

    /**
     * Prints the chatbot's exit response.
     */
    public void printExitMessage() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    /**
     * Prints all tasks in the task list.
     */
    public void printTasks() {
        System.out.println(LINE);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println(String.format("%d.%s", i + 1, task));
        }
        System.out.println(LINE);
    }

    /**
     * Marks a task as done and prints its updated state.
     *
     * @param taskNumber the one-based task number
     */
    public void markAsDone(int taskNumber) {
        Task task = tasks.get(taskNumber - 1);
        task.markAsDone();
        System.out.println(LINE);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
        System.out.println(LINE);
    }

    /**
     * Marks a task as incomplete and prints its updated state.
     *
     * @param taskNumber the one-based task number
     */
    public void unmark(int taskNumber) {
        Task task = tasks.get(taskNumber - 1);
        task.unmark();
        System.out.println(LINE);
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
        System.out.println(LINE);
    }

    /**
     * Deletes a task from the task list and prints a confirmation message.
     *
     * @param taskNumber the one-based task number
     */
    public void delete(int taskNumber) {
        Task task = tasks.get(taskNumber - 1);
        tasks.remove(taskNumber - 1);
        System.out.println(LINE);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println(String.format("Now you have %d tasks in the list.", tasks.size()));
        System.out.println(LINE);
    }

    /**
     * Prints an error message for invalid user input.
     *
     * @param message the user-facing error message
     */
    public void printErrorMessage(String message) {
        System.out.println(LINE);
        System.out.println("QUACK! " + message);
        System.out.println(LINE);
    }

    /**
     * Parses and validates a task number from a task command.
     *
     * @param command the complete user command
     * @param commandPrefix the command prefix to remove
     * @return the requested one-based task number
     * @throws DuckyException if the task number is invalid or does not exist
     */
    public int getTaskNumber(String command, String commandPrefix) throws DuckyException {
        String numberText = command.substring(commandPrefix.length()).trim();
        int taskNumber;

        try {
            taskNumber = Integer.parseInt(numberText);
        } catch (NumberFormatException e) {
            throw new DuckyException("Please enter a valid task number 🐥");
        }

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new DuckyException("That task number does not exist 🐥");
        }

        return taskNumber;
    }

    /**
     * Processes one user command.
     *
     * @param command the user's command
     * @return {@code false} when the chatbot should exit, otherwise {@code true}
     * @throws DuckyException if the command is invalid
     */
    public boolean processCommand(String command) throws DuckyException {
        switch (command) {
        case "bye":
            printExitMessage();
            return false;

        case "list":
            printTasks();
            return true;

        default:
            if (command.startsWith("mark ")) {
                int taskNumber = getTaskNumber(command, "mark ");
                markAsDone(taskNumber);
                saveTasks();
            } else if (command.startsWith("unmark ")) {
                int taskNumber = getTaskNumber(command, "unmark ");
                unmark(taskNumber);
                saveTasks();
            } else if (command.equals("todo") || command.startsWith("todo ")) {
                processTodoCommand(command);
                saveTasks();
            } else if (command.equals("deadline") || command.startsWith("deadline ")) {
                processDeadlineCommand(command);
                saveTasks();
            } else if (command.equals("event") || command.startsWith("event ")) {
                processEventCommand(command);
                saveTasks();
            } else if (command.startsWith("delete ")) {
                int taskNumber = getTaskNumber(command, "delete ");
                delete(taskNumber);
                saveTasks();
            } else {
                throw new DuckyException("I didn't get what you said 🐥");
            }
        }
        return true;
    }

    /**
     * Saves the current task list to the hard disk.
     *
     * @throws DuckyException if the task list cannot be saved
     */
    private void saveTasks() throws DuckyException {
        storage.save(tasks);
    }

    /**
     * Processes a todo command after validating its description.
     *
     * @param command the complete todo command
     * @throws DuckyException if the description is empty
     */
    private void processTodoCommand(String command) throws DuckyException {
        String description = command.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new DuckyException("To do task is empty! 🐥");
        }
        addTask(new ToDo(description));
    }

    /**
     * Processes a deadline command after validating its description and due date.
     *
     * @param command the complete deadline command
     * @throws DuckyException if the command is malformed
     */
    private void processDeadlineCommand(String command) throws DuckyException {
        String commandWithoutPrefix = command.substring("deadline".length()).trim();
        int markerIndex = commandWithoutPrefix.indexOf(" /by ");

        if (markerIndex == -1) {
            throw new DuckyException("A deadline must include '/by' followed by a date or time 🐥");
        }

        String description = commandWithoutPrefix.substring(0, markerIndex).trim();
        String by = commandWithoutPrefix.substring(markerIndex + " /by ".length()).trim();

        if (description.isEmpty()) {
            throw new DuckyException("A deadline description cannot be empty 🐥");
        }
        if (by.isEmpty()) {
            throw new DuckyException("A deadline must include a date or time after '/by' 🐥");
        }

        addTask(new Deadline(description, by));
    }

    /**
     * Processes an event command after validating its description and time range.
     *
     * @param command the complete event command
     * @throws DuckyException if the command is malformed
     */
    private void processEventCommand(String command) throws DuckyException {
        String commandWithoutPrefix = command.substring("event".length()).trim();
        int fromIndex = commandWithoutPrefix.indexOf(" /from ");
        int toIndex = commandWithoutPrefix.indexOf(" /to ");

        if (fromIndex == -1 || toIndex == -1) {
            throw new DuckyException("An event must include both '/from' and '/to' 🐥");
        }
        if (fromIndex > toIndex) {
            throw new DuckyException("'/from' must appear before '/to' 🐥");
        }

        String description = commandWithoutPrefix.substring(0, fromIndex).trim();
        String start = commandWithoutPrefix.substring(fromIndex + " /from ".length(), toIndex).trim();
        String end = commandWithoutPrefix.substring(toIndex + " /to ".length()).trim();

        if (description.isEmpty()) {
            throw new DuckyException("An event description cannot be empty 🐥");
        }
        if (start.isEmpty()) {
            throw new DuckyException("An event must include a start time after '/from' 🐥");
        }
        if (end.isEmpty()) {
            throw new DuckyException("An event must include an end time after '/to'🐥");
        }

        addTask(new Event(description, start, end));
    }

    /**
     * Starts the chatbot and reads commands from standard input.
     *
     * @param args command-line arguments, which are currently unused
     */
    public static void main(String[] args) {
        Ducky ducky;
        try {
            ducky = new Ducky();
        } catch (DuckyException e) {
            System.out.println(LINE);
            System.out.println("QUACK! " + e.getMessage());
            System.out.println(LINE);
            return;
        }

        System.out.println(LINE);
        System.out.println(BANNER);
        System.out.println("Hello! I'm Ducky 🐥");
        System.out.println("What can I do for you?");
        System.out.println(LINE);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            try {
                if (!ducky.processCommand(command)) {
                    return;
                }
            } catch (DuckyException e) {
                ducky.printErrorMessage(e.getMessage());
            }
        }
    }
}
