package ducky;

import java.util.Scanner;

/**
 * Handles all interactions between Ducky and the user.
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
     * Displays the chatbot's welcome message.
     */
    public void showWelcome() {
        System.out.println(LINE);
        System.out.println(BANNER);
        System.out.println("Hello! I'm Ducky 🐥");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    /**
     * Reads one command from the user.
     *
     * @return the user's command
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the chatbot's exit message.
     */
    public void showExitMessage() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    /**
     * Displays the task-added confirmation.
     *
     * @param task the added task
     * @param taskCount the number of tasks after adding
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println(String.format("Now you have %d tasks in the list.", taskCount));
        System.out.println(LINE);
    }

    /**
     * Displays all tasks.
     *
     * @param tasks the tasks to display
     */
    public void showTasks(TaskList tasks) {
        System.out.println(LINE);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(String.format("%d.%s", i + 1, tasks.get(i)));
        }
        System.out.println(LINE);
    }

    /**
     * Displays a task-marked-as-done confirmation.
     *
     * @param task the updated task
     */
    public void showTaskMarkedAsDone(Task task) {
        System.out.println(LINE);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
        System.out.println(LINE);
    }

    /**
     * Displays a task-unmarked confirmation.
     *
     * @param task the updated task
     */
    public void showTaskUnmarked(Task task) {
        System.out.println(LINE);
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
        System.out.println(LINE);
    }

    /**
     * Displays a task-deleted confirmation.
     *
     * @param task the deleted task
     * @param taskCount the number of tasks after deletion
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println(LINE);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println(String.format("Now you have %d tasks in the list.", taskCount));
        System.out.println(LINE);
    }

    /**
     * Displays an error message.
     *
     * @param message the user-facing error message
     */
    public void showError(String message) {
        System.out.println(LINE);
        System.out.println("QUACK! " + message);
        System.out.println(LINE);
    }
}
