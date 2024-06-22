package org.example.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.backend.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> { }
