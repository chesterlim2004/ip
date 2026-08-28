/**
 * Represents an executable instruction understood by Crystal.
 */
public abstract class Command {
    /**
     * Performs this command using the application's shared dependencies.
     *
     * @param tasks task list to query or mutate
     * @param ui console interface used to present the result
     * @param storage persistence service for task mutations
     * @throws CrystalException if the command cannot be completed
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage)
            throws CrystalException;

    /**
     * Returns whether this command should end the chatbot session.
     *
     * @return {@code false} for ordinary commands
     */
    public boolean isExit() {
        return false;
    }
}
