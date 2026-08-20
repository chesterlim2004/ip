import java.util.Scanner;

/**
 * Greets the user, echoes commands, and exits when the user enters {@code bye}.
 */
public class Crystal {
    public static void main(String[] args) {
        String horizontalLine = "____________________________________________________________";
        String banner = "  ____ ______   ______ _____  _    _\n"
                + " / ___|  _ \\ \\ / / ___|_   _|/ \\  | |\n"
                + "| |   | |_) \\ V /\\___ \\ | | / _ \\ | |\n"
                + "| |___|  _ < | |  ___) || |/ ___ \\| |___\n"
                + " \\____|_| \\_\\|_| |____/ |_/_/   \\_\\_____|\n";

        System.out.println(horizontalLine);
        System.out.print(banner);
        System.out.println("\nHello!!! I'm Crystal.");
        System.out.println("What can I do for you?");
        System.out.println(horizontalLine);

        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        while (!command.equals("bye")) {
            System.out.println(horizontalLine);
            System.out.println(command);
            System.out.println(horizontalLine);
            command = scanner.nextLine();
        }

        System.out.println(horizontalLine);
        System.out.println("Bye!!! Hope to see you again soon!");
        System.out.println(horizontalLine);
    }
}
