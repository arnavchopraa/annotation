package services;

import org.example.database.SubmissionRepository;
import org.example.backend.models.SubmissionDB;
import org.example.backend.services.SubmissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SubmissionServiceTest {

    @Mock
    private SubmissionRepository repo;

    @InjectMocks
    private SubmissionService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetSubmission() {
        SubmissionDB submission = new SubmissionDB();
        submission.setId("1");
        when(repo.findById("1")).thenReturn(Optional.of(submission));

        SubmissionDB result = service.getSubmission("1");
        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    public void testGetSubmission_NotFound() {
        when(repo.findById("1")).thenReturn(Optional.empty());

        SubmissionDB result = service.getSubmission("1");
        assertNull(result);
    }

    @Test
    public void testGetSubmissions() {
        SubmissionDB submission1 = new SubmissionDB();
        SubmissionDB submission2 = new SubmissionDB();
        when(repo.findAll()).thenReturn(Arrays.asList(submission1, submission2));

        List<SubmissionDB> results = service.getSubmissions();
        assertEquals(2, results.size());
    }

    @Test
    public void testGetCoordinatorsSubmissions() {
        SubmissionDB submission1 = new SubmissionDB();
        SubmissionDB submission2 = new SubmissionDB();
        when(repo.findByAssignedCoordinator("coordinator1")).thenReturn(Arrays.asList(submission1, submission2));

        List<SubmissionDB> results = service.getCoordinatorsSubmissions("coordinator1");
        assertEquals(2, results.size());
    }

    @Test
    public void testAddSubmission() {
        SubmissionDB submission = new SubmissionDB();
        submission.setId("1");
        when(repo.save(any(SubmissionDB.class))).thenReturn(submission);
        when(repo.findById("1")).thenReturn(Optional.empty());

        SubmissionDB result = service.addSubmission(submission);
        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    public void testAddSubmission_NullInput() {
        SubmissionDB result = service.addSubmission(null);
        assertNull(result);
    }

    @Test
    public void testAddSubmission_AlreadyExists() {
        SubmissionDB submission = new SubmissionDB();
        submission.setId("1");
        when(repo.findById("1")).thenReturn(Optional.of(submission));

        SubmissionDB result = service.addSubmission(submission);
        assertNull(result);
    }

    @Test
    public void testDeleteSubmission() {
        SubmissionDB submission = new SubmissionDB();
        submission.setId("1");
        when(repo.findById("1")).thenReturn(Optional.of(submission));

        SubmissionDB result = service.deleteSubmission("1");
        assertNotNull(result);
        assertEquals("1", result.getId());
        verify(repo, times(1)).deleteById("1");
    }

    @Test
    public void testDeleteSubmission_NotFound() {
        when(repo.findById("1")).thenReturn(Optional.empty());

        SubmissionDB result = service.deleteSubmission("1");
        assertNull(result);
        verify(repo, never()).deleteById("1");
    }

    @Test
    public void testUpdateSubmission() {
        SubmissionDB submission = new SubmissionDB();
        submission.setId("1");
        when(repo.save(any(SubmissionDB.class))).thenReturn(submission);

        SubmissionDB result = service.updateSubmission(submission);
        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    public void testUpdateSubmission_NullInput() {
        SubmissionDB result = service.updateSubmission(null);
        assertNull(result);
    }
}
