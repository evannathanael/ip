package ducky;

/**
 * Represents a task with a due date.
 */
public class Deadline extends Task {
    protected final String by;

    /**
     * Creates a deadline task with the given description and due date.
     *
     * @param description the description of the task
     * @param by the due date or time
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the due date or time for this task.
     *
     * @return the deadline
     */
    public String getBy() {
        return by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
