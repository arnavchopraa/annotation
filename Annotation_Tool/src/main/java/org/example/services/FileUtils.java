package org.example.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    /**
     * Method for converting a Spring MultipartFile to a Java.io File
     * @param multipartFile Spring file
     * @return Java.io file
     */
    public static File convertToFile(MultipartFile multipartFile) {
        if(multipartFile.getOriginalFilename() == null)
            throw new IllegalArgumentException("Filename cannot be null");
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
        try {
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(multipartFile.getBytes());
            stream.close();
            return file;
        } catch(Exception e) {
            throw new IllegalArgumentException("Not a file");
        }
    }
}
