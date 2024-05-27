package org.example.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Submissions")
public class Submission {
    @Id
    private Long id;

    @Column(name="user")
    private User user;

    @Column(name="files")
    private List<File> files;
    /**
     * Basic constructor for Submission
     *
     * @param id the id of the submission
     * @param user the user of the submission
     */
    public Submission(Long id, User user) {
        this.id = id;
        this.user = user;
        this.files = new ArrayList<>();
    }

    /**
     * Basic constructor for Submission
     *
     * @param id the id of the submission
     * @param user the user of the submission
     * @param file the file of the submission
     */
    public Submission(Long id, User user, File file){
        this.id = id;
        this.user = user;
        this.files = new ArrayList<>();
        this.files.add(file);
    }

    /**
     * Basic constructor for Submission
     *
     * @param id the id of the submission
     * @param user the user of the submission
     * @param files the files of the submission
     */
    public Submission(Long id, User user, List<File> files){
        this.id = id;
        this.user = user;
        this.files = files;
    }

    /**
     * Getter for the id of the submission
     *
     * @return the id of the submission
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for the id of the submission
     *
     * @param id the id of the submission
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for the user of the submission
     *
     * @return the user of the submission
     */
    public User getUser() {
        return user;
    }

    /**
     * Setter for the user of the submission
     *
     * @param user the user of the submission
     */

    public void setUser(User user) {
        this.user = user;
    }
}
