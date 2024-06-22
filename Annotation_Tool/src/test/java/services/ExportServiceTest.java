package services;

import org.example.backend.exceptions.PDFException;
import org.example.backend.models.SubmissionDB;
import org.example.backend.services.ExportService;
import org.example.backend.services.ParsingService;
import org.example.backend.services.SubmissionService;
import org.example.backend.utils.PairUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExportServiceTest {

    @Mock
    private SubmissionService submissionService;

    private ExportService exportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        // Initialize the ExportService with mocked SubmissionService
        exportService = new ExportService(submissionService, null); // pass null for AnnotationCodeService for this test
    }

    @Test
    public void testGetSubmission_Success() throws IOException, SQLException {
        // Mock data
        String submissionId = "123";
        byte[] submissionContent = "Sample PDF content".getBytes();
        Blob mockBlob = mock(Blob.class);
        when(mockBlob.getBytes(1, (int) mockBlob.length())).thenReturn(submissionContent);

        SubmissionDB mockSubmission = new SubmissionDB();
        mockSubmission.setId(submissionId);
        mockSubmission.setFileName("sample.pdf");
        mockSubmission.setFileSubmission(mockBlob);

        // Mock submissionService behavior
        when(submissionService.getSubmission(submissionId)).thenReturn(mockSubmission);

        // Call the method under test
        File result = exportService.getSubmission(submissionId);

        // Assertions
        assertNotNull(result);
        assertTrue(result.exists());
        assertEquals("123 - sample.pdf", result.getName());

        // Clean up
        result.delete();
    }

    @Test
    public void testGetSubmission_NullBlob() throws IOException, SQLException {
        // Mock data
        String submissionId = "123";
        SubmissionDB mockSubmission = new SubmissionDB();
        mockSubmission.setId(submissionId);
        mockSubmission.setFileName("sample.pdf");
        mockSubmission.setFileSubmission(null);

        // Mock submissionService behavior
        when(submissionService.getSubmission(submissionId)).thenReturn(mockSubmission);

        // Call the method under test
        File result = exportService.getSubmission(submissionId);

        // Assertions
        assertNull(result);
    }

    @Test
    public void testGetAllSubmissionsByCoordinator_Success() throws IOException, SQLException {
        // Mock data
        String coordinator = "Coordinator1";
        List<SubmissionDB> mockSubmissions = new ArrayList<>();
        mockSubmissions.add(createMockSubmissionOne("123", "submission1.pdf"));
        mockSubmissions.add(createMockSubmissionOne("456", "submission2.pdf"));

        // Mock submissionService behavior
        when(submissionService.getCoordinatorsSubmissions(coordinator)).thenReturn(mockSubmissions);

        // Call the method under test
        File result = exportService.getAllSubmissionsByCoordinator(coordinator);

        // Assertions
        assertNotNull(result);
        assertTrue(result.exists());
        assertTrue(result.getName().endsWith(".zip"));

        // Clean up
        result.delete();
    }

    @Test
    public void testGetAllSubmissionsParsed_Success() throws IOException, SQLException, PDFException {
        // Mock data
        List<SubmissionDB> mockSubmissions = new ArrayList<>();
        mockSubmissions.add(createMockSubmissionTwo("123", "submission1.pdf"));
        mockSubmissions.add(createMockSubmissionTwo("456", "submission2.pdf"));

        // Mock submissionService behavior
        when(submissionService.getSubmissions()).thenReturn(mockSubmissions);

        // Mock parsingService behavior
        PairUtils mockPairUtils = new PairUtils("Parsed text", "Annotations" , "");
        ParsingService mockParsingService = mock(ParsingService.class);
        when(mockParsingService.parsePDF(any(File.class))).thenReturn(mockPairUtils);

        // Set the mock parsing service in ExportService
        exportService = new ExportService(submissionService, null);

        // Call the method under test
        File result = exportService.getAllSubmissionsParsed();

        // Assertions
        assertNotNull(result);
        assertTrue(result.exists());
        assertTrue(result.getName().endsWith(".zip"));

        // Clean up
        result.delete();
    }

    private SubmissionDB createMockSubmissionOne(String id, String fileName) throws SQLException {
        SubmissionDB submission = new SubmissionDB();
        submission.setId(id);
        submission.setFileName(fileName);
        submission.setFileSubmission(new SerialBlob(new byte[]{0}));
        // Mock Blob is not set here as it depends on specific tests
        return submission;
    }

    private SubmissionDB createMockSubmissionTwo(String id, String fileName) throws SQLException {
        SubmissionDB submission = new SubmissionDB();
        submission.setId(id);
        submission.setFileName(fileName);
        //submission.setFileSubmission(new SerialBlob(new byte[]{0}));
        // Mock Blob is not set here as it depends on specific tests
        return submission;
    }
}
