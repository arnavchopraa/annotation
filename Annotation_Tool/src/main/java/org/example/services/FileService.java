package org.example.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FileService {
    PDType1Font titleFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
    int titleFontSize = 16;

    PDType1Font contentFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    int contentFontSize = 14;

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

        float contentHeight = (contentFont.getFontDescriptor().getFontBoundingBox().getHeight() / 1000) * contentFontSize;

        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        writeTitle(contentStream, "Text");

        // Split text into multiple lines and handle word wrapping and page breaks
        String[] words = text.split("\\s+");
        StringBuilder line = new StringBuilder();

        float yOffset = 700;
        for (String word : words) {
            // Calculate the width of the current line
            float lineWidth = contentFont.getStringWidth(line + " " + word) / 1000 * contentFontSize;

            if (lineWidth > 400) { // Check if the line exceeds the page width
                contentStream.newLineAtOffset(0, -1.5f * contentHeight); // Move to the next line
                contentStream.setFont(contentFont, contentFontSize);
                contentStream.showText(line.toString()); // Show the current line

                line.setLength(0); // Reset the line buffer
                line.append(word); // Start a new line with the current word

                yOffset -= 1.5f * contentHeight; // Update y-offset
                if (yOffset < 50) { // Check if there's enough space for the next line
                    closePage(contentStream);

                    // Create a new page
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);

                    yOffset = createNewPage(contentStream);
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

        closePage(contentStream);

        // Write annotations content on a new page
        page = new PDPage();
        document.addPage(page);
        contentStream = new PDPageContentStream(document, page);
        writeTitle(contentStream, "Annotations");

        // Split annotations into multiple lines and handle word wrapping and page breaks
        words = annotations.split("\\s+");
        line.setLength(0); // Reset the line buffer
        yOffset = 700; // Reset y-offset
        for (String word : words) {
            float lineWidth = contentFont.getStringWidth(line + " " + word) / 1000 * contentFontSize;
            if (lineWidth > 400) { // Check if the line exceeds the page width
                contentStream.newLineAtOffset(0, -1.5f * contentHeight); // Move to the next line
                contentStream.setFont(contentFont, contentFontSize);
                contentStream.showText(line.toString()); // Show the current line

                line.setLength(0); // Reset the line buffer
                line.append(word); // Start a new line with the current word

                yOffset -= 1.5f * contentHeight; // Update y-offset
                if (yOffset < 50) { // Check if there's enough space for the next line
                    closePage(contentStream);

                    // Create a new page
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);

                    yOffset = createNewPage(contentStream);
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

        closePage(contentStream);
    }


    /**
     * Helper method for writing the title of a section to a PDF file
     * @param contentStream the content stream to write to
     * @param title the title to write
     * @throws IOException if the title cannot be written
     */
    public void writeTitle(PDPageContentStream contentStream, String title) throws IOException {
        contentStream.beginText();

        contentStream.setFont(titleFont, titleFontSize);
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText(title + ":");

        contentStream.newLine();
    }

    /**
     * Helper method for creating a new page in a PDF file
     * @param contentStream the content stream to write to
     * @return the y-offset for the new page
     * @throws IOException if the new page cannot be created
     */
    public int createNewPage(PDPageContentStream contentStream) throws IOException {
        contentStream.beginText();

        contentStream.setFont(contentFont, contentFontSize);
        contentStream.newLineAtOffset(100, 700);
        return 700; // Reset y-offset
    }

    /**
     * Helper method for closing a page in a PDF file
     * @param contentStream the content stream to write to
     * @throws IOException if the page cannot be closed
     */
    public void closePage(PDPageContentStream contentStream) throws IOException {
        contentStream.endText();
        contentStream.close();
    }
}