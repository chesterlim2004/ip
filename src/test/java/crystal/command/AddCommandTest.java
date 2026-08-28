package crystal.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

import crystal.exception.CrystalException;
import crystal.task.TaskList;
import crystal.task.Todo;

/**
 * Tests adding, saving, and reporting a task through {@link AddCommand}.
 */
public class AddCommandTest extends CommandTestBase {
    /** Verifies successful task addition, persistence, and user feedback. */
    @Test
    public void execute_validTask_addsPersistsAndDisplaysTask() throws Exception {
        Todo todo = new Todo("read book");
        TaskList tasks = new TaskList();
        AddCommand command = new AddCommand(todo);

        command.execute(tasks, createUi(), createStorage());

        assertEquals(1, tasks.getTaskCount());
        assertSame(todo, tasks.getTask(0));
        Path dataFile = tempDirectory.resolve("data").resolve("crystal.txt");
        assertEquals(List.of("T | 0 | read book"),
                Files.readAllLines(dataFile, StandardCharsets.UTF_8));
        assertEquals("Crystal: Got it! I've added this task:\n"
                + "         [T][ ] read book\n"
                + "         Now you have 1 task in the list.\n", getOutput());
    }

    /** Verifies that task-saving failures propagate as Crystal exceptions. */
    @Test
    public void execute_storageFailure_propagatesCrystalException() throws Exception {
        Path directoryAsFile = tempDirectory.resolve("crystal.txt");
        Files.createDirectory(directoryAsFile);
        AddCommand command = new AddCommand(new Todo("read book"));

        CrystalException exception = org.junit.jupiter.api.Assertions.assertThrows(
                CrystalException.class,
                () -> command.execute(new TaskList(), createUi(),
                        new crystal.storage.Storage(directoryAsFile)));

        assertEquals("I couldn't save your tasks to the hard disk.", exception.getMessage());
    }
}
