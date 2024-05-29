package org.example.services;

import org.example.models.Submission;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.example.database.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class SubmissionService {
    private final SubmissionRepository repo;

    /**
     * Constructor for the SubmissionService
     *
     * @param repo the repository for the submission
     *             database
     */
    @Autowired
    public SubmissionService(SubmissionRepository repo) {
        this.repo = repo;
    }

    /**
     * This method returns the submission with the given id
     *
     * @param id the id of the submission
     * @return the submission with the given id
     */
    public Submission getSubmission(String id) {
        if (repo.findById(id).isEmpty()) {
            return null;
        }
        return repo.findById(id).get();
    }

    /**
     * This method returns all the submissions in the database
     *
     * @return a list of all the submissions in the database
     */
    public List<Submission> getSubmissions() {
        return Streamable.of(repo.findAll()).toList();
    }

    /**
     * This method adds a submission to the database
     *
     * @param submission the submission to be added
     * @return the submission that was added
     */
    public Submission addSubmission(Submission submission) {
        // check if the submission is null
        if (submission == null) {
            return null;
        }
        // Check if the submission already exists
        if (repo.findById(submission.getId()).isPresent()) {
            return null;
        }
        return repo.save(submission);
    }

    /**
     * This method deletes a submission from the database
     *
     * @param id the id of the submission to be deleted
     * @return the submission that was deleted
     */
    public Submission deleteSubmission(String id) {
        if (repo.findById(id).isEmpty()) {
            return null;
        }
        Submission submission = repo.findById(id).get();
        repo.deleteById(id);
        return submission;
    }

    /**
     * This method updates a submission in the database
     *
     * @param submission the submission to be updated
     * @return the submission that was updated
     */
    public Submission updateSubmission(Submission submission) {
        // check if the submission is null
        if (submission == null) {
            return null;
        }
        return repo.save(submission);
    }
}
