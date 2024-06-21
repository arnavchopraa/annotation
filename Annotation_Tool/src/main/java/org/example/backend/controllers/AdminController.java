package org.example.backend.controllers;

import lombok.Getter;
import lombok.Setter;
import org.example.backend.exceptions.FileException;
import org.example.backend.exceptions.ImportException;
import org.example.backend.exceptions.NoSubmissionException;
import org.example.backend.exceptions.PDFException;
import org.example.backend.models.User;
import org.example.backend.services.*;
import org.example.backend.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.zip.ZipException;

@RestController
@CrossOrigin(origins = "*")
@Getter
public class AdminController {
    private final ImportService importService;
    private final ExportService exportService;
    private final SubmissionService submissionService;

    /**
     * Constructor for AdminController, autowired by Spring
     *
     * @param userService Service that manages users
     * @param submissionService Service that manages submissions
     * @param annotationCodeService Service that manages annotation codes
     */
    @Autowired
    public AdminController(UserService userService, SubmissionService submissionService,
        AnnotationCodeService annotationCodeService) {
        this.submissionService = submissionService;
        this.importService = new ImportService(userService, submissionService);
        this.exportService = new ExportService(submissionService, annotationCodeService);
    }

    /**
     * Constructor for AdminController, with predefined importService
     *
     * @param importService Service that manages imports
     * @param submissionService Service that manages submissions
     * @param annotationCodeService Service that manages annotation codes
     */
    public AdminController(ImportService importService,
        SubmissionService submissionService, AnnotationCodeService annotationCodeService) {
        this.importService = importService;
        this.exportService = new ExportService(submissionService, annotationCodeService);
        this.submissionService = submissionService;
    }

    /**
     * Controller to retrieve the 3 files that have been uploaded by an admin
     *
     * @param zipFile zip file containing assignment submissions
     * @param csvFile csv file containing student distribution over groups
     * @param xlsxFile xlsx file containing coordinator distribution over groups
     * @return 200 OK - Successfully saved submissions and created accounts for users
     *         400 Bad Request - Files submitted are null
     *         400 Bad Request - Import functionality fails
     */
    @PostMapping("/admin/files")
    public ResponseEntity<String> getFiles(@RequestParam("zipFile") MultipartFile zipFile,
        @RequestParam("csvFile") MultipartFile csvFile,
        @RequestParam("xlsxFile") MultipartFile xlsxFile) {

        if(zipFile == null || zipFile.isEmpty() ||
            csvFile == null || csvFile.isEmpty() ||
            xlsxFile == null || xlsxFile.isEmpty()) {

            return new ResponseEntity<>("Request body is malformed", HttpStatus.BAD_REQUEST);
        }
        try {
            File zip = FileUtils.convertToFile(zipFile);
            File csv = FileUtils.convertToFile(csvFile);
            File xlsx = FileUtils.convertToFile(xlsxFile);
            importService.importData(zip, csv, xlsx);
            return new ResponseEntity<>("Files imported successfully", HttpStatus.OK);
        } catch (ImportException | NoSubmissionException | FileException | SQLException | ZipException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint used for downloading all files from the system, after applying the parsing operation on them.
     *
     * @return 200 OK - A zip file, containing all files from the system, parsed.
     *         500 INTERNAL SERVER ERROR - If something went wrong with temporary files.
     *         400 BAD REQUEST - If something went wrong while parsing files.
     */
    @GetMapping("/admin/bulkdownload")
    public ResponseEntity<byte[]> downloadAllParsed() {
        File zipFile;
        try {
            zipFile = exportService.getAllSubmissionsParsed();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (PDFException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try (InputStream inputStream = new FileInputStream(zipFile)) {
            byte[] bytes = inputStream.readAllBytes();
            return new ResponseEntity<>(bytes, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for deleting all submissions from the database - DO NOT TEST!
     *
     * @return 200 OK - all submissions have been deleted
     */
    @DeleteMapping("/admin/deleteall")
    public ResponseEntity<String> deleteAllSubmissions() {
        try {
            submissionService.deleteAll();
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong while trying to delete all submissions", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Submission database has been cleared!", HttpStatus.OK);
    }

}
