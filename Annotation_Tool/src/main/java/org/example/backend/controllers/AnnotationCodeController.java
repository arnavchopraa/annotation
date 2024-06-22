package org.example.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.backend.models.AnnotationCode;
import org.example.backend.services.AnnotationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/annotations")
public class AnnotationCodeController {
    private final AnnotationCodeService service;

    /**
     * Constructor for the AnnotationCodeController
     *
     * @param service the service for the annotation code
     */
    @Autowired
    public AnnotationCodeController(AnnotationCodeService service) {
        this.service = service;
    }

    /**
     * This method returns all the annotation codes in the database
     *
     * @return a list of all the annotation codes in the database
     */
    @Operation(summary = "Get all annotation codes from the database",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "Successfully retrieved all annotation codes",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = AnnotationCode.class))
                )
                )
        })
    @GetMapping("/")
    public ResponseEntity<List<AnnotationCode>> getAnnotationCodes() {
        return ResponseEntity.ok(service.getAnnotationCodes());
    }

    /**
     * This method returns the annotation code with the given id
     *
     * @param id the id of the annotation code
     * @return the annotation code with the given id
     */
    @Operation(summary = "Get an annotation code from the database",
        parameters = {
            @Parameter(name = "id", description = "ID of the annotation code to be retrieved", required = true, in = ParameterIn.PATH)
        },
        responses = {
            @ApiResponse(responseCode = "200",
                description = "Successfully retrieved the annotation code",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnnotationCode.class))),
            @ApiResponse(responseCode = "404", description = "The annotation code was not found")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AnnotationCode> getAnnotationCode(@PathVariable("id") String id) {
        AnnotationCode annotationCode = service.getAnnotationCode(id);
        if (annotationCode == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(annotationCode);
    }

    /**
     * This method adds an annotation code to the database
     *
     * @param annotationCode the annotation code to be added
     * @return the annotation code that was added
     */
    @Operation(summary = "Add an annotation code to the database",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "The annotation code to be added",
        content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = AnnotationCode.class)
        )
        ),
        responses = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully added a new annotation code to the database",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AnnotationCode.class)
                    )
                ),
            @ApiResponse(responseCode = "400", description = "Couldn't add the annotation code")
        }
    )
    @PostMapping("/")
    public ResponseEntity<AnnotationCode> addAnnotationCode(@RequestBody AnnotationCode annotationCode) {
        AnnotationCode savedAnnotationCode = service.addAnnotationCode(annotationCode);
        if (savedAnnotationCode == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(savedAnnotationCode);
    }

    /**
     * This method updates an annotation code in the database
     *
     * @param id the id of the annotation code
     * @param annotationCode the annotation code to be updated
     * @return the annotation code that was updated
     */
    @Operation(summary = "Update an existing annotation code from the database",
        parameters = {
            @Parameter(name = "id", description = "ID of the annotation code to be retrieved", required = true, in = ParameterIn.PATH)
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "The updated annotation code",
        content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = AnnotationCode.class)
        )
        ),
        responses = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully updated annotation code",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AnnotationCode.class)
                    )
                ),
            @ApiResponse(responseCode = "400", description = "Couldn't update the annotation code")
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<AnnotationCode> updateAnnotationCode(@PathVariable("id") String id, @RequestBody AnnotationCode annotationCode) {
        annotationCode.setId(id);
        AnnotationCode updatedAnnotationCode = service.updateAnnotationCode(annotationCode);
        if (updatedAnnotationCode == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updatedAnnotationCode);
    }

    /**
     * This method deletes an annotation code from the database
     *
     * @param id the id of the annotation code to be deleted
     * @return the annotation code that was deleted
     */
    @Operation(summary = "Delete an annotation code from the database",
        parameters = {
            @Parameter(name = "id", description = "ID of the annotation code to be deleted", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully deleted the annotation code",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AnnotationCode.class)
                    )
                ),
            @ApiResponse(responseCode = "404", description = "Couldn't delete the annotation code.")
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<AnnotationCode> deleteAnnotationCode(@PathVariable("id") String id) {
        AnnotationCode deletedAnnotationCode = service.deleteAnnotationCode(id);
        if (deletedAnnotationCode == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deletedAnnotationCode);
    }
}
