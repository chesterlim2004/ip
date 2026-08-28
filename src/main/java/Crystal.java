import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
        while (true) {
            String command = ui.readCommand();
            CommandType commandType = Parser.parseCommandType(command);
            if (commandType == CommandType.EXIT) {
                break;
            }

            ui.showDivider();
            try {
                switch (commandType) {
                case LIST -> {
                    Optional<LocalDate> date = Parser.parseListCommand(command);
                    if (date.isEmpty()) {
                        ui.showTaskList(tasks);
                    } else {
                        listTasksOnDate(date.get(), tasks, ui);
                    }
                }
                case MARK -> {
                    int taskIndex = Parser.parseTaskIndex(
                            command, commandType, tasks.getTaskCount());
                    Task task = tasks.getTask(taskIndex);
                    if (task.isDone()) {
                        ui.showTaskAlreadyDone(task);
                    } else {
                        task.markAsDone();
                        storage.saveTasks(tasks.getTasks());
                        ui.showTaskMarkedDone(task);
                    }
                }
                case UNMARK -> {
                    int taskIndex = Parser.parseTaskIndex(
                            command, commandType, tasks.getTaskCount());
                    Task task = tasks.getTask(taskIndex);
                    if (!task.isDone()) {
                        ui.showTaskAlreadyNotDone(task);
                    } else {
                        task.markAsNotDone();
                        storage.saveTasks(tasks.getTasks());
                        ui.showTaskMarkedNotDone(task);
                    }
                }
                case DELETE -> {
                    int taskIndex = Parser.parseTaskIndex(
                            command, commandType, tasks.getTaskCount());
                    Task removedTask = tasks.deleteTask(taskIndex);
                    storage.saveTasks(tasks.getTasks());
                    ui.showTaskDeleted(removedTask, tasks.getTaskCount());
                }
                case TODO, DEADLINE, EVENT -> {
                    Task newTask = Parser.parseTask(command, commandType);
                    tasks.addTask(newTask);
                    storage.saveTasks(tasks.getTasks());
                    ui.showTaskAdded(newTask, tasks.getTaskCount());
                }
                case UNKNOWN, EXIT -> throw new CrystalException("I don't know what that means :-(");
                }
            } catch (CrystalException exception) {
                ui.showError(exception);
            }
            ui.showDivider();
        }

        ui.showGoodbye();
    }

    /**
     * Starts Crystal using its OS-independent relative data path.
     *
     * @param args command-line arguments; not used
     */
    public static void main(String[] args) {
        new Crystal(Path.of("data", "crystal.txt")).run();
    }

    /**
     * Asks the UI to show tasks occurring on a parsed date.
     *
     * @param date date parsed from a {@code list /on} command
     * @param tasks complete task list
     * @param ui console UI used to display the temporary list
     */
    private static void listTasksOnDate(LocalDate date, TaskList tasks, Ui ui) {
        List<Task> matchingTasks = tasks.getTasksOnDate(date);
        String formattedDate = TaskDateTime.formatDate(date);
        ui.showTasksOnDate(formattedDate, matchingTasks);
    }
}
