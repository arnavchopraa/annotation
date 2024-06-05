package utils;

import org.example.backend.utils.Line;
import org.example.backend.utils.Table;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class TableTest {

    @Test
    void combineTableTest() {
        Table t = new Table(0, 0, 0, 0);
        Line l = new Line(-1, -1, 2, 2);

        t.combineTable(l);

        Table ans = new Table(-1, -1, 2, 2);

        assertEquals(ans, t);
    }

    @Test
    void toStringTest() {
        Table t = new Table(0, 0, 0, 0);
        assertEquals("Top Left: (0.0, 0.0) Bottom right: (0.0, 0.0)", t.toString());
    }
}
