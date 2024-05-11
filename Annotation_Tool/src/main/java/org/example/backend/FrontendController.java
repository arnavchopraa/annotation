package org.example.backend;

import org.example.exceptions.PDFException;
import org.example.services.FileUtils;
import org.example.services.ParsingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@CrossOrigin(origins = "*")
public class FrontendController {

    private final ParsingService parsingService = new ParsingService();

    /**
     * POST - Endpoint for retrieving pdf files from frontend, and passing them to backend
     * @param file received file from frontend
     * @return 200 OK - Parsed text from the file
     *         400 Bad Request - Conversion to PDF fails
     *         400 Bad Request - Parsing PDF fails
     */
    @PostMapping("/frontend")
    public ResponseEntity<String> retrieveFile(@RequestParam("file") MultipartFile file) {
        File PDFFile;
        try {
            PDFFile = FileUtils.convertToFile(file);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        String result;
        try {
            result = parsingService.parsePDF(PDFFile);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (PDFException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
