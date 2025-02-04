package org.example.backend.importmodels;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Association {
    private Student student;
    private Submission submission;

    /**
     * Override of toString method from Object class
     *
     * @return A string representation of an org.example.backend.importmodels.Association object.
     */
    @Override
    public String toString() {
        return "Association@" + Integer.toHexString(hashCode()) +
                ":[student=" + student.toString() +
                ",submission=" + submission.toString();
    }
}
