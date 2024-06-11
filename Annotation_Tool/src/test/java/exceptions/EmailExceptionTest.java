package exceptions;

import static org.junit.jupiter.api.Assertions.*;

import org.example.backend.exceptions.EmailException;
import org.junit.jupiter.api.Test;

class EmailExceptionTest {

    @Test
    void testNoArgsConstructor() {
        EmailException exception = new EmailException();
        assertNull(exception.getMessage());
    }

    @Test
    void testConstructorWithMessage() {
        String errorMessage = "This is an error message";
        EmailException exception = new EmailException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testConstructorWithException() {
        Exception cause = new Exception("Cause of the exception");
        EmailException exception = new EmailException(cause);
        assertEquals(cause, exception.getCause());
    }
}