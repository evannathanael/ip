package ducky;

import java.util.List;
import java.util.Scanner;

/**
 * Formats all messages shown to the user and reads commands from standard input.
 * Callers (the text UI or the GUI) decide how to display the formatted messages.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private static final String BANNER = " ____  _   _  ____ _  ____   __\n"
            + "|  _ \\| | | |/ ___| |/ /\\ \\ / /\n"
            + "| | | | | | | |   | ' /  \\ V / \n"
            + "| |_| | |_| | |___| . \\   | |  \n"
            + "|____/ \\___/ \\____|_|\\_\\  |_|  \n";

    private final Scanner scanner;

    /**
     * Creates a user interface that reads commands from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Formats the chatbot's welcome message.
     *
     * @return the welcome message.
     */
    public String showWelcome() {
        return LINE + "\n" + BANNER + "\n"
                + "Hello! I'm Ducky 🐥\n"
                + "What can I do for you?\n"
                + LINE;
    }

    /**
     * Reads one command from the user.
     *
     * @return the user's command.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Formats the chatbot's exit message.
     *
     * @return the exit message.
     */
    public String showExitMessage() {
        return LINE + "\n" + "Bye. Hope to see you again soon!" + "\n" + LINE;
    }

    /**
     * Formats the task-added confirmation.
     *
     * @param task the added task.
     * @param taskCount the number of tasks after adding.
     * @return the task-added confirmation.
     */
    public String showTaskAdded(Task task, int taskCount) {
        return LINE + "\n"
                + "Got it. I've added this task:\n"
                + "  " + task + "\n"
                + String.format("Now you have %d tasks in the list.", taskCount) + "\n"
                + LINE;
    }

    /**
     * Formats all tasks.
     *
     * @param tasks the tasks to display.
     * @return the formatted task list.
     */
    public String showTasks(TaskList tasks) {
        StringBuilder message = new StringBuilder(LINE + "\n" + "Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            message.append(String.format("%d.%s", i + 1, tasks.get(i))).append("\n");
        }
        message.append(LINE);
        return message.toString();
    }

    /**
     * Formats tasks matching a search keyword.
     *
     * @param matchingTasks the tasks matching the search keyword.
     * @return the formatted matching task list.
     */
    public String showMatchingTasks(List<Task> matchingTasks) {
        StringBuilder message = new StringBuilder(LINE + "\n" + "Here are the matching tasks in your list:\n");
        for (int i = 0; i < matchingTasks.size(); i++) {
            message.append(String.format("%d.%s", i + 1, matchingTasks.get(i))).append("\n");
        }
        message.append(LINE);
        return message.toString();
    }

    /**
     * Formats a task-marked-as-done confirmation.
     *
     * @param task the updated task.
     * @return the task-marked-as-done confirmation.
     */
    public String showTaskMarkedAsDone(Task task) {
        return LINE + "\n"
                + "Nice! I've marked this task as done:\n"
                + "  " + task + "\n"
                + LINE;
    }

    /**
     * Formats a task-unmarked confirmation.
     *
     * @param task the updated task.
     * @return the task-unmarked confirmation.
     */
    public String showTaskUnmarked(Task task) {
        return LINE + "\n"
                + "OK, I've marked this task as not done yet:\n"
                + "  " + task + "\n"
                + LINE;
    }

    /**
     * Formats a task-deleted confirmation.
     *
     * @param task the deleted task.
     * @param taskCount the number of tasks after deletion.
     * @return the task-deleted confirmation.
     */
    public String showTaskDeleted(Task task, int taskCount) {
        return LINE + "\n"
                + "Noted. I've removed this task:\n"
                + "  " + task + "\n"
                + String.format("Now you have %d tasks in the list.", taskCount) + "\n"
                + LINE;
    }

    /**
     * Formats an error message.
     *
     * @param message the user-facing error message.
     * @return the formatted error message.
     */
    public String showError(String message) {
        return LINE + "\n" + "QUACK! " + message + "\n" + LINE;
    }
}
