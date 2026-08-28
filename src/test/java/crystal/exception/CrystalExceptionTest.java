package crystal.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests Crystal's user-facing exception formatting.
 */
public class CrystalExceptionTest {
    @Test
    public void getUserMessage_messageProvided_addsCrystalErrorPrefix() {
        CrystalException exception = new CrystalException("That task number does not exist!");

        assertEquals("That task number does not exist!", exception.getMessage());
        assertEquals("Crystal: Oopsies!!! That task number does not exist!",
                exception.getUserMessage());
    }
}
