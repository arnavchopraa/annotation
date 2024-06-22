package controllers;

import org.example.backend.controllers.AdminController;
import org.example.backend.exceptions.ImportException;
import org.example.backend.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        adminController = new AdminController(importService, submissionService, annotationCodeService);
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
    void testDownloadAllParsed_Success() throws IOException, SQLException, PDFException {
        // Mocking behavior of exportService
        //File mockZipFile = createMockZipFile();
        when(submissionService.getSubmissions()).thenReturn(new ArrayList<>());

        // Execute the method
        ResponseEntity<byte[]> response = adminController.downloadAllParsed();

        // Verify the response
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    void testDownloadAllParsed_IOException() throws IOException, SQLException, PDFException {
        // Mocking behavior of exportService
        when(submissionService.getSubmissions()).thenThrow(IOException.class);

        // Execute the method
        ResponseEntity<byte[]> response = adminController.downloadAllParsed();

        // Verify the response
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testDownloadAllParsed_SQLException() throws IOException, SQLException, PDFException {
        // Mocking behavior of exportService
        when(submissionService.getSubmissions()).thenThrow(SQLException.class);

        // Execute the method
        ResponseEntity<byte[]> response = adminController.downloadAllParsed();

        // Verify the response
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testDownloadAllParsed_PDFException() throws IOException, SQLException, PDFException {
        // Mocking behavior of exportService
        when(submissionService.getSubmissions()).thenThrow(PDFException.class);

        // Execute the method
        ResponseEntity<byte[]> response = adminController.downloadAllParsed();

        // Verify the response
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
