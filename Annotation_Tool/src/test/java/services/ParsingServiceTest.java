package services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.example.TestUtils;
import org.example.exceptions.PDFException;
import org.example.services.ParsingService;
import org.example.utils.Line;
import org.example.utils.PairUtils;
import org.example.utils.Table;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParsingServiceTest {

    private final TestUtils testUtils = new TestUtils();
    private final ParsingService ps = new ParsingService();

    @Test
    void mergeLinesTest() {

        Line l1 = new Line(0, 1, 2, 1);
        Line l2 = new Line(2.3f, 1, 5, 1);
        Line l3 = new Line(5.5f, 1, 5, 1);

        List<Line> lines = new ArrayList<>();
        lines.add(l1);
        lines.add(l2);
        lines.add(l3);

        Line ans1 = new Line(0, 1, 5, 1);
        List<Line> ans = new ArrayList<>();
        ans.add(ans1);
        ans.add(l3);

        assertEquals(ans, ps.mergeLines(lines));
    }

    @Test
    void processLinesTest() {

        Line l1 = new Line(0, 1, 4, 1);
        Line l2 = new Line(0, 1, 0, 5);

        Line l3 = new Line(10, 10, 15, 10);
        Line l4 = new Line(12, 10, 12, 15);
        Line l5 = new Line(10, 13, 20, 13);
        Line l6 = new Line(20, 13, 20, 15);

        List<Line> lines = new ArrayList<>();
        lines.add(l1);
        lines.add(l2);
        lines.add(l3);
        lines.add(l4);
        lines.add(l5);
        lines.add(l6);

        Table table1 = new Table(0, 1, 4, 5);
        Table table2 = new Table(10, 10, 20, 15);
        List<Table> ans = new ArrayList<>();
        ans.add(table1);
        ans.add(table2);

        assertEquals(ans, ps.processLines(lines));
    }

    @Test
    public void testParsePdfNoAnnotations() {
        String text = "This is a PDF file";
        try {
            File pdf = testUtils.convertPDFtoFile(testUtils.generatePDF(text));
            PairUtils pair = ps.parsePDF(pdf);
            assertEquals(text + "\r\n", pair.getText());
            assertEquals("", pair.getAnnotations());
            assertEquals(pdf.getName(), pair.getFileName());
            pdf.deleteOnExit();
        } catch (IOException | PDFException e) {
            throw new RuntimeException("Test failed - Could not generate PDF");
        }
    }

    @Test
    public void testParsePdfAnnotations() {
        String text = "This is a PDF file";
        String content = "This is an annotation";
        try {
            PDDocument pdf = testUtils.generatePDF(text);
            testUtils.addAnnotation(pdf, content);
            File pdfFile = testUtils.convertPDFtoFile(pdf);
            PairUtils pair = ps.parsePDF(pdfFile);
            assertEquals(text + "\r\n", pair.getText());
            assertEquals("This is\r\n - " + content + "\n", pair.getAnnotations());
            assertEquals(pdfFile.getName(), pair.getFileName());
            pdfFile.deleteOnExit();
        } catch (IOException | PDFException e) {
            throw new RuntimeException("Test failed - Could not generate PDF");
        }
    }

    /*@Test
    public void testParsePdfNer() {
        String text = "This is a PDF file";
        String content = "This is an annotation";
        try {
            PDDocument pdf = testUtils.generatePDF(text);
            testUtils.addAnnotation(pdf, content);
            File pdfFile = testUtils.convertPDFtoFile(pdf);
            PairUtils pair = ps.parsePDFwithNer(pdfFile);
            assertEquals("", pair.getText());
            assertEquals("This is\r\n - " + content + "\n", pair.getAnnotations());
            assertEquals(pdfFile.getName(), pair.getFileName());
            pdfFile.deleteOnExit();
        } catch (IOException | PDFException e) {
            throw new RuntimeException("Test failed - Could not generate PDF");
        }
    }*/

    @Test
    public void testParseFolderFile() {
        String text = "This is a PDF file";
        String content = "This is an annotation";
        try {
            PDDocument pdf = testUtils.generatePDF(text);
            testUtils.addAnnotation(pdf, content);
            File pdfFile = testUtils.convertPDFtoFile(pdf);
            assertThrows(IOException.class, () -> ps.parseFilesFromFolder(pdfFile));
            pdfFile.deleteOnExit();
        } catch (IOException e) {
            throw new RuntimeException("Test failed - Could not generate PDF");
        }
    }

    @Test
    public void testParseMultipleFiles() {
        String text = "This is a PDF file";
        String content = "This is an annotation";
        try {
            PDDocument pdf = testUtils.generatePDF(text);
            testUtils.addAnnotation(pdf, content);
            File pdfFile = testUtils.convertPDFtoFile(pdf);
            List<PairUtils> pairList = ps.parseFilesList(pdfFile, pdfFile);
            PairUtils pair = pairList.get(0);
            assertEquals(pair, pairList.get(1));
            assertEquals(text + "\r\n", pair.getText());
            assertEquals("This is\r\n - " + content + "\n", pair.getAnnotations());
            assertEquals(pdfFile.getName(), pair.getFileName());
            pdfFile.deleteOnExit();
        } catch (IOException | PDFException e) {
            throw new RuntimeException("Test failed - Could not generate PDF");
        }
    }
}
