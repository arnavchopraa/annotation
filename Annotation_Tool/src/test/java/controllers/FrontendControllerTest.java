package controllers;

import org.example.backend.controllers.FrontendController;
import org.example.backend.exceptions.PDFException;
import org.example.backend.models.AnnotationCode;
import org.example.backend.services.AnnotationCodeService;
import org.example.backend.services.ExportService;
import org.example.backend.services.ParsingService;
import org.example.backend.services.SubmissionService;
import org.example.backend.utils.FileUtils;
import org.example.backend.utils.PairUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FrontendControllerTest {

    @InjectMocks
    private FrontendController frontendController;

    @Mock
    private ParsingService parsingService;

    @Mock
    private AnnotationCodeService annotationCodeService;

    @Mock
    private SubmissionService submissionService;

    @Mock
    private ExportService exportService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        frontendController = new FrontendController(parsingService, annotationCodeService, submissionService, exportService);
    }

    @Test
    public void retrieveFileReturnsOkWhenParsingSucceeds() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "Hello, World!".getBytes());
        PairUtils pairUtils = new PairUtils("message", null, "status", "info");
        File normalFile = new File("file");
        try (MockedStatic<FileUtils> mocked = Mockito.mockStatic(FileUtils.class)) {
            mocked.when(() -> FileUtils.convertToFile(any())).thenReturn(normalFile);
            when(parsingService.parsePDF(any())).thenReturn(pairUtils);

            ResponseEntity<PairUtils> response = frontendController.retrieveFile(file);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(pairUtils, response.getBody());
        }
    }

    @Test
    public void retrieveFileReturnsBadRequestWhenParsingFails() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "Hello, World!".getBytes());
        when(parsingService.parsePDF(any())).thenThrow(PDFException.class);

        ResponseEntity<PairUtils> response = frontendController.retrieveFile(file);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void getCodesReturnsOkWithListOfCodes() {
        when(annotationCodeService.getAnnotationCodes()).thenReturn(Collections.emptyList());

        ResponseEntity<List<AnnotationCode>> response = frontendController.getCodes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }

    @Test
    public void exportAllFilesReturnsOkWhenExportSucceeds() throws Exception {
        String coordinatorId = "test@example.com";
        File mockFile = new File("file");
        byte[] mockBytes = new byte[0];

        when(exportService.getAllSubmissionsByCoordinator(coordinatorId)).thenReturn(mockFile);

        try (MockedConstruction<FileInputStream> mocked = Mockito.mockConstruction(FileInputStream.class,
                (mock, context) -> {
                    when(mock.readAllBytes()).thenReturn(mockBytes);
                })) {
            ResponseEntity<byte[]> response = frontendController.exportAllFiles(coordinatorId);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(mockBytes, response.getBody());
        }
    }

    @Test
    public void exportAllFilesReturnsInternalServerErrorWhenExportFails() throws Exception {
        when(exportService.getAllSubmissionsByCoordinator(any())).thenThrow(IOException.class);

        ResponseEntity<byte[]> response = frontendController.exportAllFiles("test@example.com");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void exportAllFilesReturnsNotFoundWhenFileNotFound() throws Exception {
        when(exportService.getAllSubmissionsByCoordinator(any())).thenThrow(SQLException.class);

        ResponseEntity<byte[]> response = frontendController.exportAllFiles("test@example.com");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}