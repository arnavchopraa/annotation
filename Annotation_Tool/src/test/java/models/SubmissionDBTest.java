package models;

import org.example.models.SubmissionDB;
import org.example.models.SubmissionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import javax.sql.rowset.serial.SerialBlob;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class SubmissionDBTest {

    private SubmissionDB submissionDB;

    @BeforeEach
    void setUp() {
        submissionDB = new SubmissionDB();
    }

    @Test
    void testConvertWithNullBlob() {
        submissionDB.setId("test@example.com");
        submissionDB.setFileSubmission(null);
        submissionDB.setAssignedCoordinator("coordinator");

        SubmissionDTO dto = SubmissionDB.convert(submissionDB);

        assertEquals("test@example.com", dto.getId());
        assertNull(dto.getFileSubmission());
        assertEquals("coordinator", dto.getAssignedCoordinator());
    }

    @Test
    void testConvertWithBlob() throws Exception {
        String content = "This is a test file content";
        Blob blob = new SerialBlob(content.getBytes());
        submissionDB.setId("test@example.com");
        submissionDB.setFileSubmission(blob);
        submissionDB.setAssignedCoordinator("coordinator");

        SubmissionDTO dto = SubmissionDB.convert(submissionDB);

        assertEquals("test@example.com", dto.getId());
        String base64File = Base64.getEncoder().encodeToString(content.getBytes());
        assertEquals(base64File, dto.getFileSubmission());
        assertEquals("coordinator", dto.getAssignedCoordinator());
    }

}
