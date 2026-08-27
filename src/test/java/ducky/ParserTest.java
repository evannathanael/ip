package ducky;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the command parsing behavior of {@link Parser}.
 */
class ParserTest {
    private final Parser parser = new Parser();

    @Test
    void parse_simpleCommand_correctCommandTypeReturned() throws DuckyException {
        assertEquals(Parser.CommandType.BYE, parser.parse("bye").getType());
        assertEquals(Parser.CommandType.LIST, parser.parse("list").getType());
    }

    @Test
    void parse_markCommand_oneBasedNumberConvertedToZeroBasedIndex() throws DuckyException {
        Parser.ParsedCommand parsed = parser.parse("mark 3");

        assertEquals(Parser.CommandType.MARK, parsed.getType());
        assertEquals(2, parsed.getTaskIndex());
    }

    @Test
    void parse_todoCommand_trimmedDescriptionAndTodoReturned() throws DuckyException {
        Parser.ParsedCommand parsed = parser.parse("todo   buy milk  ");

        assertEquals(Parser.CommandType.ADD, parsed.getType());
        ToDo task = assertInstanceOf(ToDo.class, parsed.getTask());
        assertEquals("buy milk", task.getDescription());
    }

    @Test
    void parse_deadlineCommand_dateAndDescriptionParsed() throws DuckyException {
        Parser.ParsedCommand parsed = parser.parse("deadline submit report /by 2026-09-01");

        Deadline task = assertInstanceOf(Deadline.class, parsed.getTask());
        assertEquals("submit report", task.getDescription());
        assertEquals(LocalDate.of(2026, 9, 1), task.getBy());
    }

    @Test
    void parse_eventCommand_startAndEndParsed() throws DuckyException {
        Parser.ParsedCommand parsed = parser.parse(
                "event team meeting /from 2026-09-01 0900 /to 2026-09-01 1030");

        Event task = assertInstanceOf(Event.class, parsed.getTask());
        assertEquals("team meeting", task.getDescription());
        assertEquals(LocalDateTime.of(2026, 9, 1, 9, 0), task.getStart());
        assertEquals(LocalDateTime.of(2026, 9, 1, 10, 30), task.getEnd());
    }

    @Test
    void parse_invalidCommand_exceptionThrown() {
        assertThrows(DuckyException.class, () -> parser.parse("unknown command"));
    }

    @Test
    void parse_invalidTaskNumbers_exceptionThrown() {
        assertThrows(DuckyException.class, () -> parser.parse("mark 0"));
        assertThrows(DuckyException.class, () -> parser.parse("delete abc"));
    }

    @Test
    void parse_incompleteStructuredCommand_exceptionThrown() {
        assertThrows(DuckyException.class, () -> parser.parse("todo"));
        assertThrows(DuckyException.class, () -> parser.parse("deadline submit report"));
        assertThrows(DuckyException.class, () -> parser.parse("event meeting /from 2026-09-01 0900"));
    }
}
