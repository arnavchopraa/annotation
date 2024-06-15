package models;

import org.example.backend.models.SubmissionDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubmissionDTOTest {

    @Test
    public void testGettersAndSetters() {
        String id = "test@example.com";
        String fileSubmission = "base64File";
        String assignedCoordinator = "coordinator1";
        String fileName = "test.txt";
        String now = String.valueOf(System.currentTimeMillis());

        SubmissionDTO submissionDTO = new SubmissionDTO();
        submissionDTO.setId(id);
        submissionDTO.setFileSubmission(fileSubmission);
        submissionDTO.setAssignedCoordinator(assignedCoordinator);
        submissionDTO.setFileName(fileName);
        submissionDTO.setLastSubmitted(now);
        submissionDTO.setLastEdited(now);

        assertEquals(id, submissionDTO.getId());
        assertEquals(fileSubmission, submissionDTO.getFileSubmission());
        assertEquals(assignedCoordinator, submissionDTO.getAssignedCoordinator());
        assertEquals(fileName, submissionDTO.getFileName());
        assertEquals(now, submissionDTO.getLastSubmitted());
        assertEquals(now, submissionDTO.getLastEdited());
    }

    @Test
    public void testConstructorAndToString() {
        String id = "test@example.com";
        String fileSubmission = "base64File";
        String assignedCoordinator = "coordinator1";
        String fileName = "test.txt";
        String now = String.valueOf(System.currentTimeMillis());

        SubmissionDTO submissionDTO = new SubmissionDTO(id, fileSubmission, assignedCoordinator, fileName, now, now, false);

        assertEquals(id, submissionDTO.getId());
        assertEquals(fileSubmission, submissionDTO.getFileSubmission());
        assertEquals(assignedCoordinator, submissionDTO.getAssignedCoordinator());
        assertEquals(fileName, submissionDTO.getFileName());
        assertEquals(now, submissionDTO.getLastSubmitted());
        assertEquals(now, submissionDTO.getLastEdited());
        assertNotNull(submissionDTO.toString());
    }
}
