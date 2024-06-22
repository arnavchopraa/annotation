package org.example.backend.exceptions;

import java.io.Serial;

public class ImportException extends Exception{

    @Serial
    private static final long serialVersionUID = -3759525805074320742L;

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
