package org.example.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PDFTextStripperUtils extends PDFTextStripper {
    private List<List<TextPosition>> currentLine = new ArrayList<>();
    private List<List<List<TextPosition>>> allLines = new ArrayList<>();

    /**
     * Constructor for our custom PDFTextStripper
     */
    public PDFTextStripperUtils() {
        super();
    }

    @Override
    protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
        super.writeString(text, textPositions);

        currentLine.add(new ArrayList<>(textPositions));             //adding a word to the current line
    }

    @Override
    protected void writeLineSeparator() throws IOException {    //here current line contains the entire line
        super.writeLineSeparator();
        allLines.add(new ArrayList<>(currentLine));
        currentLine.clear();
    }

    @Override
    protected void endDocument(PDDocument document) throws IOException {    //need to add the last line
        allLines.add(new ArrayList<>(currentLine));
        super.endDocument(document);
    }

    /**
     * Returns all lines in the document
     * @return The lines containing words containing letters
     */
    public List<List<List<TextPosition>>> getAllLines() {
        return allLines;
    }


}
