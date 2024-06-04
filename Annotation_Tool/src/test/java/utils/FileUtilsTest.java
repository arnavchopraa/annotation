package utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.example.TestUtils;
import org.example.utils.FileUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class FileUtilsTest {
    private final TestUtils testUtils = new TestUtils();

    @Test
    public void testConversion() {
        String text = "This is a PDF file";
        String content = "This is an annotation";
        try {
            PDDocument pdf = testUtils.generatePDF(text);
            testUtils.addAnnotation(pdf, content);
            File pdfFile = testUtils.convertPDFtoFile(pdf);
            FileInputStream fileInputStream = new FileInputStream(pdfFile);
            byte[] bytes = fileInputStream.readAllBytes();
            MultipartFile multipartFile = new MockMultipartFile(pdfFile.getName(), pdfFile.getName(), "text/plain", bytes);
            File result = FileUtils.convertToFile(multipartFile);
            FileInputStream resultStream = new FileInputStream(result);
            assertArrayEquals(resultStream.readAllBytes(), bytes);
            pdfFile.deleteOnExit();
        } catch (IOException e) {
            throw new RuntimeException("Test failed - Could not generate PDF");
        }
    }

    @Test
    public void testNullName() {
        MultipartFile multipartFile = new MockMultipartFile("test", null, "text/plain", new byte[0]);
        assertThrows(IllegalArgumentException.class, () -> FileUtils.convertToFile(multipartFile));
    }

}
