package org.example.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="submissions")
public class SubmissionDB {
    @Id
    @Column(name="email")
    private String id;

    @Column(name="fileSubmission")
    @Lob
    private Blob fileSubmission;

    @Column(name="assignedCoordinator")
    private String assignedCoordinator;

}
