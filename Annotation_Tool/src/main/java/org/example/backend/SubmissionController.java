package org.example.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.example.database.SubmissionRepository;
import org.example.services.SubmissionService;
import org.example.models.SubmissionDB;

import java.util.List;


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
    public ResponseEntity<List<SubmissionDB>> getSubmissions() {
        return ResponseEntity.ok(service.getSubmissions());
    }

    /**
     * This method returns the submission with the given id
     *
     * @param id the id of the submission
     * @return the submission with the given id
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<SubmissionDB> getSubmission(@PathVariable("id") String id) {
        SubmissionDB sub = service.getSubmission(id);
        if (sub == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(sub);
    }

    /**
     * This method returns all the submissions of a coordinator
     *
     * @param id the email of the coordinator
     * @return the submissions
     */
    @GetMapping("/coordinator/{id}")
    @ResponseBody
    public ResponseEntity<List<SubmissionDB>> getCoordinatorsSubmission(@PathVariable("id") String id) {
        List<SubmissionDB> sub = service.getCoordinatorsSubmissions(id);
        return ResponseEntity.ok(sub);
    }

    /**
     * This method adds a submission to the database
     *
     * @param submissionDB the submission to be added
     * @return the submission that was added
     */
    @PostMapping("/{id}")
    @ResponseBody
    public ResponseEntity<SubmissionDB> addSubmission(@RequestBody SubmissionDB submissionDB) {
        SubmissionDB sub1 = service.addSubmission(submissionDB);
        if (sub1 == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(sub1);
    }

    /**
     * This method updates a submission in the database
     *
     * @param id the id of the submission
     * @param submissionDB the submission to be updated
     * @return the submission that was updated
     */
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<SubmissionDB> updateSubmission(@PathVariable("id") String id, @RequestBody SubmissionDB submissionDB) {
        SubmissionDB sub1 = service.updateSubmission(submissionDB);
        if (sub1 == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(sub1);
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
}
