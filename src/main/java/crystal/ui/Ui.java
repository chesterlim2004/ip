package crystal.ui;

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
            + "- To mark a task as done, enter 'mark [task number]'\n"
            + "- To mark a task as not done, enter 'unmark [task number]'\n"
            + "- To delete a task, enter 'delete [task number]'\n"
            + "- To exit, enter 'bye']";

    /** Reads commands from the console. */
    private final Scanner scanner;

    /**
     * Creates a console UI that reads from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Shows Crystal's logo, greeting, and command guide.
     */
    public void showWelcome() {
        showDivider();
        System.out.print(BANNER);
        System.out.println("\nHello!!! I'm Crystal.");
        System.out.println(COMMANDS);
        showDivider();
    }

    /**
     * Prompts for and returns the user's next command.
     *
     * @return command entered by the user.
     */
    public String readCommand() {
        System.out.print("You: ");
        return scanner.nextLine();
    }

    /**
     * Shows a divider between console interactions.
     */
    public void showDivider() {
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Shows Crystal's farewell.
     */
    public void showGoodbye() {
        System.out.println("Crystal: Bye!!! Hope to see you again soon!");
    }

    /**
     * Shows a user-facing Crystal error.
     *
     * @param exception error to display.
     */
    public void showError(CrystalException exception) {
        System.out.println(exception.getUserMessage());
    }

    /**
     * Shows the complete task list with its persistent task numbers.
     *
     * @param tasks complete task list.
     */
    public void showTaskList(TaskList tasks) {
        if (tasks.isEmpty()) {
            System.out.println("Crystal: Your task list is empty!");
            return;
        }

        System.out.println("Crystal: Here are the tasks in your list:");
        for (int i = 0; i < tasks.getTaskCount(); i++) {
            System.out.println("         " + (i + 1) + "." + tasks.getTask(i));
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
            System.out.println(
                    "Crystal: There are no deadlines or events on " + formattedDate + "!");
            return;
        }

        System.out.println("Crystal: Here are the deadlines and events on "
                + formattedDate + ":");
        for (Task task : matchingTasks) {
            System.out.println("         - " + task);
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
        System.out.println(heading);
        System.out.println("         " + task);
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
        System.out.println("         Now you have " + taskCount
                + " " + taskWord + " in the list.");
    }
}
