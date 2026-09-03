package crystal.ui;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import crystal.exception.CrystalException;
import crystal.task.Task;
import crystal.task.TaskList;

/**
 * Handles console input and presents Crystal's responses to the user.
 */
public class Ui {
    /** Divider used to separate console interactions. */
    private static final String HORIZONTAL_LINE =
            "____________________________________________________________";

    /** Crystal's startup logo. */
    private static final String BANNER = "  ____ ______   ______ _____  _    _\n"
            + " / ___|  _ \\ \\ / / ___|_   _|/ \\  | |\n"
            + "| |   | |_) \\ V /\\___ \\ | | / _ \\ | |\n"
            + "| |___|  _ < | |  ___) || |/ ___ \\| |___\n"
            + " \\____|_| \\_\\|_| |____/ |_/_/   \\_\\_____|\n";

    /** Command guide shown when Crystal starts. */
    private static final String COMMANDS = "[Commands:\n"
            + "- To add a todo, enter 'todo [description]'\n"
            + "- To add a deadline, enter 'deadline [description] /by [deadline]'\n"
            + "- To add an event, enter 'event [description] /from [start] /to [end]'\n"
            + "- To view your task list, enter 'list'\n"
            + "- To view deadlines and events on a date, enter 'list /on [date]'\n"
            + "- To find tasks by description, enter 'find [keyword]'\n"
            + "- To mark a task as done, enter 'mark [task number]'\n"
            + "- To mark a task as not done, enter 'unmark [task number]'\n"
            + "- To delete a task, enter 'delete [task number]'\n"
            + "- To exit, enter 'bye']";

    /** Reads commands from the console. */
    private final Scanner scanner;

    /** Destination for all text presented by this UI. */
    private final PrintStream output;

    /**
     * Creates a console UI that reads from standard input.
     */
    public Ui() {
        this(new Scanner(System.in), System.out);
    }

    /**
     * Creates a UI with explicit input and output channels.
     *
     * @param scanner source of console commands.
     * @param output destination for application responses.
     */
    private Ui(Scanner scanner, PrintStream output) {
        this.scanner = scanner;
        this.output = output;
    }

    /**
     * Creates a response-only UI that writes to the supplied stream.
     *
     * @param output destination for application responses.
     * @return UI suitable for collecting a single command response.
     */
    public static Ui createForOutput(PrintStream output) {
        return new Ui(new Scanner(""), output);
    }

    /**
     * Shows Crystal's logo, greeting, and command guide.
     */
    public void showWelcome() {
        showDivider();
        output.print(BANNER);
        output.println("\nHello!!! I'm Crystal.");
        output.println(COMMANDS);
        showDivider();
    }

    /**
     * Prompts for and returns the user's next command.
     *
     * @return command entered by the user.
     */
    public String readCommand() {
        output.print("You: ");
        return scanner.nextLine();
    }

    /**
     * Shows a divider between console interactions.
     */
    public void showDivider() {
        output.println(HORIZONTAL_LINE);
    }

    /**
     * Shows Crystal's farewell.
     */
    public void showGoodbye() {
        output.println("Crystal: Bye!!! Hope to see you again soon!");
    }

    /**
     * Shows a user-facing Crystal error.
     *
     * @param exception error to display.
     */
    public void showError(CrystalException exception) {
        output.println(exception.getUserMessage());
    }

    /**
     * Shows the complete task list with its persistent task numbers.
     *
     * @param tasks complete task list.
     */
    public void showTaskList(TaskList tasks) {
        if (tasks.isEmpty()) {
            output.println("Crystal: Your task list is empty!");
            return;
        }

        output.println("Crystal: Here are the tasks in your list:");
        for (int i = 0; i < tasks.getTaskCount(); i++) {
            output.println("         " + (i + 1) + "." + tasks.getTask(i));
        }
    }

    /**
     * Shows an unnumbered temporary view of tasks occurring on a date.
     *
     * @param formattedDate date in Crystal's display format.
     * @param matchingTasks deadlines and events occurring on the date.
     */
    public void showTasksOnDate(String formattedDate, List<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            output.println(
                    "Crystal: There are no deadlines or events on " + formattedDate + "!");
            return;
        }

        output.println("Crystal: Here are the deadlines and events on "
                + formattedDate + ":");
        for (Task task : matchingTasks) {
            output.println("         - " + task);
        }
    }

    /**
     * Shows a temporary numbered view of tasks matching a search keyword.
     *
     * @param matchingTasks tasks whose descriptions contain the keyword.
     */
    public void showMatchingTasks(List<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            output.println("Crystal: There are no matching tasks in your list!");
            return;
        }

        output.println("Crystal: Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            output.println("         " + (i + 1) + "." + matchingTasks.get(i));
        }
    }

    /**
     * Shows that a task was already marked as done.
     *
     * @param task task whose status was unchanged.
     */
    public void showTaskAlreadyDone(Task task) {
        showTaskWithHeading("Crystal: You have already completed this task!", task);
    }

    /**
     * Shows that a task was already marked as not done.
     *
     * @param task task whose status was unchanged.
     */
    public void showTaskAlreadyNotDone(Task task) {
        showTaskWithHeading(
                "Crystal: You have not completed this task in the first place!", task);
    }

    /**
     * Shows that a task was marked as done.
     *
     * @param task task whose status changed.
     */
    public void showTaskMarkedDone(Task task) {
        showTaskWithHeading("Crystal: Nice! I've marked this task as done:", task);
    }

    /**
     * Shows that a task was marked as not done.
     *
     * @param task task whose status changed.
     */
    public void showTaskMarkedNotDone(Task task) {
        showTaskWithHeading("Crystal: OK, I've marked this task as not done yet:", task);
    }

    /**
     * Shows a newly added task and the updated task count.
     *
     * @param task added task.
     * @param taskCount number of tasks after the addition.
     */
    public void showTaskAdded(Task task, int taskCount) {
        showTaskAndCount("Crystal: Got it! I've added this task:", task, taskCount);
    }

    /**
     * Shows a deleted task and the updated task count.
     *
     * @param task deleted task.
     * @param taskCount number of tasks after the deletion.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        showTaskAndCount("Crystal: Noted. I've removed this task:", task, taskCount);
    }

    /**
     * Shows a response heading followed by one indented task.
     *
     * @param heading response heading.
     * @param task task to display.
     */
    private void showTaskWithHeading(String heading, Task task) {
        output.println(heading);
        output.println("         " + task);
    }

    /**
     * Shows a response heading, one task, and the updated task count.
     *
     * @param heading response heading.
     * @param task task to display.
     * @param taskCount current number of tasks.
     */
    private void showTaskAndCount(String heading, Task task, int taskCount) {
        showTaskWithHeading(heading, task);
        String taskWord = taskCount == 1 ? "task" : "tasks";
        output.println("         Now you have " + taskCount
                + " " + taskWord + " in the list.");
    }
}
