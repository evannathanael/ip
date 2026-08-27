package ducky;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link Task}.
 */
class TaskTest {
    @Test
    void constructor_descriptionAndIncompleteStatusSet() {
        Task task = new Task("read book");

        assertEquals("read book", task.getDescription());
        assertFalse(task.isDone());
    }

    @Test
    void getDescription_taskDescriptionReturned() {
        Task task = new Task("read book");

        assertEquals("read book", task.getDescription());
    }

    @Test
    void isDone_newTask_falseReturned() {
        Task task = new Task("read book");

        assertFalse(task.isDone());
    }

    @Test
    void getStatusIcon_newTask_spaceReturned() {
        Task task = new Task("read book");

        assertEquals(" ", task.getStatusIcon());
    }

    @Test
    void getStatusIcon_completedTask_xReturned() {
        Task task = new Task("read book");
        task.markAsDone();

        assertEquals("X", task.getStatusIcon());
    }

    @Test
    void markAsDone_incompleteTask_taskBecomesDone() {
        Task task = new Task("read book");

        task.markAsDone();

        assertTrue(task.isDone());
    }

    @Test
    void markAsDone_completedTask_remainsDone() {
        Task task = new Task("read book");

        task.markAsDone();
        task.markAsDone();

        assertTrue(task.isDone());
        assertEquals("X", task.getStatusIcon());
    }

    @Test
    void unmark_completedTask_taskBecomesIncomplete() {
        Task task = new Task("read book");

        task.markAsDone();
        task.unmark();

        assertFalse(task.isDone());
        assertEquals(" ", task.getStatusIcon());
    }

    @Test
    void unmark_incompleteTask_remainsIncomplete() {
        Task task = new Task("read book");

        task.unmark();

        assertFalse(task.isDone());
        assertEquals(" ", task.getStatusIcon());
    }

    @Test
    void toString_incompleteTask_formattedDescriptionReturned() {
        Task task = new Task("read book");

        assertEquals("[ ] read book", task.toString());
    }

    @Test
    void toString_completedTask_formattedDescriptionReturned() {
        Task task = new Task("read book");
        task.markAsDone();

        assertEquals("[X] read book", task.toString());
    }
}
