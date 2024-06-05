package org.example.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.backend.models.SubmissionDB;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<SubmissionDB, String> {
    /**
     * This method queries the database for all the PDF files associated with a coordinator
     *
     * @param assignedCoordinator the id of the coordinator
     * @return the list of files associated with this coordinator
     */
    List<SubmissionDB> findByAssignedCoordinator(String assignedCoordinator);
}
