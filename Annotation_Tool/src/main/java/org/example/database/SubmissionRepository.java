package org.example.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    //List<SubmissionDB> findByAssignedCoordinator(String assignedCoordinator);

    @Query("SELECT s FROM SubmissionDB s JOIN s.assignedCoordinators c WHERE c.id = :assignedCoordinator")
    List<SubmissionDB> findByAssignedCoordinator(@Param("assignedCoordinator") String assignedCoordinator);

    /**
     * This method queries the database for all files associated to a coordinator, which contain given text in their id (email)
     * @param search The text which must be in the email
     * @param assignedCoordinator the id of the coordinator
     * @return the list of files associated with this coordinator containing id in their email
     */
    @Query("SELECT s FROM SubmissionDB s JOIN s.assignedCoordinators c WHERE c.id = :assignedCoordinator " +
        "AND LOWER(s.id) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<SubmissionDB> findByIdIgnoreCaseContainingAndAssignedCoordinator(
        @Param("search") String search, @Param("assignedCoordinator") String assignedCoordinator);
}
