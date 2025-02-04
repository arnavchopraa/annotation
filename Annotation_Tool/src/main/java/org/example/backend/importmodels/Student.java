package org.example.backend.importmodels;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Student {
    private String studentNo;
    private String username;
    private String studentName;
    private String email;
    private String groupCategory;
    private String groupName;

    /**
     * Override of toString method from Object class
     *
     * @return A string representation of a org.example.backend.importmodels.Student object.
     */
    @Override
    public String toString() {
        return "Student@" + Integer.toHexString(hashCode()) +
                ":[studentNo=" + studentNo +
                ",username=" + username +
                ",studentName=" + studentName +
                ",email=" + email +
                ",groupCategory=" + groupCategory +
                ",groupName=" + groupName + "]";
    }
}
