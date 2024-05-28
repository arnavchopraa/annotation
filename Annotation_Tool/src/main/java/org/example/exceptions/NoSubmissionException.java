package org.example.exceptions;

public class NoSubmissionException extends Exception {
    public NoSubmissionException () {
        super();
    }

    public NoSubmissionException (String message) {
        super(message);
    }
}
