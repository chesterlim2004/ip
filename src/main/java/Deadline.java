/**
 * Represents a task that must be completed by a specific time.
 */
public class Deadline extends Task {
    /** Deadline date, time, or unrestricted scheduling text. */
    protected TaskDateTime by;

    /**
     * Creates an incomplete deadline.
     *
     * @param description description of the deadline
     * @param by deadline date, time, or unrestricted text
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = TaskDateTime.parse(by);
    }

    /**
     * Returns this deadline in the format stored on disk.
     *
     * @return pipe-separated deadline data
     */
    @Override
    public String toDataString() {
        return "D | " + super.toDataString() + " | " + by;
    }

    /**
     * Returns this task with its deadline type icon and deadline.
     *
     * @return formatted deadline details
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
