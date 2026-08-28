package crystal.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests todo-specific display and storage representations.
 */
public class TodoTest {
    @Test
    public void representations_incompleteTodo_includeTypeAndStatus() {
        Todo todo = new Todo("read book");

        assertEquals("T | 0 | read book", todo.toDataString());
        assertEquals("[T][ ] read book", todo.toString());
    }

    @Test
    public void representations_completedTodo_includeCompletedStatus() {
        Todo todo = new Todo("read book");
        todo.markAsDone();

        assertEquals("T | 1 | read book", todo.toDataString());
        assertEquals("[T][X] read book", todo.toString());
    }
}
