package org.example.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Blob;
import java.util.Base64;

@Entity
@Setter
@Getter
@ToString
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

    /**
     * This method
     *
     * @param submissionDB the submission to convert to base64
     * @return the encrypted submission
     */
    public static SubmissionDTO convert(SubmissionDB submissionDB) {
        String base64File = null;
        if(submissionDB.getFileSubmission() == null)
            return new SubmissionDTO(submissionDB.getId(), null, submissionDB.getAssignedCoordinator());
        try {
            byte[] fileByte = submissionDB.getFileSubmission().getBinaryStream().readAllBytes();
            base64File = Base64.getEncoder().encodeToString(fileByte);
            try {
                FileWriter myWriter = new FileWriter("filename.txt");
                myWriter.write(base64File);
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SubmissionDTO(submissionDB.getId(), base64File, submissionDB.getAssignedCoordinator());
    }

}
