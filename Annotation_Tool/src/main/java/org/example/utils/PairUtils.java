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

}
