package org.example.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="submissions")
public class Submission implements Serializable{
    @Id
    @Column(name="email")
    private String id;

    @Column(name="fileSubmission")
    @Lob
    private Blob fileSubmission;

    @Column(name="assignedCoordinator")
    private String assignedCoordinator;

}
