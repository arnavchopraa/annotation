package importmodels;

import static org.junit.jupiter.api.Assertions.*;

import org.example.backend.importmodels.Coordinator;
import org.example.backend.importmodels.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ProjectTest {
    private Project project;
    private Coordinator coordinator1;
    private Coordinator coordinator2;

    @BeforeEach
    void setUp() {
        coordinator1 = new Coordinator("c1", "c1@sp.com");
        coordinator2 = new Coordinator("c2", "c2@sp.com");

        List<String> studentNos = Arrays.asList("12345", "67890");
        List<Coordinator> coordinators = Arrays.asList(coordinator1, coordinator2);

        project = new Project(studentNos, coordinators);
    }

    @Test
    void testGettersAndSetters() {
        List<String> newStudentNos = Arrays.asList("54321", "09876");
        project.setStudentNos(newStudentNos);

        List<Coordinator> newCoordinators = new ArrayList<>();
        newCoordinators.add(coordinator2);
        project.setCoordinators(newCoordinators);

        assertEquals(newStudentNos, project.getStudentNos());
        assertEquals(newCoordinators, project.getCoordinators());
    }

    @Test
    void testEqualsAndHashCode() {
        List<String> studentNos = Arrays.asList("12345", "67890");
        List<Coordinator> coordinators = Arrays.asList(coordinator1, coordinator2);

        Project sameProject = new Project(studentNos, coordinators);

        assertEquals(project, sameProject);
        assertEquals(project.hashCode(), sameProject.hashCode());

        List<String> differentStudentNos = Arrays.asList("54321", "09876");
        List<Coordinator> differentCoordinators = new ArrayList<>();
        differentCoordinators.add(coordinator1);

        Project differentProject = new Project(differentStudentNos, differentCoordinators);
        assertNotEquals(project, differentProject);
        assertNotEquals(project.hashCode(), differentProject.hashCode());
    }

    @Test
    void testToString() {
        String expectedString = "Project@" + Integer.toHexString(project.hashCode()) +
                ":[students=" + project.getStudentNos().toString() +
                ",coordinators=" + project.getCoordinators().toString();

        assertEquals(expectedString, project.toString());
    }
}
