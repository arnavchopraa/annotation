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
        float contentHeight = (contentFont.getFontDescriptor().getFontBoundingBox().getHeight() / 1000) * contentFontSize;

        // Write text content
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(titleFont, titleFontSize);
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText("Text:");
        contentStream.newLine();

        // Split text into multiple lines and handle word wrapping and page breaks
        String[] words = text.split("\\s+");
        StringBuilder line = new StringBuilder();
        float yOffset = 700; // Initial y-offset
        for (String word : words) {
            // Calculate the width of the current line
            float lineWidth = contentFont.getStringWidth(line + " " + word) / 1000 * contentFontSize;
            if (lineWidth > 400) { // Check if the line exceeds the page width
                contentStream.newLineAtOffset(0, -1.5f * contentHeight); // Move to the next line
                contentStream.setFont(contentFont, contentFontSize); // Set content font
                contentStream.showText(line.toString()); // Show the current line
                line.setLength(0); // Reset the line buffer
                line.append(word); // Start a new line with the current word
                yOffset -= 1.5f * contentHeight; // Update y-offset
                if (yOffset < 50) { // Check if there's enough space for the next line
                    contentStream.endText(); // End current text
                    contentStream.close(); // Close content stream

                    page = new PDPage(); // Create a new page
                    document.addPage(page); // Add page to document
                    contentStream = new PDPageContentStream(document, page); // Create new content stream
                    contentStream.beginText(); // Begin new text
                    contentStream.setFont(titleFont, titleFontSize); // Set title font
                    contentStream.newLineAtOffset(100, 700); // Set new y-offset
                    yOffset = 700; // Reset y-offset
                }
            } else {
                if (line.length() > 0) {
                    line.append(" "); // Add space between words
                }
                line.append(word); // Add the word to the current line
            }
        }
        // Show the remaining text on the last line
        if (line.length() > 0) {
            contentStream.newLineAtOffset(0, -1.5f * contentHeight);
            contentStream.setFont(contentFont, contentFontSize);
            contentStream.showText(line.toString());
        }

        contentStream.endText();
        contentStream.close();

        // Write annotations content on a new page
        page = new PDPage();
        document.addPage(page);
        contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(titleFont, titleFontSize);
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText("Annotations:");
        contentStream.newLine();

        // Split annotations into multiple lines and handle word wrapping and page breaks
        words = annotations.split("\\s+");
        line.setLength(0); // Reset the line buffer
        yOffset = 700; // Reset y-offset
        for (String word : words) {
            float lineWidth = contentFont.getStringWidth(line + " " + word) / 1000 * contentFontSize;
            if (lineWidth > 400) { // Check if the line exceeds the page width
                contentStream.newLineAtOffset(0, -1.5f * contentHeight); // Move to the next line
                contentStream.setFont(contentFont, contentFontSize); // Set content font
                contentStream.showText(line.toString()); // Show the current line
                line.setLength(0); // Reset the line buffer
                line.append(word); // Start a new line with the current word
                yOffset -= 1.5f * contentHeight; // Update y-offset
                if (yOffset < 50) { // Check if there's enough space for the next line
                    contentStream.endText(); // End current text
                    contentStream.close(); // Close content stream

                    page = new PDPage(); // Create a new page
                    document.addPage(page); // Add page to document
                    contentStream = new PDPageContentStream(document, page); // Create new content stream
                    contentStream.beginText(); // Begin new text
                    contentStream.setFont(titleFont, titleFontSize); // Set title font
                    contentStream.newLineAtOffset(100, 700); // Set new y-offset
                    yOffset = 700; // Reset y-offset
                }
            } else {
                if (line.length() > 0) {
                    line.append(" "); // Add space between words
                }
                line.append(word); // Add the word to the current line
            }
        }
        // Show the remaining text on the last line
        if (line.length() > 0) {
            contentStream.newLineAtOffset(0, -1.5f * contentHeight);
            contentStream.setFont(contentFont, contentFontSize);
            contentStream.showText(line.toString());
        }

        contentStream.endText();
        contentStream.close();
    }
}
