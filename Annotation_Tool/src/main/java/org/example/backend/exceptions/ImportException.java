package org.example.backend.exceptions;

public class ImportException extends Exception{

    /**
     * Default constructor for ImportException - NoArgsConstructor
     */
    public ImportException() {
        super();
    }

    /**
     * Constructor for ImportException with a message
     *
     * @param message Exception message
     */
    public ImportException(String message) {
        super(message);
    }

    /**
     * Constructor for ImportException with another exception
     *
     * @param e other exception
     */
    public ImportException(Exception e) {
        super(e);
    }
}
