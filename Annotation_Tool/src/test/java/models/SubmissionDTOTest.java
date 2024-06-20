package models;

import org.example.backend.models.SubmissionDTO;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class SubmissionDTOTest {

    @Test
    public void testGettersAndSetters() {
        String id = "test@example.com";
        String fileSubmission = "base64File";
        String groupName = "groupName";
        String fileName = "test.txt";
        String now = String.valueOf(System.currentTimeMillis());

        SubmissionDTO submissionDTO = new SubmissionDTO();
        submissionDTO.setId(id);
        submissionDTO.setFileSubmission(fileSubmission);
        submissionDTO.setGroupName(groupName);
        submissionDTO.setFileName(fileName);
        submissionDTO.setLastSubmitted(now);
        submissionDTO.setLastEdited(now);
        submissionDTO.setLocked(false);

        assertEquals(id, submissionDTO.getId());
        assertEquals(fileSubmission, submissionDTO.getFileSubmission());
        assertEquals(groupName, submissionDTO.getGroupName());
        assertEquals(fileName, submissionDTO.getFileName());
        assertEquals(now, submissionDTO.getLastSubmitted());
        assertEquals(now, submissionDTO.getLastEdited());
        assertFalse(submissionDTO.isLocked());
    }

    @Test
    public void testConstructorAndToString() {
        String id = "test@example.com";
        String fileSubmission = "base64File";
        String groupName = "groupName";
        String fileName = "test.txt";
        String now = String.valueOf(System.currentTimeMillis());

        SubmissionDTO submissionDTO = new SubmissionDTO(id, fileSubmission, groupName, new HashSet<>(), fileName, now, now, false, false);

        assertEquals(id, submissionDTO.getId());
        assertEquals(fileSubmission, submissionDTO.getFileSubmission());
        assertEquals(groupName, submissionDTO.getGroupName());
        assertEquals(fileName, submissionDTO.getFileName());
        assertEquals(now, submissionDTO.getLastSubmitted());
        assertEquals(now, submissionDTO.getLastEdited());
        assertNotNull(submissionDTO.toString());
    }
}
