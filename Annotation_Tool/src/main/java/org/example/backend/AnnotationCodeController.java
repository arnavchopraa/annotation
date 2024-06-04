package org.example.backend;

import org.example.models.AnnotationCode;
import org.example.services.AnnotationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/annotations")
public class AnnotationCodeController {
    private final AnnotationCodeService service;

    /**
     * Constructor for the AnnotationCodeController
     *
     * @param service the service for the annotation code
     */
    @Autowired
    public AnnotationCodeController(AnnotationCodeService service) {
        this.service = service;
    }

    /**
     * This method returns all the annotation codes in the database
     *
     * @return a list of all the annotation codes in the database
     */
    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<AnnotationCode>> getAnnotationCodes() {
        return ResponseEntity.ok(service.getAnnotationCodes());
    }

    /**
     * This method returns the annotation code with the given id
     *
     * @param id the id of the annotation code
     * @return the annotation code with the given id
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<AnnotationCode> getAnnotationCode(@PathVariable("id") String id) {
        AnnotationCode annotationCode = service.getAnnotationCode(id);
        if (annotationCode == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(annotationCode);
    }

    /**
     * This method adds an annotation code to the database
     *
     * @param annotationCode the annotation code to be added
     * @return the annotation code that was added
     */
    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<AnnotationCode> addAnnotationCode(@RequestBody AnnotationCode annotationCode) {
        AnnotationCode savedAnnotationCode = service.addAnnotationCode(annotationCode);
        if (savedAnnotationCode == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(savedAnnotationCode);
    }

    /**
     * This method updates an annotation code in the database
     *
     * @param id the id of the annotation code
     * @param annotationCode the annotation code to be updated
     * @return the annotation code that was updated
     */
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<AnnotationCode> updateAnnotationCode(@PathVariable("id") String id, @RequestBody AnnotationCode annotationCode) {
        annotationCode.setId(id);
        AnnotationCode updatedAnnotationCode = service.updateAnnotationCode(annotationCode);
        if (updatedAnnotationCode == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updatedAnnotationCode);
    }

    /**
     * This method deletes an annotation code from the database
     *
     * @param id the id of the annotation code to be deleted
     * @return the annotation code that was deleted
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<AnnotationCode> deleteAnnotationCode(@PathVariable("id") String id) {
        AnnotationCode deletedAnnotationCode = service.deleteAnnotationCode(id);
        if (deletedAnnotationCode == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deletedAnnotationCode);
    }
}
