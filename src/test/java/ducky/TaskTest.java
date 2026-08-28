package ducky;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Task}.
 */
class TaskTest {
    @Test
    /** Tests that construction preserves a task's description and incomplete status. */
    void constructor_descriptionAndIncompleteStatusSet() {
        Task task = new Task("read book");

        assertEquals("read book", task.getDescription());
        assertFalse(task.isDone());
    }

    @Test
    /** Tests retrieval of a task's description. */
    void getDescription_taskDescriptionReturned() {
        Task task = new Task("read book");

        assertEquals("read book", task.getDescription());
    }

    @Test
    /** Tests that a newly created task is incomplete. */
    void isDone_newTask_falseReturned() {
        Task task = new Task("read book");

        assertFalse(task.isDone());
    }

    @Test
    /** Tests the status icon for an incomplete task. */
    void getStatusIcon_newTask_spaceReturned() {
        Task task = new Task("read book");

        assertEquals(" ", task.getStatusIcon());
    }

    @Test
    /** Tests the status icon for a completed task. */
    void getStatusIcon_completedTask_xReturned() {
        Task task = new Task("read book");
        task.markAsDone();

        assertEquals("X", task.getStatusIcon());
    }

    @Test
    /** Tests marking an incomplete task as complete. */
    void markAsDone_incompleteTask_taskBecomesDone() {
        Task task = new Task("read book");

        task.markAsDone();

        assertTrue(task.isDone());
    }

    @Test
    /** Tests that marking a completed task again leaves it completed. */
    void markAsDone_completedTask_remainsDone() {
        Task task = new Task("read book");

        task.markAsDone();
        task.markAsDone();

        assertTrue(task.isDone());
        assertEquals("X", task.getStatusIcon());
    }

    @Test
    /** Tests unmarking a completed task. */
    void unmark_completedTask_taskBecomesIncomplete() {
        Task task = new Task("read book");

        task.markAsDone();
        task.unmark();

        assertFalse(task.isDone());
        assertEquals(" ", task.getStatusIcon());
    }

    @Test
    /** Tests that unmarking an incomplete task leaves it incomplete. */
    void unmark_incompleteTask_remainsIncomplete() {
        Task task = new Task("read book");

        task.unmark();

        assertFalse(task.isDone());
        assertEquals(" ", task.getStatusIcon());
    }

    @Test
    /** Tests the display string for an incomplete task. */
    void toString_incompleteTask_formattedDescriptionReturned() {
        Task task = new Task("read book");

        assertEquals("[ ] read book", task.toString());
    }

    @Test
    /** Tests the display string for a completed task. */
    void toString_completedTask_formattedDescriptionReturned() {
        Task task = new Task("read book");
        task.markAsDone();

        assertEquals("[X] read book", task.toString());
    }

    @Test
    void find_keywordMatchingDescriptions_matchingTasksReturned() {
        TaskList tasks = new TaskList(List.of(
                new ToDo("Read a book"),
                new ToDo("Buy groceries"),
                new ToDo("Return the BOOK")));

        List<Task> matchingTasks = tasks.find("book");

        assertEquals(2, matchingTasks.size());
        assertEquals("Read a book", matchingTasks.get(0).getDescription());
        assertEquals("Return the BOOK", matchingTasks.get(1).getDescription());
    }
}
