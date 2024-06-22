package org.example.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.backend.models.SubmissionDTO;
import org.example.backend.services.AnnotationCodeService;
import org.example.backend.services.ExportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.example.database.SubmissionRepository;
import org.example.backend.services.SubmissionService;
import org.example.backend.models.SubmissionDB;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/submissions")
public class SubmissionController {
    private final SubmissionService service;
    private final AnnotationCodeService annotationCodeService;
    private final ExportService exportService;

    /**
     * Constructor for the SubmissionController
     *
     * @param service the service for the submission
     * @param annotationCodeService the service for the annotation code
     */
    @Autowired
    public SubmissionController(SubmissionService service, AnnotationCodeService annotationCodeService){
        this.service = service;
        this.annotationCodeService = annotationCodeService;
        this.exportService = new ExportService(service, annotationCodeService);
    }

    /**
     * This method returns all the submissions in the database
     *
     * @return a list of all the submissions in the database
     */
    @Operation(summary = "Retrieve all submissions from the database",
        responses = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved all submissions",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SubmissionDTO.class))
                    )
                )
        }
    )
    @GetMapping("/")
    public ResponseEntity<List<SubmissionDTO>> getSubmissions() {
        return ResponseEntity.ok(service.getSubmissions().stream()
                .map(x -> SubmissionDB.convertToBinary(x))
                .collect(Collectors.toList()));
    }

    /**
     * This method returns the submission with the given id
     *
     * @param id the id of the submission
     * @return the submission with the given id
     */
    @Operation(summary = "Retrieve a submission from the database",
        parameters = {
            @Parameter(name = "id", description = "ID of the submission to be retrieved", required = true, in = ParameterIn.PATH)
        },
        responses = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved submission with specified id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SubmissionDTO.class)
                    )
                ),
            @ApiResponse(responseCode = "404", description = "Could not find the specified submission in the database")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<SubmissionDTO> getSubmission(@PathVariable("id") String id) {
        SubmissionDB sub = service.getSubmission(id);
        if (sub == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(SubmissionDB.convertToBinary(sub));
    }

    /**
     * This method exports the submission with the given id as a pdf
     *
     * @param id the id of the submission
     * @return a pdf of the submission with the given id
     */
    @Operation(summary = "Download a submission's file from the database",
        parameters = {
            @Parameter(name = "id", description = "ID of the submission whose file should be downloaded", required = true, in = ParameterIn.PATH)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully passed the file's bytes"),
            @ApiResponse(responseCode = "404", description = "Couldn't find the submission's file"),
            @ApiResponse(responseCode = "500", description = "Couldn't retrieve the file bytes")
        }
    )
    @GetMapping("/export/{id}")
    public ResponseEntity<byte[]> downloadSubmission(@PathVariable("id") String id) {
        File pdfFile;
        try {
            pdfFile = exportService.getSubmission(id);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try (InputStream inputStream = new FileInputStream(pdfFile)) {
            byte[] bytes = inputStream.readAllBytes();
            return new ResponseEntity<>(bytes, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * This method returns all the submissions of a coordinator
     *
     * @param id the email of the coordinator
     * @return the submissions
     */
    @Operation(summary = "Retrieve all submissions associated to a coordinator, as a list",
        parameters = {
            @Parameter(name = "id", description = "ID of the coordinator whose submissions should be retrieved",
                    required = true, in = ParameterIn.PATH)
        },
        responses = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved all submissions",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SubmissionDTO.class))
                    )
                ),
            @ApiResponse(responseCode = "500", description = "Could not retrieve submissions from the database")
        }
    )
    @GetMapping("/coordinator/{id}")
    public ResponseEntity<List<SubmissionDTO>> getCoordinatorsSubmission(@PathVariable("id") String id) {
        try {
            List<SubmissionDB> sub = service.getCoordinatorsSubmissions(id);
            List<SubmissionDTO> resp = sub.stream()
                .map(SubmissionDB::convertToBinary)
                .collect(Collectors.toList());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This method updates a submission in the database
     *
     * @param id the id of the submission
     * @param submissionDTO the submission to be updated
     * @return the submission that was updated
     */
    @Operation(summary = "Update a submission in the database",
        parameters = {
            @Parameter(name = "id", description = "ID of the submission to be updated", required = true, in = ParameterIn.PATH)
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "New submission details to be saved",
        content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = SubmissionDTO.class)
        )
        ),
        responses = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully updated a submission",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SubmissionDTO.class)
                    )
                ),
            @ApiResponse(responseCode = "400", description = "Submission could not be updated")
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<SubmissionDTO> updateSubmission(@PathVariable("id") String id, @RequestBody SubmissionDTO submissionDTO) {
        // TODO: check if id is same in both


        SubmissionDB sub1 = service.updateSubmission(SubmissionDB.convertToBlob(submissionDTO));
        if (sub1 == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(submissionDTO);
    }

    /**
     * This method deletes a submission from the database
     *
     * @param id the id of the submission to be deleted
     * @return the submission that was deleted
     */
    @Operation(summary = "Delete a submission from the database",
        parameters = {
            @Parameter(name = "id", description = "ID of the submission who should be deleted", required = true, in = ParameterIn.PATH)
        },
        responses = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully deleted submission",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubmissionDB.class))
                ),
            @ApiResponse(responseCode = "404", description = "Could not find the submission to be deleted")
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<SubmissionDB> deleteSubmission(@PathVariable String id) {
        SubmissionDB deleted = service.deleteSubmission(id);
        if (deleted == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deleted);
    }

    /**
     * This method searches for submissions containing some text in their email
     * which also have the given coordinator
     *
     * @param text The text that must be in the submission email
     * @param coordinator The coordinator which the submissions must have
     * @return A list of submission which match this
     */
    @Operation(summary = "Search for submissions given some text",
        parameters = {
            @Parameter(name = "text", description = "Text to be searched in the submissions", required = true, in = ParameterIn.PATH),
            @Parameter(name = "coordinator", description = "Coordinator whose submissions must belong to", required = true, in = ParameterIn.PATH)
        },
        responses = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved submissions which match the given text",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SubmissionDTO.class))
                    )
                )
        }
    )
    @GetMapping("/search/{text}/{coordinator}")
    public ResponseEntity<List<SubmissionDTO>> searchSubmission(@PathVariable String text, @PathVariable String coordinator) {
        List<SubmissionDB> results = service.searchSubmissions(text, coordinator);
        List<SubmissionDTO> resp = results.stream()
            .map(SubmissionDB::convertToBinary)
            .collect(Collectors.toList());
        return ResponseEntity.ok(resp);
    }

    /**
     * This method returns all the documents that have been submitted sorted by the last submitted date
     *
     * @param id the email of the coordinator
     * @return the submissions sorted by the last submitted date
     */
    @Operation(summary = "Recently submitted documents",
        parameters = {
            @Parameter(name = "id", description = "ID of the coordinator", required = true, in = ParameterIn.PATH)
        },
        responses = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved last submitted files",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SubmissionDTO.class))
                    )
                )
        }
    )
    @GetMapping("/{id}/submitted")
    public ResponseEntity<List<SubmissionDTO>> getSubmittedSortedByLastSubmitted(@PathVariable("id") String id) {
        List<SubmissionDTO> submissions = service.getCoordinatorsSubmissions(id).stream()
            .filter(SubmissionDB::isSubmitted)
            .sorted((x, other) -> {
                Timestamp xDate = service.convertStringToTimestamp(x.getLastSubmitted());
                Timestamp otherDate = service.convertStringToTimestamp(other.getLastSubmitted());

                return otherDate.compareTo(xDate);
            })
            .map(SubmissionDB::convertToBinary)
            .toList();
        return new ResponseEntity<>(submissions, HttpStatus.OK);
    }

    /**
     * Locks the file with the given id
     * @param id id of the file to be locked
     * @return value of the lock
     */
    @Operation(summary = "Lock file",
        parameters = {
            @Parameter(name = "id", description = "ID of the file", required = true, in = ParameterIn.PATH)
        },
        responses = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully locked file",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class)
                    )
                ),
            @ApiResponse(responseCode = "404", description = "Did not find file")
        }
    )
    @PutMapping("/{id}/lock")
    public ResponseEntity<Boolean> lockFile(@PathVariable("id") String id) {
        SubmissionDB updated = service.setIsLocked(id, true);

        if(updated == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(updated.isLocked());
    }

    /**
     * Unlocks the file with the given id
     * @param id id of the file to be unlocked
     * @return value of the lock
     */
    @Operation(summary = "Unlock file",
        parameters = {
            @Parameter(name = "id", description = "ID of the file", required = true, in = ParameterIn.PATH)
        },
        responses = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully unlocked file",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class)
                    )
                ),
            @ApiResponse(responseCode = "404", description = "Did not find file")
        }
    )
    @PutMapping("/{id}/unlock")
    public ResponseEntity<Boolean> unlockFile(@PathVariable("id") String id) {
        SubmissionDB updated = service.setIsLocked(id, false);

        if(updated == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(updated.isLocked());
    }

    /**
     * Method that returns the value of the lock of the given file
     * @param id id of the file to check
     * @return value of the lock field - either true or false
     */
    @Operation(summary = "Get lock of file",
        parameters = {
            @Parameter(name = "id", description = "ID of the file", required = true, in = ParameterIn.PATH)
        },
        responses = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved lock status of file",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class)
                    )
                ),
            @ApiResponse(responseCode = "404", description = "Did not find file")
        }
    )
    @GetMapping("/{id}/getLock")
    public ResponseEntity<Boolean> getLock(@PathVariable("id") String id) {
        SubmissionDB sub = service.getSubmission(id);

        if(sub == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(sub.isLocked());
    }
}
