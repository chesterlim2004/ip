/**
 * Represents a task that takes place over a period of time.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates an incomplete event.
     *
     * @param description description of the event
     * @param from event start stored as text
     * @param to event end stored as text
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns this task with its event type icon and time period.
     *
     * @return formatted event details
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
