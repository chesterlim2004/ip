package crystal.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import crystal.command.AddCommand;
import crystal.command.Command;
import crystal.command.DeleteCommand;
import crystal.command.ExitCommand;
import crystal.command.ListCommand;
import crystal.command.MarkCommand;
import crystal.command.UnmarkCommand;
import crystal.exception.CrystalException;
import crystal.storage.Storage;
import crystal.task.Task;
import crystal.task.TaskList;
import crystal.task.Todo;
import crystal.ui.Ui;

/**
 * Tests command recognition, argument extraction, and parser error messages.
 */
public class ParserTest {
    @TempDir
    private Path tempDirectory;

    /** Verifies that every supported keyword creates the corresponding command type. */
    @Test
    public void parse_supportedCommandKeywords_returnsMatchingCommandTypes()
            throws CrystalException {
        assertInstanceOf(AddCommand.class, Parser.parse("todo read book"));
        assertInstanceOf(AddCommand.class,
                Parser.parse("deadline submit report /by 2Dec26"));
        assertInstanceOf(AddCommand.class,
                Parser.parse("event workshop /from 6am /to 630pm"));
        assertInstanceOf(ListCommand.class, Parser.parse("list"));
        assertInstanceOf(ListCommand.class, Parser.parse("list /on 2Dec26"));
        assertInstanceOf(MarkCommand.class, Parser.parse("mark 1"));
        assertInstanceOf(UnmarkCommand.class, Parser.parse("unmark 1"));
        assertInstanceOf(DeleteCommand.class, Parser.parse("delete 1"));
        assertInstanceOf(ExitCommand.class, Parser.parse("bye"));
    }

    /** Verifies extraction and normalization of todo, deadline, and event details. */
    @Test
    public void parse_addCommands_executeWithExtractedAndNormalizedTaskDetails()
            throws CrystalException {
        assertParsedTaskData("todo read book", "T | 0 | read book");
        assertParsedTaskData("deadline submit report /by 2Dec26 630pm",
                "D | 0 | submit report | 02 Dec 2026 1830");
        assertParsedTaskData("event workshop /from Monday 6am /to 6.30pm",
                "E | 0 | workshop | Monday 0600 | 1830");
    }

    /** Verifies conversion of user-facing task numbers into zero-based indexes. */
    @Test
    public void parse_mutationCommands_convertsOneBasedTaskNumberToZeroBasedIndex()
            throws CrystalException {
        Task first = new Todo("first");
        Task second = new Todo("second");
        TaskList tasks = new TaskList(List.of(first, second));
        Ui ui = new Ui();
        Storage storage = new Storage(tempDirectory.resolve("mutations.txt"));

        Parser.parse("mark 2").execute(tasks, ui, storage);
        assertFalse(first.isDone());
        assertTrue(second.isDone());

        Parser.parse("unmark 2").execute(tasks, ui, storage);
        assertFalse(second.isDone());

        Parser.parse("delete 1").execute(tasks, ui, storage);
        assertEquals(List.of(second), tasks.getTasks());
    }

    /** Verifies that unknown input and non-exact exit commands receive the unknown error. */
    @Test
    public void parse_unknownOrNonExactExitCommands_throwUnknownCommandError() {
        assertParseError("", "I don't know what that means :-(");
        assertParseError("read book", "I don't know what that means :-(");
        assertParseError("bye now", "I don't know what that means :-(");
    }

    /** Verifies todo usage errors for missing and blank descriptions. */
    @Test
    public void parse_malformedTodoCommands_throwTodoUsageError() {
        String expected = "A todo must have a description!";

        assertParseError("todo", expected);
        assertParseError("todo ", expected);
        assertParseError("todo    ", expected);
    }

    /** Verifies deadline usage errors for missing descriptions and deadline values. */
    @Test
    public void parse_malformedDeadlineCommands_throwDeadlineUsageError() {
        String expected = "A deadline must have a description and a /by time!";

        assertParseError("deadline", expected);
        assertParseError("deadline submit report", expected);
        assertParseError("deadline /by 2Dec26", expected);
        assertParseError("deadline submit report /by ", expected);
    }

    /** Verifies event usage errors for missing descriptions, starts, and ends. */
    @Test
    public void parse_malformedEventCommands_throwEventUsageError() {
        String expected = "An event must have a description, a /from time and a /to time!";

        assertParseError("event", expected);
        assertParseError("event workshop", expected);
        assertParseError("event /from 6am /to 7am", expected);
        assertParseError("event workshop /from /to 7am", expected);
        assertParseError("event workshop /from 6am /to ", expected);
    }

    /** Verifies specific errors for malformed list and list-on-date commands. */
    @Test
    public void parse_malformedListCommands_throwSpecificListErrors() {
        assertParseError("list ", "To view your task list, simply enter 'list'!");
        assertParseError("list tomorrow", "To view your task list, simply enter 'list'!");
        assertParseError("list /on",
                "To list tasks on a date, enter 'list /on [date]'!");
        assertParseError("list /on    ",
                "To list tasks on a date, enter 'list /on [date]'!");
        assertParseError("list /on Monday", "I couldn't understand that date!");
        assertParseError("list /on 31/02/26", "I couldn't understand that date!");
    }

    /** Verifies action-specific errors for malformed task mutation commands. */
    @Test
    public void parse_malformedMutationCommands_throwActionSpecificErrors() {
        assertParseError("mark", "You have to mark a task number!");
        assertParseError("mark abc", "You have to mark a task number!");
        assertParseError("mark 1 2", "You have to mark a task number!");
        assertParseError("unmark", "You have to unmark a task number!");
        assertParseError("unmark 1.5", "You have to unmark a task number!");
        assertParseError("delete", "You have to delete a task number!");
        assertParseError("delete one", "You have to delete a task number!");
    }

    /**
     * Parses and executes an add command, then compares its persisted representation.
     *
     * @param input complete add command
     * @param expectedData expected data representation of the parsed task
     * @throws CrystalException if parsing or execution unexpectedly fails
     */
    private void assertParsedTaskData(String input, String expectedData) throws CrystalException {
        Command command = Parser.parse(input);
        TaskList tasks = new TaskList();
        command.execute(tasks, new Ui(),
                new Storage(tempDirectory.resolve("parsed-task.txt")));

        assertEquals(1, tasks.getTaskCount());
        assertEquals(expectedData, tasks.getTask(0).toDataString());
    }

    /**
     * Verifies that parsing fails with Crystal's exact internal error message.
     *
     * @param input malformed command
     * @param expectedMessage expected exception message
     */
    private static void assertParseError(String input, String expectedMessage) {
        CrystalException exception = assertThrows(
                CrystalException.class, () -> Parser.parse(input), input);
        assertEquals(expectedMessage, exception.getMessage());
    }
}
