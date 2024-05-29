package org.example.importmodels;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Submission {
    private String groupNo;
    private String assignmentNo;
    private String groupName;
    private String studentName;
    private String date;
    private byte[] submittedFile;

    /**
     * Constructor for submission, excluding the submittedFile, which is set in another method
     * inside ImportService.
     *
     * @param groupNo Group number of student who submitted
     * @param assignmentNo Assignment number for which submission was recorded
     * @param groupName Group name of student who submitted
     * @param studentName Name of student who submitted
     * @param date Date when the assignment was submitted
     */
    public Submission(String groupNo, String assignmentNo, String groupName, String studentName, String date) {
        this.groupNo = groupNo;
        this.assignmentNo = assignmentNo;
        this.groupName = groupName;
        this.studentName = studentName;
        this.date = date;
        this.submittedFile = null;
    }

    /**
     * Override of toString method from Object class
     *
     * @return A string representation of a Submission object.
     */
    @Override
    public String toString() {
        return "Submission@" + Integer.toHexString(hashCode()) +
                ":[groupNo=" + groupNo +
                ",assignmentNo=" + assignmentNo +
                ",groupName=" + groupName +
                ",studentName=" + studentName +
                ",date=" + date +
                ",file length=" + (submittedFile != null ? submittedFile.length : "null") + "]";
    }
}
