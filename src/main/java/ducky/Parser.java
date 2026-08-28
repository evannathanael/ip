package ducky;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parses user commands into structured commands for Ducky.
 */
public class Parser {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /**
     * Parses a complete user command.
     *
     * @param command the user's command.
     * @return the parsed command.
     * @throws DuckyException if the command is invalid.
     */
    public ParsedCommand parse(String command) throws DuckyException {
        if ("bye".equals(command)) {
            return new ParsedCommand(CommandType.BYE);
        }
        if ("list".equals(command)) {
            return new ParsedCommand(CommandType.LIST);
        }
        if ("find".equals(command) || command.startsWith("find ")) {
            return new ParsedCommand(CommandType.FIND, parseFindKeyword(command));
        }
        if (command.startsWith("mark ")) {
            return new ParsedCommand(CommandType.MARK, parseTaskNumber(command, "mark "));
        }
        if (command.startsWith("unmark ")) {
            return new ParsedCommand(CommandType.UNMARK, parseTaskNumber(command, "unmark "));
        }
        if (command.startsWith("delete ")) {
            return new ParsedCommand(CommandType.DELETE, parseTaskNumber(command, "delete "));
        }
        if ("todo".equals(command) || command.startsWith("todo ")) {
            return new ParsedCommand(CommandType.ADD, parseTodo(command));
        }
        if ("deadline".equals(command) || command.startsWith("deadline ")) {
            return new ParsedCommand(CommandType.ADD, parseDeadline(command));
        }
        if ("event".equals(command) || command.startsWith("event ")) {
            return new ParsedCommand(CommandType.ADD, parseEvent(command));
        }
        throw new DuckyException("I didn't get what you said 🐥");
    }

    /**
     * Parses a todo command.
     *
     * @param command the complete todo command.
     * @return the parsed todo task.
     * @throws DuckyException if the description is empty.
     */
    private Task parseTodo(String command) throws DuckyException {
        String description = command.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new DuckyException("To do task is empty! 🐥");
        }
        return new ToDo(description);
    }

    /**
     * Parses the keyword from a find command.
     *
     * @param command the complete find command
     * @return the search keyword
     * @throws DuckyException if the keyword is empty
     */
    private String parseFindKeyword(String command) throws DuckyException {
        String keyword = command.substring("find".length()).trim();
        if (keyword.isEmpty()) {
            throw new DuckyException("Please provide a keyword to find 🐥");
        }
        return keyword;
    }

    /**
     * Parses a deadline command and converts its date to a LocalDate.
     *
     * @param command the complete deadline command.
     * @return the parsed deadline task.
     * @throws DuckyException if the command or date is invalid.
     */
    private Task parseDeadline(String command) throws DuckyException {
        String commandWithoutPrefix = command.substring("deadline".length()).trim();
        int markerIndex = commandWithoutPrefix.indexOf(" /by ");
        if (markerIndex == -1) {
            throw new DuckyException("A deadline must include '/by' followed by a date 🐥");
        }

        String description = commandWithoutPrefix.substring(0, markerIndex).trim();
        String dateText = commandWithoutPrefix.substring(markerIndex + " /by ".length()).trim();
        if (description.isEmpty()) {
            throw new DuckyException("A deadline description cannot be empty 🐥");
        }
        if (dateText.isEmpty()) {
            throw new DuckyException("A deadline must include a date after '/by' 🐥");
        }

        try {
            return new Deadline(description, LocalDate.parse(dateText, DATE_FORMAT));
        } catch (DateTimeParseException e) {
            throw new DuckyException("Please enter the deadline in yyyy-MM-dd format 🐥");
        }
    }

    /**
     * Parses an event command and converts its times to LocalDateTime values.
     *
     * @param command the complete event command.
     * @return the parsed event task.
     * @throws DuckyException if the command or times are invalid.
     */
    private Task parseEvent(String command) throws DuckyException {
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
        String startText = commandWithoutPrefix.substring(
                fromIndex + " /from ".length(), toIndex).trim();
        String endText = commandWithoutPrefix.substring(toIndex + " /to ".length()).trim();
        if (description.isEmpty()) {
            throw new DuckyException("An event description cannot be empty 🐥");
        }
        if (startText.isEmpty()) {
            throw new DuckyException("An event must include a start time after '/from' 🐥");
        }
        if (endText.isEmpty()) {
            throw new DuckyException("An event must include an end time after '/to' 🐥");
        }

        try {
            LocalDateTime start = LocalDateTime.parse(startText, DATE_TIME_FORMAT);
            LocalDateTime end = LocalDateTime.parse(endText, DATE_TIME_FORMAT);
            return new Event(description, start, end);
        } catch (DateTimeParseException e) {
            throw new DuckyException("Please enter event times in yyyy-MM-dd HHmm format 🐥");
        }
    }

    /**
     * Parses a one-based task number and converts it to a zero-based index.
     *
     * @param command the complete task command.
     * @param commandPrefix the command prefix to remove.
     * @return the zero-based task index.
     * @throws DuckyException if the task number is invalid.
     */
    private int parseTaskNumber(String command, String commandPrefix) throws DuckyException {
        String numberText = command.substring(commandPrefix.length()).trim();
        try {
            int taskNumber = Integer.parseInt(numberText);
            if (taskNumber < 1) {
                throw new DuckyException("That task number does not exist 🐥");
            }
            return taskNumber - 1;
        } catch (NumberFormatException e) {
            throw new DuckyException("Please enter a valid task number 🐥");
        }
    }

    /**
     * Represents the type of a parsed command.
     */
    public enum CommandType {
        ADD, MARK, UNMARK, DELETE, LIST, FIND, BYE
    }

    /**
     * Represents a parsed command and its optional argument.
     */
    public static class ParsedCommand {
        private final CommandType type;
        private final Task task;
        private final String keyword;
        private final int taskIndex;

        /**
         * Creates a command without a task or task index.
         *
         * @param type the command type.
         */
        public ParsedCommand(CommandType type) {
            this(type, null, null, -1);
        }

        /**
         * Creates an add command.
         *
         * @param type the command type.
         * @param task the task argument.
         */
        public ParsedCommand(CommandType type, Task task) {
            this(type, task, null, -1);
        }

        /**
         * Creates a find command.
         *
         * @param type the command type.
         * @param keyword the search keyword.
         */
        public ParsedCommand(CommandType type, String keyword) {
            this(type, null, keyword, -1);
        }

        /**
         * Creates a mark, unmark, or delete command.
         *
         * @param type the command type.
         * @param taskIndex the zero-based task index.
         */
        public ParsedCommand(CommandType type, int taskIndex) {
            this(type, null, null, taskIndex);
        }

        private ParsedCommand(CommandType type, Task task, String keyword, int taskIndex) {
            this.type = type;
            this.task = task;
            this.keyword = keyword;
            this.taskIndex = taskIndex;
        }

        /**
         * Returns the command type.
         *
         * @return the command type.
         */
        public CommandType getType() {
            return type;
        }

        /**
         * Returns the task argument.
         *
         * @return the task argument.
         */
        public Task getTask() {
            return task;
        }

        /**
         * Returns the search keyword.
         *
         * @return the search keyword.
         */
        public String getKeyword() {
            return keyword;
        }

        /**
         * Returns the zero-based task index.
         *
         * @return the task index.
         */
        public int getTaskIndex() {
            return taskIndex;
        }
    }
}
