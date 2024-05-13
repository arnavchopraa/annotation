package org.example.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
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
    public byte[] generatePDF(String text, String annotations) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             PDDocument document = new PDDocument()) {

            // PDFBox cannot parse newlines and carriage returns
            // Replace after data pre-processing is finished
            text = text.replaceAll("\\r", "");
            annotations = annotations.replaceAll("\\r", "");

            writeToFile(document, text, annotations);

            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }

    /**
     * Helper method for writing text and annotations to a PDF file
     * @param text the text to be written
     * @param annotations the annotations to be written
     * @throws IOException if the text cannot be written
     */
    public void writeToFile(PDDocument document,
                            String text, String annotations) throws IOException {
        PDType1Font titleFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        int titleFontSize = 16;

        PDType1Font contentFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        int contentFontSize = 14;

        // Calculate text height
        float textHeight = (contentFont.getFontDescriptor().getFontBoundingBox().getHeight() / 1000) * contentFontSize;

        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        contentStream.beginText();

        contentStream.setFont(titleFont, titleFontSize);
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText("Text:");

        // Split text into multiple lines
        String[] textLines = text.split("\\n");
        float yOffset = 700; // Initial y-offset
        for (String line : textLines) {
            float lineWidth = contentFont.getStringWidth(line) / 1000 * contentFontSize;
            if (yOffset - 1.5 * textHeight < 50) { // Check if there's enough space for the next line
                contentStream.endText();
                contentStream.close();

                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);

                contentStream.beginText();
                contentStream.setFont(titleFont, titleFontSize);
                contentStream.newLineAtOffset(100, 700);
                yOffset = 700;
            }

            contentStream.newLineAtOffset(0, (float)-1.5 * textHeight); // Adjust vertical offset
            contentStream.setFont(contentFont, contentFontSize);
            contentStream.showText(line); // Show text without adding dot
            yOffset -= 1.5 * textHeight; // Update yOffset
        }

        // Calculate annotations height
        float annotationsHeight = (contentFont.getFontDescriptor().getFontBoundingBox().getHeight() / 1000) * contentFontSize;

        // Split annotations into multiple lines
        String[] annotationsLines = annotations.split("\\n");
        for (String line : annotationsLines) {
            float lineWidth = contentFont.getStringWidth(line) / 1000 * contentFontSize;
            if (yOffset - 1.5 * annotationsHeight < 50) { // Check if there's enough space for the next line
                contentStream.endText();
                contentStream.close();

                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);

                contentStream.beginText();
                contentStream.setFont(titleFont, titleFontSize);
                contentStream.newLineAtOffset(100, 700);
                yOffset = 700;
            }
            contentStream.newLineAtOffset(0, (float)-1.5 * annotationsHeight); // Adjust vertical offset
            contentStream.setFont(contentFont, contentFontSize);
            contentStream.showText(line);
            yOffset -= 1.5 * annotationsHeight; // Update yOffset
        }

        contentStream.endText();
        contentStream.close();
    }
}
