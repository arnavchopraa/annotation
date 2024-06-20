package org.example.backend.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Set;

@Schema(description = "Submission entity")
@Entity
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="submissions")
public class SubmissionDB {

    @Schema(description = "ID of the submission, which represents the student's email", example = "student@tudelft.nl")
    @Id
    @Column(name="email")
    private String id;

    @Schema(description = "File submitted by the student", example = "<file>")
    @Column(name="file_submission")
    @Lob
    private Blob fileSubmission;

    @Schema(description = "Group name of the student", example = "Test Group")
    @Column(name="group_name")
    private String groupName;

    @Schema(description = "Assigned coordinators", example = "[{\"email\": \"supervisor@tudelft.nl\"," +
        "\"username\": \"supervisor1\"," +
        "\"password\": \"superPass\"," +
        "\"role\": \"supervisor\"}]")
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "coordinator_assignments",
        joinColumns = @JoinColumn(name = "submission", referencedColumnName = "email"),
        inverseJoinColumns = @JoinColumn(name = "coordinator", referencedColumnName = "email"))
    Set<User> assignedCoordinators;

    @Schema(description = "File name of the submission", example = "file.pdf")
    @Column(name="file_name")
    private String fileName;

    @Schema(description = "Date of last submission by the coordinator", example = "Tue, 14 Jun 2024 16:50:02 GMT")
    @Column(name="last_submitted")
    private String lastSubmitted;

    @Schema(description = "Date of last edit made by the coordinator", example = "Never")
    @Column(name="last_edited")
    private String lastEdited;

    @Schema(description = "Flag that remembers whether the file is submitted", example = "true")
    @Column(name="is_submitted")
    private boolean isSubmitted;

    /**
     * This method encrypts the submission to a supported format for JSON
     *
     * @param submissionDB the submission to convert to base64
     * @return the encrypted submission
     */
    public static SubmissionDTO convertToBinary(SubmissionDB submissionDB) {
        String base64File = null;
        if(submissionDB.getFileSubmission() == null)
            return new SubmissionDTO(submissionDB.getId(), null, submissionDB.getGroupName(), submissionDB.getAssignedCoordinators()
                    , submissionDB.getFileName(), submissionDB.getLastSubmitted(), submissionDB.getLastEdited(), submissionDB.isSubmitted());
        try {
            byte[] fileByte = submissionDB.getFileSubmission().getBinaryStream().readAllBytes();
            base64File = Base64.getEncoder().encodeToString(fileByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SubmissionDTO(submissionDB.getId(), base64File, submissionDB.getGroupName(), submissionDB.getAssignedCoordinators()
                , submissionDB.getFileName(), submissionDB.getLastSubmitted(), submissionDB.getLastEdited(), submissionDB.isSubmitted());
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
            return new SubmissionDB(submissionDTO.getId(), new SerialBlob(decodedBytes), submissionDTO.getGroupName()
                    , submissionDTO.getAssignedCoordinators(), submissionDTO.getFileName(), submissionDTO.getLastSubmitted()
                    , submissionDTO.getLastEdited(), submissionDTO.isSubmitted());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a user to the submission's assigned coordinator list.
     *
     * @param user User to be added to the list
     */
    public void addUser(User user) {
        this.assignedCoordinators.add(user);
    }

}
