package ducky;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving tasks to and loading tasks from the hard disk.
 */
public class Storage {
    private static final String TODO_TYPE = "T";
    private static final String DEADLINE_TYPE = "D";
    private static final String EVENT_TYPE = "E";
    private static final String COMPLETE_STATUS = "1";
    private static final String INCOMPLETE_STATUS = "0";
    private static final String FIELD_SEPARATOR = " | ";
    private static final String FIELD_SEPARATOR_REGEX = " \\| ";

    private static final int TYPE_FIELD_INDEX = 0;
    private static final int STATUS_FIELD_INDEX = 1;
    private static final int DESCRIPTION_FIELD_INDEX = 2;
    private static final int DEADLINE_DATE_FIELD_INDEX = 3;
    private static final int EVENT_START_FIELD_INDEX = 3;
    private static final int EVENT_END_FIELD_INDEX = 4;

    private static final int MINIMUM_FIELD_COUNT = 3;
    private static final int TODO_FIELD_COUNT = 3;
    private static final int DEADLINE_FIELD_COUNT = 4;
    private static final int EVENT_FIELD_COUNT = 5;

    private final Path filePath;

    /**
     * Creates storage that uses the given data file.
     *
     * @param filePath the path of the data file.
     */
    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     * Loads all saved tasks from the data file.
     *
     * @return the saved tasks, or an empty list when the file does not exist.
     * @throws DuckyException if the file cannot be read or contains invalid data.
     */
    public List<Task> load() throws DuckyException {
        if (Files.notExists(filePath)) {
            return new ArrayList<>();
        }

        try {
            List<Task> tasks = new ArrayList<>();
            for (String line : Files.readAllLines(filePath)) {
                if (!line.isBlank()) {
                    tasks.add(parseTask(line));
                }
            }
            return tasks;
        } catch (IOException e) {
            throw new DuckyException("Sorry, I could not load your saved tasks 🐥");
        }
    }

    /**
     * Saves all current tasks to the data file.
     *
     * @param taskList the tasks to save.
     * @throws DuckyException if the directory or file cannot be written.
     */
    public void save(TaskList taskList) throws DuckyException {
        try {
            Path parentDirectory = filePath.getParent();
            if (parentDirectory != null) {
                Files.createDirectories(parentDirectory);
            }

            List<String> lines = new ArrayList<>();
            for (Task task : taskList.getTasks()) {
                lines.add(formatTask(task));
            }
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new DuckyException("Sorry, I could not save your tasks 🐥");
        }
    }

    /**
     * Converts one saved line into a task object.
     *
     * @param line the saved task line.
     * @return the task represented by the line.
     * @throws DuckyException if the line is invalid.
     */
    private Task parseTask(String line) throws DuckyException {
        String[] fields = line.split(FIELD_SEPARATOR_REGEX, -1);
        if (fields.length < MINIMUM_FIELD_COUNT) {
            throw new DuckyException("Sorry, your save file contains invalid task data 🐥");
        }

        Task task;
        switch (fields[TYPE_FIELD_INDEX]) {
            case TODO_TYPE:
                task = parseTodoTask(fields);
                break;
            case DEADLINE_TYPE:
                task = parseDeadlineTask(fields);
                break;
            case EVENT_TYPE:
                task = parseEventTask(fields);
                break;
            default:
                throw new DuckyException("Sorry, your save file contains an unknown task type 🐥");
        }

        restoreCompletionStatus(task, fields[STATUS_FIELD_INDEX]);
        return task;
    }

    /**
     * Creates a to-do task from its saved fields.
     *
     * @param fields the fields in a saved to-do record.
     * @return the task represented by the fields.
     * @throws DuckyException if the record has an invalid number of fields.
     */
    private Task parseTodoTask(String[] fields) throws DuckyException {
        if (fields.length != TODO_FIELD_COUNT) {
            throw new DuckyException("Sorry, your save file contains invalid todo data 🐥");
        }
        return new ToDo(fields[DESCRIPTION_FIELD_INDEX]);
    }

    /**
     * Creates a deadline task from its saved fields.
     *
     * @param fields the fields in a saved deadline record.
     * @return the task represented by the fields.
     * @throws DuckyException if the record structure or deadline date is invalid.
     */
    private Task parseDeadlineTask(String[] fields) throws DuckyException {
        if (fields.length != DEADLINE_FIELD_COUNT) {
            throw new DuckyException("Sorry, your save file contains invalid deadline data 🐥");
        }
        try {
            String description = fields[DESCRIPTION_FIELD_INDEX];
            LocalDate deadline = LocalDate.parse(fields[DEADLINE_DATE_FIELD_INDEX]);
            return new Deadline(description, deadline);
        } catch (DateTimeParseException e) {
            throw new DuckyException("Sorry, your save file contains an invalid deadline date 🐥");
        }
    }

    /**
     * Creates an event task from its saved fields.
     *
     * @param fields the fields in a saved event record.
     * @return the task represented by the fields.
     * @throws DuckyException if the record structure or event times are invalid.
     */
    private Task parseEventTask(String[] fields) throws DuckyException {
        if (fields.length != EVENT_FIELD_COUNT) {
            throw new DuckyException("Sorry, your save file contains invalid event data 🐥");
        }
        try {
            String description = fields[DESCRIPTION_FIELD_INDEX];
            LocalDateTime start = LocalDateTime.parse(fields[EVENT_START_FIELD_INDEX]);
            LocalDateTime end = LocalDateTime.parse(fields[EVENT_END_FIELD_INDEX]);
            return new Event(description, start, end);
        } catch (DateTimeParseException e) {
            throw new DuckyException("Sorry, your save file contains invalid event times 🐥");
        }
    }

    /**
     * Restores the completion status stored in a task line.
     *
     * @param task the task to update.
     * @param status the saved completion status.
     * @throws DuckyException if the status is invalid.
     */
    private void restoreCompletionStatus(Task task, String status) throws DuckyException {
        if (COMPLETE_STATUS.equals(status)) {
            task.markAsDone();
        } else if (!INCOMPLETE_STATUS.equals(status)) {
            throw new DuckyException("Sorry, your save file contains an invalid task status.");
        }
    }

    /**
     * Converts a task object into one line for the data file.
     *
     * @param task the task to format.
     * @return the serialized task.
     * @throws DuckyException if the task type is unsupported.
     */
    private String formatTask(Task task) throws DuckyException {
        String status = task.isDone() ? COMPLETE_STATUS : INCOMPLETE_STATUS;
        if (task instanceof ToDo) {
            return String.join(FIELD_SEPARATOR, TODO_TYPE, status, task.getDescription());
        } else if (task instanceof Deadline deadline) {
            return String.join(FIELD_SEPARATOR, DEADLINE_TYPE, status,
                    task.getDescription(), deadline.getBy().toString());
        } else if (task instanceof Event event) {
            return String.join(FIELD_SEPARATOR, EVENT_TYPE, status,
                    task.getDescription(), event.getStart().toString(), event.getEnd().toString());
        }
        throw new DuckyException("Sorry, I could not save an unsupported task type 🐥");
    }
}
