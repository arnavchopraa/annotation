package org.example.models;

import jakarta.persistence.*;
import lombok.*;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
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

    @Column(name="file_name")
    private String fileName;

    @Column(name="last_submitted")
    private Date lastSubmitted;

    @Column(name="last_edited")
    private Date lastEdited;

    /**
     * This method encrypts the submission to a supported format for JSON
     *
     * @param submissionDB the submission to convert to base64
     * @return the encrypted submission
     */
    public static SubmissionDTO convertToBinary(SubmissionDB submissionDB) {
        String base64File = null;
        if(submissionDB.getFileSubmission() == null)
            return new SubmissionDTO(submissionDB.getId(), null, submissionDB.getAssignedCoordinator()
                    , submissionDB.getFileName(), submissionDB.getLastSubmitted(), submissionDB.getLastEdited());
        try {
            byte[] fileByte = submissionDB.getFileSubmission().getBinaryStream().readAllBytes();
            base64File = Base64.getEncoder().encodeToString(fileByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SubmissionDTO(submissionDB.getId(), base64File, submissionDB.getAssignedCoordinator()
                , submissionDB.getFileName(), submissionDB.getLastSubmitted(), submissionDB.getLastEdited());
    }

    /**
     * This method decrypts the submission to a supported format for JSON
     *
     * @param submissionDTO the submission to convert to base64
     * @return the decrypted submission
     */
    public static SubmissionDB convertToBlob(SubmissionDTO submissionDTO) {
        byte[] decodedBytes = Base64.getDecoder().decode(submissionDTO.getFileSubmission());
        try {
            return new SubmissionDB(submissionDTO.getId(), new SerialBlob(decodedBytes), submissionDTO.getAssignedCoordinator()
                    , submissionDTO.getFileName(), submissionDTO.getLastSubmitted(), submissionDTO.getLastEdited());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
