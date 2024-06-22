package utils;

import org.example.backend.utils.PairUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PairUtilsTest {
    @Test
    public void testRemoveExtension() {
        PairUtils pair = new PairUtils("This is a PDF file", "", "test.pdf", "");
        assertEquals("test", pair.getFileName());
    }

    @Test
    public void testRemoveExtension2() {
        PairUtils pair = new PairUtils("", "", "");
        pair.setText("This is a PDF file");
        pair.setAnnotations("");
        pair.setFileName("test");
        assertEquals("test", pair.getFileName());
    }
}
