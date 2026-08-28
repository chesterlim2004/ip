import java.nio.file.Path;

/**
 * Coordinates Crystal's command loop, task operations, storage, and console UI.
 */
public class Crystal {
    /** Console interface used to interact with the user. */
    private final Ui ui;

    /** Persistence service for the task list. */
    private final Storage storage;

    /** Tasks available during the current chatbot session. */
    private TaskList tasks;

    /**
     * Creates a Crystal chatbot backed by the supplied data file.
     *
     * @param dataFilePath relative path to the task data file
     */
    public Crystal(Path dataFilePath) {
        ui = new Ui();
        storage = new Storage(dataFilePath);
    }

    /**
     * Loads saved tasks and runs the chatbot's command loop.
     */
    public void run() {
        ui.showWelcome();

        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (CrystalException exception) {
            ui.showError(exception);
            tasks = new TaskList();
        }
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showDivider();
                Command command = Parser.parse(fullCommand);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (CrystalException exception) {
                ui.showError(exception);
            } finally {
                ui.showDivider();
            }
        }
    }

    /**
     * Starts Crystal using its OS-independent relative data path.
     *
     * @param args command-line arguments; not used
     */
    public static void main(String[] args) {
        new Crystal(Path.of("data", "crystal.txt")).run();
    }
}
