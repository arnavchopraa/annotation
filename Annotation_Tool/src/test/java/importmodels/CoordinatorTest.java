package importmodels;

import static org.junit.jupiter.api.Assertions.*;

import org.example.backend.importmodels.Association;
import org.example.backend.importmodels.Coordinator;
import org.example.backend.importmodels.Student;
import org.example.backend.importmodels.Submission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class CoordinatorTest {
    private Coordinator coordinator;
    private Association association1;
    private Association association2;

    @BeforeEach
    void setUp() {
        Student student1 = new Student("1", "user", "John", "john@sp.com", "cat1", "gr1");
        Submission submission1 = new Submission("1", "1", "gr1", "John", "", "", new byte[0]);
        association1 = new Association(student1, submission1);

        Student student2 = new Student("2", "userM", "Mihai", "john@sp.com", "cat1", "gr1");
        Submission submission2 = new Submission("2", "2", "gr2", "Mihai", "");
        association2 = new Association(student2, submission2);

        List<Association> associations = new ArrayList<>();
        associations.add(association1);
        associations.add(association2);

        coordinator = new Coordinator("c1", "c1@sp.com", associations);
    }

    @Test
    void testGettersAndSetters() {
        coordinator.setFullName("c2");
        coordinator.setEmail("c2@sp.com");

        List<Association> newAssociations = new ArrayList<>();
        newAssociations.add(association2);
        coordinator.setAssociations(newAssociations);

        assertEquals("c2", coordinator.getFullName());
        assertEquals("c2@sp.com", coordinator.getEmail());
        assertEquals(newAssociations, coordinator.getAssociations());
    }

    @Test
    void testEqualsAndHashCode() {
        List<Association> associations = new ArrayList<>();
        associations.add(association1);
        associations.add(association2);

        Coordinator sameCoordinator = new Coordinator("c1", "c1@sp.com", associations);

        assertEquals(coordinator, sameCoordinator);
        assertEquals(coordinator.hashCode(), sameCoordinator.hashCode());

        Coordinator differentCoordinator = new Coordinator("c2", "c2@sp.com");
        assertNotEquals(coordinator, differentCoordinator);
        assertNotEquals(coordinator.hashCode(), differentCoordinator.hashCode());
    }

    @Test
    void testToString() {
        String expectedString = "Coordinator@" + Integer.toHexString(coordinator.hashCode()) +
                ":[fullName=c1" +
                ",email=c1@sp.com]" +
                "associations= " + coordinator.getAssociations().toString();

        assertEquals(expectedString, coordinator.toString());
    }

    @Test
    void testConstructorWithoutAssociations() {
        Coordinator coordinatorWithoutAssociations = new Coordinator("c3", "c3@sp.com");
        assertEquals("c3", coordinatorWithoutAssociations.getFullName());
        assertEquals("c3@sp.com", coordinatorWithoutAssociations.getEmail());
        assertTrue(coordinatorWithoutAssociations.getAssociations().isEmpty());
    }
}
