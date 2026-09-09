package ducky;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parses user commands into structured commands for Ducky.
 */
public class Parser {
    private static final String BYE_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String FIND_COMMAND = "find";
    private static final String FIND_COMMAND_PREFIX = FIND_COMMAND + " ";
    private static final String MARK_COMMAND_PREFIX = "mark ";
    private static final String UNMARK_COMMAND_PREFIX = "unmark ";
    private static final String DELETE_COMMAND_PREFIX = "delete ";
    private static final String TODO_COMMAND = "todo";
    private static final String TODO_COMMAND_PREFIX = TODO_COMMAND + " ";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String DEADLINE_COMMAND_PREFIX = DEADLINE_COMMAND + " ";
    private static final String EVENT_COMMAND = "event";
    private static final String EVENT_COMMAND_PREFIX = EVENT_COMMAND + " ";
    private static final String DEADLINE_MARKER = " /by ";
    private static final String EVENT_START_MARKER = " /from ";
    private static final String EVENT_END_MARKER = " /to ";

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
        if (BYE_COMMAND.equals(command)) {
            return new ParsedCommand(CommandType.BYE);
        }
        if (LIST_COMMAND.equals(command)) {
            return new ParsedCommand(CommandType.LIST);
        }
        if (FIND_COMMAND.equals(command) || command.startsWith(FIND_COMMAND_PREFIX)) {
            return new ParsedCommand(CommandType.FIND, parseFindKeyword(command));
        }
        if (command.startsWith(MARK_COMMAND_PREFIX)) {
            return new ParsedCommand(CommandType.MARK, parseTaskNumber(command, MARK_COMMAND_PREFIX));
        }
        if (command.startsWith(UNMARK_COMMAND_PREFIX)) {
            return new ParsedCommand(CommandType.UNMARK, parseTaskNumber(command, UNMARK_COMMAND_PREFIX));
        }
        if (command.startsWith(DELETE_COMMAND_PREFIX)) {
            return new ParsedCommand(CommandType.DELETE, parseTaskNumber(command, DELETE_COMMAND_PREFIX));
        }
        if (TODO_COMMAND.equals(command) || command.startsWith(TODO_COMMAND_PREFIX)) {
            return new ParsedCommand(CommandType.ADD, parseTodo(command));
        }
        if (DEADLINE_COMMAND.equals(command) || command.startsWith(DEADLINE_COMMAND_PREFIX)) {
            return new ParsedCommand(CommandType.ADD, parseDeadline(command));
        }
        if (EVENT_COMMAND.equals(command) || command.startsWith(EVENT_COMMAND_PREFIX)) {
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
        String description = command.substring(TODO_COMMAND.length()).trim();
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
        String keyword = command.substring(FIND_COMMAND.length()).trim();
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
        String commandWithoutPrefix = command.substring(DEADLINE_COMMAND.length()).trim();
        int markerIndex = commandWithoutPrefix.indexOf(DEADLINE_MARKER);
        if (markerIndex == -1) {
            throw new DuckyException("A deadline must include '/by' followed by a date 🐥");
        }

        String description = commandWithoutPrefix.substring(0, markerIndex).trim();
        String dateText = commandWithoutPrefix.substring(markerIndex + DEADLINE_MARKER.length()).trim();
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
        String commandWithoutPrefix = command.substring(EVENT_COMMAND.length()).trim();
        int fromIndex = commandWithoutPrefix.indexOf(EVENT_START_MARKER);
        int toIndex = commandWithoutPrefix.indexOf(EVENT_END_MARKER);
        if (fromIndex == -1 || toIndex == -1) {
            throw new DuckyException("An event must include both '/from' and '/to' 🐥");
        }
        if (fromIndex > toIndex) {
            throw new DuckyException("'/from' must appear before '/to' 🐥");
        }

        String description = commandWithoutPrefix.substring(0, fromIndex).trim();
        String startText = commandWithoutPrefix.substring(
                fromIndex + EVENT_START_MARKER.length(), toIndex).trim();
        String endText = commandWithoutPrefix.substring(toIndex + EVENT_END_MARKER.length()).trim();
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
