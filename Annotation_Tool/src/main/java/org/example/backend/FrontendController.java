package org.example.backend;

import org.example.exceptions.FileException;
import org.example.exceptions.ImportException;
import org.example.exceptions.NoSubmissionException;
import org.example.exceptions.PDFException;
import org.example.importmodels.Coordinator;
import org.example.services.FileService;
import org.example.services.ImportService;
import org.example.services.QueryService;
import org.example.utils.FileUtils;
import org.example.services.ParsingService;
import org.example.utils.PairUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.zip.ZipException;

@RestController
@CrossOrigin(origins = "*")
public class FrontendController {

    private final ParsingService parsingService = new ParsingService();
    private final FileService fileService = new FileService();

    /**
     * POST - Endpoint for retrieving pdf files from frontend, and passing them to backend
     * @param file received file from frontend
     * @return 200 OK - Parsed text from the file
     *         400 Bad Request - Conversion to PDF fails
     *         400 Bad Request - Parsing PDF fails
     */
    @PostMapping("/frontend")
    public ResponseEntity<PairUtils> retrieveFile(@RequestParam("file") MultipartFile file) {
        try {
            File aPDFFile = FileUtils.convertToFile(file);
            PairUtils result = parsingService.parsePDF(aPDFFile);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new PairUtils(e.getMessage(), null, "invalid"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (PDFException e) {
            return new ResponseEntity<>(new PairUtils(e.getMessage(), null, "invalid"), HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<byte[]> exportPDF(@RequestParam("text") String text, @RequestParam("annotations") String annotations) {
        try {
            byte[] pdfBytes = fileService.generatePDF(text, annotations);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "exported.pdf");
            headers.setContentLength(pdfBytes.length);
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * GET - Endpoint for retrieving the list of codes from the database
     * @return 200 OK - List of codes
     */
    @GetMapping("/frontend/codes")
    public List<String> getCodes() {
        File zip = new File("C:\\Users\\stefa\\Downloads\\Test_groups_3_Download_28_May_2024_1937_Group_Submission_Folder.zip");
        File csv = new File("C:\\Users\\stefa\\Downloads\\tud-sndbx-mmigut.csv");
        File xlsx = new File("C:\\Users\\stefa\\Downloads\\Example_Group.xlsx");

        ImportService is = new ImportService();
        try {
            List<Coordinator> coordinators = is.importData(zip, csv, xlsx);
            for(Coordinator c : coordinators)
                System.out.println(c);
        } catch (NoSubmissionException e) {
            throw new RuntimeException(e);
        } catch (ZipException e) {
            throw new RuntimeException(e);
        } catch (FileException e) {
            throw new RuntimeException(e);
        } catch (ImportException e) {
            throw new RuntimeException(e);
        }

        String query = "SELECT id FROM annotations";
        return QueryService.queryExecution(query);
    }
}
