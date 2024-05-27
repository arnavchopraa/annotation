package org.example.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] data;

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