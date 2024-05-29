package org.example.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.models.SubmissionDB;

@Repository
public interface SubmissionRepository extends JpaRepository<SubmissionDB, String> { }
