package org.example.backend.utils;

import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomPDFTextStripperByArea extends PDFTextStripperByArea {
    List<Float> yCoordinates = new ArrayList<>();

    /**
     * Default constructor.
     *
     * @throws IOException If there is an error loading properties.
     */
    public CustomPDFTextStripperByArea() throws IOException {
        super();
    }

    @Override
    protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
        TextPosition firstPosition = textPositions.get(0);

        float yStart = firstPosition.getYDirAdj();

        yCoordinates.add(yStart);
    }

    /**
     * Getter for the yCoordinates.
     *
     * @return List of yCoordinates.
     */
    public List<Float> getYCoordinates() {
        return yCoordinates;
    }
}
