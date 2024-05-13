package org.example.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    /**
     * Constructor for FileUtils
     */
    public FileUtils() {}

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

    /**
     * Method for generating a PDF file from existing text and annotations
     * @param text the text modified by the user
     * @param annotations the annotations modified by the user
     * @throws IOException if the file cannot be created
     */
    public File generatePDF(String text, String annotations) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                writeToFile(contentStream, text, annotations);
            }

            File tempFile = File.createTempFile("exported", ".pdf");
            document.save(tempFile);
            document.close();

            return tempFile;
        }
    }

    /**
     * Helper method for writing text and annotations to a PDF file
     * @param contentStream the content stream to write to
     * @param text the text to be written
     * @param annotations the annotations to be written
     * @throws IOException if the text cannot be written
     */
    public void writeToFile(PDPageContentStream contentStream, String text, String annotations) throws IOException {
        PDType1Font titleFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        int titleFontSize = 16;

        PDType1Font contentFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        int contentFontSize = 14;

        text = text.replaceAll("\\r", "");
        text = text.replaceAll("\\n", " ");
        annotations = annotations.replaceAll("\\r", "");
        annotations = annotations.replaceAll("\\n", " ");

        contentStream.beginText();

        contentStream.setFont(titleFont, titleFontSize);
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText("Text:");

        contentStream.setFont(contentFont, contentFontSize);
        contentStream.newLine();
        contentStream.showText(text);

        contentStream.setFont(titleFont, titleFontSize);
        contentStream.newLine();
        contentStream.showText("Annotations:");

        contentStream.setFont(contentFont, contentFontSize);
        contentStream.newLine();
        contentStream.showText(annotations);

        contentStream.endText();
    }
}
