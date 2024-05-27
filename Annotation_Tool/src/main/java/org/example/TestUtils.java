package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationHighlight;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Utility class, mostly used for testing. Has methods for: <p>
 * - Creating a PDF file <p>
 * - Converting a PDDocument to a java.io.File <p>
 * - Adding annotations to a PDDocument
 */
public class TestUtils {

    private final PDFont font = new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN);
    private final float fontSize = 12.0f;
    private final float xOffset = 20.0f;
    private final float yOffset = 700.0f;

    private static int fileNo = 0;

    /**
     * Generates a PDDocument from a string of text
     * @param text Given text
     * @return Generated PDDocument
     * @throws IOException if there are problems with creating the document
     */
    public PDDocument generatePDF(String text) throws IOException {
        PDDocument document = new PDDocument();
        PDPage firstPage = new PDPage();
        document.addPage(firstPage);

        PDPageContentStream pageContentStream = new PDPageContentStream(document, firstPage);
        pageContentStream.beginText();
        pageContentStream.setFont(font, fontSize);
        pageContentStream.newLineAtOffset(xOffset, yOffset);
        pageContentStream.showText(text);
        pageContentStream.endText();
        pageContentStream.close();

        return document;
    }

    /**
     * Converts a PDDocument to a java.io.File
     * @param document document to be converted
     * @return the resulting File
     * @throws IOException if File could not be created / converted
     */
    public File convertPDFtoFile(PDDocument document) throws IOException {
        String filename = "/test" + fileNo + ".pdf";
        final File file = new File(System.getProperty("java.io.tmpdir") + filename);
        fileNo++;
        //final File file = File.createTempFile("test", ".pdf");
        document.save(file);
        return file;
    }

    /**
     * Adds an annotation to the PDDocument to a predefined location. <p>
     * This method should only be used for testing purposes, as it has hardcoded behaviour <p>
     * This method can also be used for inspiration, in case future code requires adding annotations
     * @param document document to add annotations on
     * @param content content of annotation
     * @throws IOException if the annotation could not be generated
     */
    public void addAnnotation(PDDocument document, String content) throws IOException {
        PDAnnotationTextMarkup textMarkup = new PDAnnotationHighlight();
        textMarkup.setColor(new PDColor(new float[] {1, 1, 0}, PDDeviceRGB.INSTANCE));
        float width = font.getStringWidth("This is") / 1000 * fontSize;
        // Create rectangle that contains annotation, with position from width of annotated text.
        PDRectangle rectangle = new PDRectangle();
        rectangle.setLowerLeftX(xOffset);
        rectangle.setLowerLeftY(yOffset - 1); // Small offset added so whole text can fit in the rectangle
        rectangle.setUpperRightX(xOffset + width);
        rectangle.setUpperRightY(yOffset + fontSize - 1);
        textMarkup.setRectangle(rectangle);
        // Identify quads that define rectangle
        float[] quads = {rectangle.getLowerLeftX(), rectangle.getUpperRightY(),
                rectangle.getUpperRightX(), rectangle.getUpperRightY(),
                rectangle.getLowerLeftX(), rectangle.getLowerLeftY(),
                rectangle.getUpperRightX(), rectangle.getLowerLeftY()};

        textMarkup.setQuadPoints(quads);
        textMarkup.setContents(content);
        List<PDAnnotation> annotationList = document.getPage(0).getAnnotations();
        annotationList.add(textMarkup);
    }
}
