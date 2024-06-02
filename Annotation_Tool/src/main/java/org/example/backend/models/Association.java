package org.example.backend.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Association {
    private Student student;
    private Submission submission;

    //private User coordinator; TODO - to be added when database setup is finished

    /**
     * Override of toString method from Object class
     *
     * @return A string representation of an Association object.
     */
    @Override
    public String toString() {
        return "Association@" + Integer.toHexString(hashCode()) +
                ":[student=" + student.toString() +
                ",submission=" + submission.toString() + "]";
    }
}
