package exceptions;

import static org.junit.jupiter.api.Assertions.*;

import org.example.backend.exceptions.PDFException;
import org.junit.jupiter.api.Test;

class PDFExceptionTest {

    @Test
    void testNoArgsConstructor() {
        PDFException exception = new PDFException();
        assertNull(exception.getMessage());
    }

    @Test
    void testConstructorWithMessage() {
        String errorMessage = "This is an error message";
        PDFException exception = new PDFException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testConstructorWithException() {
        Exception cause = new Exception("Cause of the exception");
        PDFException exception = new PDFException(cause);
        assertEquals(cause, exception.getCause());
    }
}