package utils;

import org.example.backend.utils.FileUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileUtilsTest {

    @Test
    void testConvertToFile() throws IOException {
        // Mock MultipartFile
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("testfile.txt");
        when(multipartFile.getBytes()).thenReturn("Test file content".getBytes());

        // Convert file
        File file = FileUtils.convertToFile(multipartFile);

        // Verify
        assertNotNull(file);
        assertEquals("testfile.txt", file.getName());
        assertTrue(file.length() > 0);

        // Clean up
        assertTrue(file.delete());
    }

    @Test
    void testConvertToFileWithNullFilename() {
        // Mock MultipartFile
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn(null);

        // Test and verify
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            FileUtils.convertToFile(multipartFile);
        });
        assertEquals("Filename cannot be null", exception.getMessage());
    }

    @Test
    void testConvertToFileWithIOException() throws IOException {
        // Mock MultipartFile
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("testfile.txt");
        when(multipartFile.getBytes()).thenThrow(new IOException("IO Exception"));

        // Test and verify
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            FileUtils.convertToFile(multipartFile);
        });
        assertEquals("Not a file", exception.getMessage());
    }
}
