/**
 * Represents a task that must be completed by a specific time.
 */
public class Deadline extends Task {
    protected String by;

    /**
     * Creates an incomplete deadline.
     *
     * @param description description of the deadline
     * @param by deadline stored as text
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
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
