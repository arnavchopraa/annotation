package org.example.utils;

public class PairUtils {
    String text;
    String annotations;

    public PairUtils(String text, String annotations) {
        this.text = text;
        this.annotations = annotations;
    }

    /**
     * Getter for the text within the PDF.
     */
    public String getText() {
        return text;
    }

    /**
     * Getter for the annotations within the PDF.
     */
    public String getAnnotations() {
        return annotations;
    }

    /**
     * Setter for the text within the PDF.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Setter for the annotations within the PDF.
     */
    public void setAnnotations(String annotations) {
        this.annotations = annotations;
    }
}
