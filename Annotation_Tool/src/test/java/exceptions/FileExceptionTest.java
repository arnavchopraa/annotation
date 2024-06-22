package exceptions;

import static org.junit.jupiter.api.Assertions.*;

import org.example.backend.exceptions.FileException;
import org.junit.jupiter.api.Test;

class FileExceptionTest {

    @Test
    void testNoArgsConstructor() {
        FileException exception = new FileException();
        assertNull(exception.getMessage());
    }

    @Test
    void testConstructorWithMessage() {
        String errorMessage = "This is an error message";
        FileException exception = new FileException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testConstructorWithException() {
        Exception cause = new Exception("Cause of the exception");
        FileException exception = new FileException(cause);
        assertEquals(cause, exception.getCause());
    }
}