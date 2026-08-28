package ducky;

/**
 * Represents a task with a description and completion status.
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description the description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the symbol representing whether this task is complete.
     *
     * @return {@code X} for a completed task or a space otherwise.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the task description.
     *
     * @return the task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether this task is completed.
     *
     * @return {@code true} if the task is completed.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as incomplete.
     */
    public void unmark() {
        this.isDone = false;
    }

    /**
     * Returns the task description together with its completion status.
     *
     * @return the formatted task description.
     */
    @Override
    public String toString() {
        return String.format("[%s] %s", getStatusIcon(), description);
    }
}
