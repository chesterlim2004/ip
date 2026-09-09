package crystal.task;

import java.time.LocalDate;

/**
 * Represents a task that can be completed within an inclusive date range.
 */
public class WithinPeriod extends Task {
    /** First date on which the task can be completed. */
    private final LocalDate fromDate;

    /** Last date on which the task can be completed. */
    private final LocalDate toDate;

    /**
     * Creates an incomplete task with an inclusive completion period.
     *
     * @param description description of the task.
     * @param fromDate first date on which the task can be completed.
     * @param toDate last date on which the task can be completed.
     */
    public WithinPeriod(String description, LocalDate fromDate, LocalDate toDate) {
        super(description);
        assert fromDate != null : "A within-period task requires a start date";
        assert toDate != null : "A within-period task requires an end date";
        assert !toDate.isBefore(fromDate) : "A within-period task requires an ordered date range";
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    /**
     * Returns this within-period task in the format stored on disk.
     *
     * @return pipe-separated within-period task data.
     */
    @Override
    public String toDataString() {
        return "W | " + super.toDataString()
                + " | " + TaskDateTime.formatDate(fromDate)
                + " | " + TaskDateTime.formatDate(toDate);
    }

    /**
     * Returns whether the date falls within this task's inclusive completion period.
     *
     * @param date date to check.
     * @return {@code true} if the task can be completed on the date.
     */
    @Override
    public boolean occursOn(LocalDate date) {
        return !date.isBefore(fromDate) && !date.isAfter(toDate);
    }

    /**
     * Returns this task with its type icon and completion period.
     *
     * @return formatted within-period task details.
     */
    @Override
    public String toString() {
        return "[W]" + super.toString()
                + " (from: " + TaskDateTime.formatDate(fromDate)
                + " to: " + TaskDateTime.formatDate(toDate) + ")";
    }
}
