package ducky;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving tasks to and loading tasks from the hard disk.
 */
public class Storage {
    private static final String TODO_TYPE = "T";
    private static final String DEADLINE_TYPE = "D";
    private static final String EVENT_TYPE = "E";
    private static final String FIELD_SEPARATOR = " \\| ";

    private final Path filePath;

    /**
     * Creates storage that uses {@code data/duke.txt} as its data file.
     */
    public Storage() {
        filePath = Paths.get("data", "duke.txt");
    }

    /**
     * Loads all saved tasks from the data file.
     *
     * @return the saved tasks, or an empty list when the file does not exist
     * @throws DuckyException if the file cannot be read or contains invalid data
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
     * @param tasks the tasks to save
     * @throws DuckyException if the directory or file cannot be written
     */
    public void save(List<Task> tasks) throws DuckyException {
        try {
            Path parentDirectory = filePath.getParent();
            if (parentDirectory != null) {
                Files.createDirectories(parentDirectory);
            }

            List<String> lines = new ArrayList<>();
            for (Task task : tasks) {
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
     * @param line the saved task line
     * @return the task represented by the line
     * @throws DuckyException if the line is invalid
     */
    private Task parseTask(String line) throws DuckyException {
        String[] fields = line.split(FIELD_SEPARATOR, -1);
        if (fields.length < 3) {
            throw new DuckyException("Sorry, your save file contains invalid task data 🐥");
        }

        Task task;
        switch (fields[0]) {
        case TODO_TYPE:
            if (fields.length != 3) {
                throw new DuckyException("Sorry, your save file contains invalid todo data 🐥");
            }
            task = new ToDo(fields[2]);
            break;
        case DEADLINE_TYPE:
            if (fields.length != 4) {
                throw new DuckyException("Sorry, your save file contains invalid deadline data 🐥");
            }
            task = new Deadline(fields[2], fields[3]);
            break;
        case EVENT_TYPE:
            if (fields.length != 5) {
                throw new DuckyException("Sorry, your save file contains invalid event data 🐥");
            }
            task = new Event(fields[2], fields[3], fields[4]);
            break;
        default:
            throw new DuckyException("Sorry, your save file contains an unknown task type 🐥");
        }

        restoreCompletionStatus(task, fields[1]);
        return task;
    }

    /**
     * Restores the completion status stored in a task line.
     *
     * @param task the task to update
     * @param status the saved completion status
     * @throws DuckyException if the status is invalid
     */
    private void restoreCompletionStatus(Task task, String status) throws DuckyException {
        if ("1".equals(status)) {
            task.markAsDone();
        } else if (!"0".equals(status)) {
            throw new DuckyException("Sorry, your save file contains an invalid task status.");
        }
    }

    /**
     * Converts a task object into one line for the data file.
     *
     * @param task the task to format
     * @return the serialized task
     * @throws DuckyException if the task type is unsupported
     */
    private String formatTask(Task task) throws DuckyException {
        String status = task.isDone() ? "1" : "0";
        if (task instanceof ToDo) {
            return String.join(" | ", TODO_TYPE, status, task.getDescription());
        } else if (task instanceof Deadline deadline) {
            return String.join(" | ", DEADLINE_TYPE, status,
                    task.getDescription(), deadline.getBy());
        } else if (task instanceof Event event) {
            return String.join(" | ", EVENT_TYPE, status,
                    task.getDescription(), event.getStart(), event.getEnd());
        }
        throw new DuckyException("Sorry, I could not save an unsupported task type 🐥");
    }
}
