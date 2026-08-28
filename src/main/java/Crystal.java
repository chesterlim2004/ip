import java.time.LocalDate;
import java.util.List;

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
            CommandType commandType = CommandType.fromCommand(command);
            if (commandType == CommandType.BYE) {
                break;
            }

            ui.showDivider();
            try {
                switch (commandType) {
                case LIST -> {
                    if (command.equals(commandType.getKeyword())) {
                        ui.showTaskList(tasks);
                    } else if (command.equals("list /on") || command.startsWith("list /on ")) {
                        listTasksOnDate(command, tasks, ui);
                    } else {
                        throw new CrystalException("To view your task list, simply enter 'list'!");
                    }
                }
                case MARK -> {
                    int taskIndex = getTaskIndex(
                            command, commandType.getKeyword() + " ", tasks.getTaskCount());
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
                    int taskIndex = getTaskIndex(
                            command, commandType.getKeyword() + " ", tasks.getTaskCount());
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
                    int taskIndex = getTaskIndex(
                            command, commandType.getKeyword() + " ", tasks.getTaskCount());
                    Task removedTask = tasks.deleteTask(taskIndex);
                    Storage.saveTasks(tasks.getTasks());
                    ui.showTaskDeleted(removedTask, tasks.getTaskCount());
                }
                case TODO, DEADLINE, EVENT -> {
                    Task newTask = createTask(command, commandType);
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
     * Parses a list-on-date request and asks the UI to show the matching tasks.
     *
     * @param command full {@code list /on} command
     * @param tasks complete task list
     * @param ui console UI used to display the temporary list
     * @throws CrystalException if the command is missing a valid calendar date
     */
    private static void listTasksOnDate(String command, TaskList tasks, Ui ui)
            throws CrystalException {
        String prefix = "list /on ";
        if (!command.startsWith(prefix) || command.substring(prefix.length()).isBlank()) {
            throw new CrystalException(
                    "To list tasks on a date, enter 'list /on [date]'!");
        }

        String dateInput = command.substring(prefix.length());
        LocalDate date = TaskDateTime.parseDate(dateInput);
        if (date == null) {
            throw new CrystalException("I couldn't understand that date!");
        }

        List<Task> matchingTasks = tasks.getTasksOnDate(date);
        String formattedDate = TaskDateTime.formatDate(date);
        ui.showTasksOnDate(formattedDate, matchingTasks);
    }

    /**
     * Converts the task number in a command into a valid list index.
     *
     * @param command full mark, unmark, or delete command
     * @param prefix command prefix before the task number
     * @param taskCount number of tasks currently stored
     * @return zero-based index of the selected task
     * @throws CrystalException if the command is malformed or the task does not exist
     */
    private static int getTaskIndex(String command, String prefix, int taskCount)
            throws CrystalException {
        String action = prefix.trim();
        if (!command.startsWith(prefix)) {
            throw new CrystalException("You have to " + action + " a task number!");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(command.substring(prefix.length()));
        } catch (NumberFormatException exception) {
            throw new CrystalException("You have to " + action + " a task number!");
        }

        int taskIndex = taskNumber - 1;
        if (taskIndex < 0 || taskIndex >= taskCount) {
            throw new CrystalException("That task number does not exist!");
        }
        return taskIndex;
    }

    /**
     * Creates the task described by a todo, deadline, or event command.
     *
     * @param command task creation command
     * @param commandType type of task to create
     * @return task represented by the command
     * @throws CrystalException if required task details are missing
     */
    private static Task createTask(String command, CommandType commandType)
            throws CrystalException {
        String prefix = commandType.getKeyword() + " ";
        switch (commandType) {
        case TODO -> {
            if (!command.startsWith(prefix)) {
                throw new CrystalException("A todo must have a description!");
            }
            String description = command.substring(prefix.length());
            if (description.isBlank()) {
                throw new CrystalException("A todo must have a description!");
            }
            return new Todo(description);
        }
        case DEADLINE -> {
            if (!command.startsWith(prefix)) {
                throw new CrystalException("A deadline must have a description and a /by time!");
            }
            String details = command.substring(prefix.length());
            int byIndex = details.indexOf(" /by ");
            if (byIndex <= 0 || byIndex + " /by ".length() >= details.length()) {
                throw new CrystalException("A deadline must have a description and a /by time!");
            }
            String description = details.substring(0, byIndex);
            String by = details.substring(byIndex + " /by ".length());
            return new Deadline(description, by);
        }
        case EVENT -> {
            if (!command.startsWith(prefix)) {
                throw new CrystalException(
                        "An event must have a description, a /from time and a /to time!");
            }
            String details = command.substring(prefix.length());
            int fromIndex = details.indexOf(" /from ");
            int toIndex = details.indexOf(" /to ", fromIndex + " /from ".length());
            if (fromIndex <= 0 || toIndex <= fromIndex + " /from ".length()
                    || toIndex + " /to ".length() >= details.length()) {
                throw new CrystalException(
                        "An event must have a description, a /from time and a /to time!");
            }
            String description = details.substring(0, fromIndex);
            String from = details.substring(fromIndex + " /from ".length(), toIndex);
            String to = details.substring(toIndex + " /to ".length());
            return new Event(description, from, to);
        }
        default -> throw new CrystalException("I don't know what that means :-(");
        }
    }
}
