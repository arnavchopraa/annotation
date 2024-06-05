package org.example.backend.utils;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
public class PairUtils {
    String text;
    String annotations;
    String fileName;

    /**
     * Constructor for PairUtils
     * @param text the text within the PDF
     * @param annotations the annotations within the PDF
     * @param fileName the name of the file
     */
    public PairUtils(String text, String annotations, String fileName) {
        this.text = text;
        this.annotations = annotations;
        this.fileName = removeFileExtension(fileName);
    }

    /**
     * Method for removing the file extension from the file name (e.g.: .pdf, .txt, etc)
     * @param fileName the name of the file
     * @return the name of the file without the extension
     */
    public String removeFileExtension(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

}
