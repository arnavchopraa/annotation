package org.example.utils;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class PairUtils {
    String text;
    String annotations;
    String fileName;

    /**
     * Method for removing the file extension from the file name (e.g.: .pdf, .txt, etc)
     * @param fileName the name of the file
     * @return the name of the file without the extension
     */
    public String removeFileExtension(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.'));
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
     * Getter for the file name.
     */
    public String getFileName() {
        return fileName;
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

    /**
     * Setter for the file name.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
