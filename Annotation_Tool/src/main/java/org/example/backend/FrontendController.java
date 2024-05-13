package org.example.backend;

import jakarta.servlet.http.HttpServletResponse;
import org.example.exceptions.PDFException;
import org.example.utils.FileUtils;
import org.example.services.ParsingService;
import org.example.utils.PairUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
public class FrontendController {

    private final ParsingService parsingService = new ParsingService();
    private final FileUtils fileUtils = new FileUtils();

    /**
     * POST - Endpoint for retrieving pdf files from frontend, and passing them to backend
     * @param file received file from frontend
     * @return 200 OK - Parsed text from the file
     *         400 Bad Request - Conversion to PDF fails
     *         400 Bad Request - Parsing PDF fails
     */
    @PostMapping("/frontend")
    public ResponseEntity<PairUtils> retrieveFile(@RequestParam("file") MultipartFile file) {
        File PDFFile;
        try {
            PDFFile = FileUtils.convertToFile(file);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new PairUtils(e.getMessage(), null, null), HttpStatus.BAD_REQUEST);
        }

        PairUtils result;
        try {
            result = parsingService.parsePDF(PDFFile);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (PDFException e) {
            return new ResponseEntity<>(new PairUtils(e.getMessage(), null, null), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * POST - Endpoint for exporting the parsed text and annotations to a PDF file
     * @param text the text to be exported
     * @param annotations the annotations to be exported
     * @return 200 OK - The exported PDF file
     *        400 Bad Request - The file cannot be created
     */
    @PostMapping("/frontend/export")
    public ResponseEntity<File> exportPDF(@RequestParam("text") String text, @RequestParam("annotations") String annotations) {
        try {
            File result = fileUtils.generatePDF(text, annotations);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new File(""), HttpStatus.BAD_REQUEST);
        }
    }
}
