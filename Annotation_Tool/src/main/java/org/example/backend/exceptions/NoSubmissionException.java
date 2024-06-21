package org.example.backend.exceptions;

import java.io.Serial;

public class NoSubmissionException extends Exception {
    @Serial
    private static final long serialVersionUID = 3556320213203120977L;

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

    /**
     * Constructor for NoSubmissionException with another exception
     *
     * @param e other exception
     */
    public NoSubmissionException(Exception e) {
        super(e);
    }
}
