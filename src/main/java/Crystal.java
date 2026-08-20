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
        ArrayList<Task> tasks = new ArrayList<>();
        while (true) {
            System.out.print("You: ");
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                break;
            }

            System.out.println(horizontalLine);
            try {
                if (command.equals("list")) {
                    System.out.println("Crystal: Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println("         " + (i + 1) + "." + tasks.get(i));
                    }
                } else if (command.startsWith("mark ")) {
                    int taskIndex = getTaskIndex(command, "mark ", tasks.size());
                    Task task = tasks.get(taskIndex);
                    if (task.isDone()) {
                        System.out.println("Crystal: You have already completed this task!");
                        System.out.println("         " + task);
                    } else {
                        task.markAsDone();
                        System.out.println("Crystal: Nice! I've marked this task as done:");
                        System.out.println("         " + task);
                    }
                } else if (command.startsWith("unmark ")) {
                    int taskIndex = getTaskIndex(command, "unmark ", tasks.size());
                    Task task = tasks.get(taskIndex);
                    if (!task.isDone()) {
                        System.out.println("Crystal: You have not completed this task in the first place!");
                        System.out.println("         " + task);
                    } else {
                        task.markAsNotDone();
                        System.out.println("Crystal: OK, I've marked this task as not done yet:");
                        System.out.println("         " + task);
                    }
                } else if (command.startsWith("delete ")) {
                    int taskIndex = getTaskIndex(command, "delete ", tasks.size());
                    Task removedTask = tasks.remove(taskIndex);
                    String taskWord = tasks.size() == 1 ? "task" : "tasks";
                    System.out.println("Crystal: Noted. I've removed this task:");
                    System.out.println("         " + removedTask);
                    System.out.println("         Now you have " + tasks.size() + " " + taskWord + " in the list.");
                } else {
                    Task newTask = createTask(command);
                    tasks.add(newTask);
                    String taskWord = tasks.size() == 1 ? "task" : "tasks";
                    System.out.println("Crystal: Got it! I've added this task:");
                    System.out.println("         " + newTask);
                    System.out.println("         Now you have " + tasks.size() + " " + taskWord + " in the list.");
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
     * @throws CrystalException if the task number is not numeric or does not exist
     */
    private static int getTaskIndex(String command, String prefix, int taskCount)
            throws CrystalException {
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(command.substring(prefix.length()));
        } catch (NumberFormatException exception) {
            throw new CrystalException("Please enter a valid task number!");
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
     * @return task represented by the command
     * @throws CrystalException if the task type or required details are missing
     */
    private static Task createTask(String command) throws CrystalException {
        if (command.startsWith("todo")) {
            if (!command.startsWith("todo ")) {
                throw new CrystalException("A todo must have a description!");
            }
            String description = command.substring("todo ".length());
            if (description.isBlank()) {
                throw new CrystalException("A todo must have a description!");
            }
            return new Todo(description);
        }

        if (command.startsWith("deadline")) {
            if (!command.startsWith("deadline ")) {
                throw new CrystalException("A deadline must have a description and a /by time!");
            }
            String details = command.substring("deadline ".length());
            int byIndex = details.indexOf(" /by ");
            if (byIndex <= 0 || byIndex + " /by ".length() >= details.length()) {
                throw new CrystalException("A deadline must have a description and a /by time!");
            }
            String description = details.substring(0, byIndex);
            String by = details.substring(byIndex + " /by ".length());
            return new Deadline(description, by);
        }

        if (command.startsWith("event")) {
            if (!command.startsWith("event ")) {
                throw new CrystalException(
                        "An event must have a description, a /from time and a /to time!");
            }
            String details = command.substring("event ".length());
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

        throw new CrystalException("I don't know what that means :-(");
    }
}
