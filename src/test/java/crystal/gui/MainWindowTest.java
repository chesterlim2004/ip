package crystal.gui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests command detection used by the JavaFX window lifecycle.
 */
public class MainWindowTest {
    /** Verifies that only the exact exit command schedules the window to close. */
    @Test
    public void isExitCommand_exactAndNonExactCommands_returnsExpectedResult() {
        assertTrue(MainWindow.isExitCommand("bye"));
        assertFalse(MainWindow.isExitCommand("bye now"));
        assertFalse(MainWindow.isExitCommand("BYE"));
    }
}
