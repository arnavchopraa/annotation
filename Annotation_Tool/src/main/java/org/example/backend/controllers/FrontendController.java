package org.example.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.backend.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.sql.SQLException;

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
