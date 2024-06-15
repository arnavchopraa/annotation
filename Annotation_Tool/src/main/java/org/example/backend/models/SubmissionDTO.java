package org.example.backend.models;

import lombok.*;

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
    private String lastSubmitted;
    private String lastEdited;
    private boolean isSubmitted;
}
