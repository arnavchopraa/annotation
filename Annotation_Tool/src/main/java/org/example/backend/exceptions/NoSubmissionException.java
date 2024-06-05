package org.example.backend.exceptions;

public class NoSubmissionException extends Exception {
    /**
     * Default constructor for NoSubmissionException - NoArgsConstructor
     */
    public NoSubmissionException () {
        super();
    }

    /**
     * Constructor for NoSubmissionException with a message
     *
     * @param message Exception message
     */
    public NoSubmissionException (String message) {
        super(message);
    }
}
