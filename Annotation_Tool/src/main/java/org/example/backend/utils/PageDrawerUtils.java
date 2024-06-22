package org.example.backend.utils;

import lombok.Getter;
import org.apache.pdfbox.contentstream.PDFGraphicsStreamEngine;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PageDrawerUtils extends PDFGraphicsStreamEngine {
    private GeneralPath path = new GeneralPath();
    @Getter
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

    /**
     *
     * @param p0 starting coordinate of the rectangle
     * @param p1 second coordinate of the rectangle
     * @param p2 third coordinate of the rectangle
     * @param p3 last coordinate of the rectangle
     *
     * @throws IOException if the content stream cannot be processed
     */
    @Override
    public void appendRectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3) throws IOException {

    }

    /**
     *
     * @param pdImage The image to draw.
     *
     * @throws IOException if the content stream cannot be processed
     */
    @Override
    public void drawImage(PDImage pdImage) throws IOException {

    }

    /**
     *
     * @param windingRule The winding rule which will be used for clipping.
     *
     * @throws IOException if the content stream cannot be processed
     */
    @Override
    public void clip(int windingRule) throws IOException {
        clipWindingRule = windingRule;
    }

    /**
     *
     * @param x the x-coordinate to move to
     * @param y the y-coordinate to move to
     *
     * @throws IOException if the content stream cannot be processed
     */
    @Override
    public void moveTo(float x, float y) throws IOException {
        lastx = x;
        lasty = y;
        path.moveTo(x, y);
    }

    /**
     *
     * @param x the X-coordinate of the ending-point of the line to be drawn
     * @param y the Y-coordinate of the ending-point of the line to be drawn
     *
     * @throws IOException if the content stream cannot be processed
     */
    @Override
    public void lineTo(float x, float y) throws IOException {
        Line line = new Line(lastx, lasty, x, y);
        lines.add(line);
        path.lineTo(x, y);
    }

    /**
     *
     * @param x1 the X coordinate of the first B&eacute;zier control point
     * @param y1 the Y coordinate of the first B&eacute;zier control point
     * @param x2 the X coordinate of the second B&eacute;zier control point
     * @param y2 the Y coordinate of the second B&eacute;zier control point
     * @param x3 the X coordinate of the final end point
     * @param y3 the Y coordinate of the final end point
     *
     * @throws IOException if the content stream cannot be processed
     */
    @Override
    public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) throws IOException {
        path.curveTo(x1, y1, x2, y2, x3, y3);
    }

    /**
     *
     * @return the current point
     * @throws IOException if the content stream cannot be processed
     */
    @Override
    public Point2D getCurrentPoint() throws IOException {
        return path.getCurrentPoint();
    }

    /**
     *
     * @throws IOException if the content stream cannot be processed
     */
    @Override
    public void closePath() throws IOException {
        path.closePath();
    }

    /**
     *
     * @throws IOException if the content stream cannot be processed
     */
    @Override
    public void endPath() throws IOException {

    }

    /**
     *
     * @throws IOException if the content stream cannot be processed
     */
    @Override
    public void strokePath() throws IOException {

    }

    /**
     *
     * @param windingRule The winding rule this path will use.
     *
     * @throws IOException if the content stream cannot be processed
     */
    @Override
    public void fillPath(int windingRule) throws IOException {

    }

    /**
     *
     * @param windingRule The winding rule this path will use.
     *
     * @throws IOException if the content stream cannot be processed
     */
    @Override
    public void fillAndStrokePath(int windingRule) throws IOException {

    }

    /**
     *
     * @param shadingName The name of the Shading Dictionary to use for this fill instruction.
     *
     * @throws IOException if the content stream cannot be processed
     */
    @Override
    public void shadingFill(COSName shadingName) throws IOException {

    }

    /**
     * Method to return all lines extracted on page.
     * @return list of lines
     */
    public List<Line> getLines() {
        return lines;
    }
}
