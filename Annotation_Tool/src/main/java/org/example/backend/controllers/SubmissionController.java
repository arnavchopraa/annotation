package org.example.backend.controllers;

import org.example.backend.models.SubmissionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.example.database.SubmissionRepository;
import org.example.backend.services.SubmissionService;
import org.example.backend.models.SubmissionDB;

import java.io.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/submissions")
public class SubmissionController {
    private SubmissionService service;

    /**
     * Constructor for the SubmissionController
     *
     * @param repo the repository for the submission
     * @param service the service for the submission
     */
    @Autowired
    public SubmissionController(SubmissionRepository repo, SubmissionService service) {
        this.service = service;
    }

    /**
     * This method returns all the submissions in the database
     *
     * @return a list of all the submissions in the database
     */
    @GetMapping("/")
    @ResponseBody
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
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<SubmissionDTO> getSubmission(@PathVariable("id") String id) {
        SubmissionDB sub = service.getSubmission(id);
        if (sub == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(SubmissionDB.convertToBinary(sub));
    }

    /**
     * This method returns all the submissions of a coordinator
     *
     * @param id the email of the coordinator
     * @return the submissions
     */
    @GetMapping("/coordinator/{id}")
    @ResponseBody
    public ResponseEntity<List<SubmissionDTO>> getCoordinatorsSubmission(@PathVariable("id") String id) throws IOException {
        /* TODO -- Creates a zip file in the same format as Brightspace. Delete this when done
        File file = new File("test.zip");
        OutputStream outputStream = new FileOutputStream(file);
        ZipOutputStream zos = new ZipOutputStream(outputStream);
        for(int i = 0;i < 10;i++) {
            String folderName;
            if(i < 5)
                folderName = "678925-136654 - Test group - Sort " + i + " - 27 May, 2024 1441";
            else
                folderName = "678925-136654 - Group 2 - Sort " + i + " - 27 May, 2024 1441";
            String path = "sample2.pdf";

            File originalFile = new File(path);
            InputStream inputStream = new FileInputStream(originalFile);
            byte[] originalBytes = inputStream.readAllBytes();
            inputStream.close();

            String newFileName = folderName + "/" + "thesis" + i + ".pdf";
            zos.putNextEntry(new ZipEntry(newFileName));
            zos.write(originalBytes, 0, originalBytes.length);
            zos.closeEntry();
        }
        zos.close();
        outputStream.close();
        */
        List<SubmissionDB> sub = service.getCoordinatorsSubmissions(id);
        List<SubmissionDTO> resp = sub.stream()
            .map(SubmissionDB::convertToBinary)
            .collect(Collectors.toList());
        return ResponseEntity.ok(resp);
    }

    /**
     * This method adds a submission to the database
     *
     * @param submissionDTO the submission to be added
     * @return the submission that was added
     */
    @PostMapping("/{id}")
    @ResponseBody
    public ResponseEntity<SubmissionDB> addSubmission(@RequestBody SubmissionDTO submissionDTO) {
        SubmissionDB sub1 = service.addSubmission(SubmissionDB.convertToBlob(submissionDTO));
        if (sub1 == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(sub1);
    }

    /**
     * This method updates a submission in the database
     *
     * @param id the id of the submission
     * @param submissionDTO the submission to be updated
     * @return the submission that was updated
     */
    @PutMapping("/{id}")
    @ResponseBody
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
    @DeleteMapping("/{id}")
    @ResponseBody
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
     * @param text The text that must be in the submission email
     * @param coordinator The coordinator which the submissions must have
     * @return A list of submission which match this
     */
    @GetMapping("/search/{text}/{coordinator}")
    @ResponseBody
    public ResponseEntity<List<SubmissionDTO>> searchSubmission(@PathVariable String text, @PathVariable String coordinator) {
        List<SubmissionDB> results = service.searchSubmissions(text, coordinator);
        List<SubmissionDTO> resp = results.stream()
            .map(SubmissionDB::convertToBinary)
            .collect(Collectors.toList());
        return ResponseEntity.ok(resp);
    }

    /**
     * This method returns all the documents that have been submitted sorted by the last submitted date
     * @param id the email of the coordinator
     * @return the submissions sorted by the last submitted date
     */
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
}
