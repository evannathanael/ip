package ducky;

/**
 * Represents a task with a start and end time/date.
 */
public class Event extends Task {
    protected final String start;
    protected final String end;

    /**
     * Creates an event task with the given description and time range.
     *
     * @param description the description of the task
     * @param start the event start time
     * @param end the event end time
     */
    public Event(String description, String start, String end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the event start time.
     *
     * @return the event start time
     */
    public String getStart() {
        return start;
    }

    /**
     * Returns the event end time.
     *
     * @return the event end time
     */
    public String getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + String.format(" (from: %s to: %s)", start, end);
    }
}
