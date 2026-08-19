import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

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
    private static final List<String> tasks = new ArrayList<>();

    /**
     * Adds a task to the existing tasks
     */
    public static void addTask(String msg) {
        tasks.add(msg);
    }

    /**
     * Prints the chatbot's response surrounded by separator lines.
     */
    public static void printEchoMessage(String msg) {
        System.out.println(LINE);
        System.out.println("added: " + msg);
        System.out.println(LINE);
    }

    /**
     * Prints the chatbot's exit response.
     */
    public static void printExitMessage() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    /**
     * Prints all task in tasks
     */
    public static void printTasks() {
        System.out.println(LINE);
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(String.format("%d. %s", i + 1, tasks.get(i)));
        }
        System.out.println(LINE);
    }

    public static void main(String[] args) {
        System.out.println(LINE);
        System.out.println(BANNER);
        System.out.println("Hello! I'm Ducky.");
        System.out.println("What can I do for you?");
        System.out.println(LINE);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String msg = scanner.nextLine();
            if (msg.equals("bye")) {
                break;
            } else if (msg.equals("list")) {
                printTasks();
            } else {
                addTask(msg);
                printEchoMessage(msg);
            }
        }
        printExitMessage();
    }
}
