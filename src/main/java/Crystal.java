import java.util.ArrayList;
import java.util.Scanner;

/**
 * Stores tasks, tracks whether they are done, lists them, and exits on {@code bye}.
 */
public class Crystal {
    /**
     * Runs the chatbot's command loop.
     *
     * @param args command-line arguments; not used
     */
    public static void main(String[] args) {
        String horizontalLine = "____________________________________________________________";
        String banner = "  ____ ______   ______ _____  _    _\n"
                + " / ___|  _ \\ \\ / / ___|_   _|/ \\  | |\n"
                + "| |   | |_) \\ V /\\___ \\ | | / _ \\ | |\n"
                + "| |___|  _ < | |  ___) || |/ ___ \\| |___\n"
                + " \\____|_| \\_\\|_| |____/ |_/_/   \\_\\_____|\n";
        String commands = "[Commands:\n"
                + "- To add a todo, enter 'todo [description]'\n"
                + "- To add a deadline, enter 'deadline [description] /by [deadline]'\n"
                + "- To add an event, enter 'event [description] /from [start] /to [end]'\n"
                + "- To view your list, enter 'list'\n"
                + "- To mark a task as done, enter 'mark [task number]'\n"
                + "- To mark a task as not done, enter 'unmark [task number]'\n"
                + "- To delete a task, enter 'delete [task number]'\n"
                + "- To exit, enter 'bye']";

        System.out.println(horizontalLine);
        System.out.print(banner);
        System.out.println("\nHello!!! I'm Crystal.");
        System.out.println(commands);
        System.out.println(horizontalLine);

        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks;
        try {
            tasks = Storage.loadTasks();
        } catch (CrystalException exception) {
            System.out.println(exception.getUserMessage());
            tasks = new ArrayList<>();
        }
        while (true) {
            System.out.print("You: ");
            String command = scanner.nextLine();
            CommandType commandType = CommandType.fromCommand(command);
            if (commandType == CommandType.BYE) {
                break;
            }

            System.out.println(horizontalLine);
            try {
                switch (commandType) {
                case LIST -> {
                    if (!command.equals(commandType.getKeyword())) {
                        throw new CrystalException("To view your task list, simply enter 'list'!");
                    }
                    if (tasks.isEmpty()) {
                        System.out.println("Crystal: Your task list is empty!");
                    } else {
                        System.out.println("Crystal: Here are the tasks in your list:");
                        for (int i = 0; i < tasks.size(); i++) {
                            System.out.println("         " + (i + 1) + "." + tasks.get(i));
                        }
                    }
                }
                case MARK -> {
                    int taskIndex = getTaskIndex(
                            command, commandType.getKeyword() + " ", tasks.size());
                    Task task = tasks.get(taskIndex);
                    if (task.isDone()) {
                        System.out.println("Crystal: You have already completed this task!");
                        System.out.println("         " + task);
                    } else {
                        task.markAsDone();
                        Storage.saveTasks(tasks);
                        System.out.println("Crystal: Nice! I've marked this task as done:");
                        System.out.println("         " + task);
                    }
                }
                case UNMARK -> {
                    int taskIndex = getTaskIndex(
                            command, commandType.getKeyword() + " ", tasks.size());
                    Task task = tasks.get(taskIndex);
                    if (!task.isDone()) {
                        System.out.println("Crystal: You have not completed this task in the first place!");
                        System.out.println("         " + task);
                    } else {
                        task.markAsNotDone();
                        Storage.saveTasks(tasks);
                        System.out.println("Crystal: OK, I've marked this task as not done yet:");
                        System.out.println("         " + task);
                    }
                }
                case DELETE -> {
                    int taskIndex = getTaskIndex(
                            command, commandType.getKeyword() + " ", tasks.size());
                    Task removedTask = tasks.remove(taskIndex);
                    Storage.saveTasks(tasks);
                    String taskWord = tasks.size() == 1 ? "task" : "tasks";
                    System.out.println("Crystal: Noted. I've removed this task:");
                    System.out.println("         " + removedTask);
                    System.out.println("         Now you have " + tasks.size() + " " + taskWord + " in the list.");
                }
                case TODO, DEADLINE, EVENT -> {
                    Task newTask = createTask(command, commandType);
                    tasks.add(newTask);
                    Storage.saveTasks(tasks);
                    String taskWord = tasks.size() == 1 ? "task" : "tasks";
                    System.out.println("Crystal: Got it! I've added this task:");
                    System.out.println("         " + newTask);
                    System.out.println("         Now you have " + tasks.size() + " " + taskWord + " in the list.");
                }
                case UNKNOWN, BYE -> throw new CrystalException("I don't know what that means :-(");
                }
            } catch (CrystalException exception) {
                System.out.println(exception.getUserMessage());
            }
            System.out.println(horizontalLine);
        }

        System.out.println(horizontalLine);
        System.out.println("Crystal: Bye!!! Hope to see you again soon!");
        System.out.println(horizontalLine);
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
