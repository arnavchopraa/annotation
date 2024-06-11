package org.example.backend.exceptions;

public class PDFException extends Exception {

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
