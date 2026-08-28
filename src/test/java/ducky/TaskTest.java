package ducky;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Task}.
 */
class TaskTest {
    /** Tests that construction preserves a task's description and incomplete status. */
    @Test
    void constructor_descriptionAndIncompleteStatusSet() {
        Task task = new Task("read book");

        assertEquals("read book", task.getDescription());
        assertFalse(task.isDone());
    }

    /** Tests retrieval of a task's description. */
    @Test
    void getDescription_taskDescriptionReturned() {
        Task task = new Task("read book");

        assertEquals("read book", task.getDescription());
    }

    /** Tests that a newly created task is incomplete. */
    @Test
    void isDone_newTask_falseReturned() {
        Task task = new Task("read book");

        assertFalse(task.isDone());
    }

    /** Tests the status icon for an incomplete task. */
    @Test
    void getStatusIcon_newTask_spaceReturned() {
        Task task = new Task("read book");

        assertEquals(" ", task.getStatusIcon());
    }

    /** Tests the status icon for a completed task. */
    @Test
    void getStatusIcon_completedTask_xReturned() {
        Task task = new Task("read book");
        task.markAsDone();

        assertEquals("X", task.getStatusIcon());
    }

    /** Tests marking an incomplete task as complete. */
    @Test
    void markAsDone_incompleteTask_taskBecomesDone() {
        Task task = new Task("read book");

        task.markAsDone();

        assertTrue(task.isDone());
    }

    /** Tests that marking a completed task again leaves it completed. */
    @Test
    void markAsDone_completedTask_remainsDone() {
        Task task = new Task("read book");

        task.markAsDone();
        task.markAsDone();

        assertTrue(task.isDone());
        assertEquals("X", task.getStatusIcon());
    }

    /** Tests unmarking a completed task. */
    @Test
    void unmark_completedTask_taskBecomesIncomplete() {
        Task task = new Task("read book");

        task.markAsDone();
        task.unmark();

        assertFalse(task.isDone());
        assertEquals(" ", task.getStatusIcon());
    }

    /** Tests that unmarking an incomplete task leaves it incomplete. */
    @Test
    void unmark_incompleteTask_remainsIncomplete() {
        Task task = new Task("read book");

        task.unmark();

        assertFalse(task.isDone());
        assertEquals(" ", task.getStatusIcon());
    }

    /** Tests the display string for an incomplete task. */
    @Test
    void toString_incompleteTask_formattedDescriptionReturned() {
        Task task = new Task("read book");

        assertEquals("[ ] read book", task.toString());
    }

    /** Tests the display string for a completed task. */
    @Test
    void toString_completedTask_formattedDescriptionReturned() {
        Task task = new Task("read book");
        task.markAsDone();

        assertEquals("[X] read book", task.toString());
    }
}
