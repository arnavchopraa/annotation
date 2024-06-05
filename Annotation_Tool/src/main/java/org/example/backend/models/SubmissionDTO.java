package org.example.backend.models;

import lombok.*;

import java.sql.Date;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionDTO {
    private String id;
    private String fileSubmission;
    private String assignedCoordinator;
    private String fileName;
    private Date lastSubmitted;
    private Date lastEdited;
}
