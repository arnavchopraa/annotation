package org.example.services;

import org.example.models.SubmissionEntity;
import org.springframework.stereotype.Service;
import org.example.database.SubmissionEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class SubmissionEntityService {
    private final SubmissionEntityRepository repo;

    /**
     * Constructor for the SubmissionEntityService
     *
     * @param repo the repository for the SubmissionEntity
     *             database
     */
    @Autowired
    public SubmissionEntityService(SubmissionEntityRepository repo) {
        this.repo = repo;
    }

    /**
     * This method returns the SubmissionEntity with the given id
     *
     * @param id the id of the SubmissionEntity
     * @return the SubmissionEntity with the given id
     */
    public SubmissionEntity getSubmissionEntity(long id) {
        if (repo.findById(id).isEmpty()) {
            return null;
        }
        return repo.findById(id).get();
    }

    /**
     * This method returns all the SubmissionEntitys in the database
     *
     * @return a list of all the SubmissionEntitys in the database
     */
    public List<SubmissionEntity> getSubmissionEntitys() {
        return repo.findAll();
    }

    /**
     * This method adds a SubmissionEntity to the database
     *
     * @param SubmissionEntity the SubmissionEntity to be added
     * @return the SubmissionEntity that was added
     */
    public SubmissionEntity addSubmissionEntity(SubmissionEntity SubmissionEntity) {
        // check if the SubmissionEntity is null
        if (SubmissionEntity == null) {
            return null;
        }
        // Check if the SubmissionEntity already exists
        if (repo.findById(SubmissionEntity.getId()).isPresent()) {
            return null;
        }
        return repo.save(SubmissionEntity);
    }

    /**
     * This method deletes a SubmissionEntity from the database
     *
     * @param id the id of the SubmissionEntity to be deleted
     * @return the SubmissionEntity that was deleted
     */
    public SubmissionEntity deleteSubmissionEntity(long id) {
        if (repo.findById(id).isEmpty()) {
            return null;
        }
        SubmissionEntity SubmissionEntity = repo.findById(id).get();
        repo.deleteById(id);
        return SubmissionEntity;
    }

    /**
     * This method updates a SubmissionEntity in the database
     *
     * @param SubmissionEntity the SubmissionEntity to be updated
     * @return the SubmissionEntity that was updated
     */
    public SubmissionEntity updateSubmissionEntity(SubmissionEntity SubmissionEntity) {
        // check if the SubmissionEntity is null
        if (SubmissionEntity == null) {
            return null;
        }
        return repo.save(SubmissionEntity);
    }
}
