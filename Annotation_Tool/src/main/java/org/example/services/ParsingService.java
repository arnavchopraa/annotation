package org.example.services;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.text.TextPosition;
import org.example.exceptions.PDFException;
import org.example.utils.*;

import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.*;

public class ParsingService {

    private QueryService queryService = new QueryService();
    /**
     * Parses a pdf file including text and annotations
     * @param file the file that needs to be parsed
     * @return the parsed text
     * @throws PDFException if the file is not of type pdf
     */
    public PairUtils parsePDF(File file) throws PDFException {
        try {
            PDDocument document = Loader.loadPDF(file);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            // this gets rid of the references
            //CaptionExtractionService.imageCoordinates(file);
            // what about abstract??
            text = removeAbstract(text);
            // still need to separate references and appendices
            // remove bad hyphenation of the text (EOL)
            text = preprocess(text);
            String annotations = "";
            for(PDPage page : document.getPages()) {
                List<PDAnnotation> annotationList = page.getAnnotations();
                for(PDAnnotation a : annotationList) {
                    if(a.getSubtype().equals("Highlight")) {
                        //annotations = annotations + "\n" + getHighlightedText(a, page) + " - " + queryService.queryResults(a.getContents()) + "\n";
                        if (!annotations.equals(""))
                            annotations = annotations + "\n";
                        //annotations = annotations + getHighlightedText(a, page) + " - " + a.getContents() + "\n";
                        annotations = annotations + "\n" + getHighlightedText(a, page) + " - "
                                + preprocess(queryService.queryResults(a.getContents())) + "\n";
                    }
                    else if(a.getSubtype().equals("Text")) {
                        if (!annotations.equals(""))
                            annotations = annotations + "\n";
                        annotations = annotations + a.getContents() + "\n";
                    }
                }

                PageDrawerUtils pdu = new PageDrawerUtils(page);
                pdu.processPage(page);
                List<Line> lines = mergeLines(pdu.getLines());
                List<Table> tables = processLines(lines);
                text = removeTables(text, tables, page);
            }

            text = removeReferences(text, document);

            return new PairUtils(text, annotations, file.getName());

        } catch (IOException e) {
            throw new PDFException(file.getName());
        }
    }

    /**
     * Parses a pdf file including text and annotations using NER
     * @param file the file that needs to be parsed
     * @return the parsed text
     * @throws PDFException if the file is not of type pdf
     */
    public PairUtils parsePDFwithNer(File file) throws PDFException {
        try {
            PDDocument document = Loader.loadPDF(file);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);

            String annotations = "";
            for(PDPage page : document.getPages()) {
                List<PDAnnotation> annotationList = page.getAnnotations();
                for (PDAnnotation a : annotationList) {
                    if (a.getSubtype().equals("Highlight")) {
                        //annotations = annotations + "\n" + getHighlightedText(a, page) + " - " + queryService.queryResults(a.getContents()) + "\n";
                        if (!annotations.equals(""))
                            annotations = annotations + "\n";
                        annotations = annotations + getHighlightedText(a, page) + " - " + a.getContents() + "\n";
                    } else if (a.getSubtype().equals("Text")) {
                        if (!annotations.equals(""))
                            annotations = annotations + "\n";
                        annotations = annotations + a.getContents() + "\n";
                    }
                }
            }
            try {
                String nerScriptPath = "../../python/org/example/ner.py";
                ProcessBuilder pb = new ProcessBuilder("python3", nerScriptPath);
                Process process = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                String modifiedText = "";
                while ((line = reader.readLine()) != null) {
                    modifiedText += line;
                }
                return new PairUtils(modifiedText, annotations, file.getName());
            } catch (IOException e) {
                throw new PDFException(file.getName());
            }
        } catch (IOException e) {
            throw new PDFException(file.getName());
        }
    }

    /**
     * Recursively parses all pdfs in a given folder
     * @param file the folder from which to parse the files
     * @return the list of parsed texts from each file in the folder
     * @throws PDFException if one of the files is not of type pdf
     */
    public List<PairUtils> parseFilesFromFolder(File file) throws PDFException, IOException {
        List<PairUtils> parsed = new LinkedList<>();
        if(file.isDirectory()) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                if (f.isDirectory())
                    parsed.addAll(parseFilesFromFolder(f));
                else if (f.isFile())
                    parsed.add(parsePDF(f));
            }
        } else {
            throw new IOException("Uploaded file is not a folder");
        }

        return parsed;
    }

    /**
     * Parses all given pdfs
     * @param files the files to parse
     * @return the list of parsed texts from each file in the folder
     * @throws PDFException if one of the files is not of type pdf
     */
    public List<PairUtils> parseFilesList(File... files) throws PDFException, IOException {
        List<PairUtils> parsed = new LinkedList<>();

        for(File f : files) {
            if(f.isDirectory())
                parsed.addAll(parseFilesFromFolder(f));
            else if(f.isFile())
                parsed.add(parsePDF(f));
        }

        return parsed;
    }

    /**
     * Retrieves the highlighted text from an annotation
     * @param a the annotation from which to retrieve the highlighted text
     * @param page the page on which the annotation is situated
     * @return the highlighted text
     * @throws IOException if the text can't be read
     */
    public String getHighlightedText(PDAnnotation a, PDPage page) throws IOException {
        PDFTextStripperByArea stripperByArea = new PDFTextStripperByArea();

        COSArray quads = a.getCOSObject().getCOSArray(COSName.getPDFName("QuadPoints"));    //getting all the rectangles the annotation highlights
        String annot = "";

        int rectangle = 0;
        while(rectangle < quads.size()) {

            //getting the coordinates of the rectangle
            COSFloat ulx = (COSFloat) quads.get(0+rectangle);       //upper left x coordinate
            COSFloat uly = (COSFloat) quads.get(1+rectangle);       //upper left y coordinate
            COSFloat urx = (COSFloat) quads.get(2+rectangle);       //upper right x coordinate
            COSFloat ury = (COSFloat) quads.get(3+rectangle);       //upper right y coordinate
            COSFloat llx = (COSFloat) quads.get(4+rectangle);       //lower left x coordinate
            COSFloat lly = (COSFloat) quads.get(5+rectangle);       //lower left y coordinate

            float xStart = ulx.floatValue() - 1;                    //x coordinate at the top left of the rectangle
            float yStart = uly.floatValue();                        //y coordinate at the top left of the rectangle
            float width = urx.floatValue() - llx.floatValue();      //width of rectangle
            float height = ury.floatValue() - lly.floatValue();     //height of the rectangle

            PDRectangle pageSize = page.getMediaBox();
            yStart = pageSize.getHeight() - yStart;             //aligning box with corresponding page

            Rectangle2D.Float box = new Rectangle2D.Float(xStart, yStart, width, height);   //the box from which we will parse text
            stripperByArea.addRegion("annotation", box);                          //set the region from which we will extract
            stripperByArea.extractRegions(page);                                            //set the page from which we will extract

            annot = annot + stripperByArea.getTextForRegion("annotation");

            rectangle = rectangle + 8;                                                      //moving to the next rectangle
        }

        //annot = annot.replace("\n", "");

        return annot;
    }

    /**
     * This method removes hyphenation from end of lines.
     *
     * @param text the string we want to remove unnecessary hyphenation from
     * @return the processed text
     */
    public String preprocess(String text) {
        int i = 0;
        StringBuilder sb = new StringBuilder();
        while(i < text.length()) {
            try {
                if(text.charAt(i) == '-' && text.charAt(i - 1) != ' ') {
                    if(text.charAt(i + 1) == '\r' && text.charAt(i + 2) == '\n') {
                        // skip over the indices containing EOL characters
                        i += 3;
                        while(text.charAt(i) != ' ') {
                            // check if the next characters are EOL such that to not search next space
                            if(text.charAt(i) == '\r') {
                                i ++;
                                break;
                            }
                            sb.append(text.charAt(i));
                            i ++;
                        }
                        // after the word is finished add EOL
                        sb.append("\r\n");
                    }
                    // hyphen is in the middle of the line
                    else {
                        sb.append(text.charAt(i));
                    }
                }
                // there is no hyphen
                else {
                    sb.append(text.charAt(i));
                }
                i ++;
            }
            // if the hyphen goes past the last character of the file
            catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * This method removes all content presented before the abstract
     *
     * @param text the text we want to remove the lines before the abstract from
     * @return processed text
     */
    public String removeAbstract(String text) {
        int index = text.indexOf("Abstract\r\n");
        // skip over the Abstract\r\n characters (Abstract\r\n is 10 characters)
        if(index != -1)
            return text.substring(index + 10);
        return text;
    }

    /**
     * Given a list of lines, separates tables
     * @param lines list of identified lines
     * @return list of identified table coordinates
     */
    public List<Table> processLines(List<Line> lines) {
        List<Line> horizontalLines = new ArrayList<>();
        List<Line> verticalLines = new ArrayList<>();
        List<Table> tables = new ArrayList<>();
        Map<Line, Integer> correspondingTable = new HashMap<>();

        for(Line l : lines) {           //splitting vertical and horizontal lines
            if(l.isVertical())
                verticalLines.add(l);
            else
                horizontalLines.add(l);
        }

        for(Line horizontal : horizontalLines) {
            Table table = new Table(horizontal.getStartX(), horizontal.getStartY(), horizontal.getEndX(), horizontal.getEndY());
            List<Line> addedLines = new ArrayList<>();
            int pos = -1;

            for(Line vertical : verticalLines) {
                if(horizontal.intersectsWith(vertical)) {
                    if(correspondingTable.containsKey(vertical)) {
                        pos = correspondingTable.get(vertical);
                        table = tables.get(pos);

                        table.combineTable(horizontal);         //what if we need to combine 2 tables?
                    }
                    else {
                        table.combineTable(vertical);
                        addedLines.add(vertical);
                    }
                }
            }

            if(pos == -1) {
                tables.add(table);
                for(Line l : addedLines) {
                    correspondingTable.put(l, tables.size() - 1);
                }
            }
            else {
                for(Line l : addedLines) {
                    correspondingTable.put(l, pos);
                }
            }
        }
        return tables;
    }

    /**
     * Given a list of Lines, merge lines that are close to each other
     * @param lines list of lines extracted
     * @return list of lines after executing the merges
     */
    public List<Line> mergeLines(List<Line> lines) {
        float error = 0.4f; // define error parameter; Observed error is always 0.398, leave room for precision errors

        Collections.sort(lines);
        boolean changes = true;
        while(changes) {
            changes = false;
            for(int i = 0;i < lines.size();i++) {
                Line current = lines.get(i);
                for(int j = i + 1;j < lines.size();j++) {
                    Line other = lines.get(j);
                    if(current.mergeWith(other, error)) {
                        changes = true;
                        lines.remove(j);
                    }
                }
            }
        }
        return lines;
    }

    /**
     * Remove the text present in the given tables from the specified text
     * @param text Text to remove from
     * @param tables Tables which contain the text that needs to be removed
     * @param page Page in which the tables are located
     * @return The initial text without the text in the tables
     * @throws IOException if the text can't be read
     */
    public String removeTables(String text, List<Table> tables, PDPage page) throws IOException {
        PDFTextStripperByArea stripperByArea = new PDFTextStripperByArea();
        for(Table t : tables) {
            float xStart = t.getTopLeftX();
            float yStart = t.getBottomRightY();
            float width = t.getBottomRightX() - xStart;
            float height = t.getBottomRightY() - t.getTopLeftY();

            PDRectangle pageSize = page.getMediaBox();
            yStart = pageSize.getHeight() - yStart;

            Rectangle2D.Float box = new Rectangle2D.Float(xStart, yStart, width, height);
            stripperByArea.addRegion("table", box);
            stripperByArea.extractRegions(page);

            String remove = stripperByArea.getTextForRegion("table");

            text = text.replace(remove, "");
        }
        return text;
    }

    /**
     * Removes the references in the document from the given text
     * @param text Text that needs references removed
     * @param document Document which we are parsing
     * @return Updated text with references removed
     * @throws IOException When parsing the document fails
     */
    public String removeReferences(String text, PDDocument document) throws IOException {

        PDFTextStripperUtils ptsu = new PDFTextStripperUtils();
        ptsu.getText(document);

        String lastLine = findLastLineFromReferences(ptsu.getAllLines());

        if(lastLine == null)
            return text;

        String[] cut1 = text.split("\r\nReferences\r\n");

        if(cut1.length == 1)
            cut1 = text.split("\r\nreferences\r\n");

        if(cut1.length == 1)
            return text;

        String[] cut2 = cut1[1].split(lastLine);

        text = cut1[0] + "\r\n" + cut2[1];

        return text;
    }

    /**
     * Finds the text from the last line in the references subsection
     * @param allLines All lines contained in the document
     * @return Text from the last line in the references
     */
    public String findLastLineFromReferences(List<List<List<TextPosition>>> allLines) {
        //first we need to find the "References" line

        List<String> referencesUnicode = new ArrayList<>(); //we will use this to compare it with each line
        referencesUnicode.add("R");
        referencesUnicode.add("e");
        referencesUnicode.add("f");
        referencesUnicode.add("e");
        referencesUnicode.add("r");
        referencesUnicode.add("e");
        referencesUnicode.add("n");
        referencesUnicode.add("c");
        referencesUnicode.add("e");
        referencesUnicode.add("s");

        int lineNumber = 0;
        boolean found = false;
        String font = null;
        float height = 0;


        while(lineNumber < allLines.size() && !found) {
            List<List<TextPosition>> line = allLines.get(lineNumber);

            if(line.size() > 1) {                             //if it's more than 1 word it can't be references
                lineNumber++;
                continue;
            }

            List<TextPosition> word = line.get(0);

            if(word.size() != referencesUnicode.size()) {   //if the word is not of the same size it can't be references
                lineNumber++;
                continue;
            }

            //if it doesn't start with r/R it can't be references
            //we check the first letter manually in case it is not capitalised
            if(!Objects.equals(word.get(0).toString(), "r") && !Objects.equals(word.get(0).toString(), "R")) {
                lineNumber++;
                continue;
            }

            int i = 1;

            while(i < word.size() && Objects.equals(word.get(i).toString(), referencesUnicode.get(i)))
                i++;

            if(i == word.size()) {                               //if we reached the end it's the same word
                found = true;
                font = word.get(0).getFont().getName();
                height = word.get(0).getHeightDir();

            }

            lineNumber++;
        }

        if(!found)              //there are no references
            return null;

        lineNumber = findNextSubsectionTitleLine(lineNumber, allLines, font, height);

        lineNumber--;

        String lastLine = "\r\n";
        List<List<TextPosition>> line = allLines.get(lineNumber);

        for(List<TextPosition> word : line) {
            for (TextPosition letter : word) {
                String currentFont = letter.getFont().getName();
                float currentHeight = letter.getHeightDir();

                if (!(currentFont.contains(font) && currentHeight == height))
                    lastLine = lastLine + letter;
            }
            lastLine = lastLine + " ";
        }

        lastLine = lastLine.trim();             //removing the last "_"
        return lastLine + "\r\n";
    }

    /**
     * Finds the line number of the next subsection title
     * @param lineNumber Line number of the current subsection title
     * @param allLines All lines in the document
     * @param font The font of the title
     * @param height The size of the title
     * @return Line number of the next subsection title
     */
    public int findNextSubsectionTitleLine(int lineNumber, List<List<List<TextPosition>>> allLines, String font, float height) {
        //finding the next subsection title

        boolean found = false;
        while(lineNumber < allLines.size() && !found) {
            List<List<TextPosition>> line = allLines.get(lineNumber);
            for(List<TextPosition> word : line)
                for(TextPosition letter : word) {
                    String currentFont = letter.getFont().getName();
                    float currentHeight = letter.getHeightDir();

                    //the letters have to be of the same font and size as "References" title
                    if (currentFont.contains(font) && currentHeight == height)
                        found = true;
                }

            lineNumber++;
        }

        return lineNumber;
    }
}
