package org.example.backend.exceptions;

public class FileException extends Exception {
    /**
     * Default constructor for FileException - NoArgsConstructor
     */
    public FileException() {
        super();
    }

    /**
     * Constructor for FileException with a message
     *
     * @param message Exception message
     */
    public FileException(String message) {
        super(message);
    }

    /**
     * Constructor for FileException with another exception
     *
     * @param e other exception
     */
    public FileException(Exception e) {
        super(e);
    }
}
