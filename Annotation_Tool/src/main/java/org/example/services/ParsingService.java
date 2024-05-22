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
import org.example.exceptions.PDFException;
import org.example.utils.PairUtils;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

            String annotations = "";
            for(PDPage page : document.getPages()) {
                List<PDAnnotation> annotationList = page.getAnnotations();
                for(PDAnnotation a : annotationList) {
                    if(a.getSubtype().equals("Highlight")) {
                        //annotations = annotations + "\n" + getHighlightedText(a, page) + " - " + queryService.queryResults(a.getContents()) + "\n";
                        if (!annotations.equals(""))
                            annotations = annotations + "\n";
                        annotations = annotations + getHighlightedText(a, page) + " - " + a.getContents() + "\n";
                    }
                    else if(a.getSubtype().equals("Text")) {
                        if (!annotations.equals(""))
                            annotations = annotations + "\n";
                        annotations = annotations + a.getContents() + "\n";
                    }
                }
            }

            return new PairUtils(text, annotations, file.getName());

        } catch (IOException e) {
            throw new PDFException(file.getName());
        }
    }

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
                return new PairUtils(modifiedText, annotations);
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
    public List<PairUtils> parseFilesFromFolder(File file) throws PDFException {
        List<PairUtils> parsed = new LinkedList<>();

        for(File f : file.listFiles()) {
            if(f.isDirectory())
                parseFilesFromFolder(f);
            else if(f.isFile())
                parsed.add(parsePDF(f));
        }

        return parsed;
    }

    /**
     * Parses all given pdfs
     * @param files the files to parse
     * @return the list of parsed texts from each file in the folder
     * @throws PDFException if one of the files is not of type pdf
     */
    public List<PairUtils> parseFilesList(File... files) throws PDFException {
        List<PairUtils> parsed = new LinkedList<>();

        for(File f : files) {
            if(f.isDirectory())
                parseFilesFromFolder(f);
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
            COSFloat ULX = (COSFloat) quads.get(0+rectangle);       //upper left x coordinate
            COSFloat ULY = (COSFloat) quads.get(1+rectangle);       //upper left y coordinate
            COSFloat URX = (COSFloat) quads.get(2+rectangle);       //upper right x coordinate
            COSFloat URY = (COSFloat) quads.get(3+rectangle);       //upper right y coordinate
            COSFloat LLX = (COSFloat) quads.get(4+rectangle);       //lower left x coordinate
            COSFloat LLY = (COSFloat) quads.get(5+rectangle);       //lower left y coordinate

            float xStart = ULX.floatValue() - 1;                    //x coordinate at the top left of the rectangle
            float yStart = ULY.floatValue();                        //y coordinate at the top left of the rectangle
            float width = URX.floatValue() - LLX.floatValue();      //width of rectangle
            float height = URY.floatValue() - LLY.floatValue();     //height of the rectangle

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
}
