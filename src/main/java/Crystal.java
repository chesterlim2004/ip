import java.util.Scanner;

/**
 * Stores tasks entered by the user, lists them on request, and exits on {@code bye}.
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
                + "- To exit, enter 'bye'\n"
                + "- To view your list, enter 'list']";

        System.out.println(horizontalLine);
        System.out.print(banner);
        System.out.println("\nHello!!! I'm Crystal.");
        System.out.println(commands);
        System.out.println(horizontalLine);

        Scanner scanner = new Scanner(System.in);
        String[] tasks = new String[100];
        int taskCount = 0;
        while (true) {
            System.out.print("You: ");
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                break;
            }

            System.out.println(horizontalLine);
            if (command.equals("list")) {
                System.out.println("Crystal: Here is your list of items!");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println("         " + (i + 1) + ". " + tasks[i]);
                }
            } else {
                tasks[taskCount] = command;
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
