/**
 * Greets the user as Crystal and exits.
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
        System.out.println("Bye!!! Hope to see you again soon!");
        System.out.println(horizontalLine);
    }
}
