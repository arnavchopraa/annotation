package exceptions;

import static org.junit.jupiter.api.Assertions.*;

import org.example.backend.exceptions.ImportException;
import org.junit.jupiter.api.Test;

class ImportExceptionTest {

    @Test
    void testNoArgsConstructor() {
        ImportException exception = new ImportException();
        assertNull(exception.getMessage());
    }

    @Test
    void testConstructorWithMessage() {
        String errorMessage = "This is an error message";
        ImportException exception = new ImportException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testConstructorWithException() {
        Exception cause = new Exception("Cause of the exception");
        ImportException exception = new ImportException(cause);
        assertEquals(cause, exception.getCause());
    }
}