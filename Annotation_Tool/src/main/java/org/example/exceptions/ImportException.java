package org.example.exceptions;

public class ImportException extends Exception {
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
}
