package org.example.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.models.SubmissionDB;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<SubmissionDB, String> {
    List<SubmissionDB> findByAssignedCoordinator(String assignedCoordinator);
}
