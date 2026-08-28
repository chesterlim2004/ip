package crystal.parser;

/**
 * Represents the commands understood by the Crystal chatbot.
 */
public enum CommandType {
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    LIST("list"),
    MARK("mark"),
    UNMARK("unmark"),
    DELETE("delete"),
    EXIT("bye"),
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
