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
    private static final List<Task> tasks = new ArrayList<>();

    /**
     * Adds a task to the existing tasks
     */
    public static void addTask(Task task) {
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
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println(String.format("%d.%s", i + 1, task));
        }
        System.out.println(LINE);
    }

    /**
     * Marks a task as done and prints the state and description of the task
     */
    public static void markAsDone(int taskNumber) {
        System.out.println(LINE);
        System.out.println("Nice! I've marked this task as done:");
        tasks.get(taskNumber - 1).markAsDone();
        System.out.println("  " + tasks.get(taskNumber - 1));
        System.out.println(LINE);
    }

    /**
     * Unmarks a task and prints the state and description of the task
     */
    public static void unmark(int taskNumber) {
        System.out.println(LINE);
        System.out.println("OK, I've marked this task as not done yet:");
        tasks.get(taskNumber - 1).unmark();
        System.out.println("  " + tasks.get(taskNumber - 1));
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
            switch (msg) {
                case "bye":
                    printExitMessage();
                    return;

                case "list":
                    printTasks();
                    break;

                default:
                    if (msg.startsWith("mark ")) {
                        int taskNumber = Integer.parseInt(msg.substring(5));
                        markAsDone(taskNumber);
                    } else if (msg.startsWith("unmark ")) {
                        int taskNumber = Integer.parseInt(msg.substring(7));
                        unmark(taskNumber);
                    } else if (msg.startsWith("todo ")){
                        String description = msg.substring(5);
                        addTask(new ToDo(description));
                    } else if (msg.startsWith("deadline ")){
                        String prefix = "deadline ";
                        String marker = " /by ";

                        int markerIndex = msg.indexOf(marker);

                        String description = msg.substring(prefix.length(), markerIndex);
                        String by = msg.substring(markerIndex + marker.length());

                        addTask(new Deadline(description, by));
                    } else if (msg.startsWith("event ")) {
                        String prefix = "event ";
                        String fromMarker = " /from ";
                        String toMarker = " /to ";

                        int fromIndex = msg.indexOf(fromMarker);
                        int toIndex = msg.indexOf(toMarker);

                        String description = msg.substring(prefix.length(), fromIndex);
                        String start = msg.substring(fromIndex + fromMarker.length(), toIndex);
                        String end = msg.substring(toIndex + toMarker.length());

                        addTask(new Event(description, start, end));
                    } else {
                        // Do nothing for now
                    }
                    break;
                }
        }
    }
}
