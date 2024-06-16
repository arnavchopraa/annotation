package org.example.backend.services;

import org.example.backend.exceptions.PDFException;
import org.example.backend.models.SubmissionDB;
import org.example.backend.utils.PairUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ExportService {

    private final SubmissionService submissionService;
    private final ParsingService parsingService;

    /**
     * Constructor for ExportService, Autowired by Spring
     * @param submissionService Service used to manage submissions
     * @param annotationCodeService Service used to manage annotation codes
     */
    @Autowired
    public ExportService(SubmissionService submissionService, AnnotationCodeService annotationCodeService) {
        this.submissionService = submissionService;
        this.parsingService = new ParsingService(annotationCodeService);
    }

    /**
     * Method that allows mass download of all files, allocated to a coordinator, into a zip file
     *
     * @param coordinator coordinator for which to download associated submissions
     * @return zipFile containing all submissions
     * @throws IOException if zip file could not be opened
     * @throws SQLException if there is an error with processing blobs
     */
    public File getAllSubmissionsByCoordinator(String coordinator) throws IOException, SQLException {
        List<SubmissionDB> files = submissionService.getCoordinatorsSubmissions(coordinator).stream()
            .filter(s -> s.getAssignedCoordinator().equals(coordinator))
            .toList();
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + "download.zip");
        OutputStream outputStream = new FileOutputStream(file);
        ZipOutputStream zos = new ZipOutputStream(outputStream);
        for(SubmissionDB submission : files) {
            Blob b = submission.getFileSubmission();
            byte[] content = b.getBytes(1,(int) b.length());
            String fileName = submission.getFileName();
            if(fileName.lastIndexOf(".") == -1)
                fileName = fileName + ".pdf";
            fileName = submission.getId() + " - " + fileName;
            try {
                zos.putNextEntry(new ZipEntry(fileName));
                zos.write(content, 0, content.length);
            } catch (FileNotFoundException e) {
                throw new IOException(e);
            }
            zos.closeEntry();
        }
        zos.close();
        outputStream.close();
        return file;
    }

    /**
     * Method that returns all submissions from the database, after applying the parse operation on them all.
     *
     * @return .zip file, containing many text files, each containing the parsed version of the PDF from the database.
     * @throws IOException If something went wrong with temporary files
     * @throws SQLException If something went wrong with retrieving files
     * @throws PDFException If something went wrong while parsing
     */
    public File getAllSubmissionsParsed() throws IOException, SQLException, PDFException {
        List<SubmissionDB> submissions = submissionService.getSubmissions();
        File file = new File(System.getProperty("java.io.tmpdir") + "/download.zip");
        OutputStream outputStream = new FileOutputStream(file);
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        for(SubmissionDB submission : submissions) {
            Blob fileSubmission = submission.getFileSubmission();
            if(fileSubmission == null)
                continue;
            byte[] submissionBytes = fileSubmission.getBytes(1,(int) fileSubmission.length());

            File textFile = new File(System.getProperty("java.io.tmpdir") + "/text.pdf");
            OutputStream textOutputStream = new FileOutputStream(textFile);
            textOutputStream.write(submissionBytes);
            textOutputStream.close();
            PairUtils result;
            try {
                result = parsingService.parsePDF(textFile);
            } catch (PDFException e) {
                throw new PDFException("Something went wrong while parsing file: " + submission.getFileName() + ", submitted by: " +
                        submission.getId() + ". The error encountered is: " + e.getMessage());
            }
            String content = result.getText() + "\n----------Annotations----------\n\n" + result.getAnnotations();
            String fileName = result.removeFileExtension(submission.getFileName());
            fileName = submission.getId() + " - " + fileName + ".txt";
            zipOutputStream.putNextEntry(new ZipEntry(fileName));
            zipOutputStream.write(content.getBytes(), 0, content.getBytes().length);
            zipOutputStream.closeEntry();
        }
        zipOutputStream.close();
        outputStream.close();

        return file;
    }
}
