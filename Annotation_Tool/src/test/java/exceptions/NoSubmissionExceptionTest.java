package exceptions;

import static org.junit.jupiter.api.Assertions.*;

import org.example.backend.exceptions.NoSubmissionException;
import org.junit.jupiter.api.Test;

class NoSubmissionExceptionTest {

    @Test
    void testNoArgsConstructor() {
        NoSubmissionException exception = new NoSubmissionException();
        assertNull(exception.getMessage());
    }

    @Test
    void testConstructorWithMessage() {
        String errorMessage = "This is an error message";
        NoSubmissionException exception = new NoSubmissionException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testConstructorWithException() {
        Exception cause = new Exception("Cause of the exception");
        NoSubmissionException exception = new NoSubmissionException(cause);
        assertEquals(cause, exception.getCause());
    }
}