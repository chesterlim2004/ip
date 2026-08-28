/**
 * Represents a task with a description and completion status.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the symbol used to display the task's completion status.
     *
     * @return {@code X} if the task is done, or a space otherwise
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns whether this task has been completed.
     *
     * @return {@code true} if this task is done
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the common fields used to store this task on disk.
     *
     * @return pipe-separated completion status and description
     */
    public String toDataString() {
        return (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Returns the task's status icon followed by its description.
     *
     * @return formatted task details
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
