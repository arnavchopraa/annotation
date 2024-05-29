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

    @Column(name="file_submission")
    @Lob
    private Blob fileSubmission;

    @Column(name="assigned_coordinator")
    private String assignedCoordinator;

}
