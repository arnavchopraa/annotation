package org.example.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.example.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> { }
