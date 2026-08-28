import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Coordinates Crystal's command loop, task operations, storage, and console UI.
 */
public class Crystal {
    /**
     * Runs the chatbot's command loop.
     *
     * @param args command-line arguments; not used
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        TaskList tasks;
        try {
            tasks = new TaskList(Storage.loadTasks());
        } catch (CrystalException exception) {
            ui.showError(exception);
            tasks = new TaskList();
        }
        while (true) {
            String command = ui.readCommand();
            CommandType commandType = Parser.parseCommandType(command);
            if (commandType == CommandType.BYE) {
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
                        Storage.saveTasks(tasks.getTasks());
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
                        Storage.saveTasks(tasks.getTasks());
                        ui.showTaskMarkedNotDone(task);
                    }
                }
                case DELETE -> {
                    int taskIndex = Parser.parseTaskIndex(
                            command, commandType, tasks.getTaskCount());
                    Task removedTask = tasks.deleteTask(taskIndex);
                    Storage.saveTasks(tasks.getTasks());
                    ui.showTaskDeleted(removedTask, tasks.getTaskCount());
                }
                case TODO, DEADLINE, EVENT -> {
                    Task newTask = Parser.parseTask(command, commandType);
                    tasks.addTask(newTask);
                    Storage.saveTasks(tasks.getTasks());
                    ui.showTaskAdded(newTask, tasks.getTaskCount());
                }
                case UNKNOWN, BYE -> throw new CrystalException("I don't know what that means :-(");
                }
            } catch (CrystalException exception) {
                ui.showError(exception);
            }
            ui.showDivider();
        }

        ui.showGoodbye();
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
