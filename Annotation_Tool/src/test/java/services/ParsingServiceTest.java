package services;

import org.example.backend.services.AnnotationCodeService;
import org.example.backend.services.ParsingService;
import org.example.backend.utils.Line;
import org.example.backend.utils.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParsingServiceTest {


    private AnnotationCodeService annotationCodeService;
    private ParsingService ps;

    @BeforeEach
    public void setUp() {
        annotationCodeService = Mockito.mock(AnnotationCodeService.class);
        ps = new ParsingService(annotationCodeService);
    }
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
}
