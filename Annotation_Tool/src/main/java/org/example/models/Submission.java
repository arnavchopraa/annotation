package org.example.models;

import lombok.*;

import java.io.File;

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
    private File submittedFile;

    public Submission(String groupNo, String assignmentNo, String groupName, String studentName, String date) {
        this.groupNo = groupNo;
        this.assignmentNo = assignmentNo;
        this.groupName = groupName;
        this.studentName = studentName;
        this.date = date;
        this.submittedFile = null;
    }

    @Override
    public String toString() {
        return "Submission@" + Integer.toHexString(hashCode()) +
                ":[groupNo=" + groupNo +
                ",assignmentNo=" + assignmentNo +
                ",groupName=" + groupName +
                ",studentName=" + studentName +
                ",date=" + date +
                ",fileName=" + submittedFile.getName() + "].\n";
    }
}
