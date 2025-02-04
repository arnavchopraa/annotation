package controllers;

import org.example.backend.controllers.SubmissionController;
import org.example.backend.models.SubmissionDB;
import org.example.backend.models.SubmissionDTO;
import org.example.backend.models.User;
import org.example.backend.services.AccountService;
import org.example.backend.services.SubmissionService;
import org.example.backend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import javax.sql.rowset.serial.SerialBlob;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubmissionControllerTest {

    @Mock
    private SubmissionService service;
    @Mock
    private UserService userService;
    @Mock
    private AccountService accountService;

    @InjectMocks
    private SubmissionController controller;

    private SubmissionDB submissionDB;
    private SubmissionDTO submissionDTO;
    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Blob blob = new SerialBlob("dummy content".getBytes());
        submissionDB = new SubmissionDB("test@example.com", blob, "groupName", new HashSet<>(), "file.pdf", String.valueOf(System.currentTimeMillis()), String.valueOf(System.currentTimeMillis()), false, false);

        String base64File = Base64.getEncoder().encodeToString("dummy content".getBytes());
        submissionDTO = new SubmissionDTO("test@example.com", base64File, "groupName", new HashSet<>(), "file.pdf", String.valueOf(System.currentTimeMillis()), String.valueOf(System.currentTimeMillis()), false, false);

        String file = "abc";
        multipartFile = new MockMultipartFile("name", "name", "text/plain", file.getBytes());
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

    @Test
    void testGetLockGood() {
        SubmissionDB sub = new SubmissionDB();
        sub.setLocked(true);

        when(service.getSubmission("1")).thenReturn(sub);

        ResponseEntity<Boolean> response = controller.getLock("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void testGetLockNotGood() {

        when(service.getSubmission("1")).thenReturn(null);

        ResponseEntity<Boolean> response = controller.getLock("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testLockGood() {
        SubmissionDB sub = new SubmissionDB();
        sub.setLocked(true);

        when(service.setIsLocked("1", true)).thenReturn(sub);

        ResponseEntity<Boolean> response = controller.lockFile("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void testLockBad() {
        when(service.setIsLocked("1", true)).thenReturn(null);

        ResponseEntity<Boolean> response = controller.lockFile("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUnlockGood() {
        SubmissionDB sub = new SubmissionDB();
        sub.setLocked(true);

        when(service.setIsLocked("1", false)).thenReturn(sub);

        ResponseEntity<Boolean> response = controller.unlockFile("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void testUnlockBad() {
        when(service.setIsLocked("1", false)).thenReturn(null);

        ResponseEntity<Boolean> response = controller.unlockFile("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testSubmissionControllerNotFound() {
        when(userService.getUser("1")).thenReturn(null);

        ResponseEntity<String> response = controller.addSubmission(multipartFile, "test@test.com", "group", "1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testSubmissionControllerConflict() {
        when(userService.getUser("1")).thenReturn(new User());
        when(userService.getUser("test@test.com")).thenReturn(new User());
        when(service.getSubmission(any())).thenReturn(null);

        ResponseEntity<String> response = controller.addSubmission(multipartFile, "test@test.com", "group", "1");

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void testSubmissionControllerGood() {
        when(userService.getUser("1")).thenReturn(new User());
        when(userService.getUser("test@test.com")).thenReturn(new User());
        when(service.addSubmission(any())).thenReturn(new SubmissionDB());

        ResponseEntity<String> response = controller.addSubmission(multipartFile, "test@test.com", "group", "1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSubmissionControllerCreateAccount() {
        when(userService.getUser("1")).thenReturn(new User());
        when(userService.getUser("test@test.com")).thenReturn(null);
        when(service.addSubmission(any())).thenReturn(new SubmissionDB());

        ResponseEntity<String> response = controller.addSubmission(multipartFile, "test@test.com", "group", "1");

        verify(accountService).createStudentAccounts(any());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
