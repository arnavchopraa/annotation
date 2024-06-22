package controllers;

import org.example.TestUtils;
import org.example.backend.controllers.AdminController;
import org.example.backend.exceptions.ImportException;
import org.example.backend.exceptions.PDFException;
import org.example.backend.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminControllerTest {


    @Mock
    private SubmissionService submissionService;

    @Mock
    private ImportService importService;

    @Mock
    private AnnotationCodeService annotationCodeService;

    @InjectMocks
    private AdminController adminController;

    @Mock
    private ExportService exportService;

    private TestUtils testUtils = new TestUtils();
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        adminController = new AdminController(importService, submissionService, exportService);
    }

    @Test
    public void getFiles_Success() throws Exception {
        MockMultipartFile zipFile = new MockMultipartFile("zipFile", "test.zip", "application/zip", "test data".getBytes());
        MockMultipartFile csvFile = new MockMultipartFile("csvFile", "test.csv", "text/csv", "test data".getBytes());
        MockMultipartFile xlsxFile = new MockMultipartFile("xlsxFile", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "test data".getBytes());
        doNothing().when(importService).importData(any(), any(), any());
        ResponseEntity<String> response = adminController.getFiles(zipFile, csvFile, xlsxFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Files imported successfully", response.getBody());
    }

    @Test
    public void getFiles_Failure_ImportException() throws Exception {
        MockMultipartFile zipFile = new MockMultipartFile("zipFile", "test.zip", "application/zip", "test data".getBytes());
        MockMultipartFile csvFile = new MockMultipartFile("csvFile", "test.csv", "text/csv", "test data".getBytes());
        MockMultipartFile xlsxFile = new MockMultipartFile("xlsxFile", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "test data".getBytes());

        doThrow(ImportException.class).when(importService).importData(any(), any(), any());

        ResponseEntity<String> response = adminController.getFiles(zipFile, csvFile, xlsxFile);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void deleteAllSubmissions_Success() {
        ResponseEntity<String> response = adminController.deleteAllSubmissions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Submission database has been cleared!", response.getBody());
    }

    @Test
    public void deleteAllSubmissions_Failure() {
        doThrow(RuntimeException.class).when(submissionService).deleteAll();

        ResponseEntity<String> response = adminController.deleteAllSubmissions();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Something went wrong while trying to delete all submissions", response.getBody());
    }

    @Test
    void testDownloadAllParsed_Success() throws SQLException, PDFException, IOException {
        // Mocking behavior of exportService
        File mockZipFile = testUtils.convertPDFtoFile(testUtils.generatePDF("This is a PDF file"));
        when(exportService.getAllSubmissionsParsed()).thenReturn(mockZipFile);

        // Execute the method
        ResponseEntity<byte[]> response = adminController.downloadAllParsed();

        // Verify the response
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    void testDownloadAllParsed_IOException() throws SQLException, PDFException, IOException {
        // Mocking behavior of exportService
        Mockito.when(exportService.getAllSubmissionsParsed()).thenThrow(IOException.class);

        // Execute the method
        ResponseEntity<byte[]> response = adminController.downloadAllParsed();

        // Verify the response
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testDownloadAllParsed_SQLException() throws SQLException, PDFException, IOException {
        // Mocking behavior of exportService
        Mockito.when(exportService.getAllSubmissionsParsed()).thenThrow(SQLException.class);

        // Execute the method
        ResponseEntity<byte[]> response = adminController.downloadAllParsed();

        // Verify the response
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testDownloadAllParsed_PDFException() throws SQLException, PDFException, IOException {
        // Mocking behavior of exportService
        Mockito.when(exportService.getAllSubmissionsParsed()).thenThrow(PDFException.class);

        // Execute the method
        ResponseEntity<byte[]> response = adminController.downloadAllParsed();

        // Verify the response
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
