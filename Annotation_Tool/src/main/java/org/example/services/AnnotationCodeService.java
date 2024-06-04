package org.example.services;

import org.example.database.AnnotationCodeRepository;
import org.example.models.AnnotationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnotationCodeService {
    private final AnnotationCodeRepository repo;

    /**
     * Constructor for the AnnotationCodeService
     *
     * @param repo the repository for the annotation code
     *             database
     */
    @Autowired
    public AnnotationCodeService(AnnotationCodeRepository repo) {
        this.repo = repo;
    }

    /**
     * This method returns the annotation code with the given id
     *
     * @param id the id of the annotation code
     * @return the annotation code with the given id
     */
    public AnnotationCode getAnnotationCode(String id) {
        return repo.findById(id).orElse(null);
    }

    /**
     * This method returns all the annotation codes in the database
     *
     * @return a list of all the annotation codes in the database
     */
    public List<AnnotationCode> getAnnotationCodes() {
        return Streamable.of(repo.findAll()).toList();
    }

    /**
     * This method adds an annotation code to the database
     *
     * @param annotationCode the annotation code to be added
     * @return the annotation code that was added
     */
    public AnnotationCode addAnnotationCode(AnnotationCode annotationCode) {
        // check if the annotation code is null
        if (annotationCode == null) {
            return null;
        }
        // Check if the annotation code already exists
        if (repo.findById(annotationCode.getId()).isPresent()) {
            return null;
        }
        return repo.save(annotationCode);
    }

    /**
     * This method updates an annotation code in the database
     *
     * @param annotationCode the annotation code to be updated
     * @return the annotation code that was updated
     */
    public AnnotationCode updateAnnotationCode(AnnotationCode annotationCode) {
        // check if the annotation code is null
        if (annotationCode == null) {
            return null;
        }
        return repo.save(annotationCode);
    }

    /**
     * This method deletes an annotation code from the database
     *
     * @param id the id of the annotation code to be deleted
     * @return the annotation code that was deleted
     */
    public AnnotationCode deleteAnnotationCode(String id) {
        AnnotationCode deleted = repo.findById(id).orElse(null);
        if (deleted == null) {
            return null;
        }
        repo.deleteById(id);
        return deleted;
    }
}
