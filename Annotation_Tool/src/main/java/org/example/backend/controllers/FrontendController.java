package org.example.backend.controllers;

import org.example.backend.exceptions.PDFException;
import org.example.backend.services.AnnotationCodeService;
import org.example.backend.services.FileService;
import org.example.backend.services.SubmissionService;
import org.example.backend.utils.FileUtils;
import org.example.backend.services.ParsingService;
import org.example.backend.utils.PairUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class FrontendController {

    private final ParsingService parsingService;
    private final FileService fileService;

    private final AnnotationCodeService annotationCodeService;
    private final SubmissionService submissionService;

    /**
     * This method creates a new instance of the FrontendController class
     *
     * @param annotationCodeService the annotation service
     * @param submissionService the submission service
     */
    @Autowired
    public FrontendController(AnnotationCodeService annotationCodeService, SubmissionService submissionService) {
        this.parsingService = new ParsingService(annotationCodeService);
        this.fileService = new FileService();
        this.annotationCodeService = annotationCodeService;
        this.submissionService = submissionService;
    }

    /**
     * POST - Endpoint for retrieving pdf files from frontend, and passing them to backend
     * @param file received file from frontend
     * @return 200 OK - Parsed text from the file
     *         400 Bad Request - Conversion to PDF fails
     *         400 Bad Request - Parsing PDF fails
     */
    @PostMapping("/frontend")
    public ResponseEntity<PairUtils> retrieveFile(@RequestParam("file") MultipartFile file) {
        try {
            File aPDFFile = FileUtils.convertToFile(file);
            PairUtils result = parsingService.parsePDF(aPDFFile);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new PairUtils(e.getMessage(), null, "invalid"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (PDFException e) {
            return new ResponseEntity<>(new PairUtils(e.getMessage(), null, "invalid"), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * POST - Endpoint for exporting the parsed text and annotations to a PDF file
     * @param text the text to be exported
     * @param annotations the annotations to be exported
     * @return 200 OK - The exported PDF file
     *        400 Bad Request - The file cannot be created
     */
    @PostMapping("/frontend/export")
    public ResponseEntity<byte[]> exportPDF(@RequestParam("text") String text, @RequestParam("annotations") String annotations) {
        try {
            byte[] pdfBytes = fileService.generatePDF(text, annotations);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "exported.pdf");
            headers.setContentLength(pdfBytes.length);
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * GET - Endpoint for retrieving the list of codes from the database
     * @return 200 OK - List of codes
     */
    @GetMapping("/frontend/codes")
    public ResponseEntity<List<AnnotationCode>> getCodes() throws IOException {
        return ResponseEntity.ok(Streamable.of(annotationCodeService.getAnnotationCodes()).toList());
    }
}
