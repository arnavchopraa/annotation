package org.example.backend.controllers;

import org.example.backend.exceptions.FileException;
import org.example.backend.exceptions.ImportException;
import org.example.backend.exceptions.NoSubmissionException;
import org.example.backend.services.ImportService;
import org.example.backend.utils.FileUtils;
import org.example.database.SubmissionRepository;
import org.example.database.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.SQLException;
import java.util.zip.ZipException;

@RestController
@CrossOrigin(origins = "*")
public class AdminController {
    private final ImportService importService;
    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;

    @Autowired
    public AdminController(UserRepository userRepository, SubmissionRepository submissionRepository) {
        this.userRepository = userRepository;
        this.submissionRepository = submissionRepository;
        this.importService = new ImportService(userRepository, submissionRepository);
    }

    @PostMapping("/admin/files")
    public ResponseEntity<String> getFiles(@RequestParam("zipFile") MultipartFile zipFile,
                                           @RequestParam("csvFile") MultipartFile csvFile,
                                           @RequestParam("xlsxFile") MultipartFile xlsxFile) {
        System.out.println("reached");
        if(zipFile == null || zipFile.isEmpty() ||
        csvFile == null || csvFile.isEmpty() ||
        xlsxFile == null || xlsxFile.isEmpty())
            return new ResponseEntity<>("Request body is malformed", HttpStatus.BAD_REQUEST);
        try {
            File zip = FileUtils.convertToFile(zipFile);
            File csv = FileUtils.convertToFile(csvFile);
            File xlsx = FileUtils.convertToFile(xlsxFile);
            importService.importData(zip, csv, xlsx);
            return new ResponseEntity<>("Import successful", HttpStatus.OK);
        } catch (ImportException | NoSubmissionException | FileException | SQLException | ZipException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
