package org.example.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

@Entity
public class FileEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] data;

    @ManyToOne
    private Submission submission;

    public FileEntity(){

    }

    /**
     * Basic constructor for FileEntity
     *
     * @param data the data of the file
     */
    public FileEntity( byte[] data) {
        this.data = data;
    }

    /**
     * Getter for the id
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for the id
     *
     * @param id the id
     */

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for the data of the file
     *
     * @return the data of the file
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Setter for the data of the file
     *
     * @param data the data of the file
     */
    public void setData(byte[] data) {
        this.data = data;
    }
    // getters, setters, etc.
}