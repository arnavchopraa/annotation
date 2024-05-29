package org.example.importmodels;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Association {
    private Student student;
    private Submission submission;

    private List<Coordinator> coordinators; // TODO - to be changed to new class

    /**
     * Constructor for Association without coordinators - to make use of already available
     * functionality in ImportService.
     *
     * @param student Student part of the association
     * @param submission Submission associated to student
     */
    public Association(Student student, Submission submission) {
        this.student = student;
        this.submission = submission;
        this.coordinators = new ArrayList<>();
    }

    /**
     * Override of toString method from Object class
     *
     * @return A string representation of an Association object.
     */
    @Override
    public String toString() {
        return "Association@" + Integer.toHexString(hashCode()) +
                ":[student=" + student.toString() +
                ",submission=" + submission.toString() +
                ",coordinators=" + coordinators.toString() + "]";
    }
}
