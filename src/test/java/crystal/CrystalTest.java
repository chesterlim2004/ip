package crystal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests Crystal's startup, command loop, persistence, recovery, and exit orchestration.
 */
public class CrystalTest {
    @TempDir
    private Path tempDirectory;

    private InputStream originalInput;
    private PrintStream originalOutput;
    private ByteArrayOutputStream capturedOutput;

    @BeforeEach
    public void redirectStandardOutput() {
        originalInput = System.in;
        originalOutput = System.out;
        capturedOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedOutput, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    public void restoreStandardStreams() {
        System.setIn(originalInput);
        System.setOut(originalOutput);
    }

    @Test
    public void run_existingData_loadsMutatesPersistsAndExits() throws Exception {
        Path dataFile = tempDirectory.resolve("data").resolve("crystal.txt");
        Files.createDirectories(dataFile.getParent());
        Files.write(dataFile, List.of("T | 0 | read book"), StandardCharsets.UTF_8);
        setInput("list\nmark 1\nbye\n");

        new Crystal(dataFile).run();

        assertEquals(List.of("T | 1 | read book"),
                Files.readAllLines(dataFile, StandardCharsets.UTF_8));
        String output = getOutput();
        assertTrue(output.contains("Crystal: Here are the tasks in your list:\n"
                + "         1.[T][ ] read book\n"));
        assertTrue(output.contains("Crystal: Nice! I've marked this task as done:\n"
                + "         [T][X] read book\n"));
        assertTrue(output.contains("Crystal: Bye!!! Hope to see you again soon!"));
    }

    @Test
    public void run_corruptedData_reportsErrorUsesEmptyListAndReplacesFileOnMutation()
            throws Exception {
        Path dataFile = tempDirectory.resolve("crystal.txt");
        Files.writeString(dataFile, "corrupted data", StandardCharsets.UTF_8);
        setInput("todo recovered task\nbye\n");

        new Crystal(dataFile).run();

        assertEquals(List.of("T | 0 | recovered task"),
                Files.readAllLines(dataFile, StandardCharsets.UTF_8));
        String output = getOutput();
        assertTrue(output.contains("Crystal: Oopsies!!! Your saved task data is invalid."));
        assertTrue(output.contains("Crystal: Got it! I've added this task:\n"
                + "         [T][ ] recovered task\n"));
    }

    @Test
    public void run_invalidCommand_reportsErrorAndContinuesToExit() {
        Path dataFile = tempDirectory.resolve("crystal.txt");
        setInput("unknown command\nbye\n");

        new Crystal(dataFile).run();

        String output = getOutput();
        int errorIndex = output.indexOf("Crystal: Oopsies!!! I don't know what that means :-(");
        int goodbyeIndex = output.indexOf("Crystal: Bye!!! Hope to see you again soon!");
        assertTrue(errorIndex >= 0);
        assertTrue(goodbyeIndex > errorIndex);
    }

    /**
     * Supplies complete console commands before Crystal constructs its scanner.
     *
     * @param input newline-separated commands
     */
    private static void setInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Returns console output with platform line endings normalized.
     *
     * @return captured console output
     */
    private String getOutput() {
        return capturedOutput.toString(StandardCharsets.UTF_8).replace("\r\n", "\n");
    }
}
