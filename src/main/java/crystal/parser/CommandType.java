package crystal.parser;

/**
 * Represents the commands understood by the Crystal chatbot.
 */
public enum CommandType {
    /** Adds a task without a date or time. */
    TODO("todo"),

    /** Adds a task with a deadline. */
    DEADLINE("deadline"),

    /** Adds a task occurring over a period. */
    EVENT("event"),

    /** Displays all tasks or tasks occurring on a date. */
    LIST("list"),

    /** Marks a task as completed. */
    MARK("mark"),

    /** Marks a task as incomplete. */
    UNMARK("unmark"),

    /** Removes a task. */
    DELETE("delete"),

    /** Ends the chatbot session. */
    EXIT("bye"),

    /** Represents input that does not match a supported command. */
    UNKNOWN("");

    /** Keyword that identifies this command at the start of user input. */
    private final String keyword;

    /**
     * Creates a command type with its identifying keyword.
     *
     * @param keyword text that identifies the command
     */
    CommandType(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Returns the keyword used to identify this command.
     *
     * @return command keyword
     */
    public String getKeyword() {
        return keyword;
    }
}
