package org.example.backend.models;

import jakarta.persistence.*;
import lombok.*;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name="group_name")
    private String groupName;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "coordinator_assignments",
            joinColumns = @JoinColumn(name = "submission", referencedColumnName = "email"),
            inverseJoinColumns = @JoinColumn(name = "coordinator", referencedColumnName = "email"))
    Set<User> assignedCoordinators;

    @Column(name="file_name")
    private String fileName;

    @Column(name="last_submitted")
    private String lastSubmitted;

    @Column(name="last_edited")
    private String lastEdited;

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
                    , submissionDTO.getAssignedCoordinators(), submissionDTO.getFileName(), submissionDTO.getLastSubmitted(), submissionDTO.getLastEdited(), submissionDTO.isSubmitted());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addUser(User user) {
        this.assignedCoordinators.add(user);
    }

}
