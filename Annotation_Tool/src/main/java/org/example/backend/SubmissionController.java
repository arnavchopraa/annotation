package org.example.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.example.database.SubmissionRepository;
import org.example.services.SubmissionService;
import org.example.models.Submission;

import java.util.List;


@RestController
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
    public ResponseEntity<List<Submission>> getSubmissions() {
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
    public ResponseEntity<Submission> getSubmission(@PathVariable("id") String id) {
        Submission sub = service.getSubmission(id);
        if (sub == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(sub);
    }

    /**
     * This method adds a submission to the database
     *
     * @param submission the submission to be added
     * @return the submission that was added
     */
    @PostMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Submission> addSubmission( @RequestBody Submission submission) {
        Submission sub1 = service.addSubmission(submission);
        if (sub1 == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(sub1);
    }

    /**
     * This method updates a submission in the database
     *
     * @param id the id of the submission
     * @param submission the submission to be updated
     * @return the submission that was updated
     */
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Submission> updateSubmission( @PathVariable("id") String id, @RequestBody Submission submission) {
        Submission sub1 = service.updateSubmission(submission);
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
    public ResponseEntity<Submission> deleteSubmission(@PathVariable String id) {
        Submission deleted = service.deleteSubmission(id);
        if (deleted == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deleted);
    }
}
