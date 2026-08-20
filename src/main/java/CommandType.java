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
    BYE("bye"),
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

    /**
     * Determines the type of a command entered by the user.
     * The exit command must match exactly, while other known commands are
     * recognized by prefix so their malformed forms can receive specific errors.
     *
     * @param command command entered by the user
     * @return matching command type, or {@link #UNKNOWN} if none matches
     */
    public static CommandType fromCommand(String command) {
        if (command.equals(BYE.keyword)) {
            return BYE;
        }

        for (CommandType commandType : values()) {
            if (commandType == BYE || commandType == UNKNOWN) {
                continue;
            }
            if (command.startsWith(commandType.keyword)) {
                return commandType;
            }
        }
        return UNKNOWN;
    }
}
