package org.example.backend.exceptions;

import java.io.Serial;

public class PDFException extends Exception {

    @Serial
    private static final long serialVersionUID = 6461788465605399002L;

    /**
     * No argument exception
     */
    public PDFException() {
        super();
    }

    /**
     * Exception with an argument
     * @param text the text contained in the exception
     */
    public PDFException(String text) {
        super(text);
    }

    /**
     * Constructor for PDFException with another exception
     *
     * @param e other exception
     */
    public PDFException(Exception e) {
        super(e);
    }

}
