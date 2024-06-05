package org.example.backend.exceptions;

public class EmailException extends Exception {
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
