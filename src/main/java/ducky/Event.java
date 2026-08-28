package ducky;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task with a start and end time/date.
 */
public class Event extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy h:mm a", Locale.ENGLISH);

    private final LocalDateTime start;
    private final LocalDateTime end;

    /**
     * Creates an event task with the given description and time range.
     *
     * @param description the description of the task.
     * @param start the event start time.
     * @param end the event end time.
     */
    public Event(String description, LocalDateTime start, LocalDateTime end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the event start time.
     *
     * @return the event start time.
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Returns the event end time.
     *
     * @return the event end time.
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Returns this event in Ducky's display format.
     *
     * @return the formatted event.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + String.format(" (from: %s to: %s)",
                start.format(DISPLAY_FORMAT), end.format(DISPLAY_FORMAT));
    }
}
