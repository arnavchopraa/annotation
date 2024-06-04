package org.example.backend.utils;

import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

@NoArgsConstructor
public class FileUtils {


    /**
     * Method for converting a Spring MultipartFile to a Java.io File
     *
     * @param multipartFile Spring file
     * @return Java.io file
     */
    public static File convertToFile(MultipartFile multipartFile) {
        if (multipartFile.getOriginalFilename() == null)
            throw new IllegalArgumentException("Filename cannot be null");
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
        try {
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(multipartFile.getBytes());
            stream.close();
            return file;
        } catch (Exception e) {
            throw new IllegalArgumentException("Not a file");
        }
    }

    /**
     * Method for getting the extension of a file
     *
     * @param file File for which to retrieve extension
     * @return File extension
     */
    public static String getFileExtension(File file) {
        String fileName = file.getName();
        int pos = fileName.lastIndexOf('.');
        return fileName.substring(pos);
    }
}