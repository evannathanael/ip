package ducky;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests user-interface message formatting.
 */
class UiTest {
    private final Ui ui = new Ui();

    @Test
    void showWelcome_containsDuckyPersonality() {
        String message = ui.showWelcome();

        assertTrue(message.contains("Ducky 🐥"));
        assertTrue(message.contains("ready to help"));
    }

    @Test
    void showExitMessage_containsDuckThemedFarewell() {
        assertTrue(ui.showExitMessage().contains("See you in the pond soon"));
    }

    @Test
    void showError_containsErrorPrefix() {
        assertTrue(ui.showError("invalid command").contains("QUACK! invalid command"));
    }
}
