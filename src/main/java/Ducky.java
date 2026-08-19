import java.util.Scanner;

/**
 * Represents the main chatbot application.
 */
public class Ducky {
    public static String LINE = "____________________________________________________________";
    public static String BANNER = " ____  _   _  ____ _  ____   __\n"
            + "|  _ \\| | | |/ ___| |/ /\\ \\ / /\n"
            + "| | | | | | | |   | ' /  \\ V / \n"
            + "| |_| | |_| | |___| . \\   | |  \n"
            + "|____/ \\___/ \\____|_|\\_\\  |_|  \n";

    /**
     * Prints the chatbot's response surrounded by separator lines.
     */
    public static void printEchoMessage(String msg) {
        System.out.println(LINE);
        System.out.println(msg);
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
            }
            printEchoMessage(msg);
        }
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }
}
