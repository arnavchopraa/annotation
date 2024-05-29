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

import org.example.database.SubmissionEntityRepository;
import org.example.services.SubmissionEntityService;
import org.example.models.SubmissionEntity;

import java.util.List;


@RestController
@RequestMapping("/SubmissionEntitys")
public class SubmissionEntityController {
    private SubmissionEntityRepository repo;
    private SubmissionEntityService service;

    /**
     * Constructor for the SubmissionEntityController
     *
     * @param repo the repository for the SubmissionEntity
     * @param service the service for the SubmissionEntity
     */
    @Autowired
    public SubmissionEntityController(SubmissionEntityRepository repo, SubmissionEntityService service) {
        this.repo = repo;
        this.service = service;
    }

    /**
     * This method returns all the SubmissionEntitys in the database
     *
     * @return a list of all the SubmissionEntitys in the database
     */
    @GetMapping("/")
    @ResponseBody
    public List<SubmissionEntity> getSubmissionEntitys() {
        return repo.findAll();
    }

    /**
     * This method returns the SubmissionEntity with the given id
     *
     * @param id the id of the SubmissionEntity
     * @return the SubmissionEntity with the given id
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<SubmissionEntity> getSubmissionEntity(@PathVariable("id") long id) {
        SubmissionEntity sub = service.getSubmissionEntity(id);
        if (sub == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(sub);
    }

    /**
     * This method adds a SubmissionEntity to the database
     *
     * @param SubmissionEntity the SubmissionEntity to be added
     * @return the SubmissionEntity that was added
     */
    @PostMapping("/{id}")
    @ResponseBody
    public ResponseEntity<SubmissionEntity> addSubmissionEntity( @RequestBody SubmissionEntity SubmissionEntity) {
        SubmissionEntity sub1 = service.addSubmissionEntity(SubmissionEntity);
        if (sub1 == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(sub1);
    }

    /**
     * This method updates a SubmissionEntity in the database
     *
     * @param id the id of the SubmissionEntity
     * @param SubmissionEntity the SubmissionEntity to be updated
     * @return the SubmissionEntity that was updated
     */
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<SubmissionEntity> updateSubmissionEntity( @PathVariable("id") long id, @RequestBody SubmissionEntity SubmissionEntity) {
        SubmissionEntity sub1 = service.updateSubmissionEntity(SubmissionEntity);
        if (sub1 == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(sub1);
    }

    /**
     * This method deletes a SubmissionEntity from the database
     *
     * @param id the id of the SubmissionEntity to be deleted
     * @return the SubmissionEntity that was deleted
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<SubmissionEntity> deleteSubmissionEntity(@PathVariable Long id) {
        SubmissionEntity deleted = service.deleteSubmissionEntity(id);
        if (deleted == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deleted);
    }
}
