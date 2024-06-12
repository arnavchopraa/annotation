package controllers;

import org.example.backend.controllers.SubmissionController;
import org.example.backend.models.SubmissionDB;
import org.example.backend.models.SubmissionDTO;
import org.example.backend.services.SubmissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Blob;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.sql.rowset.serial.SerialBlob;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubmissionControllerTest {

    @Mock
    private SubmissionService service;

    @InjectMocks
    private SubmissionController controller;

    private SubmissionDB submissionDB;
    private SubmissionDTO submissionDTO;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Blob blob = new SerialBlob("dummy content".getBytes());
        submissionDB = new SubmissionDB("test@example.com", blob, "coordinator@example.com", "file.pdf", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), false);

        String base64File = Base64.getEncoder().encodeToString("dummy content".getBytes());
        submissionDTO = new SubmissionDTO("test@example.com", base64File, "coordinator@example.com", "file.pdf", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), false);
    }

    @Test
    void testGetSubmissions() {
        List<SubmissionDB> submissionDBList = new ArrayList<>();
        submissionDBList.add(submissionDB);
        when(service.getSubmissions()).thenReturn(submissionDBList);

        ResponseEntity<List<SubmissionDTO>> response = controller.getSubmissions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(service, times(1)).getSubmissions();
    }

    @Test
    void testGetSubmission() {
        when(service.getSubmission("test@example.com")).thenReturn(submissionDB);

        ResponseEntity<SubmissionDTO> response = controller.getSubmission("test@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(submissionDTO.getId(), response.getBody().getId());
        verify(service, times(1)).getSubmission("test@example.com");
    }

    @Test
    void testGetSubmission_NotFound() {
        when(service.getSubmission("test@example.com")).thenReturn(null);

        ResponseEntity<SubmissionDTO> response = controller.getSubmission("test@example.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(service, times(1)).getSubmission("test@example.com");
    }

    @Test
    void testGetCoordinatorsSubmission() {
        List<SubmissionDB> submissionDBList = new ArrayList<>();
        submissionDBList.add(submissionDB);
        when(service.getCoordinatorsSubmissions("coordinator@example.com")).thenReturn(submissionDBList);

        ResponseEntity<List<SubmissionDTO>> response = controller.getCoordinatorsSubmission("coordinator@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(service, times(1)).getCoordinatorsSubmissions("coordinator@example.com");
    }

    @Test
    void testAddSubmission() {
        when(service.addSubmission(any(SubmissionDB.class))).thenReturn(submissionDB);

        ResponseEntity<SubmissionDB> response = controller.addSubmission(submissionDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(submissionDB.getId(), response.getBody().getId());
        verify(service, times(1)).addSubmission(any(SubmissionDB.class));
    }

    @Test
    void testAddSubmission_BadRequest() {
        when(service.addSubmission(any(SubmissionDB.class))).thenReturn(null);

        ResponseEntity<SubmissionDB> response = controller.addSubmission(submissionDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(service, times(1)).addSubmission(any(SubmissionDB.class));
    }

    @Test
    void testUpdateSubmission() {
        when(service.updateSubmission(any(SubmissionDB.class))).thenReturn(submissionDB);

        ResponseEntity<SubmissionDTO> response = controller.updateSubmission("test@example.com", submissionDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(submissionDTO.getId(), response.getBody().getId());
        verify(service, times(1)).updateSubmission(any(SubmissionDB.class));
    }

    @Test
    void testUpdateSubmission_BadRequest() {
        when(service.updateSubmission(any(SubmissionDB.class))).thenReturn(null);

        ResponseEntity<SubmissionDTO> response = controller.updateSubmission("test@example.com", submissionDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(service, times(1)).updateSubmission(any(SubmissionDB.class));
    }

    @Test
    void testDeleteSubmission() {
        when(service.deleteSubmission("test@example.com")).thenReturn(submissionDB);

        ResponseEntity<SubmissionDB> response = controller.deleteSubmission("test@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(submissionDB.getId(), response.getBody().getId());
        verify(service, times(1)).deleteSubmission("test@example.com");
    }

    @Test
    void testDeleteSubmission_NotFound() {
        when(service.deleteSubmission("test@example.com")).thenReturn(null);

        ResponseEntity<SubmissionDB> response = controller.deleteSubmission("test@example.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(service, times(1)).deleteSubmission("test@example.com");
    }

    @Test
    void testSearchSubmission() {
        List<SubmissionDB> submissionDBList = new ArrayList<>();
        submissionDBList.add(submissionDB);
        when(service.searchSubmissions("test", "coordinator@example.com")).thenReturn(submissionDBList);

        ResponseEntity<List<SubmissionDTO>> response = controller.searchSubmission("test", "coordinator@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(service, times(1)).searchSubmissions("test", "coordinator@example.com");
    }
}
