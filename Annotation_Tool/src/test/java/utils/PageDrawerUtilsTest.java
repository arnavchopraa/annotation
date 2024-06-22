package utils;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.example.backend.utils.Line;
import org.example.backend.utils.PageDrawerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PageDrawerUtilsTest {

    private PageDrawerUtils pageDrawerUtils;
    private PDPage pdPage;

    @BeforeEach
    void setUp() {
        pdPage = new PDPage();
        pageDrawerUtils = new PageDrawerUtils(pdPage, 0);
    }

    @Test
    void testMoveToAndLineTo() throws IOException {
        pageDrawerUtils.moveTo(10, 10);
        pageDrawerUtils.lineTo(20, 20);
        pageDrawerUtils.lineTo(30, 30);

        List<Line> lines = pageDrawerUtils.getLines();
        assertEquals(2, lines.size());
        assertEquals(new Line(10, 10, 20, 20), lines.get(0));
        assertEquals(new Line(10, 10, 30, 30), lines.get(1));
    }

    @Test
    void testGetCurrentPoint() throws IOException {
        pageDrawerUtils.moveTo(10, 10);
        Point2D currentPoint = pageDrawerUtils.getCurrentPoint();
        assertEquals(10, currentPoint.getX());
        assertEquals(10, currentPoint.getY());
    }

    @Test
    void testCurveTo() throws IOException {
        pageDrawerUtils.moveTo(10, 10);
        pageDrawerUtils.curveTo(15, 15, 20, 20, 25, 25);

        Point2D currentPoint = pageDrawerUtils.getCurrentPoint();
        assertEquals(25, currentPoint.getX());
        assertEquals(25, currentPoint.getY());
    }

    @Test
    void testClosePath() throws IOException {
        pageDrawerUtils.moveTo(10, 10);
        pageDrawerUtils.lineTo(20, 20);
        pageDrawerUtils.lineTo(30, 30);
        pageDrawerUtils.closePath();

        List<Line> lines = pageDrawerUtils.getLines();
        assertEquals(2, lines.size());
    }

    @Test
    void testConstructor() {
        assertNotNull(pageDrawerUtils);
    }

    @Test
    void testAppendRectangle() throws IOException {
        Point2D p0 = new Point2D.Float(0, 0);
        Point2D p1 = new Point2D.Float(1, 0);
        Point2D p2 = new Point2D.Float(1, 1);
        Point2D p3 = new Point2D.Float(0, 1);
        pageDrawerUtils.appendRectangle(p0, p1, p2, p3);
        // Nothing to assert as method is not implemented
    }

    @Test
    void testClip() throws IOException {
        int windingRule = GeneralPath.WIND_EVEN_ODD;
        pageDrawerUtils.clip(windingRule);
        assertEquals(windingRule, pageDrawerUtils.getClipWindingRule());
    }

    @Test
    void testMoveTo() throws IOException {
        float x = 1.0f;
        float y = 2.0f;
        pageDrawerUtils.moveTo(x, y);
        assertNotNull(pageDrawerUtils.getCurrentPoint());
        assertEquals(x, (float) pageDrawerUtils.getCurrentPoint().getX());
        assertEquals(y, (float) pageDrawerUtils.getCurrentPoint().getY());
    }

    @Test
    void testLineTo() throws IOException {
        float x1 = 1.0f;
        float y1 = 2.0f;
        float x2 = 3.0f;
        float y2 = 4.0f;
        pageDrawerUtils.moveTo(x1, y1);
        pageDrawerUtils.lineTo(x2, y2);
        List<Line> lines = pageDrawerUtils.getLines();
        assertEquals(1, lines.size());
        Line line = lines.get(0);
        assertEquals(x1, line.getStartX());
        assertEquals(y1, line.getStartY());
        assertEquals(x2, line.getEndX());
        assertEquals(y2, line.getEndY());
    }


    @Test
    void testStrokePath() throws IOException {
        pageDrawerUtils.strokePath();
        // No direct way to assert strokePath, just ensure no exceptions
    }

    @Test
    void testFillPath() throws IOException {
        int windingRule = GeneralPath.WIND_NON_ZERO;
        pageDrawerUtils.fillPath(windingRule);
        // No direct way to assert fillPath, just ensure no exceptions
    }

    @Test
    void testFillAndStrokePath() throws IOException {
        int windingRule = GeneralPath.WIND_NON_ZERO;
        pageDrawerUtils.fillAndStrokePath(windingRule);
        // No direct way to assert fillAndStrokePath, just ensure no exceptions
    }

    @Test
    void testShadingFill() throws IOException {
        pageDrawerUtils.shadingFill(COSName.A);
        // No direct way to assert shadingFill, just ensure no exceptions
    }

    @Test
    void testEndPath() throws IOException {
        pageDrawerUtils.endPath();
        // No direct way to assert endPath, just ensure no exceptions
    }

    @Test
    void testGetLines() {
        assertNotNull(pageDrawerUtils.getLines());
        assertTrue(pageDrawerUtils.getLines().isEmpty());
    }
}
