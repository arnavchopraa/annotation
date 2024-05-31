package org.example.models;

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
}
