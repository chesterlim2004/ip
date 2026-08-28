import java.time.LocalDate;
import java.util.Optional;

/**
 * Interprets user commands and converts their arguments into application data.
 */
public class Parser {
    /** Prefix before a date in a list-on-date command. */
    private static final String LIST_ON_PREFIX = "list /on ";

    /** Separator between a deadline description and its date or time. */
    private static final String BY_SEPARATOR = " /by ";

    /** Separator between an event description and its start. */
    private static final String FROM_SEPARATOR = " /from ";

    /** Separator before an event's end. */
    private static final String TO_SEPARATOR = " /to ";

    /**
     * Prevents construction because command parsing has no instance state.
     */
    private Parser() {
    }

    /**
     * Determines the type of a command entered by the user.
     * The exit command must match exactly, while other known commands are
     * recognized by prefix so their malformed forms receive specific errors.
     *
     * @param command command entered by the user
     * @return matching command type, or {@link CommandType#UNKNOWN} if none matches
     */
    public static CommandType parseCommandType(String command) {
        if (command.equals(CommandType.EXIT.getKeyword())) {
            return CommandType.EXIT;
        }

        for (CommandType commandType : CommandType.values()) {
            if (commandType == CommandType.EXIT || commandType == CommandType.UNKNOWN) {
                continue;
            }
            if (command.startsWith(commandType.getKeyword())) {
                return commandType;
            }
        }
        return CommandType.UNKNOWN;
    }

    /**
     * Parses a list command and returns its optional date filter.
     *
     * @param command full list command
     * @return empty for {@code list}, or the date supplied to {@code list /on}
     * @throws CrystalException if the list command or its date is invalid
     */
    public static Optional<LocalDate> parseListCommand(String command)
            throws CrystalException {
        if (command.equals(CommandType.LIST.getKeyword())) {
            return Optional.empty();
        }

        if (command.equals("list /on") || command.startsWith(LIST_ON_PREFIX)) {
            if (!command.startsWith(LIST_ON_PREFIX)
                    || command.substring(LIST_ON_PREFIX.length()).isBlank()) {
                throw new CrystalException(
                        "To list tasks on a date, enter 'list /on [date]'!");
            }

            String dateInput = command.substring(LIST_ON_PREFIX.length());
            LocalDate date = TaskDateTime.parseDate(dateInput);
            if (date == null) {
                throw new CrystalException("I couldn't understand that date!");
            }
            return Optional.of(date);
        }

        throw new CrystalException("To view your task list, simply enter 'list'!");
    }

    /**
     * Converts the task number in a command into a valid list index.
     *
     * @param command full mark, unmark, or delete command
     * @param commandType type of task mutation
     * @param taskCount number of tasks currently stored
     * @return zero-based index of the selected task
     * @throws CrystalException if the command is malformed or the task does not exist
     */
    public static int parseTaskIndex(
            String command, CommandType commandType, int taskCount) throws CrystalException {
        String action = commandType.getKeyword();
        String prefix = action + " ";
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
     * Creates an add command containing the described todo, deadline, or event.
     *
     * @param command task creation command
     * @param commandType type of task to create
     * @return add command containing the parsed task
     * @throws CrystalException if required task details are missing
     */
    public static Command parseAddCommand(String command, CommandType commandType)
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
            return new AddCommand(new Todo(description));
        }
        case DEADLINE -> {
            if (!command.startsWith(prefix)) {
                throw new CrystalException("A deadline must have a description and a /by time!");
            }
            String details = command.substring(prefix.length());
            int byIndex = details.indexOf(BY_SEPARATOR);
            if (byIndex <= 0 || byIndex + BY_SEPARATOR.length() >= details.length()) {
                throw new CrystalException("A deadline must have a description and a /by time!");
            }
            String description = details.substring(0, byIndex);
            String by = details.substring(byIndex + BY_SEPARATOR.length());
            return new AddCommand(new Deadline(description, by));
        }
        case EVENT -> {
            if (!command.startsWith(prefix)) {
                throw new CrystalException(
                        "An event must have a description, a /from time and a /to time!");
            }
            String details = command.substring(prefix.length());
            int fromIndex = details.indexOf(FROM_SEPARATOR);
            int toIndex = details.indexOf(TO_SEPARATOR, fromIndex + FROM_SEPARATOR.length());
            if (fromIndex <= 0 || toIndex <= fromIndex + FROM_SEPARATOR.length()
                    || toIndex + TO_SEPARATOR.length() >= details.length()) {
                throw new CrystalException(
                        "An event must have a description, a /from time and a /to time!");
            }
            String description = details.substring(0, fromIndex);
            String from = details.substring(fromIndex + FROM_SEPARATOR.length(), toIndex);
            String to = details.substring(toIndex + TO_SEPARATOR.length());
            return new AddCommand(new Event(description, from, to));
        }
        default -> throw new CrystalException("I don't know what that means :-(");
        }
    }
}
