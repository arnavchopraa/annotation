package models;

import org.example.backend.models.SubmissionDB;
import org.example.backend.models.SubmissionDTO;
import org.junit.jupiter.api.Test;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class SubmissionDBTest {

    @Test
    void testConvertToBinaryWithNullBlob() {
        SubmissionDB submissionDB = new SubmissionDB();
        submissionDB.setId("test@example.com");
        submissionDB.setFileSubmission(null);
        submissionDB.setAssignedCoordinators(new HashSet<>());
        submissionDB.setGroupName("groupName");
        submissionDB.setFileName("testFile.txt");
        submissionDB.setLastSubmitted(String.valueOf(System.currentTimeMillis()));

        SubmissionDTO dto = SubmissionDB.convertToBinary(submissionDB);

        assertEquals(submissionDB.getId(), dto.getId());
        assertNull(dto.getFileSubmission());
        assertEquals(submissionDB.getGroupName(), dto.getGroupName());
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
        submissionDB.setGroupName("name");
        submissionDB.setFileName("testFile.txt");
        submissionDB.setLastSubmitted(String.valueOf(System.currentTimeMillis()));

        SubmissionDTO dto = SubmissionDB.convertToBinary(submissionDB);

        assertEquals(submissionDB.getId(), dto.getId());
        String base64File = Base64.getEncoder().encodeToString(content.getBytes());
        assertEquals(base64File, dto.getFileSubmission());
        assertEquals(submissionDB.getGroupName(), dto.getGroupName());
        assertEquals(submissionDB.getFileName(), dto.getFileName());
        assertEquals(submissionDB.getLastSubmitted(), dto.getLastSubmitted());
    }

    @Test
    void testConvertToBlob() throws SQLException {
        String content = "This is a test file content";
        String base64File = Base64.getEncoder().encodeToString(content.getBytes());
        SubmissionDTO submissionDTO = new SubmissionDTO("test@example.com", base64File, "groupName", new HashSet<>(),"testFile.txt", String.valueOf(System.currentTimeMillis()), String.valueOf(System.currentTimeMillis()), false, false);

        SubmissionDB submissionDB = SubmissionDB.convertToBlob(submissionDTO);

        assertEquals(submissionDTO.getId(), submissionDB.getId());
        byte[] blobBytes = submissionDB.getFileSubmission().getBytes(1, (int) submissionDB.getFileSubmission().length());
        assertEquals(content, new String(blobBytes));
        assertEquals(submissionDTO.getGroupName(), submissionDB.getGroupName());
        assertEquals(submissionDTO.getFileName(), submissionDB.getFileName());
        assertEquals(submissionDTO.getLastSubmitted(), submissionDB.getLastSubmitted());
    }
}
