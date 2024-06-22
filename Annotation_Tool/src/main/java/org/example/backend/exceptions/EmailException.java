package org.example.backend.exceptions;

import java.io.Serial;

public class EmailException extends Exception {
    @Serial
    private static final long serialVersionUID = 3149898318595426846L;

    /**
     * Default constructor for EmailException - NoArgsConstructor
     */
    public EmailException() {
        super();
    }

    /**
     * Constructor for EmailException with a message
     *
     * @param message Exception message
     */
    public EmailException(String message) {
        super(message);
    }

    /**
     * Constructor for EmailException with another exception
     *
     * @param e other exception
     */
    public EmailException(Exception e) {
        super(e);
    }
}
