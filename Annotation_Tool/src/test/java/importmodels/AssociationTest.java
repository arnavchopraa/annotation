package importmodels;

import static org.junit.jupiter.api.Assertions.*;

import org.example.backend.importmodels.Association;
import org.example.backend.importmodels.Student;
import org.example.backend.importmodels.Submission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssociationTest {
    private Student student;
    private Submission submission;
    private Association association;

    @BeforeEach
    void setUp() {
        student = new Student("1", "user", "John", "john@sp.com", "cat1", "gr1");
        submission = new Submission("1", "1", "gr1", "John", "", "", new byte[0]);
        association = new Association(student, submission);
    }

    @Test
    void testGettersAndSetters() {
        Student newStudent = student;
        newStudent.setStudentNo("2");
        newStudent.setUsername("userM");
        newStudent.setStudentName("Mihai");
        newStudent.setEmail("mihai@sp.com");
        newStudent.setGroupCategory("cat2");
        newStudent.setGroupName("gr1");
        Submission newSubmission = submission;
        newSubmission.setGroupNo("2");
        newSubmission.setAssignmentNo("2");
        newSubmission.setGroupName("gr2");
        newSubmission.setStudentName("Mihai");
        newSubmission.setDate("");
        newSubmission.setFileName("");
        newSubmission.setSubmittedFile(new byte[0]);

        association.setStudent(newStudent);
        association.setSubmission(newSubmission);

        assertEquals(newStudent, association.getStudent());
        assertEquals(newSubmission, association.getSubmission());
    }

    @Test
    void testEqualsAndHashCode() {
        Student sameStudent = new Student("1", "user", "John", "john@sp.com", "cat1", "gr1");
        Submission sameSubmission = new Submission("1", "1", "gr1", "John", "", "", new byte[0]);
        Association sameAssociation = new Association(sameStudent, sameSubmission);

        assertEquals(association, sameAssociation);
        assertEquals(association.hashCode(), sameAssociation.hashCode());

        Student differentStudent = new Student("2", "userM", "Mihai", "john@sp.com", "cat1", "gr1");
        Submission differentSubmission = new Submission("2", "2", "gr2", "Mihai", "", "", new byte[0]);
        Association differentAssociation = new Association(differentStudent, differentSubmission);

        assertNotEquals(association, differentAssociation);
        assertNotEquals(association.hashCode(), differentAssociation.hashCode());
    }

    @Test
    void testToString() {
        String expectedString = "Association@" + Integer.toHexString(association.hashCode()) +
                ":[student=" + student.toString() +
                ",submission=" + submission.toString();

        assertEquals(expectedString, association.toString());
    }
}
