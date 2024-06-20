package org.example.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.backend.exceptions.PDFException;
import org.example.backend.models.AnnotationCode;
import org.example.backend.services.*;
import org.example.backend.utils.FileUtils;
import org.example.backend.utils.PairUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class FrontendController {

    private final ParsingService parsingService;

    private final AnnotationCodeService annotationCodeService;
    private final SubmissionService submissionService;
    private final ExportService exportService;

    /**
     * This method creates a new instance of the FrontendController class
     *
     * @param annotationCodeService the annotation service
     * @param submissionService the submission service
     */
    @Autowired
    public FrontendController(AnnotationCodeService annotationCodeService, SubmissionService submissionService) {
        this.parsingService = new ParsingService(annotationCodeService);
        this.annotationCodeService = annotationCodeService;
        this.submissionService = submissionService;
        this.exportService = new ExportService(submissionService, annotationCodeService);
    }

    /**
     * POST - Endpoint for retrieving pdf files from frontend, and passing them to backend
     * @param file received file from frontend
     * @return 200 OK - Parsed text from the file
     *         400 Bad Request - Conversion to PDF fails
     *         400 Bad Request - Parsing PDF fails
     *
     *         ??? - IS THIS UNUSED? if yes, please comment in the MR so I can delete it.
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
     * GET - Endpoint for retrieving the list of codes from the database
     * @return 200 OK - List of codes
     *
     * ??? - IS THIS REPEATED with getAnnotationCodes() from AnnotationCodeController? if yes, please comment in the MR.
     */
    @GetMapping("/frontend/codes")
    public ResponseEntity<List<AnnotationCode>> getCodes() {
        return ResponseEntity.ok(Streamable.of(annotationCodeService.getAnnotationCodes()).toList());
    }

    /**
     * Endpoint used to download all files, for a coordinator, containing all modifications done on the frontend.
     *
     * @param id Email of the coordinator for which to download all assigned files.
     * @return 200 OK - .zip file containing all PDF files, containing all modifications done on our tool.
     *         500 INTERNAL SERVER ERROR - if there was a problem with temporary files.
     *         404 NOT FOUND - if there was a problem upon creating the zip file.
     */
    @Operation(summary = "Export all pdf files assigned to a coordinator, compressed into a zip file",
        parameters = {
            @Parameter(name = "id", description = "Coordinator's email, whose assigned files should be retrieved",
                    required = true, in = ParameterIn.PATH)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully exported all files"),
            @ApiResponse(responseCode = "404", description = "Could not find the zip file"),
            @ApiResponse(responseCode = "500", description = "Could not retrieve all submissions")
        }
    )
    @GetMapping("/frontend/allsubmissions/{id}")
    public ResponseEntity<byte[]> exportAllFiles(@PathVariable("id") String id) {
        File zipFile;
        try {
            zipFile = exportService.getAllSubmissionsByCoordinator(id);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try (InputStream inputStream = new FileInputStream(zipFile)) {
            byte[] bytes = inputStream.readAllBytes();
            return new ResponseEntity<>(bytes, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
