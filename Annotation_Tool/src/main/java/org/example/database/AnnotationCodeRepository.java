package org.example.database;

import org.example.backend.models.AnnotationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnotationCodeRepository extends JpaRepository<AnnotationCode, String> {
}
