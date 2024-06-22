package utils;

import org.example.backend.utils.Line;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class LineTest {

    @Test
    void isVerticalTest() {
        Line l = new Line(0, 1, 0, 5);
        assertTrue(l.isVertical());
    }

    @Test
    void isNotVerticalTest() {
        Line l = new Line(0, 1, 4, 1);
        assertFalse(l.isVertical());
    }

    @Test
    void intersectsVerticalTest() {
        Line l1 = new Line(0, 1, 0, 5);
        Line l2 = new Line(2, 1, 2, 5);
        assertFalse(l1.intersectsWith(l2));
    }

    @Test
    void intersectsHorizontalTest() {
        Line l1 = new Line(1, 1, 4, 1);
        Line l2 = new Line(1, 1, 2, 1);
        assertFalse(l1.intersectsWith(l2));
    }

    @Test
    void intersectsFirstVerticalTest() {
        Line l1 = new Line(0, 1, 0, 5);
        Line l2 = new Line(1, 1, 4, 1);
        assertFalse(l1.intersectsWith(l2));
    }

    @Test
    void intersectsSecondVerticalTest() {
        Line l1 = new Line(0, 2, 4, 2);
        Line l2 = new Line(1, 1, 1, 4);
        assertTrue(l1.intersectsWith(l2));
    }

    @Test
    void mergeSameOrientationTest() {
        Line l1 = new Line(0, 1, 0, 5);
        Line l2 = new Line(1, 1, 1, 7);
        assertFalse(l1.mergeWith(l2, 0.4f));
    }

    @Test
    void mergeNotMergeableTest() {
        Line l1 = new Line(0, 1, 2, 1);
        Line l2 = new Line(3, 1, 5, 1);
        assertFalse(l1.mergeWith(l2, 0.4f));
    }

    @Test
    void mergeL1FirstTest() {
        Line l1 = new Line(0, 1, 2, 1);
        Line l2 = new Line(2.3f, 1, 5, 1);
        assertTrue(l1.mergeWith(l2, 0.4f));
    }

    @Test
    void mergeL2FirstTest() {
        Line l2 = new Line(0, 1, 2, 1);
        Line l1 = new Line(2.3f, 1, 5, 1);
        assertTrue(l1.mergeWith(l2, 0.4f));
    }

    @Test
    void compareEqualXTest() {
        Line l1 = new Line(0, 1, 2, 1);
        Line l2 = new Line(0, 2, 5, 1);
        assertEquals(-1, l1.compareTo(l2));
    }

    @Test
    void compareDifferentXTest() {
        Line l1 = new Line(2, 1, 2, 1);
        Line l2 = new Line(0, 2, 5, 1);
        assertEquals(1, l1.compareTo(l2));
    }

    @Test
    void toStringTest() {
        Line l = new Line(0, 1, 2, 1);
        assertEquals("startX = 0.0, startY = 1.0, endX = 2.0, endY = 1.0", l.toString());
    }
}
