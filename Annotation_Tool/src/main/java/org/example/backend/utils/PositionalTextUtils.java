package org.example.backend.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.text.TextPosition;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PositionalTextUtils extends PDFTextStripperByArea {
    private final List<CoordPairs> textBlocks = new ArrayList<>();

    /**
     * Default constructor.
     *
     * @throws IOException If there is an error loading properties.
     */
    public PositionalTextUtils() throws IOException {
        super();
    }

    /**
     * Override the writeString method to extract the text and its position.
     *
     * @param text The text to write to the stream.
     * @param textPositions The TextPositions belonging to the text.
     * @throws IOException If there is an error when writing the text.
     */
    @Override
    protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
        if (!textPositions.isEmpty()) {
            TextPosition firstPosition = textPositions.get(0);
            TextPosition lastPosition = textPositions.get(textPositions.size() - 1);
            Rectangle2D.Float rect = new Rectangle2D.Float(
                firstPosition.getXDirAdj(),
                firstPosition.getYDirAdj(),
                lastPosition.getXDirAdj() - firstPosition.getXDirAdj(),
                lastPosition.getYDirAdj() - firstPosition.getYDirAdj()
            );

            // Logging for debugging
            //System.out.println("Extracted text: " + text + " in region " + rect);

            textBlocks.add(new CoordPairs(rect, text));
        }
    }

    /**
     * Extracts text blocks from the PDF document.
     *
     * @param document The PDF document to extract text blocks from.
     * @return List of CoordPairs containing the text and its position.
     * @throws IOException If there is an error when extracting text blocks.
     */

    public List<CoordPairs> getTextBlocks(PDDocument document) throws IOException {
        for (PDPage page : document.getPages()) {
            // Define region for the whole page
            this.addRegion("fullPage", new Rectangle2D.Float(
                0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight()));
            this.extractRegions(page);
        }
        return textBlocks;
    }
}
