package org.example.backend.utils;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
public class PairUtils {
    String text;
    String annotations;
    String fileName;
    String captions;

    /**
     * Constructor for PairUtils
     * @param text the text within the PDF
     * @param annotations the annotations within the PDF
     * @param fileName the name of the file
     * @param captions the captions within the PDF
     */
    public PairUtils(String text, String annotations, String fileName, String captions) {
        this.text = text;
        this.annotations = annotations;
        this.fileName = removeFileExtension(fileName);
        this.captions = captions;
    }

    /**
     * Method for removing the file extension from the file name (e.g.: .pdf, .txt, etc)
     * @param fileName the name of the file
     * @return the name of the file without the extension
     */
    public String removeFileExtension(String fileName) {
        if(fileName.lastIndexOf('.') == -1)
            return fileName;
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

}
