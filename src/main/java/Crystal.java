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
                + "- To add a task to the list, simply enter your task\n"
                + "- To view your list, enter 'list'\n"
                + "- To mark a task as done, enter 'mark [task number]'\n"
                + "- To mark a task as not done, enter 'unmark [task number]'\n"
                + "- To exit, enter 'bye'\n";

        System.out.println(horizontalLine);
        System.out.print(banner);
        System.out.println("\nHello!!! I'm Crystal.");
        System.out.println(commands);
        System.out.println(horizontalLine);

        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int taskCount = 0;
        while (true) {
            System.out.print("You: ");
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                break;
            }

            System.out.println(horizontalLine);
            if (command.equals("list")) {
                System.out.println("Crystal: Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println("         " + (i + 1) + "." + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                int taskIndex = Integer.parseInt(command.substring(5)) - 1;
                if (tasks[taskIndex].isDone()) {
                    System.out.println("Crystal: You have already completed this task!");
                    System.out.println("         " + tasks[taskIndex]);
                } else {
                    tasks[taskIndex].markAsDone();
                    System.out.println("Crystal: Nice! I've marked this task as done:");
                    System.out.println("         " + tasks[taskIndex]);
                }
            } else if (command.startsWith("unmark ")) {
                int taskIndex = Integer.parseInt(command.substring(7)) - 1;
                if (!tasks[taskIndex].isDone()) {
                    System.out.println("Crystal: You have not completed this task in the first place!");
                    System.out.println("         " + tasks[taskIndex]);
                } else {
                    tasks[taskIndex].markAsNotDone();
                    System.out.println("Crystal: OK, I've marked this task as not done yet:");
                    System.out.println("         " + tasks[taskIndex]);
                }
            } else {
                tasks[taskCount] = new Task(command);
                taskCount++;
                System.out.println("Crystal: I have added '" + command + "' to your list!");
            }
            System.out.println(horizontalLine);
        }

        System.out.println(horizontalLine);
        System.out.println("Crystal: Bye!!! Hope to see you again soon!");
        System.out.println(horizontalLine);
    }
}
