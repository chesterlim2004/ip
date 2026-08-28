package crystal.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

import crystal.exception.CrystalException;
import crystal.task.TaskList;
import crystal.task.Todo;

/**
 * Tests task removal, persistence, output, and index validation.
 */
public class DeleteCommandTest extends CommandTestBase {
    @Test
    public void execute_validIndex_deletesPersistsAndDisplaysTask() throws Exception {
        TaskList tasks = new TaskList(List.of(new Todo("read book"), new Todo("return book")));
        DeleteCommand command = new DeleteCommand(0);

        command.execute(tasks, createUi(), createStorage());

        assertEquals(1, tasks.getTaskCount());
        assertEquals("[T][ ] return book", tasks.getTask(0).toString());
        Path dataFile = tempDirectory.resolve("data").resolve("crystal.txt");
        assertEquals(List.of("T | 0 | return book"),
                Files.readAllLines(dataFile, StandardCharsets.UTF_8));
        assertEquals("Crystal: Noted. I've removed this task:\n"
                + "         [T][ ] read book\n"
                + "         Now you have 1 task in the list.\n", getOutput());
    }

    @Test
    public void execute_indexOutsideTaskList_throwsCrystalException() {
        TaskList tasks = new TaskList(List.of(new Todo("read book")));

        CrystalException negativeIndex = assertThrows(CrystalException.class,
                () -> new DeleteCommand(-1).execute(tasks, createUi(), createStorage()));
        CrystalException highIndex = assertThrows(CrystalException.class,
                () -> new DeleteCommand(1).execute(tasks, createUi(), createStorage()));

        assertEquals("That task number does not exist!", negativeIndex.getMessage());
        assertEquals("That task number does not exist!", highIndex.getMessage());
        assertEquals(1, tasks.getTaskCount());
    }
}
