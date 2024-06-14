package org.example.backend.services;

import org.example.backend.models.SubmissionDB;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.example.database.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    public SubmissionDB getSubmission(String id) {
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
    public List<SubmissionDB> getSubmissions() {
        return Streamable.of(repo.findAll()).toList();
    }

    /**
     * This method returns all the submissions related to a given coordinator from the database
     *
     * @param id the id of the coordinator
     * @return the submissions associated with a coordinator
     */
    public List<SubmissionDB> getCoordinatorsSubmissions(String id) {
        return repo.findByAssignedCoordinator(id);
    }

    /**
     * This method adds a submission to the database
     *
     * @param submissionDB the submission to be added
     * @return the submission that was added
     */
    public SubmissionDB addSubmission(SubmissionDB submissionDB) {
        // check if the submission is null
        if (submissionDB == null) {
            return null;
        }
        // Check if the submission already exists
        if (repo.findById(submissionDB.getId()).isPresent()) {
            return null;
        }
        return repo.save(submissionDB);
    }

    /**
     * This method deletes a submission from the database
     *
     * @param id the id of the submission to be deleted
     * @return the submission that was deleted
     */
    public SubmissionDB deleteSubmission(String id) {
        if (repo.findById(id).isEmpty()) {
            return null;
        }
        SubmissionDB submissionDB = repo.findById(id).get();
        repo.deleteById(id);
        return submissionDB;
    }

    /**
     * This method updates a submission in the database
     *
     * @param submissionDB the submission to be updated
     * @return the submission that was updated
     */
    public SubmissionDB updateSubmission(SubmissionDB submissionDB) {
        // check if the submission is null
        if (submissionDB == null) {
            return null;
        }
        return repo.save(submissionDB);
    }

    /**
     * This method searches for submissions in the database based on their coordinator
     * and part of text in its email
     * @param text The text which must be present in the email
     * @param coordinator The coordinator which must be associated with the submission
     * @return The list of submissions based on the given data
     */
    public List<SubmissionDB> searchSubmissions(String text, String coordinator) {
        if(text == null)
            return new ArrayList<>();
        return repo.findByIdIgnoreCaseContainingAndAssignedCoordinator(text, coordinator);
    }

    /**
     * This method converts a string to a timestamp
     * @param dateString The string to convert
     * @return The timestamp
     */
    public Timestamp convertStringToTimestamp(String dateString) {
        if (dateString == null)
            return new Timestamp(0L);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm yyyy-MM-dd");
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Timestamp.from(instant);
    }
}
