package utils;

import org.example.backend.utils.Line;
import org.example.backend.utils.PDFObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PDFObjectTest {

    @Test
    void combinePDFObjectTest() {
        PDFObject t = new PDFObject(0, 0, 0, 0, 0);
        Line l = new Line(-1, -1, 2, 2);

        t.combineTable(l);

        PDFObject ans = new PDFObject(-1, -1, 2, 2, 0);

        assertEquals(ans, t);
    }

    @Test
    void toStringTest() {
        PDFObject t = new PDFObject(0, 0, 0, 0, 0);
        assertEquals("Top Left: (0.0, 0.0) Bottom right: (0.0, 0.0)", t.toString());
    }
}
