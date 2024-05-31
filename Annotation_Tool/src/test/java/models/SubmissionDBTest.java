package models;

import org.example.models.SubmissionDB;
import org.example.models.SubmissionDTO;
import org.junit.jupiter.api.Test;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class SubmissionDBTest {

    @Test
    void testConvertToBinaryWithNullBlob() {
        SubmissionDB submissionDB = new SubmissionDB();
        submissionDB.setId("test@example.com");
        submissionDB.setFileSubmission(null);
        submissionDB.setAssignedCoordinator("coordinator");
        submissionDB.setFileName("testFile.txt");
        submissionDB.setLastSubmitted(new Date(System.currentTimeMillis()));

        SubmissionDTO dto = SubmissionDB.convertToBinary(submissionDB);

        assertEquals(submissionDB.getId(), dto.getId());
        assertNull(dto.getFileSubmission());
        assertEquals(submissionDB.getAssignedCoordinator(), dto.getAssignedCoordinator());
        assertEquals(submissionDB.getFileName(), dto.getFileName());
        assertEquals(submissionDB.getLastSubmitted(), dto.getLastSubmitted());
    }

    @Test
    void testConvertToBinaryWithBlob() throws Exception {
        String content = "This is a test file content";
        Blob blob = new SerialBlob(content.getBytes());
        SubmissionDB submissionDB = new SubmissionDB();
        submissionDB.setId("test@example.com");
        submissionDB.setFileSubmission(blob);
        submissionDB.setAssignedCoordinator("coordinator");
        submissionDB.setFileName("testFile.txt");
        submissionDB.setLastSubmitted(new Date(System.currentTimeMillis()));

        SubmissionDTO dto = SubmissionDB.convertToBinary(submissionDB);

        assertEquals(submissionDB.getId(), dto.getId());
        String base64File = Base64.getEncoder().encodeToString(content.getBytes());
        assertEquals(base64File, dto.getFileSubmission());
        assertEquals(submissionDB.getAssignedCoordinator(), dto.getAssignedCoordinator());
        assertEquals(submissionDB.getFileName(), dto.getFileName());
        assertEquals(submissionDB.getLastSubmitted(), dto.getLastSubmitted());
    }

    @Test
    void testConvertToBlob() throws SQLException {
        String content = "This is a test file content";
        String base64File = Base64.getEncoder().encodeToString(content.getBytes());
        SubmissionDTO submissionDTO = new SubmissionDTO("test@example.com", base64File, "coordinator", "testFile.txt", new Date(System.currentTimeMillis()));

        SubmissionDB submissionDB = SubmissionDB.convertToBlob(submissionDTO);

        assertEquals(submissionDTO.getId(), submissionDB.getId());
        byte[] blobBytes = submissionDB.getFileSubmission().getBytes(1, (int) submissionDB.getFileSubmission().length());
        assertEquals(content, new String(blobBytes));
        assertEquals(submissionDTO.getAssignedCoordinator(), submissionDB.getAssignedCoordinator());
        assertEquals(submissionDTO.getFileName(), submissionDB.getFileName());
        assertEquals(submissionDTO.getLastSubmitted(), submissionDB.getLastSubmitted());
    }
}
