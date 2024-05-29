package org.example.database;

import org.example.models.AnnotationCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnotationCodeRepository extends CrudRepository<AnnotationCode, String> {
}
