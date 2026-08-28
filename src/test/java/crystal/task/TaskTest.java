package crystal.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * Tests the common completion state and representations of {@link Task}.
 */
public class TaskTest {
    @Test
    public void completionState_markAndUnmark_statusAndRepresentationsUpdated() {
        Task task = new Task("read book");

        assertFalse(task.isDone());
        assertEquals(" ", task.getStatusIcon());
        assertEquals("0 | read book", task.toDataString());
        assertEquals("[ ] read book", task.toString());

        task.markAsDone();
        assertTrue(task.isDone());
        assertEquals("X", task.getStatusIcon());
        assertEquals("1 | read book", task.toDataString());
        assertEquals("[X] read book", task.toString());

        task.markAsNotDone();
        assertFalse(task.isDone());
        assertEquals(" ", task.getStatusIcon());
    }

    @Test
    public void occursOn_taskWithoutDate_returnsFalse() {
        Task task = new Task("read book");

        assertFalse(task.occursOn(LocalDate.of(2026, 12, 2)));
    }
}
