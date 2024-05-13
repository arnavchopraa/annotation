package org.example.utils;

import org.apache.pdfbox.contentstream.PDFGraphicsStreamEngine;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.rendering.PageDrawer;
import org.apache.pdfbox.rendering.PageDrawerParameters;

import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PageDrawerUtils extends PDFGraphicsStreamEngine {
    private GeneralPath path = new GeneralPath();
    private int clipWindingRule = -1;
    private List<Line> lines = new ArrayList<>();
    private float lastx;
    private float lasty;

    /**
     * Constructor.
     *
     * @param page the page the content stream belongs to
     */
    public PageDrawerUtils(PDPage page) {
        super(page);
    }

    @Override
    public void appendRectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3) throws IOException {

    }

    @Override
    public void drawImage(PDImage pdImage) throws IOException {

    }

    @Override
    public void clip(int windingRule) throws IOException {
        clipWindingRule = windingRule;
    }

    @Override
    public void moveTo(float x, float y) throws IOException {
        //System.out.println("line starts at: x = " + x + ",y = " + y + "\n");
        lastx = x;
        lasty = y;
        path.moveTo(x, y);
    }

    @Override
    public void lineTo(float x, float y) throws IOException {
        Line line = new Line(lastx, lasty, x, y);
        lines.add(line);
        path.lineTo(x, y);
    }

    @Override
    public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) throws IOException {
        path.curveTo(x1, y1, x2, y2, x3, y3);
    }

    @Override
    public Point2D getCurrentPoint() throws IOException {
        return path.getCurrentPoint();
    }

    @Override
    public void closePath() throws IOException {
        path.closePath();
    }

    @Override
    public void endPath() throws IOException {

    }

    @Override
    public void strokePath() throws IOException {

    }

    @Override
    public void fillPath(int windingRule) throws IOException {

    }

    @Override
    public void fillAndStrokePath(int windingRule) throws IOException {

    }

    @Override
    public void shadingFill(COSName shadingName) throws IOException {

    }

    public List<Line> getLines() {
        return lines;
    }
}
