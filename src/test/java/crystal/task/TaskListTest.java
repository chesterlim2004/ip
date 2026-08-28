package crystal.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests task collection ownership, mutation, and date filtering.
 */
public class TaskListTest {
    @Test
    public void constructor_noTasks_createsEmptyTaskList() {
        TaskList tasks = new TaskList();

        assertTrue(tasks.isEmpty());
        assertEquals(0, tasks.getTaskCount());
        assertEquals(List.of(), tasks.getTasks());
    }

    @Test
    public void constructor_sourceListModified_taskListRetainsDefensiveCopy() {
        ArrayList<Task> source = new ArrayList<>();
        source.add(new Todo("read book"));
        TaskList tasks = new TaskList(source);

        source.clear();

        assertEquals(1, tasks.getTaskCount());
        assertEquals("[T][ ] read book", tasks.getTask(0).toString());
    }

    @Test
    public void addAndDelete_validTasks_updatesOrderAndReturnsDeletedTask() {
        Task first = new Todo("read book");
        Task second = new Todo("return book");
        TaskList tasks = new TaskList();

        tasks.addTask(first);
        tasks.addTask(second);
        Task deleted = tasks.deleteTask(0);

        assertSame(first, deleted);
        assertEquals(1, tasks.getTaskCount());
        assertSame(second, tasks.getTask(0));
        assertFalse(tasks.isEmpty());
    }

    @Test
    public void indexedOperations_invalidIndexes_throwIndexOutOfBoundsException() {
        TaskList tasks = new TaskList(List.of(new Todo("read book")));

        assertThrows(IndexOutOfBoundsException.class, () -> tasks.getTask(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> tasks.getTask(1));
        assertThrows(IndexOutOfBoundsException.class, () -> tasks.deleteTask(1));
    }

    @Test
    public void getTasks_returnedSnapshotCannotMutateTaskList() {
        TaskList tasks = new TaskList(List.of(new Todo("read book")));
        List<Task> snapshot = tasks.getTasks();

        assertThrows(UnsupportedOperationException.class,
                () -> snapshot.add(new Todo("return book")));
        assertEquals(1, tasks.getTaskCount());
    }

    @Test
    public void getTasksOnDate_mixedTasks_returnsMatchingTasksInOriginalOrder() {
        Task todo = new Todo("mention 02 Dec 2026");
        Task matchingDeadline = new Deadline("submit report", "2Dec26 0900");
        Task otherDeadline = new Deadline("later", "3Dec26");
        Task spanningEvent = new Event("conference", "1Dec26", "3Dec26");
        Task textEvent = new Event("weekly call", "Monday", "Tuesday");
        TaskList tasks = new TaskList(List.of(
                todo, matchingDeadline, otherDeadline, spanningEvent, textEvent));

        List<Task> matches = tasks.getTasksOnDate(LocalDate.of(2026, 12, 2));

        assertEquals(List.of(matchingDeadline, spanningEvent), matches);
        assertThrows(UnsupportedOperationException.class,
                () -> matches.add(new Todo("another")));
    }

    @Test
    public void findTasks_mixedTasks_matchesDescriptionsCaseInsensitivelyInOriginalOrder() {
        Task firstMatch = new Todo("READ book");
        Task dateOnlyMatch = new Deadline("submit report", "book collection day");
        Task secondMatch = new Event("book club", "Monday", "Tuesday");
        TaskList tasks = new TaskList(List.of(firstMatch, dateOnlyMatch, secondMatch));

        List<Task> matches = tasks.findTasks("BoOk");

        assertEquals(List.of(firstMatch, secondMatch), matches);
        assertThrows(UnsupportedOperationException.class,
                () -> matches.add(new Todo("book review")));
    }
}
