package org.example.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Submissions")
public class SubmissionEntity implements Serializable {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "SubmissionEntity")
    private List<FileEntity> files;

    /**
     * Basic constructor for SubmissionEntity
     *
     * @param id   the id of the SubmissionEntity
     * @param user the user of the SubmissionEntity
     */
    public SubmissionEntity(Long id, User user) {
        this.id = id;
        this.user = user;
        this.files = new ArrayList<FileEntity>();
    }

    /**
     * Basic constructor for SubmissionEntity
     *
     * @param id   the id of the SubmissionEntity
     * @param user the user of the SubmissionEntity
     * @param file the file of the SubmissionEntity
     * @throws IOException if the file could not be read
     */
    public SubmissionEntity(Long id, User user, File file) throws IOException {
        this.id = id;
        this.user = user;
        this.files = new ArrayList<FileEntity>();
        byte[] fileContent = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            int bytesRead = fis.read(fileContent);
            if (bytesRead != fileContent.length) {
                throw new IOException("Could not read the entire file");
            }
        }
        files.add(new FileEntity(fileContent));
    }

    /**
     * Basic constructor for SubmissionEntity
     *
     * @param id    the id of the SubmissionEntity
     * @param user  the user of the SubmissionEntity
     * @param files the files of the SubmissionEntity
     * @throws IOException if the files could not be read
     */
    public SubmissionEntity(Long id, User user, List<File> files) throws IOException {
        this.id = id;
        this.user = user;
        this.files = new ArrayList<FileEntity>();
        for (File file : files) {
            byte[] fileContent = new byte[(int) file.length()];
            try (FileInputStream fis = new FileInputStream(file)) {
                int bytesRead = fis.read(fileContent);
                if (bytesRead != fileContent.length) {
                    throw new IOException("Could not read the entire file");
                }
            }
            this.files.add(new FileEntity(fileContent));
        }
    }

    /**
     * Basic constructor for SubmissionEntity
     */

    public SubmissionEntity() {
    }

    /**
     * Getter for the id of the SubmissionEntity
     *
     * @return the id of the SubmissionEntity
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for the id of the SubmissionEntity
     *
     * @param id the id of the SubmissionEntity
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for the user of the SubmissionEntity
     *
     * @return the user of the SubmissionEntity
     */
    public User getUser() {
        return user;
    }

    /**
     * Setter for the user of the SubmissionEntity
     *
     * @param user the user of the SubmissionEntity
     */

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Getter for the files of the SubmissionEntity
     *
     * @return the files of the SubmissionEntity
     * @throws IOException if the files could not be read
     */
    public List<File> getFiles() throws IOException {
        List<File> files = new ArrayList<>();
        for (FileEntity fileContent : this.files) {
            try {
                File file = File.createTempFile("SubmissionEntity", ".tmp");
                file.deleteOnExit();
                try (FileOutputStream fis = new FileOutputStream(file)) {
                    fis.write(fileContent.getData());
                }
                files.add(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return files;
    }

    /**
     * Setter for the files of the SubmissionEntity
     *
     * @param files the files of the submission
     * @throws IOException if the files could not be read
     */
    public void setFiles(List<File> files) throws IOException {
        this.files = new ArrayList<>();
        for (File file : files) {
            byte[] fileContent = new byte[(int) file.length()];
            try (FileInputStream fis = new FileInputStream(file)) {
                int bytesRead = fis.read(fileContent);
                if (bytesRead != fileContent.length) {
                    throw new IOException("Could not read the entire file");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.files.add(new FileEntity(fileContent));
        }
    }
}