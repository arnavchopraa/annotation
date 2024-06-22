package org.example.backend.controllers;

import org.example.backend.exceptions.PDFException;
import org.example.backend.services.ExportService;
import org.example.backend.services.SubmissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminControllerTest {
    @Mock
    private SubmissionService submissionService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
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
