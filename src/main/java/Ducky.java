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

    /**
     * Delete a task from tasks
     */
    public static void delete(int taskNumber) {
        System.out.println(LINE);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + tasks.get(taskNumber - 1));
        tasks.remove(taskNumber - 1);
        System.out.println(String.format("Now you have %d tasks in the list.", tasks.size()));
        System.out.println(LINE);
    }

    /**
     * Unmarks a task and prints the state and description of the task
     */
    public static void printErrorMessage(String msg) {
        System.out.println(LINE);
        System.out.println("QUACK! " + msg);
        System.out.println(LINE);
    }

    /**
     * Parses and validates a task number from a mark or unmark command.
     */
    public static int getTaskNumber(String msg, String command) throws DuckyException {
        String numberText = msg.substring(command.length()).trim();
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
     * Processes one user command and throws a DuckyException for invalid input.
     */
    public static boolean processCommand(String msg) throws DuckyException {
        switch (msg) {
            case "bye":
                printExitMessage();
                return false;

            case "list":
                printTasks();
                return true;

            default:
                if (msg.startsWith("mark ")) {
                    int taskNumber = getTaskNumber(msg, "mark ");
                    markAsDone(taskNumber);
                } else if (msg.startsWith("unmark ")) {
                    int taskNumber = getTaskNumber(msg, "unmark ");
                    unmark(taskNumber);
                } else if (msg.equals("todo") || msg.startsWith("todo ")) {
                    if (msg.equals("todo")) {
                        throw new DuckyException("A todo description cannot be empty 🐥");
                    }

                    String description = msg.substring(4).trim();
                    if (description.isEmpty()) {
                        throw new DuckyException("To do task is empty! 🐥");
                    }
                    addTask(new ToDo(description));
                } else if (msg.equals("deadline") || msg.startsWith("deadline ")) {
                    if (msg.equals("deadline")) {
                        throw new DuckyException("A deadline description cannot be empty 🐥");
                    }

                    int markerIndex = msg.indexOf(" /by ");

                    if (markerIndex == -1) {
                        throw new DuckyException("A deadline must include '/by' followed by a date or time 🐥");
                    }

                    String description = msg.substring("deadline ".length(), markerIndex).trim();
                    String by = msg.substring(markerIndex + " /by ".length()).trim();

                    if (description.isEmpty()) {
                        throw new DuckyException("A deadline description cannot be empty 🐥");
                    }

                    if (by.isEmpty()) {
                        throw new DuckyException("A deadline must include a date or time after '/by' 🐥");
                    }

                    addTask(new Deadline(description, by));
                } else if (msg.equals("event") || msg.startsWith("event ")) {
                    if (msg.equals("event")) {
                        throw new DuckyException("An event description cannot be empty 🐥");
                    }

                    int fromIndex = msg.indexOf(" /from ");
                    int toIndex = msg.indexOf(" /to ");

                    if (fromIndex == -1 || toIndex == -1) {
                        throw new DuckyException(
                                "An event must include both '/from' and '/to' 🐥");
                    }

                    if (fromIndex > toIndex) {
                        throw new DuckyException(
                                "'/from' must appear before '/to' 🐥");
                    }

                    String description = msg.substring("event ".length(), fromIndex).trim();
                    String start = msg.substring(fromIndex + " /from ".length(), toIndex).trim();
                    String end = msg.substring(toIndex + " /to ".length()).trim();

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
                } else if (msg.startsWith("delete ")) {
                    int taskNumber = getTaskNumber(msg, "delete ");
                    delete(taskNumber);
                } else {
                    throw new DuckyException("I didn't get what you said 🐥");
                }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(LINE);
        System.out.println(BANNER);
        System.out.println("Hello! I'm Ducky 🐥");
        System.out.println("What can I do for you?");
        System.out.println(LINE);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String msg = scanner.nextLine();
            try {
                if (!processCommand(msg)) {
                    return;
                }
            } catch (DuckyException e) {
                printErrorMessage(e.getMessage());
            }
        }
    }
}
